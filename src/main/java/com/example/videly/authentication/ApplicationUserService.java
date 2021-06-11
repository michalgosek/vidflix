package com.example.videly.authentication;

import com.example.videly.dao.ApplicationUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<ApplicationUser> user = dao.findByEmail(applicationUser.getEmail());
        user.ifPresentOrElse(foundUser -> {
            final boolean userWithNameExists = foundUser.getUsername().equals(applicationUser.getUsername());

            if (userWithNameExists) {
                final String USERNAME_FOUND_MSG = "Choose another username for creating account.";
                throw new IllegalStateException(USERNAME_FOUND_MSG);
            }

            final String USER_EMAIL_FOUND = "Choose another email was found for creating account.";
            throw new IllegalStateException(USER_EMAIL_FOUND);
        }, () -> {
            final int insertSucceedStatus = dao.insertUser(applicationUser);
            System.out.printf("Insertion %s user into database users status code %d\n", applicationUser.getUsername(),
                    insertSucceedStatus);
        });
    }
}
