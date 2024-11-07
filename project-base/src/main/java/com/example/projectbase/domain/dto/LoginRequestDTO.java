package com.example.projectbase.domain.dto;

import com.example.projectbase.constant.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginRequestDTO {

  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  private String emailOrPhone;

  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  private String password;

}