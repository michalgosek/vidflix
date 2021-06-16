package com.example.videly.video.dataaccess.dummy.videos;

import com.example.videly.dao.VideoCategoryDAO;
import com.example.videly.dao.VideoDAO;
import com.example.videly.video.Video;
import com.example.videly.video.VideoCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository("dummyVideosRepository")
public class DataAccessService implements VideoDAO {
    private final ConcurrentHashMap<String, Video> videos = new ConcurrentHashMap<>();
    private final Map<String, VideoCategory> hash = new HashMap<>();

    @Autowired
    public DataAccessService(@Qualifier("dummyCategoriesRepository") VideoCategoryDAO videoCategoryDAO) {
        Optional<List<VideoCategory>> categories = videoCategoryDAO.listAllCategories();
        categories.ifPresent(categoryList -> categoryList.forEach(c -> hash.put(c.getName(), c)));

        final VideoCategory horror = hash.get("HORROR");
        final VideoCategory comedy = hash.get("COMEDY");
        final VideoCategory drama = hash.get("DRAMA");

        final Video v1 = new Video(
                1L,
                "Video1",
                "This is example description...",
                """
                        Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                        Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
                        when an unknown printer took a galley of type and scrambled it to make a type
                        specimen book. It has survived not only five centuries, but also the leap into
                        electronic typesetting, remaining essentially unchanged. It was popularised in
                        the 1960s with the release of Letraset sheets containing Lorem Ipsum passages,
                        and more recently with desktop publishing software like Aldus PageMaker including
                        versions of Lorem Ipsum.
                        """,
                1,
                2002,
                "https://www.a4bd.eu/wp-content/uploads/2019/05/video.png"
        );
        v1.setCategories(Set.of(horror, comedy));

        final Video v2 = new Video(
                2L,
                "Video2",
                "This is example description2...",
                """
                        Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                        Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
                        when an unknown printer took a galley of type and scrambled it to make a type
                        specimen book. It has survived not only five centuries, but also the leap into
                        electronic typesetting, remaining essentially unchanged. It was popularised in
                        the 1960s with the release of Letraset sheets containing Lorem Ipsum passages,
                        and more recently with desktop publishing software like Aldus PageMaker including
                        versions of Lorem Ipsum.
                        """,
                1,
                1993,
                "https://www.a4bd.eu/wp-content/uploads/2019/05/video.png"
        );


        v2.setCategories(Set.of(drama, comedy));
        videos.put("Video1", v1);
        videos.put("Video2", v2);
    }


    @Override
    public Optional<Video> findVideo(Long id) {
        return videos
                .values()
                .stream()
                .filter(key -> key.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<List<Video>> findVideosFromCategory(Long id) {
        List<Video> filtered = new ArrayList<>();
        listAllVideos()
                .ifPresent(videoKey -> videoKey
                        .forEach(video -> video
                                .getCategories()
                                .forEach(category -> {
                                            if (id.equals(category.getId()))
                                                filtered.add(video);
                                        }
                                )
                        )
                );

        return Optional.of(filtered);
    }

    @Override
    public Optional<List<Video>> listAllVideos() {
        return Optional.of(new ArrayList<>(videos.values()));
    }

    @Override
    public Optional<List<Video>> listUserVideos(Long userID) {
        return Optional.empty();
    }

    @Override
    public void addUserVideo(Long videoID, Long userID) {

    }

    @Override
    public void returnUserVideo(Long videoID, Long userID) {

    }

    @Override
    public void setQuantity(Long id, int value) {
        Optional<Video> video = findVideo(id);
        video.ifPresent(v -> v.setQuantity(value));
    }

    @Override
    public boolean isUserHasVideo(Long videoID, Long userID) {
        return false;
    }
}
