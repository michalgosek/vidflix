package com.example.videly.authentication;

import lombok.Data;
import java.util.Set;

@Data
public class Role {
    private final Long id;
    private final String name;
    private Set<User> users;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
