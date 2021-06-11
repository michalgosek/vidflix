package com.example.videly.video;

import com.example.videly.dao.VideoDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VideoService {
    private final VideoDAO dao;

    public List<Video> listAllVideos() {
        return dao.listAllVideos();
    }
}
