package com.example.videly.dao;

import com.example.videly.video.Video;

import java.util.List;
import java.util.Optional;

public interface VideoDAO {
    Optional<Video> findVideo(String name);

    Optional<Video> findVideo(Long Id);

    void setQuantity(Long id, int value);

    List<Video> listAllVideos();
}
