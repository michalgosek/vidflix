package com.example.videly.registration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @GetMapping
    public String getRegisterView(Model model) {
        model.addAttribute("accountData", new RegistrationForm());
        return "register";
    }
}
