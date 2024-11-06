package com.example.projectbase.service;

import com.example.projectbase.domain.dto.RoomRatingCreateDTO;
import com.example.projectbase.domain.dto.RoomRatingDTO;
import com.example.projectbase.domain.dto.RoomRatingUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSortRequestDTO;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.security.UserPrincipal;

public interface RoomRatingService {

    RoomRatingDTO getRoomRating(Long id);

    PaginationResponseDTO<RoomRatingDTO> getRoomRatingsByRoom(PaginationSortRequestDTO requestDTO, Integer star, Long roomId);

    RoomRatingDTO createRoomRating(Long roomId, RoomRatingCreateDTO roomRatingCreateDTO, UserPrincipal currentUser);

    RoomRatingDTO updateRoomRating(Long id, RoomRatingUpdateDTO roomRatingUpdateDTO, UserPrincipal currentUser);

    CommonResponseDTO deleteRoomRating(Long id, UserPrincipal currentUser);

    void deleteRoomRatingByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

}
