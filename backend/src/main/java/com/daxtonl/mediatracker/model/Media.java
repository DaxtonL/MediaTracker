package com.daxtonl.mediatracker.model;

import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tracker_id", "name"}))
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tracker_id")
    private Tracker tracker;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.WAITLIST;

    private Integer length;    // null = unknown (was -1)
    private Integer priority;  // null = none
    private Integer rating;    // null = not rated

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<ViewLog> logs = new ArrayList<>();

    protected Media() {}

    public Media(String name, MediaType type, Integer length, Integer priority) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.priority = priority;
    }

    // Same rules as your original logViewing()
    public void logViewing(ViewLog viewing) {
        viewing.setMedia(this);
        logs.add(viewing);
        if (length != null && getTotalViewProgress() >= length) {
            status = Status.FINISHED;
        } else {
            status = Status.VIEWING;
        }
    }

    public void removeLatestLog() {
        if (!logs.isEmpty()) {
            logs.remove(logs.size() - 1);
        }
    }

    public int getTotalViewProgress() {
        return logs.stream().mapToInt(ViewLog::getViewProgress).sum();
    }

    public Long getId() { return id; }
    public Tracker getTracker() { return tracker; }
    void setTracker(Tracker tracker) { this.tracker = tracker; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public MediaType getType() { return type; }
    public void setType(MediaType type) { this.type = type; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Integer getLength() { return length; }
    public void setLength(Integer length) { this.length = length; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public List<ViewLog> getLogs() { return logs; }
}