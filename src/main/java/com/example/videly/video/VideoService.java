package com.example.videly.video;

import com.example.videly.authentication.ApplicationUserService;
import com.example.videly.dao.UserVideoDAO;
import com.example.videly.dao.VideoCategoryDAO;
import com.example.videly.dao.VideoDAO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    private final VideoDAO videoDAO;
    private final UserVideoDAO userVideoDAO;
    private final VideoCategoryDAO videoCategoryDAO;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public VideoService(@Qualifier("MySQLVideosRepository") VideoDAO videoDAO,
                        @Qualifier("MySQLUserVideosRepository") UserVideoDAO userVideoDAO,
                        @Qualifier("MySQLCategoriesRepository") VideoCategoryDAO videoCategoryDAO,
                        ApplicationUserService applicationUserService) {
        this.videoDAO = videoDAO;
        this.userVideoDAO = userVideoDAO;
        this.videoCategoryDAO = videoCategoryDAO;
        this.applicationUserService = applicationUserService;
    }


    public Optional<List<Video>> listAllVideos() {
        return videoDAO.listAllVideos();
    }

    public Optional<List<VideoCategory>> getVideosCategories() {
        return videoCategoryDAO.listAllCategories();
    }

    public Optional<List<Video>> listVideosByCategory(Long id) {
        return videoDAO.findVideosFromCategory(id);
    }

    public boolean returnVideo(Long id, String username) {
        final boolean isUserActive = applicationUserService.verifyUserAccountState(username);
        final Optional<Video> rentedVideo = userVideoDAO.findVideo(id, username);
        final Optional<Video> videoFromShop = videoDAO.findVideo(id);
        final boolean canReturn = isUserActive && videoFromShop.isPresent() && rentedVideo.isPresent();

        if (canReturn) {
            userVideoDAO.returnVideo(id, username);
            videoDAO.setQuantity(id, videoFromShop.get().getQuantity() + 1);
            return true;
        }

        return false;
    }

    public boolean rentVideo(String username, Long id) {
        final boolean isUserActive = applicationUserService.verifyUserAccountState(username);
        final Optional<Video> videoToRent = videoDAO.findVideo(id);

        final boolean canRent = isUserActive && videoToRent.isPresent() && videoToRent.get().getQuantity() > 0;
        if (canRent) {
            userVideoDAO.insertVideo(videoToRent.get(), username);
            videoDAO.setQuantity(id, videoToRent.get().getQuantity() - 1);
            return true;
        }

        return false;
    }

    public Optional<List<Video>> getUserVideos(String username) {
        return userVideoDAO.listVideos(username);
    }

    public Optional<Video> findVideo(Long id) {
        return videoDAO.findVideo(id);
    }
}
