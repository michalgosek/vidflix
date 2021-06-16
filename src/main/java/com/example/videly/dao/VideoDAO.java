package com.example.videly.dao;

import com.example.videly.video.Video;

import java.util.List;
import java.util.Optional;

public interface VideoDAO {
    Optional<Video> findVideo(Long videoID);

    Optional<List<Video>> findVideosFromCategory(Long id);

    void setQuantity(Long videoID, int value);

    boolean isUserHasVideo(Long videoID, Long userID);

    Optional<List<Video>> listAllVideos();

    Optional<List<Video>> listUserVideos(Long userID);

    void addUserVideo(Long videoID, Long userID);

    void returnUserVideo(Long videoID, Long userID);
}
