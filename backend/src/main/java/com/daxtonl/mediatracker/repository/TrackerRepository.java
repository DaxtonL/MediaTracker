package com.daxtonl.mediatracker.repository;

import com.daxtonl.mediatracker.model.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TrackerRepository extends JpaRepository<Tracker, Long> {
    Optional<Tracker> findByName(String name);
}