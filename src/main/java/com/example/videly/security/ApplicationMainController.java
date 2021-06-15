package com.example.videly.security;

import com.example.videly.registration.RegistrationForm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class ApplicationMainController {

    @GetMapping("index")
    public String getIndexView() {
        return "index";
    }


    @GetMapping("dashboard")
    public String getDashboardView() {
        return "account/dashboard";
    }
}
