package com.knight.hairreservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Resource resource;

    private LocalDateTime slotStart;

    private LocalDateTime slotEnd;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}