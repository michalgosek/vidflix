package com.example.videly.video;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/v1/videos")
@AllArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @GetMapping(path = "{id}")
    public String GetMapping(@PathVariable("id") Long id, Authentication authentication) {
        final boolean rentVideoSucceed = videoService.rentVideo(authentication.getName(), id);
        return rentVideoSucceed ? "index" : "error" ;
    }
}
