package com.example.videly.video;

import com.example.videly.authentication.User;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "videos")
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "naive")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(name = "full_description", nullable = false)
    private String fullDescription;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToMany(mappedBy = "videos")
    private Set<User> users;

    @ManyToMany
    @JoinTable(name = "videos_categories", joinColumns = @JoinColumn(name = "video_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private Set<VideoCategory> categories;

    public Video(Long id) {
        this.id = id;
        this.categories = new HashSet<>();
    }

    public Video(Long id,
                 String name,
                 String shortDescription,
                 String fullDescription,
                 Integer quantity) {
        this.name = name;
        this.id = id;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.quantity = quantity;
        this.categories = new HashSet<>();
    }
}
