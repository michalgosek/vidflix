package com.example.videly.video.dataaccess.dummy.user;

import com.example.videly.dao.UserVideoDAO;
import com.example.videly.video.Video;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("dummyUserVideos")
public class DataAccessService implements UserVideoDAO {
    private final Map<String, List<Video>> usersVideos = new HashMap<>();

    @Override
    public List<Video> listVideos(String username) {
        return usersVideos.get(username);
    }

    @Override
    public void insertVideo(Video video, String username) {
        final boolean isUserKeyExists = usersVideos.containsKey(username);
        if (!isUserKeyExists) {
            List<Video> videos = new ArrayList<>();
            videos.add(video);
            usersVideos.put(username, videos);
            return;
        }

        final List<Video> videos = usersVideos.get(username);
        if (videos != null)
            videos.add(video);
    }

    @Override
    public Optional<Video> findVideo(Long id, String username) {
        return usersVideos.get(username).stream().filter(v -> id.equals(v.getId())).findFirst();
    }

    @Override
    public void returnVideo(Long id, String username) {
        final List<Video> videos = usersVideos.get(username);
        Optional<Video> videoToDrop = findVideo(id, username);

        if (videos != null && videoToDrop.isPresent())
            videos.remove(videoToDrop.get());
    }
}
