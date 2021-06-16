package com.example.videly.video.dataaccess.mysql.videos;

import com.example.videly.dao.VideoDAO;
import com.example.videly.video.Video;
import com.example.videly.video.VideoCategory;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Repository("MySQLVideosRepository")
@AllArgsConstructor
public class DataAccessService implements VideoDAO {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(DataAccessService.class);

    @Override
    public void setQuantity(Long videoID, int value) {
        jdbcTemplate.update("UPDATE videos SET quantity = ? WHERE id = ?", videoID, value);
    }

    @Override
    public boolean isUserHasVideo(Long videoID, Long userID) {
        final String query = "SELECT * FROM users_videos WHERE user_id = ? AND video_id = ?";
        return recordExistsQuery(query, userID, videoID);
    }

    @Override
    public Optional<List<Video>> listAllVideos() {
        return Optional.of(new ArrayList<>(jdbcTemplate.query("SELECT * FROM videos", mapVideo())));
    }

    @Override
    public Optional<List<Video>> listUserVideos(Long userID) {
        final String userVideosQuery = "SELECT * FROM users_videos WHERE user_id = ?";
        List<Long> videosIds = new ArrayList<>(jdbcTemplate.query(userVideosQuery, mapIdColumn("video_id"), userID));
        if (!videosIds.isEmpty()) {
            List<Video> videos = getUserVideosAsync(videosIds);
            return Optional.of(videos);
        }

        return Optional.empty();
    }

    @Override
    public void addUserVideo(Long videoID, Long userID) {
        int quantity = getQuantityColumnValue(videoID);
        jdbcTemplate.update("INSERT INTO users_videos (user_id, video_id) VALUES (?, ?)", userID, videoID);
        jdbcTemplate.update("UPDATE videos SET quantity = ? WHERE id = ?", quantity - 1, videoID);
    }

    @Override
    public void returnUserVideo(Long videoID, Long userID) {
        int quantity = getQuantityColumnValue(videoID) + 1;
        jdbcTemplate.update("DELETE FROM users_videos WHERE user_id = ? AND video_id = ?", userID, videoID);
        jdbcTemplate.update("UPDATE videos SET quantity = ? WHERE id = ?", quantity + 1, videoID);
    }

    private Integer getQuantityColumnValue(Long id) {
        return jdbcTemplate.query("SELECT quantity FROM videos WHERE id = ?", mapQuantityColumn(), id).stream().findFirst().orElse(-1);
    }

    @Async
    private CompletableFuture<Video> getVideoCompletableFuture(Long videoID) {
        List<Video> categories = jdbcTemplate.query("SELECT * FROM videos WHERE id = ?", mapVideo(), videoID);
        return categories.stream().map(CompletableFuture::completedFuture).findFirst().orElse(null);
    }

    @Override
    // fixme This method should be refactored due to invalid data structure usage. It should use Video class instead of List<Video>
    public Optional<Video> findVideo(Long videoID) {
        final CompletableFuture<Video> videoCompletableFuture = getVideoCompletableFuture(videoID);
        final List<CompletableFuture<VideoCategory>> videoCategoryCompletableFuture = getVideoCategoriesCompletableFuture();
        List<CompletableFuture<IDAggregate>> idAggregateFuture = new ArrayList<>();

        CompletableFuture.allOf(videoCategoryCompletableFuture.toArray(new CompletableFuture[0])).join();
        CompletableFuture.completedFuture(videoCompletableFuture).join();

        List<VideoCategory> videoCategories = new ArrayList<>();
        parseFutureToList(videoCategoryCompletableFuture, videoCategories);

        List<Video> videos = new ArrayList<>(); // desing issue
        parseFutureToList(Collections.singletonList(videoCompletableFuture), videos);

        for (Video video : videos) {
            List<CompletableFuture<IDAggregate>> pair = getVideoIDsAggregateDataCompletableFuture(video.getId());
            idAggregateFuture.addAll(pair);
        }

        CompletableFuture.allOf(idAggregateFuture.toArray(new CompletableFuture[0])).join();

        List<IDAggregate> aggregates = new ArrayList<>();
        parseFutureToList(idAggregateFuture, aggregates);

        final Map<Long, Set<VideoCategory>> categoriesMap = mapCategoriesWithVideoIDs(videoCategories, aggregates);

        List<Video> videoData = new ArrayList<>();
        for (Video video : videos) {
            Optional<Set<VideoCategory>> categories = Optional.ofNullable(categoriesMap.get(video.getId()));

            if (categories.isPresent() && categories.get().size() > 0) {
                video.setCategories(categories.get());
                videoData.add(video);
            }
        }

        return videoData.stream().findFirst();
    }

