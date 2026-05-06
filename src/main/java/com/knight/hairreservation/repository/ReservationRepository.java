package com.knight.hairreservation.repository;

import com.knight.hairreservation.entity.Reservation;
import org.springframework.data.jpa.repository.*;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 🔥 겹치는 예약 + 락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r FROM Reservation r
        WHERE r.resource.id = :resourceId
        AND r.slotStart < :end
        AND r.slotEnd > :start
    """)
    List<Reservation> findConflicts(
            @Param("resourceId") Long resourceId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}