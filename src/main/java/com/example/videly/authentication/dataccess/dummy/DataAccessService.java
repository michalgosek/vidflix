package com.example.videly.authentication.dataccess.dummy;

import com.example.videly.authentication.ApplicationUser;
import com.example.videly.authentication.User;
import com.example.videly.dao.ApplicationUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository("dummy")
public class DataAccessService implements ApplicationUserDAO {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataAccessService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> loadUserByUsername(String username) throws UsernameNotFoundException {
        return getApplicationUsers()
                .stream().filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    @Override
    public int insertUser(ApplicationUser applicationUser) {
        return 0;
    }

    @Override
    public Optional<ApplicationUser> findByEmail(String email) {
        return getApplicationUsers()
                .stream().filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers() {
        User admin = new User(1L,
                "admin",
                passwordEncoder.encode("pass"),
                "admin@spring.com",
                true,
                true,
                true,
                true
        );

        return List.of(
                new ApplicationUser(admin, Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
    }
}
