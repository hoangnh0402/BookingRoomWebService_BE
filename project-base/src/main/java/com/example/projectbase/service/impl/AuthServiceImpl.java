package com.example.projectbase.service.impl;

import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.AuthenticationRequestDTO;
import com.example.projectbase.domain.dto.AuthenticationResponseDTO;
import com.example.projectbase.domain.dto.TokenRefreshRequestDto;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.domain.dto.response.TokenRefreshResponseDto;
import com.example.projectbase.exception.UnauthorizedException;
import com.example.projectbase.security.jwt.JwtTokenProvider;
import com.example.projectbase.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public AuthenticationResponseDTO login(AuthenticationRequestDTO request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.getEmailOrPhone(), request.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtTokenProvider.generateToken(authentication);
      return new AuthenticationResponseDTO(CommonConstant.BEARER_TOKEN, jwt, null, authentication.getAuthorities());
    } catch (LockedException e) {
      throw new UnauthorizedException(ErrorMessage.Auth.ERR_ACCOUNT_LOCKED);
    } catch (DisabledException e) {
      throw new UnauthorizedException(ErrorMessage.Auth.ERR_ACCOUNT_NOT_ENABLED);
    } catch (BadCredentialsException e) {
      throw new UnauthorizedException(ErrorMessage.Auth.ERR_INCORRECT_AUTHENTICATION);
    }
  }

  @Override
  public TokenRefreshResponseDto refresh(TokenRefreshRequestDto request) {
    return null;
  }

  @Override
  public CommonResponseDTO logout(HttpServletRequest request) {
    return null;
  }

}
