package com.knight.hairreservation.controller;

import com.knight.hairreservation.dto.LoginRequest;
import com.knight.hairreservation.dto.LoginResponse;
import com.knight.hairreservation.dto.SignupRequest;
import com.knight.hairreservation.entity.User;
import com.knight.hairreservation.jwt.JwtProvider;
import com.knight.hairreservation.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder
            passwordEncoder;
    @PostMapping("/signup")
    public String signup(

            @RequestBody
            SignupRequest request

    ) {

        boolean exists =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .isPresent();

        if (exists) {

            throw new IllegalArgumentException(
                    "이미 존재하는 이메일입니다."
            );
        }

        User user = new User();

        user.setEmail(
                request.getEmail()
        );

        user.setPassword(

                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setName(
                request.getName()
        );

        userRepository.save(user);

        return "회원가입 성공";
    }

    @PostMapping("/login")
    public LoginResponse login(

            @RequestBody
            LoginRequest request

    ) {

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->

                                new IllegalArgumentException(
                                        "이메일 없음"
                                )
                        );

        if (

                !passwordEncoder.matches(

                        request.getPassword(),

                        user.getPassword()
                )

        ) {

            throw new IllegalArgumentException(
                    "비밀번호 불일치"
            );
        }

        String token =
                jwtProvider.createToken(
                        user.getId()
                );

        return new LoginResponse(token);
    }
}