    @Async
    private List<CompletableFuture<VideoCategory>> getVideoCategoriesCompletableFuture(Long categoryID) {
        List<VideoCategory> categories = jdbcTemplate.query("SELECT * FROM categories WHERE id = ?", mapVideoCategory(), categoryID);
        return categories.stream().map(CompletableFuture::completedFuture).collect(Collectors.toList());
    }

    @Async
    private List<CompletableFuture<Video>> getAllVideoCompletableFuture() {
        List<Video> categories = jdbcTemplate.query("SELECT * FROM videos", mapVideo());
        return categories.stream().map(CompletableFuture::completedFuture).collect(Collectors.toList());
    }


    @Override
    public Optional<List<Video>> findVideosFromCategory(Long categoryID) {
        List<CompletableFuture<VideoCategory>> videosCategoriesFuture = new ArrayList<>(getVideoCategoriesCompletableFuture(categoryID));
        List<CompletableFuture<IDAggregate>> idAggregateFuture = new ArrayList<>();
        List<CompletableFuture<Video>> videosListFuture = new ArrayList<>(getAllVideoCompletableFuture());

        CompletableFuture.allOf(videosListFuture.toArray(new CompletableFuture[0])).join();
        CompletableFuture.completedFuture(videosCategoriesFuture).join();

        List<VideoCategory> videoCategories = new ArrayList<>();
        parseFutureToList(videosCategoriesFuture, videoCategories);

        List<Video> videos = new ArrayList<>();
        parseFutureToList(videosListFuture, videos);

        for (Video video : videos) {
            List<CompletableFuture<IDAggregate>> pair = getVideoIDsAggregateDataCompletableFuture(video.getId());
            idAggregateFuture.addAll(pair);
        }

        CompletableFuture.allOf(idAggregateFuture.toArray(new CompletableFuture[0])).join();

        List<IDAggregate> aggregates = new ArrayList<>();
        parseFutureToList(idAggregateFuture, aggregates);

        final Map<Long, Set<VideoCategory>> categoriesMap = mapCategoriesWithVideoIDs(videoCategories, aggregates);

        List<Video> videosFromCategory = new ArrayList<>();
        for (Video video : videos) {
            Optional<Set<VideoCategory>> categories = Optional.ofNullable(categoriesMap.get(video.getId()));

            if (categories.isPresent() && categories.get().size() > 0) {
                video.setCategories(categories.get());
                videosFromCategory.add(video);
            }
        }

        return Optional.of(videosFromCategory);
    }

    @Async
    private <T> CompletableFuture<Video> getVideoCompletableFuture(String query, T value) {
        final Optional<Video> video = jdbcTemplate.query(query, mapVideo(), value).stream().findFirst();
        return video.map(CompletableFuture::completedFuture).orElseGet(() -> CompletableFuture.completedFuture(null));
    }

    @Async
    private List<CompletableFuture<VideoCategory>> getVideoCategoriesCompletableFuture() {
        List<VideoCategory> categories = new ArrayList<>(jdbcTemplate.query("SELECT * FROM categories", mapVideoCategory()));
        return categories.stream().map(CompletableFuture::completedFuture).collect(Collectors.toList());
    }

    @Async
    private List<CompletableFuture<IDAggregate>> getVideoIDsAggregateDataCompletableFuture(Long videoID) {
        List<IDAggregate> videos = jdbcTemplate.query("SELECT * FROM videos_categories WHERE video_id = ?", mapUserVideo(), videoID);
        return videos.stream().map(CompletableFuture::completedFuture).collect(Collectors.toList());
    }

