package com.example.projectbase.service;

import com.example.projectbase.domain.dto.*;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

  AuthenticationResponseDTO login(AuthenticationRequestDTO request);

  CommonResponseDTO signUp(UserCreateDTO userCreateDTO);

  CommonResponseDTO verifySignUp(String email, String token);

  CommonResponseDTO forgotPassword(String email);

  CommonResponseDTO verifyForgotPassword(String email, String token, String newPassword);

  CommonResponseDTO logout(HttpServletRequest request);

}
