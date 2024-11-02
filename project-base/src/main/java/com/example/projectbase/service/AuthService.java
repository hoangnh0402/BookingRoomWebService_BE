package com.example.projectbase.service;

import com.example.projectbase.domain.dto.AuthenticationRequestDTO;
import com.example.projectbase.domain.dto.AuthenticationResponseDTO;
import com.example.projectbase.domain.dto.LoginRequestDto;
import com.example.projectbase.domain.dto.TokenRefreshRequestDto;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.domain.dto.response.LoginResponseDto;
import com.example.projectbase.domain.dto.response.TokenRefreshResponseDto;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

  AuthenticationResponseDTO login(AuthenticationRequestDTO request);

  TokenRefreshResponseDto refresh(TokenRefreshRequestDto request);

  CommonResponseDTO logout(HttpServletRequest request);

}
