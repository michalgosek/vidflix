package com.example.videly.video;

import com.example.videly.authentication.ApplicationUserService;
import com.example.videly.dao.VideoDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VideoService {
    private final VideoDAO dao;
    private final ApplicationUserService applicationUserService;

    public List<Video> listAllVideos() {
        return dao.listAllVideos();
    }

    public Optional<Video> findVideoById(Long id) {
        return dao.findVideoById(id);
    }

    public boolean rentVideo(String username, Long videoId) {
        return applicationUserService.verifyUserAccountState(username) && dao.updateVideoQuantity(videoId);
    }
}
