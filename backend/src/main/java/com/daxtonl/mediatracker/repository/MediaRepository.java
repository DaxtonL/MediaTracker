package com.daxtonl.mediatracker.repository;

import com.daxtonl.mediatracker.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
}