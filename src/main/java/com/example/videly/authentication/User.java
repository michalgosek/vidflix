package com.example.videly.authentication;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private final String username;

    @Column(name = "password", nullable = false)
    private final String password;

    @Column(name = "is_account_non_expired", nullable = false)
    private final boolean isAccountNonExpired;

    @Column(name = "is_account_non_locked", nullable = false)
    private final boolean isAccountNonLocked;

    @Column(name = "is_credentials_non_expired", nullable = false)
    private final boolean isCredentialsNonExpired;

    @Column(name = "is_enabled", nullable = false)
    private final boolean isEnabled;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public User(Long id,
                String username,
                String password,
                boolean isAccountNonExpired,
                boolean isAccountNonLocked,
                boolean isCredentialsNonExpired,
                boolean isEnabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }
}
