package com.example.videly.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public String GetRegisterView(@ModelAttribute("accountData") RegistrationForm form) {
        registrationService.Register(form);


        System.out.printf(form.getUsername(), form.getPassword(), form.getEmail());
        return "registration";
    }
}
