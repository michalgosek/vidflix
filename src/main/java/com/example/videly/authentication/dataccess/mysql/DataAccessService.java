package com.example.videly.authentication.dataccess.mysql;

import com.example.videly.authentication.ApplicationUser;
import com.example.videly.authentication.Role;
import com.example.videly.authentication.User;
import com.example.videly.dao.ApplicationUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository("MySQL")
public class DataAccessService implements ApplicationUserDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Optional<User> selectUserByEmail(String email) {
        final String query = """
                SELECT id, email, username, password, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled
                FROM users
                WHERE email = ?""";
        return Optional.ofNullable(jdbcTemplate.query(query, mapUserFromDatabase(), email));
    }

    private Optional<User> selectUser(String username) {
        final String query = """
                SELECT id, email, username, password, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled
                FROM users
                WHERE username = ?""";
        return Optional.ofNullable(jdbcTemplate.query(query, mapUserFromDatabase(), username));
    }


    private List<Long> selectUserRoles(Long id) {
        final String query = "SELECT role_id FROM users_roles WHERE user_id = ?";
        return jdbcTemplate.query(query, mapUserRolesFromDatabase(), id);
    }

    private List<Role> selectAllRoles() {
        final String query = "SELECT id, name FROM roles";
        return jdbcTemplate.query(query, mapRolesFromDatabase());
    }

    private RowMapper<Role> mapRolesFromDatabase() {
        return (resultSet, i) -> {
            final Long id = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            return new Role(id, name);
        };
    }

    private RowMapper<Long> mapUserRolesFromDatabase() {
        return (rs, rowNum) -> rs.getLong("role_id");
    }

    private ResultSetExtractor<User> mapUserFromDatabase() {
        return resultSet -> {
            if (resultSet.next()) {
                final Long id = resultSet.getLong("id");
                final String userName = resultSet.getString("username");
                final String password = resultSet.getString("password");
                final String email = resultSet.getString("email");
                final boolean isAccountNotExpired = resultSet.getBoolean("is_account_non_expired");
                final boolean isAccountNotLocked = resultSet.getBoolean("is_account_non_locked");
                final boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
                final boolean isEnabled = resultSet.getBoolean("is_enabled");

                return new User(
                        id,
                        userName,
                        password,
                        email,
                        isAccountNotExpired,
                        isAccountNotLocked,
                        isCredentialsNonExpired,
                        isEnabled);
            } else {
                return null;
            }
        };
    }

    @Override
    public Optional<ApplicationUser> loadUserByUsername(String username) {
        final User appUser = selectUser(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return getApplicationUser(appUser);
    }

    @Override
    public int insertUser(ApplicationUser applicationUser) {
        final String query = "INSERT INTO users (email, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, password, username) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                query,
                applicationUser.getEmail(),
                applicationUser.isAccountNonExpired(),
                applicationUser.isAccountNonLocked(),
                applicationUser.isCredentialsNonExpired(),
                applicationUser.isEnabled(),
                applicationUser.getPassword(),
                applicationUser.getUsername());
    }

    @Override
    public Optional<ApplicationUser> findByEmail(String email) {
        final Optional<User> user = selectUserByEmail(email);

        if (user.isEmpty()) {
            final String USER_NOT_FOUND_MSG = "User with %s email not found in users table";
            final String ERR_MSG = String.format(USER_NOT_FOUND_MSG, email);
            System.out.println(ERR_MSG);
            return Optional.empty();
        }

        return getApplicationUser(user.get());
    }

    private Optional<ApplicationUser> getApplicationUser(User appUser) {
        final Map<Long, String> roles = selectAllRoles().stream()
                .collect(Collectors.toMap(Role::getId, Role::getName));

        final Set<SimpleGrantedAuthority> authorities = selectUserRoles(appUser.getId())
                .stream().map(roleID -> new SimpleGrantedAuthority(roles.get(roleID)))
                .collect(Collectors.toSet());

        return Optional.of(new ApplicationUser(appUser, authorities));
    }
}