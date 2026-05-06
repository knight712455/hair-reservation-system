package com.knight.hairreservation.controller;

import com.knight.hairreservation.dto.ReservationResponse;
import com.knight.hairreservation.entity.Reservation;
import com.knight.hairreservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    // ✅ 예약 생성
    @PostMapping
    public Reservation create(
            @RequestParam Long userId,
            @RequestParam Long resourceId,
            @RequestParam String start,
            @RequestParam String end
    ) {
        return reservationService.createReservation(
                userId,
                resourceId,
                LocalDateTime.parse(start),
                LocalDateTime.parse(end)
        );
    }

    // ✅ 예약 조회 (페이징)
    @GetMapping
    public Page<ReservationResponse> getReservations(Pageable pageable) {
        return reservationService.getReservations(pageable);
    }

    // ✅ 예약 취소
    @PatchMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return "취소 완료";
    }

    // ✅ 예외 처리
    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException e) {
        return e.getMessage();
    }
}