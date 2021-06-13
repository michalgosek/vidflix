package com.example.videly.security;

import com.example.videly.registration.RegistrationForm;
import com.example.videly.video.Video;
import com.example.videly.video.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class ApplicationMainController {
    private final VideoService videoService;

    @GetMapping("index")
    public String getIndexView() {
        return "index";
    }

    @GetMapping("registration")
    public String getRegisterView(Model model) {
        model.addAttribute("accountData", new RegistrationForm());
        return "register";
    }

    @GetMapping("/videos")
    public String getVideosView(Model model) {
        final List<Video> videos = videoService.listAllVideos();
        model.addAttribute("videos", videos);
        return "videos/list";
    }

    @GetMapping(path = "/videos/{id}")
    public String getVideoView(@PathVariable("id") Long id, Model model) {
        Optional<Video> video = videoService.findVideoById(id);
        video.ifPresentOrElse(v -> model.addAttribute("video", v), () -> System.out.println("vide with not found"));
        return "videos/video";
    }

    @GetMapping("dashboard")
    public String getDashboardView() {
        return "account/dashboard";
    }
}
