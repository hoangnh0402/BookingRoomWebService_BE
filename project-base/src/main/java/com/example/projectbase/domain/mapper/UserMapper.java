package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.UserCreateDTO;
import com.example.projectbase.domain.dto.UserDTO;
import com.example.projectbase.domain.dto.UserSummaryDTO;
import com.example.projectbase.domain.dto.UserUpdateDTO;
import com.example.projectbase.domain.dto.common.CreatedByDTO;
import com.example.projectbase.domain.dto.common.LastModifiedByDTO;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.projection.StatisticCustomerTopBookingProjection;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

  User toUser(UserCreateDTO userCreateDTO);

  @Mappings({
          @Mapping(target = "roleName", source = "user.role.roleName"),
  })
  UserDTO toUserDTO(User user);

  UserSummaryDTO toUserSummaryDTO(User user);

  List<UserDTO> toUserDTOs(List<User> user);

  @Mapping(target = "avatar", ignore = true)
  void updateUserFromDTO(UserUpdateDTO updateDTO, @MappingTarget User user);

  CreatedByDTO toCreatedByDTO(User creator);

  LastModifiedByDTO toLastModifiedByDTO(User updater);

  UserDTO statisticCustomerTopBookingProjectionToUserDTO(StatisticCustomerTopBookingProjection projection);

}
