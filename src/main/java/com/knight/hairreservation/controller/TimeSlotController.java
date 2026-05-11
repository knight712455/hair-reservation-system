package com.knight.hairreservation.controller;

import com.knight.hairreservation.dto.TimeSlotResponse;
import com.knight.hairreservation.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @GetMapping("/timeslots")
    public List<TimeSlotResponse> getTimeSlots(
            @RequestParam Long resourceId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return timeSlotService.getAvailableTimes(
                resourceId,
                date
        );
    }
}