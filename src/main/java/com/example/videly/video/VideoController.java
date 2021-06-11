package com.example.videly.video;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/videos")
@AllArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @GetMapping
    public String getVideosView(Model model) {
        final List<Video> videos = videoService.listAllVideos();
        model.addAttribute("videos", videos);

        return "video/videos";
    }
}
