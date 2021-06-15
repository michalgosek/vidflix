package com.example.videly.video;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/videos")
@AllArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @GetMapping()
    public String getVideosView(Model model) {
        final Optional<List<Video>> videos = videoService.listAllVideos();
        videos.ifPresent(videoList -> model.addAttribute("videos", videoList));
        return "videos/list";
    }

    @GetMapping(path = "categories")
    public String getCategoriesView(Model model) {
        final Optional<List<VideoCategory>> categories = videoService.getVideosCategories();
        categories.ifPresent(c -> model.addAttribute("categories", c));
        return "videos/categories";
    }

    @GetMapping(path = "categories/{id}")
    public String getVideoByCategory(@PathVariable("id") Long id, Model model) {
        final Optional<List<Video>> videos = videoService.listVideosByCategory(id);
        videos.ifPresent(v -> model.addAttribute("videos", v));
        return "videos/video_category";
    }

    @GetMapping(path = "{id}")
    public String getVideoView(@PathVariable("id") Long id, Model model) {
        Optional<Video> video = videoService.findVideo(id);
        video.ifPresent(v -> {
            model.addAttribute("video", v);
            model.addAttribute("categories", v.getCategories());
        });
        return "videos/video";
    }
}
