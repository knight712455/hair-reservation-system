package com.knight.hairreservation.service;

import com.knight.hairreservation.dto.ReservationResponse;
import com.knight.hairreservation.entity.*;
import com.knight.hairreservation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    // ✅ 예약 생성
    @Transactional
    public Reservation createReservation(
            Long userId,
            Long resourceId,
            LocalDateTime start,
            LocalDateTime end
    ) {

        // 🔥 겹침 체크 + 락
        List<Reservation> conflicts =
                reservationRepository.findConflicts(resourceId, start, end);

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("이미 예약된 시간입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("리소스 없음"));

        Reservation reservation = Reservation.builder()
                .user(user)
                .resource(resource)
                .slotStart(start)
                .slotEnd(end)
                .status("CONFIRMED")
                .build();

        return reservationRepository.save(reservation);
    }

    // ✅ 예약 취소
    @Transactional
    public void cancelReservation(Long id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("예약 없음"));

        if ("CANCELED".equals(reservation.getStatus())) {
            throw new RuntimeException("이미 취소된 예약입니다.");
        }

        reservation.setStatus("CANCELED");
    }

    // ✅ 예약 조회 (페이징)
    public Page<ReservationResponse> getReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable)
                .map(r -> ReservationResponse.builder()
                        .id(r.getId())
                        .userName(r.getUser().getName())
                        .resourceName(r.getResource().getName())
                        .slotStart(r.getSlotStart())
                        .slotEnd(r.getSlotEnd())
                        .status(r.getStatus())
                        .build());
    }
}