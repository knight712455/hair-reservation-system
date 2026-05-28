package com.knight.hairreservation.service;

import com.knight.hairreservation.dto.LoginRequest;
import com.knight.hairreservation.dto.SignupRequest;
import com.knight.hairreservation.entity.User;
import com.knight.hairreservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void signup(SignupRequest request) {
        System.out.println("회원가입 시작");
        System.out.println(request.getEmail());
        System.out.println("저장 직전");
        System.out.println("저장 완료");
        boolean exists = userRepository
                .findByEmail(request.getEmail())
                .isPresent();

        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userRepository.save(user);
    }

    public User login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("이메일 없음")
                );

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        return user;
    }
}