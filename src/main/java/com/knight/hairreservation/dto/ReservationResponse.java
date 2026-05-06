package com.knight.hairreservation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationResponse {

    private Long id;
    private String userName;
    private String resourceName;
    private LocalDateTime slotStart;
    private LocalDateTime slotEnd;
    private String status;
}