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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final String USER_NOT_FOUND_MSG = "Username %s not found in users table";
        final String ERR_MSG = String.format(USER_NOT_FOUND_MSG, username);
        return dao.loadUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ERR_MSG));
    }

    public void RegisterUser(ApplicationUser applicationUser) {
        final boolean isPresent = dao.findByEmail(applicationUser.getEmail()).isPresent();

        if (isPresent) {
            final String USER_NOT_FOUND_MSG = "User with %s email was found in users table";
            throw new IllegalStateException(String.format(USER_NOT_FOUND_MSG, applicationUser.getEmail()));
        }

        // todo to status code fix !
        final int insertSucceedStatus = dao.insertUser(applicationUser);
        System.out.println(insertSucceedStatus);
    }
}
