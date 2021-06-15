package com.example.videly.video.dataaccess.dummy.videos;

import com.example.videly.dao.VideoDAO;
import com.example.videly.video.Video;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("dummyVideosRepository")
public class DataAccessService implements VideoDAO {
    private final List<Video> videos = List.of(
            new Video(
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
                    1
            ), new Video(
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
                    1
            ));


    @Override
    public Optional<Video> findVideoByName(String name) {
        return getServiceVideos()
                .stream()
                .filter(video -> name.equals(video.getName()))
                .findFirst();
    }

    @Override
    public List<Video> listAllVideos() {
        return getServiceVideos();
    }

    @Override
    public Optional<Video> findVideoById(Long Id) {
        return getServiceVideos()
                .stream()
                .filter(video -> Id.equals(video.getId())).findFirst();
    }

    @Override
    public void setQuantity(Long Id, int value) {
        Optional<Video> video = findVideoById(Id);
        video.ifPresent(v -> v.setQuantity(value));
    }

    private List<Video> getServiceVideos() {
        return videos;
    }
}
