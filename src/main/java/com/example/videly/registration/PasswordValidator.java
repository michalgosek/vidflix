package com.example.videly.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class PasswordValidator implements Predicate<String> {
    public static final Pattern VALID_USER_PASSWORD_REGEX
            = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean test(String userPassword) {
        return VALID_USER_PASSWORD_REGEX.matcher(userPassword).find();
    }
}
