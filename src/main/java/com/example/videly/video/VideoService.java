package com.example.videly.video;

import com.example.videly.authentication.ApplicationUserService;
import com.example.videly.dao.UserVideoDAO;
import com.example.videly.dao.VideoDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VideoService {
    private final VideoDAO videoDAO;
    private final UserVideoDAO userVideoDAO;
    private final ApplicationUserService applicationUserService;

    public List<Video> listAllVideos() {
        return videoDAO.listAllVideos();
    }

    public boolean returnVideo(Long id, String username) {
        final boolean isUserActive = applicationUserService.verifyUserAccountState(username);
        boolean returnSucceeds = false;
        if (isUserActive) {
            Optional<Video> videoToReturn = videoDAO.findVideoById(id);

            if (videoToReturn.isPresent()) {
                Optional<Video> userVideo = userVideoDAO.findVideo(id, username);
                if (userVideo.isPresent()) {
                    userVideoDAO.returnVideo(id, username);
                    videoDAO.setQuantity(id, videoToReturn.get().getQuantity() + 1);
                    returnSucceeds = true;
                }
            }
        }

        return returnSucceeds;
    }

    public boolean rentVideo(String username, Long id) {
        final boolean isUserActive = applicationUserService.verifyUserAccountState(username);
        boolean insertSucceeds = false;
        if (isUserActive) {
            Optional<Video> video = videoDAO.findVideoById(id);
            if (video.isPresent() && video.get().getQuantity() > 0) {
                userVideoDAO.insertVideo(video.get(), username);
                videoDAO.setQuantity(id, video.get().getQuantity() - 1);
                insertSucceeds = true;
            }
        }
        return insertSucceeds;
    }

    public List<Video> getUserVideos(String username) {
        return userVideoDAO.listVideos(username);
    }

    public Optional<Video> findVideo(Long id) {
        return videoDAO.findVideoById(id);
    }
}
