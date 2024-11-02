package com.example.projectbase.service.impl;

import com.example.projectbase.constant.*;
import com.example.projectbase.domain.dto.UserCreateDTO;
import com.example.projectbase.domain.dto.UserDTO;
import com.example.projectbase.domain.dto.UserUpdateDTO;
import com.example.projectbase.domain.dto.common.CommonResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.domain.dto.pagination.PagingMeta;
import com.example.projectbase.domain.dto.response.UserDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.UserMapper;
import com.example.projectbase.exception.ForbiddenException;
import com.example.projectbase.exception.InvalidException;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.repository.RoleRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.UserService;
import com.example.projectbase.util.PaginationUtil;
import com.example.projectbase.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final UserMapper userMapper;

  private final PasswordEncoder passwordEncoder;

  private final UploadFileUtil uploadFile;

  @Override
  public UserDTO getUserById(String userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));
    }
    return userMapper.toUserDTO(user.get());
  }

  @Override
  public UserDTO getCurrentUser(UserPrincipal principal) {
    User user = userRepository.getUser(principal);
    return userMapper.toUserDTO(user);
  }

  @Override
  public PaginationResponseDTO<UserDTO> getCustomers(PaginationSearchSortRequestDTO requestDTO, Boolean isLocked) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.USER);
    Page<User> users = userRepository.findAllCustomer(requestDTO.getKeyword(), isLocked, pageable);
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.USER, users);
    List<UserDTO> userDTOs = userMapper.toUserDTOs(users.getContent());
    return new PaginationResponseDTO<UserDTO>(meta, userDTOs);
  }

  @Override
  public User createUser(UserCreateDTO userCreateDTO) {
    User user = userMapper.toUser(userCreateDTO);
    user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
    user.setRole(roleRepository.findByRoleName(RoleConstant.USER));
    if(userCreateDTO.getAvatarFile() != null) {
      user.setAvatar(uploadFile.uploadFile(userCreateDTO.getAvatarFile()));
    }
    return userRepository.save(user);
  }

  @Override
  public UserDTO updateUser(UserUpdateDTO userUpdateDTO, String userId, UserPrincipal principal) {
    Optional<User> user = userRepository.findById(userId);
    checkNotFoundUserById(user, userId);
    checkPermissionToUpdateUser(user.get(), principal);
    userMapper.updateUserFromDTO(userUpdateDTO, user.get());
    if(userUpdateDTO.getFileAvatar() != null) {
      if(user.get().getAvatar() != null) {
        uploadFile.destroyFileWithUrl(user.get().getAvatar());
      }
      user.get().setAvatar(uploadFile.uploadFile(userUpdateDTO.getFileAvatar()));
    }
    return userMapper.toUserDTO(userRepository.save(user.get()));
  }

  @Override
  public CommonResponseDTO changePassword(String oldPassword, String newPassword, UserPrincipal principal) {
    User user = userRepository.getUser(principal);
    if(passwordEncoder.matches(newPassword, user.getPassword())) {
      throw new InvalidException(ErrorMessage.User.ERR_NEW_PASSWORD_EQUAL_OLD_PASSWORD);
    }
    if(!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new InvalidException(ErrorMessage.User.ERR_OLD_PASSWORD_IS_INCORRECT);
    }
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.CHANGE_PASSWORD_SUCCESS);
  }

  @Override
  public CommonResponseDTO lockUser(String userId) {
    Optional<User> user = userRepository.findCustomerById(userId);
    checkLockUser(user, userId);
    user.get().setIsLocked(CommonConstant.TRUE);
    userRepository.save(user.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.LOCK_SUCCESS);
  }

  @Override
  public CommonResponseDTO unlockUser(String userId) {
    Optional<User> user = userRepository.findCustomerById(userId);
    checkUnlockUser(user, userId);
    user.get().setIsLocked(CommonConstant.FALSE);
    userRepository.save(user.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.UNLOCK_SUCCESS);
  }

  @Override
  public CommonResponseDTO deleteUserPermanently(String userId) {
    Optional<User> user = userRepository.findById(userId);
    checkDeleteUserPermanently(user, userId);
    userRepository.delete(user.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
  }

  private void checkPermissionToUpdateUser(User currentUserUpdate, UserPrincipal currentUser) {
    if(!currentUserUpdate.getId().equals(currentUser.getId())) {
      for(GrantedAuthority authority : currentUser.getAuthorities()) {
        if(!authority.getAuthority().equals(RoleConstant.ADMIN)) {
          throw new ForbiddenException(ErrorMessage.User.ERR_CAN_NOT_UPDATE);
        }
      }
    }
  }

  private void checkNotFoundUserById(Optional<User> user, String userId) {
    if (user.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));
    } else {
      if(!user.get().getIsEnable()) {
        throw new InvalidException(ErrorMessage.User.ERR_USER_NOT_ENABLED);
      }
      if(user.get().getIsLocked()) {
        throw new InvalidException((ErrorMessage.User.ERR_USER_IS_LOCKED));
      }
    }
  }

  private void checkLockUser(Optional<User> user, String userId) {
    if (user.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));
    } else {
      if(user.get().getIsLocked()) {
        throw new InvalidException((ErrorMessage.User.ERR_USER_IS_LOCKED));
      }
    }
  }

  private void checkUnlockUser(Optional<User> user, String userId) {
    if (user.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));
    } else {
      if(!user.get().getIsLocked()) {
        throw new InvalidException((ErrorMessage.User.ERR_USER_IS_NOT_LOCKED));
      }
    }
  }

  private void checkDeleteUserPermanently(Optional<User> user, String userId) {
    if(user.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));
    } else {
      if(!user.get().getIsLocked()) {
        throw new InvalidException((ErrorMessage.User.ERR_CAN_NOT_PERMANENTLY_DELETED));
      }
    }
  }


}
