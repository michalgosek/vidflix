package com.example.videly.registration;

import com.example.videly.authentication.ApplicationUser;
import com.example.videly.authentication.ApplicationUserService;
import com.example.videly.authentication.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final ApplicationUserService applicationUserService;
    private final EmailValidator emailValidator;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    public String Register(RegistrationForm registrationForm) {
        final boolean isValidEmail = emailValidator.test(registrationForm.getEmail());
        if (!isValidEmail) {
            final String EMAIL_NOT_VALID_MSG = "provided email for %s is not valid";
            throw new IllegalStateException(String.format(EMAIL_NOT_VALID_MSG, registrationForm.getEmail()));
        }

        final boolean isValidPassword = passwordValidator.test(registrationForm.getPassword());
        if (!isValidPassword) {
            final String USER_PASSWORD_NOT_VALID_MSG = "provided password for %s user is not valid";
            throw new IllegalStateException(String.format(USER_PASSWORD_NOT_VALID_MSG, registrationForm.getUsername()));
        }

        User user = new User(
                registrationForm.getUsername(),
                passwordEncoder.encode(registrationForm.getPassword()),
                registrationForm.getEmail(),
                true,
                true,
                true,
                true
        );

        final Set<SimpleGrantedAuthority> grantedAuthorities =
                Collections.singleton(new SimpleGrantedAuthority("USER"));

        applicationUserService.RegisterUser(new ApplicationUser(user, grantedAuthorities));

        // TODO: confirmation token

        return "";
    }
}
