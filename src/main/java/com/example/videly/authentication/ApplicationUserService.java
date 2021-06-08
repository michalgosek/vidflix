package com.example.videly.authentication;

import com.example.videly.dao.ApplicationUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {
    private final ApplicationUserDAO dao;

    @Autowired
    public ApplicationUserService(@Qualifier("MySQL") ApplicationUserDAO dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return dao.loadUserByUsername(s).orElseThrow(() -> new UsernameNotFoundException(String.format("%s not found.%n", s)));
    }
}
