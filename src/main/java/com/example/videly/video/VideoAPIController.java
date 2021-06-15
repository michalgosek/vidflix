package com.example.videly.video;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/v1/videos")
@AllArgsConstructor
public class VideoAPIController {
    private final VideoService videoService;

    @GetMapping(path = "user/rent/{id}")
    public String rentVideo(@PathVariable("id") Long id, Model model, Authentication authentication) {
        final boolean rentVideoSucceed = videoService.rentVideo(authentication.getName(), id);
        final Optional<List<Video>> videos = videoService.getUserVideos(authentication.getName());
        videos.ifPresent(videosList -> model.addAttribute("videos", videosList));
        return rentVideoSucceed ? "account/videos" : "503";
    }

    @GetMapping(path = "user/return/{id}")
    public String videoReturn(@PathVariable("id") Long id, Model model, Authentication authentication) {
        final boolean returnVideoSucceed = videoService.returnVideo(id, authentication.getName());
        final Optional<List<Video>> videos = videoService.getUserVideos(authentication.getName());
        videos.ifPresent(videosList -> model.addAttribute("videos", videosList));
        return returnVideoSucceed ? "account/videos" : "503";
    }

    @GetMapping(path = "user")
    public String getUserVideos(Model model, Authentication authentication) {
        final Optional<List<Video>> videos = videoService.getUserVideos(authentication.getName());
        videos.ifPresent(videosList -> model.addAttribute("videos", videosList));
        return "account/videos";
    }
}
