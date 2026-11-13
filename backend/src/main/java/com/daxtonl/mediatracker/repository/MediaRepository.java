package com.daxtonl.mediatracker.repository;
import java.util.List;

import com.daxtonl.mediatracker.model.Media;
import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MediaRepository extends JpaRepository<Media, Long> {
    boolean existsByTrackerIdAndName(Long trackerId, String name);
    List<Media> findByTrackerId(Long trackerId);

    // Replaces the original Filter classes (FilterStatus, FilterType, FilterRating).
    // A null parameter means "don't filter on this"; all given filters must pass (AND).
    // Unrated media (rating null) are excluded whenever a rating bound is given.
    @Query("""
            select m from Media m
            where m.tracker.id = :trackerId
              and (:status is null or m.status = :status)
              and (:type is null or m.type = :type)
              and (:minRating is null or m.rating >= :minRating)
              and (:maxRating is null or m.rating <= :maxRating)
            order by m.id
            """)
    List<Media> findFiltered(@Param("trackerId") Long trackerId,
                             @Param("status") Status status,
                             @Param("type") MediaType type,
                             @Param("minRating") Integer minRating,
                             @Param("maxRating") Integer maxRating);
}