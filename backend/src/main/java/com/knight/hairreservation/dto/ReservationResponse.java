package com.knight.hairreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationResponse {

    private Long id;

    private String userName;

    private String resourceName;

    private LocalDateTime slotStart;

    private LocalDateTime slotEnd;

    private String status;
}