package com.daxtonl.mediatracker.repository;
import java.util.List;

import com.daxtonl.mediatracker.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
    boolean existsByTrackerIdAndName(Long trackerId, String name);
    List<Media> findByTrackerId(Long trackerId);
}