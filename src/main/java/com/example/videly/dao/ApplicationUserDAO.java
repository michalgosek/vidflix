package com.example.videly.dao;

import com.example.videly.authentication.ApplicationUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

public interface ApplicationUserDAO{
    Optional<ApplicationUser> loadUserByUsername(String username) throws UsernameNotFoundException;
}