package com.example.videly.video;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "categories")
public class VideoCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "naive")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Video> videos;

    public VideoCategory(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
