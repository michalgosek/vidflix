package com.example.videly.authentication;

import com.example.videly.video.Video;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private final String username;

    @Column(name = "password", nullable = false)
    private final String password;

    @Column(name = "email", nullable = false)
    private final String email;

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

    @ManyToMany
    @JoinTable(name = "users_videos", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "video_id", referencedColumnName = "id")
    )
    private Set<Video> videos;


    public User(Long id,
                String username,
                String password,
                String email,
                boolean isAccountNonExpired,
                boolean isAccountNonLocked,
                boolean isCredentialsNonExpired,
                boolean isEnabled) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    public User(String username,
                String password,
                String email,
                boolean isAccountNonExpired,
                boolean isAccountNonLocked,
                boolean isCredentialsNonExpired,
                boolean isEnabled,
                Set<Role> roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.roles = roles;
    }

    public User(String username,
                String password,
                String email,
                boolean isAccountNonExpired,
                boolean isAccountNonLocked,
                boolean isCredentialsNonExpired,
                boolean isEnabled) {
        this(
                username,
                password,
                email,
                isAccountNonExpired,
                isAccountNonLocked,
                isCredentialsNonExpired,
                isEnabled,
                null);
    }
}
