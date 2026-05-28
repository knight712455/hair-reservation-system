package com.knight.hairreservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequest {

    private Long resourceId;

    private LocalDateTime start;
    private LocalDateTime end;
}