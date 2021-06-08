package com.example.videly.authentication;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @SequenceGenerator(name = "roles_sequence", sequenceName = "roles_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_sequence")
    @Column(name = "id", updatable = false)
    private final Long id;

    @Column(nullable = false)
    private final String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
