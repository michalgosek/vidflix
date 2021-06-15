package com.example.videly.dao;

import com.example.videly.video.Video;

import java.util.List;
import java.util.Optional;

public interface VideoDAO {
    Optional<Video> findVideo(String name);

    Optional<List<Video>> findVideosFromCategory(Long id);

    Optional<Video> findVideo(Long Id);

    void setQuantity(Long id, int value);

    Optional<List<Video>> listAllVideos();
}
