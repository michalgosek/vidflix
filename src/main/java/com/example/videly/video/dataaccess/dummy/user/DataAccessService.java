package com.example.videly.video.dataaccess.dummy.user;

import com.example.videly.dao.UserVideoDAO;
import com.example.videly.video.Video;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("dummyUserVideos")
public class DataAccessService implements UserVideoDAO {
    private final Map<String, List<Video>> usersVideos = new HashMap<>();

    @Override
    public Optional<List<Video>> listVideos(String username) {
        return Optional.ofNullable(usersVideos.get(username));
    }

    @Override
    public void insertVideo(Video video, String username) {
        Optional<List<Video>> userVideos = Optional.ofNullable(usersVideos.get(username));
        userVideos.ifPresentOrElse(videosList -> videosList.add(video),
                () -> {
                    usersVideos.put(username, new ArrayList<>());
                    usersVideos.get(username).add(video);
                });
    }

    @Override
    public Optional<Video> findVideo(Long id, String username) {
        return usersVideos.get(username).stream().filter(v -> id.equals(v.getId())).findFirst();
    }

    @Override
    public void returnVideo(Long id, String username) {
        final Optional<List<Video>> userVideos = Optional.ofNullable(usersVideos.get(username));
        final Optional<Video> videoToDrop = findVideo(id, username);
        videoToDrop.ifPresent(video -> userVideos.ifPresent(userVideosList -> userVideosList.remove(video)));
    }
}
