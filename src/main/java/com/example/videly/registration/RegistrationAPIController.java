package com.example.videly.registration;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/v1/registration")
@AllArgsConstructor
public class RegistrationAPIController {
    private final RegistrationService registrationService;

    @PostMapping
    public String getRegisterView(Model model, @ModelAttribute("accountData") RegistrationForm form) {
        try {  // fixme desing smell for handling user verification
            registrationService.createUser(form);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        final String REGISTRATION_SUCCEED_MSG = "Username %s with email %s was successfully registered in Videly :)";
        model.addAttribute("success", String.format(REGISTRATION_SUCCEED_MSG, form.getUsername(), form.getEmail()));
        return "register";
    }
}
