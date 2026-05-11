package com.knight.hairreservation.controller;

import com.knight.hairreservation.dto.ReservationRequest;
import com.knight.hairreservation.dto.ReservationResponse;
import com.knight.hairreservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponse create(
            @RequestBody ReservationRequest request
    ) {
        return reservationService.create(request);
    }

    @GetMapping
    public Page<ReservationResponse> getReservations(Pageable pageable) {
        return reservationService.getReservations(pageable);
    }

    @PatchMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return "취소 완료";
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", e.getMessage()));
    }
}