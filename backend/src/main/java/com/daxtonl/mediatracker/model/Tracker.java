package com.daxtonl.mediatracker.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "tracker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Media> mediaList = new ArrayList<>();

    protected Tracker() {}

    public Tracker(String name) {
        this.name = name;
    }

    public void addMedia(Media m) {
        m.setTracker(this);
        mediaList.add(m);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Media> getMediaList() { return mediaList; }
}