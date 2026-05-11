package com.knight.hairreservation.service;

import com.knight.hairreservation.dto.TimeSlotResponse;
import com.knight.hairreservation.entity.Reservation;
import com.knight.hairreservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final ReservationRepository reservationRepository;

    public List<TimeSlotResponse> getAvailableTimes(
            Long resourceId,
            LocalDate date
    ) {

        List<LocalTime> slots = List.of(
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0),
                LocalTime.of(17, 0),
                LocalTime.of(18, 0),
                LocalTime.of(19, 0)
        );

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Reservation> reservations =
                reservationRepository.findByResourceAndDate(
                        resourceId,
                        start,
                        end
                );

        return slots.stream().map(time -> {

            LocalDateTime slotStart =
                    date.atTime(time);

            LocalDateTime slotEnd =
                    slotStart.plusHours(1);

            boolean reserved =
                    reservations.stream().anyMatch(r ->
                            r.getSlotStart().isBefore(slotEnd)
                                    && r.getSlotEnd().isAfter(slotStart)
                    );

            return new TimeSlotResponse(
                    time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    !reserved
            );

        }).toList();
    }
}