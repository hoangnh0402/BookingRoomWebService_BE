package com.example.projectbase.service.impl;

import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.CommonMessage;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.AuthenticationRequestDTO;
import com.example.projectbase.domain.dto.AuthenticationResponseDTO;
import com.example.projectbase.domain.dto.UserCreateDTO;
import com.example.projectbase.domain.dto.common.DataMailDTO;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.entity.VerificationToken;
import com.example.projectbase.exception.*;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.jwt.JwtTokenProvider;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.UserService;
import com.example.projectbase.service.VerificationTokenService;
import com.example.projectbase.util.SendMailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;

  private final UserService userService;

  private final VerificationTokenService verificationTokenService;

  private final AuthenticationManager authenticationManager;

  private final PasswordEncoder passwordEncoder;

  private final JwtTokenProvider jwtTokenProvider;

  private final SendMailUtil sendMail;

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
  public CommonResponseDTO signUp(UserCreateDTO userCreateDTO) {
    Optional<User> findUserByEmail = userRepository.findByEmail(userCreateDTO.getEmail());
    if(findUserByEmail.isPresent() && findUserByEmail.get().getIsEnable()) {
      throw new DuplicateException(ErrorMessage.Auth.ERR_DUPLICATE_EMAIL);
    }
    Optional<User> findUserByPhone = userRepository.findByPhone(userCreateDTO.getPhoneNumber());
    if(findUserByPhone.isPresent() && findUserByPhone.get().getIsEnable()) {
      throw new DuplicateException(ErrorMessage.Auth.ERR_DUPLICATE_PHONE);
    }
    if(findUserByEmail.isPresent() && findUserByPhone.isPresent()) {
      VerificationToken verificationToken = verificationTokenService.createVerificationToken(findUserByEmail.get());
      return sendMailSignUp(findUserByEmail.get(), verificationToken);
    } else {
      User user = userService.createUser(userCreateDTO);
      VerificationToken verificationToken = verificationTokenService.createVerificationToken(user);
      return sendMailSignUp(user, verificationToken);
    }
  }

  @Override
  public CommonResponseDTO verifySignUp(String email, String token) {
    Optional<User> user = userRepository.findByEmail(email);
    checkNotFoundUserByEmailAndNotIsEnable(user, email);
    VerificationToken verificationToken = verificationTokenService.getByToken(token);
    checkAccountNotEqualTokenVerify(user.get(), verificationToken);
    user.get().setIsEnable(CommonConstant.TRUE);
    userRepository.save(user.get());
    verificationTokenService.deleteToken(verificationToken.getId());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.SIGNUP_SUCCESS);
  }

  @Override
  public CommonResponseDTO forgotPassword(String email) {
    Optional<User> user = userRepository.findByEmail(email);
    checkNotFoundUserByEmail(user, email);
    //create token
    VerificationToken verificationToken = verificationTokenService.createVerificationToken(user.get());
    //set data mail
    DataMailDTO dataMailDTO = new DataMailDTO();
    dataMailDTO.setTo(user.get().getEmail());
    dataMailDTO.setSubject(CommonMessage.SUBJECT_FORGOT_PASS);
    Map<String, Object> properties = new HashMap<>();
    properties.put("name", user.get().getLastName() + " " + user.get().getFirstName());
    properties.put("token", verificationToken.getToken());
    dataMailDTO.setProperties(properties);
    try {
      sendMail.sendEmailWithHTML(dataMailDTO, CommonMessage.FORGOT_PASSWORD_TEMPLATE);
      return new CommonResponseDTO(CommonConstant.TRUE, CommonConstant.EMPTY_STRING);
    } catch (Exception ex) {
      throw new InternalServerException(ErrorMessage.ERR_EXCEPTION_GENERAL);
    }
  }

  @Override
  public CommonResponseDTO verifyForgotPassword(String email, String token, String newPassword) {
    Optional<User> user = userRepository.findByEmail(email);
    checkNotFoundUserByEmail(user, email);
    VerificationToken verificationToken = verificationTokenService.getByToken(token);
    checkAccountNotEqualTokenVerify(user.get(), verificationToken);
    if (passwordEncoder.matches(newPassword, user.get().getPassword())) {
      throw new DuplicateException(ErrorMessage.Auth.ERR_DUPLICATE_PASSWORD);
    }
    user.get().setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user.get());
    verificationTokenService.deleteToken(verificationToken.getId());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.FORGOT_PASSWORD_SUCCESS);
  }

  @Override
  public CommonResponseDTO logout(HttpServletRequest request) {
    return new CommonResponseDTO(CommonConstant.TRUE, "Logged out successfully");
  }

  private CommonResponseDTO sendMailSignUp(User user, VerificationToken verificationToken) {
    try {
      DataMailDTO dataMailDTO = setDataMailDtoToSignUp(user, verificationToken);
      sendMail.sendEmailWithHTML(dataMailDTO, CommonMessage.SIGNUP_TEMPLATE);
      return new CommonResponseDTO(CommonConstant.TRUE, CommonConstant.EMPTY_STRING);
    } catch (Exception ex) {
      throw new InternalServerException(ErrorMessage.ERR_EXCEPTION_GENERAL);
    }
  }

  private DataMailDTO setDataMailDtoToSignUp(User user, VerificationToken verificationToken) {
    DataMailDTO dataMailDTO = new DataMailDTO();
    dataMailDTO.setTo(user.getEmail());
    dataMailDTO.setSubject(CommonMessage.SUBJECT_REGISTER);
    Map<String, Object> properties = new HashMap<>();
    properties.put("name", user.getLastName() + " " + user.getFirstName());
    properties.put("token", verificationToken.getToken());
    dataMailDTO.setProperties(properties);
    return dataMailDTO;
  }

  private void checkAccountNotEqualTokenVerify(User user, VerificationToken token) {
    if(!Objects.equals(user.getId(), token.getUser().getId())) {
      throw new InvalidException(ErrorMessage.Auth.INCORRECT_TOKEN);
    }
  }

  private void checkNotFoundUserByEmailAndNotIsEnable(Optional<User> user, String email) {
    if (user.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.User.ERR_ACCOUNT_NOT_FOUND_BY_EMAIL, email));
    }
  }

  private void checkNotFoundUserByEmail(Optional<User> user, String email) {
    if (user.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.User.ERR_ACCOUNT_NOT_FOUND_BY_EMAIL, email));
    } else {
      if(!user.get().getIsEnable()) {
        throw new InvalidException(ErrorMessage.Auth.ERR_ACCOUNT_NOT_ENABLED);
      }
      if(user.get().getIsLocked()) {
        throw new InvalidException((ErrorMessage.Auth.ERR_ACCOUNT_LOCKED));
      }
    }
  }

}
