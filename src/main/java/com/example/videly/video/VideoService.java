package com.example.videly.video;

import com.example.videly.authentication.ApplicationUser;
import com.example.videly.authentication.ApplicationUserService;
import com.example.videly.dao.VideoCategoryDAO;
import com.example.videly.dao.VideoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    private final VideoDAO videoDAO;
    private final VideoCategoryDAO videoCategoryDAO;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public VideoService(@Qualifier("MySQLVideosRepository") VideoDAO videoDAO,
                        @Qualifier("MySQLCategoriesRepository") VideoCategoryDAO videoCategoryDAO,
                        ApplicationUserService applicationUserService) {
        this.videoDAO = videoDAO;
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

    public boolean returnVideo(Long videoID, Authentication authentication) {
        Optional<ApplicationUser> applicationUser = getApplicationUser(authentication);
        if (applicationUser.isPresent()) {
            final boolean isUserActive = applicationUserService.verifyUserAccountState(applicationUser.get());
            final boolean isUserHasVideo = videoDAO.isUserHasVideo(videoID, applicationUser.get().getId());
            final Optional<Video> videoFromShop = videoDAO.findVideo(videoID);
            final boolean canReturn = isUserActive && videoFromShop.isPresent() && isUserHasVideo;

            if (canReturn) {
                videoDAO.returnUserVideo(videoID, applicationUser.get().getId());
                videoDAO.setQuantity(videoID, videoFromShop.get().getQuantity() + 1);
                return true;
            }
        }

        return false;
    }

    public boolean rentVideo(Authentication authentication, Long videoID) {
        Optional<ApplicationUser> applicationUser = getApplicationUser(authentication);
        if (applicationUser.isPresent()) {
            final boolean isUserActive = applicationUserService.verifyUserAccountState(applicationUser.get());
            final boolean isUserHasVideo = videoDAO.isUserHasVideo(videoID, applicationUser.get().getId());
            final Optional<Video> videoToRent = videoDAO.findVideo(videoID);

            final boolean canRent = !isUserHasVideo && isUserActive && videoToRent.isPresent() && videoToRent.get().getQuantity() > 0;
            if (canRent) {
                videoDAO.addUserVideo(videoID, applicationUser.get().getId());
                videoDAO.setQuantity(videoID, videoToRent.get().getQuantity() - 1);
                return true;
            }
        }
        return false;
    }


    private Optional<ApplicationUser> getApplicationUser(Authentication authentication) {
        final Object principal = authentication.getPrincipal();
        if (!(principal instanceof ApplicationUser)) {
            return Optional.empty();
        }

        ApplicationUser applicationUser = (ApplicationUser) principal;
        return Optional.of(applicationUser);
    }

    public Optional<List<Video>> getUserVideos(Authentication authentication) {
        final Optional<ApplicationUser> applicationUser = getApplicationUser(authentication);
        if (applicationUser.isPresent()) {
            return videoDAO.listUserVideos(applicationUser.get().getId());
        }

        return Optional.empty();
    }

    public Optional<Video> findVideo(Long videoID) {
        return videoDAO.findVideo(videoID);
    }
}
