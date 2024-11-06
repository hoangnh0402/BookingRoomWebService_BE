package com.example.projectbase.service.impl;

import com.example.projectbase.constant.*;
import com.example.projectbase.domain.dto.RoomRatingCreateDTO;
import com.example.projectbase.domain.dto.RoomRatingDTO;
import com.example.projectbase.domain.dto.RoomRatingUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSortRequestDTO;
import com.example.projectbase.domain.dto.pagination.PagingMeta;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.domain.entity.*;
import com.example.projectbase.exception.InvalidException;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.projection.RoomRatingProjection;
import com.example.projectbase.repository.RoomRatingRepository;
import com.example.projectbase.repository.RoomRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.RoomRatingService;
import com.example.projectbase.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import com.example.projectbase.domain.mapper.RoomRatingMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomRatingServiceImpl implements RoomRatingService {

    private final RoomRatingRepository roomRatingRepository;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    private final RoomRatingMapper roomRatingMapper;

    @Override
    public RoomRatingDTO getRoomRating(Long id) {
        RoomRatingProjection roomRatingProjection = roomRatingRepository.findRoomRatingById(id);
        checkNotFoundRoomRatingById(roomRatingProjection, id);
        return roomRatingMapper.roomRatingProjectionToRoomRatingDTO(roomRatingProjection);
    }

    @Override
    public PaginationResponseDTO<RoomRatingDTO> getRoomRatingsByRoom(PaginationSortRequestDTO requestDTO, Integer star, Long roomId) {
        //Pagination
        Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.ROOM_RATING);
        Page<RoomRatingProjection> roomRatings = roomRatingRepository.findRoomRatingsByRoomId(pageable, star, roomId);
        //Create Output
        PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.ROOM_RATING, roomRatings);
        return new PaginationResponseDTO<RoomRatingDTO>(meta, toRoomRatingDTOs(roomRatings));
    }

    @Override
    public RoomRatingDTO createRoomRating(Long roomId, RoomRatingCreateDTO roomRatingCreateDTO, UserPrincipal currentUser) {
        Optional<Room> room = roomRepository.findById(roomId);
        checkNotFoundRoomById(room, roomId);
        User creator = userRepository.getUser(currentUser);
        RoomRating roomRating = roomRatingMapper.createDtoToRoomRating(roomRatingCreateDTO);
        roomRating.setRoom(room.get());
        roomRating.setUser(creator);
        return roomRatingMapper.toRoomRatingDTO(roomRatingRepository.save(roomRating), creator, creator);
    }

    @Override
    public RoomRatingDTO updateRoomRating(Long id, RoomRatingUpdateDTO roomRatingUpdateDTO, UserPrincipal currentUser) {
        Optional<RoomRating> currentRoomRating = roomRatingRepository.findById(id);
        checkNotFoundRoomRatingById(currentRoomRating, id);
        checkPermissionToUpdateOrDeleteRoomRating(currentRoomRating.get().getUser(), currentUser);
        roomRatingMapper.updateRoomRatingFromDTO(roomRatingUpdateDTO, currentRoomRating.get());
        User creator = userRepository.findById(currentRoomRating.get().getCreatedBy()).get();
        User updater = userRepository.getUser(currentUser);
        return roomRatingMapper.toRoomRatingDTO(roomRatingRepository.save(currentRoomRating.get()), creator, updater);
    }

    @Override
    public CommonResponseDTO deleteRoomRating(Long id, UserPrincipal currentUser) {
        Optional<RoomRating> currentRoomRating = roomRatingRepository.findById(id);
        checkNotFoundRoomRatingById(currentRoomRating, id);
        checkPermissionToUpdateOrDeleteRoomRating(currentRoomRating.get().getUser(), currentUser);
        currentRoomRating.get().setDeleteFlag(CommonConstant.TRUE);
        roomRatingRepository.save(currentRoomRating.get());
        return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
    }

    @Override
    public void deleteRoomRatingByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords) {
        roomRatingRepository.deleteByDeleteFlag(isDeleteFlag, daysToDeleteRecords);
    }

    private List<RoomRatingDTO> toRoomRatingDTOs(Page<RoomRatingProjection> roomRatingProjections) {
        List<RoomRatingDTO> roomRatingDTOs = new LinkedList<>();
        for(RoomRatingProjection roomRatingProjection : roomRatingProjections) {
            roomRatingDTOs.add(roomRatingMapper.roomRatingProjectionToRoomRatingDTO(roomRatingProjection));
        }
        return roomRatingDTOs;
    }

    private void checkPermissionToUpdateOrDeleteRoomRating(User userCreatedRoomRating, UserPrincipal currentUser) {
        if(!userCreatedRoomRating.getId().equals(currentUser.getId())) {
            for(GrantedAuthority authority : currentUser.getAuthorities()) {
                if(!authority.getAuthority().equals(RoleConstant.ADMIN)) {
                    throw new InvalidException(ErrorMessage.RoomRating.ERR_CAN_NOT_UPDATE_OR_DELETED);
                }
            }
        }
    }

    private void checkNotFoundRoomRatingById(Optional<RoomRating> roomRating, Long roomRatingId) {
        if (roomRating.isEmpty()) {
            throw new NotFoundException(String.format(ErrorMessage.RoomRating.ERR_NOT_FOUND_ID, roomRatingId));
        }
    }

    private void checkNotFoundRoomRatingById(RoomRatingProjection roomRatingProjection, Long roomRatingId) {
        if (ObjectUtils.isEmpty(roomRatingProjection)) {
            throw new NotFoundException(String.format(ErrorMessage.RoomRating.ERR_NOT_FOUND_ID, roomRatingId));
        }
    }

    private void checkNotFoundRoomById(Optional<Room> room, Long roomId) {
        if (room.isEmpty()) {
            throw new NotFoundException(String.format(ErrorMessage.Room.ERR_NOT_FOUND_ID, roomId));
        }
    }

}
