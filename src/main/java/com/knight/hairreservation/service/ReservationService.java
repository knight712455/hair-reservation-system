package com.knight.hairreservation.service;

import com.knight.hairreservation.dto.ReservationRequest;
import com.knight.hairreservation.dto.ReservationResponse;
import com.knight.hairreservation.entity.Reservation;
import com.knight.hairreservation.entity.ReservationStatus;
import com.knight.hairreservation.entity.Resource;
import com.knight.hairreservation.entity.User;
import com.knight.hairreservation.repository.ReservationRepository;
import com.knight.hairreservation.repository.ResourceRepository;
import com.knight.hairreservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.knight.hairreservation.dto.TimeSlotResponse;
import java.util.List;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    // 예약 생성
    @Transactional
    public ReservationResponse create(

            ReservationRequest request,

            Long userId

    ) {

        System.out.println("서비스 시작");

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("유저 없음")
                );

        System.out.println("유저 조회 완료");

        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() ->
                        new IllegalArgumentException("디자이너 없음")
                );

        System.out.println("디자이너 조회 완료");

        // 시간 겹침 체크
        List<Reservation> conflicts =
                reservationRepository.findConflicts(
                        request.getResourceId(),
                        request.getStart(),
                        request.getEnd()
                );

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }

        System.out.println("충돌 검사 완료");

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setResource(resource);

        reservation.setSlotStart(request.getStart());
        reservation.setSlotEnd(request.getEnd());

        reservation.setStatus(ReservationStatus.CONFIRMED);

        System.out.println("저장 직전");

        Reservation saved =
                reservationRepository.save(reservation);

        System.out.println("저장 완료");

        return new ReservationResponse(
                saved.getId(),
                saved.getUser().getName(),
                saved.getResource().getName(),
                saved.getSlotStart(),
                saved.getSlotEnd(),
                saved.getStatus().name()
        );
    }

    // 예약 조회
    @Transactional(readOnly = true)
    public Page<ReservationResponse> getReservations(
            Pageable pageable
    ) {

        return reservationRepository.findAll(pageable)
                .map(r -> new ReservationResponse(
                        r.getId(),
                        r.getUser().getName(),
                        r.getResource().getName(),
                        r.getSlotStart(),
                        r.getSlotEnd(),
                        r.getStatus().name()
                ));
    }

    // 예약 취소
    @Transactional
    public void cancelReservation(Long id) {

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException("예약 없음")
                        );

        reservation.setStatus(ReservationStatus.CANCELED);
    }
    public Page<ReservationResponse>
    getReservationsByResource(

            Long resourceId,
            Pageable pageable

    ) {

        return reservationRepository
                .findByResourceId(
                        resourceId,
                        pageable
                )
                .map(r -> new ReservationResponse(

                        r.getId(),
                        r.getUser().getName(),
                        r.getResource().getName(),
                        r.getSlotStart(),
                        r.getSlotEnd(),
                        r.getStatus().name()

                ));
    }
    public List<TimeSlotResponse>
    getTimeSlots(

            Long resourceId,
            LocalDate date

    ) {

        List<String> times = List.of(

                "10:00",
                "11:00",
                "12:00",
                "13:00",
                "14:00",
                "15:00",
                "16:00",
                "17:00"

        );

        List<Reservation> reservations =
                reservationRepository.findAll();

        return times.stream()

                .map(time -> {

                    boolean reserved =

                            reservations.stream()

                                    .anyMatch(r ->

                                            r.getStatus().name()
                                                    .equals("CONFIRMED")

                                                    &&

                                                    r.getResource().getId()
                                                            .equals(resourceId)

                                                    &&

                                                    r.getSlotStart()
                                                            .toLocalDate()
                                                            .equals(date)

                                                    &&

                                                    r.getSlotStart()
                                                            .toLocalTime()
                                                            .toString()
                                                            .startsWith(time)

                                    );

                    return new TimeSlotResponse(
                            time,
                            !reserved
                    );

                })

                .toList();
    }
}