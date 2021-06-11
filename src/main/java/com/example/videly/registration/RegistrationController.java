package com.example.videly.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public String GetRegisterView(Model model, @ModelAttribute("accountData") RegistrationForm form) {
        try {
            registrationService.Register(form);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        final String REGISTRATION_SUCCEED_MSG = "Username %s with email %s was successfully registered in Videly :)";
        model.addAttribute("success", String.format(REGISTRATION_SUCCEED_MSG, form.getUsername(), form.getEmail()));

        return "register";
    }
}