    private List<Video> getUserVideosAsync(List<Long> videosIds) {
        long start = System.currentTimeMillis();

        List<CompletableFuture<VideoCategory>> videosCategoriesFuture = new ArrayList<>(getVideoCategoriesCompletableFuture());
        List<CompletableFuture<IDAggregate>> idAggregateFuture = new ArrayList<>();
        List<CompletableFuture<Video>> videosListFuture = new ArrayList<>();

        final String query = "SELECT * FROM videos WHERE id = ?";
        for (Long videoID : videosIds) {
            videosListFuture.add(getVideoCompletableFuture(query, videoID));
            List<CompletableFuture<IDAggregate>> pair = getVideoIDsAggregateDataCompletableFuture(videoID);
            idAggregateFuture.addAll(pair);
        }

        CompletableFuture.allOf(videosListFuture.toArray(new CompletableFuture[0])).join();
        CompletableFuture.allOf(idAggregateFuture.toArray(new CompletableFuture[0])).join();
        CompletableFuture.completedFuture(videosCategoriesFuture).join();

        List<VideoCategory> videosCategories = new ArrayList<>();
        parseFutureToList(videosCategoriesFuture, videosCategories);

        List<IDAggregate> aggregates = new ArrayList<>();
        parseFutureToList(idAggregateFuture, aggregates);

        final Map<Long, Set<VideoCategory>> categoriesMap = mapCategoriesWithVideoIDs(videosCategories, aggregates);

        List<Video> videos = new ArrayList<>();
        parseFutureToList(videosListFuture, videos);

        for (Video video : videos) {
            Set<VideoCategory> categories = categoriesMap.get(video.getId());
            video.setCategories(categories);
        }

        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        return videos;
    }

    private Map<Long, Set<VideoCategory>> mapCategoriesWithVideoIDs(List<VideoCategory> videosCategories,
                                                                    List<IDAggregate> aggregates) {
        Map<Long, Set<VideoCategory>> videoCategoryMap = new HashMap<>();
        for (IDAggregate data : aggregates) {
            Set<VideoCategory> categories = new HashSet<>();
            for (VideoCategory category : videosCategories) {
                if (category.getId().equals(data.getCategoryId())) {
                    categories.add(category);
                }
            }
            if (categories.size() > 0)
                videoCategoryMap.put(data.getVideoId(), categories);
        }
        return videoCategoryMap;
    }

    private RowMapper<IDAggregate> mapUserVideo() {
        return (resultSet, i) -> {
            final Long videoID = resultSet.getLong("video_id");
            final Long categoryID = resultSet.getLong("category_id");
            return new IDAggregate(videoID, categoryID);
        };
    }

    private RowMapper<VideoCategory> mapVideoCategory() {
        return (resultSet, i) -> {
            final Long id = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            return new VideoCategory(id, name);
        };
    }

    private RowMapper<Video> mapVideo() {
        return (resultSet, i) -> {
            final Long videoId = resultSet.getLong("id");
            final Integer quantity = resultSet.getInt("quantity");
            final String name = resultSet.getString("name");
            final String shortDescription = resultSet.getString("short_description");
            final String fullDescription = resultSet.getString("full_description");
            return new Video(videoId, name, shortDescription, fullDescription, quantity);
        };
    }

    private RowMapper<Long> mapIdColumn(String label) {
        return (rs, rowNum) -> rs.getLong(label);
    }

    private RowMapper<Integer> mapQuantityColumn() {
        return (rs, rowNum) -> rs.getInt("quantity");
    }


    private <T> void parseFutureToList(List<CompletableFuture<T>> futureList, List<T> container) {
        for (CompletableFuture<T> future : futureList) {
            try {
                container.add(future.get());

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean recordExistsQuery(String sql, Object... args) {
        return jdbcTemplate.query(sql, args, rs -> {
            boolean result1 = rs.next();
            return result1;
        });
    }
}
