package com.knight.hairreservation.repository;

import com.knight.hairreservation.entity.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r FROM Reservation r
        WHERE r.resource.id = :resourceId
        AND r.status = 'CONFIRMED'
        AND r.slotStart < :end
        AND r.slotEnd > :start
    """)
    List<Reservation> findConflicts(
            @Param("resourceId") Long resourceId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
    Page<Reservation> findByResourceId(
            Long resourceId,
            Pageable pageable
    );
    @Query("""
        SELECT r FROM Reservation r
        WHERE r.resource.id = :resourceId
        AND r.status = 'CONFIRMED'
        AND r.slotStart < :end
        AND r.slotEnd > :start
    """)
    List<Reservation> findByResourceAndDate(
            @Param("resourceId") Long resourceId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}