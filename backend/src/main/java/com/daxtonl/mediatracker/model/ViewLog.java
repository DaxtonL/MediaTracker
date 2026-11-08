package com.daxtonl.mediatracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ViewLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "media_id")
    private Media media;

    @Column(nullable = false)
    private LocalDate viewedOn;

    @Column(nullable = false)
    private int viewProgress;

    protected ViewLog() {}

    public ViewLog(LocalDate viewedOn, int viewProgress) {
        this.viewedOn = viewedOn;
        this.viewProgress = viewProgress;
    }

    public Long getId() { return id; }
    void setMedia(Media media) { this.media = media; }
    public LocalDate getViewedOn() { return viewedOn; }
    public int getViewProgress() { return viewProgress; }
}