package com.example.videly.dao;

import com.example.videly.video.Video;

import java.util.List;
import java.util.Optional;

public interface UserVideoDAO {
    Optional<List<Video>> listVideos(String username);

    Optional<Video> findVideo(Long id, String username);

    void returnVideo(Long id, String username);

    void insertVideo(Video video, String username);
}
