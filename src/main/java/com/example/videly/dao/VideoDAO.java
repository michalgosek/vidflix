package com.example.videly.dao;

import com.example.videly.video.Video;

import java.util.List;
import java.util.Optional;

public interface VideoDAO {
    Optional<Video> findVideoByName(String name);

    Optional<Video> findVideoById(Long Id);

    boolean updateVideoQuantity(Long Id);

    List<Video> listAllVideos();
}
