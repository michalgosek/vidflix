package com.example.videly.video;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/v1/videos")
@AllArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @GetMapping(path = "user/rent/{id}")
    public String rentVideo(@PathVariable("id") Long id, Model model, Authentication authentication) {
        final boolean rentVideoSucceed = videoService.rentVideo(authentication.getName(), id);
        model.addAttribute("videos", videoService.getUserVideos(authentication.getName()));
        return rentVideoSucceed ? "account/videos" : "503";
    }

    @GetMapping(path = "user/return/{id}")
    public String videoReturn(@PathVariable("id") Long id, Model model, Authentication authentication) {
        final boolean returnVideoSucceed = videoService.returnVideo(id, authentication.getName());
        var videos = videoService.getUserVideos(authentication.getName());
        model.addAttribute("videos",videos );
        return returnVideoSucceed ? "account/videos" : "503";
    }

    @GetMapping(path = "user")
    public String getUserVideos(Model model, Authentication authentication) {
        model.addAttribute("videos", videoService.getUserVideos(authentication.getName()));
        return "account/videos";
    }
}
