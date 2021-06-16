package com.example.videly.authentication;

import com.example.videly.dao.ApplicationUserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
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
        log.info(String.format(USER_NOT_FOUND_MSG, username));
        return dao.loadUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ERR_MSG));
    }

    public void createUser(ApplicationUser applicationUser) {
        Optional<ApplicationUser> user = dao.findByEmail(applicationUser.getEmail());
        user.ifPresentOrElse(foundUser -> {
            verifyUserExistence(applicationUser, foundUser);
        }, () -> {
            final int insertSucceedStatus = dao.insertUser(applicationUser);
            log.info(String.format("Insertion %s user into database users status code %d\n",
                    applicationUser.getUsername(),
                    insertSucceedStatus));

            final int insertUserRoleSucceedStatus = dao.insertUserRole(applicationUser, UserRole.ROLE_USER.toString());
            log.info(String.format("Insertion %s user role %s into database users status code %d\n",
                    applicationUser.getUsername(),
                    UserRole.ROLE_USER.name(),
                    insertUserRoleSucceedStatus));
        });
    }

    private void verifyUserExistence(ApplicationUser applicationUser, ApplicationUser foundUser) {
        final boolean userWithNameExists = foundUser.getUsername().equals(applicationUser.getUsername());

        if (userWithNameExists) {
            final String USERNAME_FOUND_MSG = "Choose another username for creating account.";
            final String USERNAME_FOUND_LOG_MSG = "Registration failed for username %s with email %s";
            final String USERNAME_FOUND_REASON_LOG = "Reason: %s already exists in the users_table";
            log.error(String.format(USERNAME_FOUND_LOG_MSG, applicationUser.getUsername(), applicationUser.getEmail()));
            log.error(String.format(USERNAME_FOUND_REASON_LOG, applicationUser.getUsername()));
            throw new IllegalStateException(USERNAME_FOUND_MSG);
        }

        final String USER_EMAIL_FOUND = "Choose another email was found for creating account.";
        throw new IllegalStateException(USER_EMAIL_FOUND);
    }

    public boolean verifyUserAccountState(ApplicationUser applicationUser) {
        return applicationUser.isAccountNonExpired()
                && applicationUser.isAccountNonLocked()
                && applicationUser.isCredentialsNonExpired()
                && applicationUser.isEnabled()
                && applicationUser.hasUserRoleAssigned();
    }
}
