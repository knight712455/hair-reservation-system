package com.knight.hairreservation.dto;

import lombok.Getter;

@Getter
public class SignupRequest {

    private String email;

    private String password;

    private String name;
}