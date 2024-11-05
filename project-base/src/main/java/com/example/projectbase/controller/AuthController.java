package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.AuthenticationRequestDTO;
import com.example.projectbase.domain.dto.UserCreateDTO;
import com.example.projectbase.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@RequiredArgsConstructor
@Validated
@RestApiV1
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "API Login")
  @PostMapping(UrlConstant.Auth.LOGIN)
  public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequestDTO request) {
    return VsResponseUtil.ok(authService.login(request));
  }

  @Operation(summary = "API logout")
  @PostMapping(UrlConstant.Auth.LOGOUT)
  public ResponseEntity<?> logout(HttpServletRequest request) {
    return VsResponseUtil.ok(authService.logout(request));
  }

  @Operation(summary = "(1) API SignUp and send mail token")
  @PostMapping(value = UrlConstant.Auth.SIGNUP, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> signUp(@Valid @ModelAttribute UserCreateDTO userCreateDTO) {
    return VsResponseUtil.ok(authService.signUp(userCreateDTO));
  }

  @Operation(summary = "(2) API verify signup")
  @PostMapping(UrlConstant.Auth.VERIFY_SIGNUP)
  public ResponseEntity<?> FormVerificationTokenSignUp(@RequestParam("email") String email,
                                                       @RequestParam("token") String token) {
    return VsResponseUtil.ok(authService.verifySignUp(email, token));
  }

  @Operation(summary = "(1) API forgot password")
  @PostMapping(UrlConstant.Auth.FORGOT_PASS)
  public ResponseEntity<?> forgotPassword(@Email(message = ErrorMessage.INVALID_FORMAT_SOME_THING_FIELD)
                                          @RequestParam("email") String email) {
    return VsResponseUtil.ok(authService.forgotPassword(email));
  }

  @Operation(summary = "(2) API verify forgot password")
  @PostMapping(UrlConstant.Auth.VERIFY_FORGOT_PASS)
  public ResponseEntity<?> forgotPassword(@Email(message = ErrorMessage.INVALID_FORMAT_SOME_THING_FIELD)
                                          @RequestParam("email") String email,
                                          @RequestParam(name = "token") String token,
                                          @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$",
                                                  message = ErrorMessage.INVALID_FORMAT_PASSWORD)
                                          @RequestParam(name = "password") String newPassword) {
    return VsResponseUtil.ok(authService.verifyForgotPassword(email, token, newPassword));
  }
}

