package com.example.projectbase.service;

import com.example.projectbase.domain.dto.UserCreateDTO;
import com.example.projectbase.domain.dto.UserDTO;
import com.example.projectbase.domain.dto.UserUpdateDTO;
import com.example.projectbase.domain.dto.common.CommonResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.UserPrincipal;

public interface UserService {

  UserDTO getUserById(String userId);

  UserDTO getCurrentUser(UserPrincipal principal);

  PaginationResponseDTO<UserDTO> getCustomers(PaginationSearchSortRequestDTO requestDTO, Boolean isLocked);

  User createUser(UserCreateDTO userCreateDTO);

  UserDTO updateUser(UserUpdateDTO userUpdateDTO, String userId, UserPrincipal principal);

  CommonResponseDTO changePassword(String oldPassword, String newPassword, UserPrincipal principal);

  CommonResponseDTO lockUser(String userId);

  CommonResponseDTO unlockUser(String userId);

  CommonResponseDTO deleteUserPermanently(String userId);

}
