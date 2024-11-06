package com.example.projectbase.service;

import com.example.projectbase.domain.dto.*;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.security.UserPrincipal;

public interface RoomService {
    RoomDTO getRoom(Long roomId);

    PaginationResponseDTO<RoomDTO> getRooms(PaginationSearchSortRequestDTO requestDTO, String roomType, Boolean deleteFlag);

    PaginationResponseDTO<RoomAvailableDTO> getRoomsAvailable(PaginationSearchSortRequestDTO requestDTO, RoomFilterDTO roomFilterDTO);

    RoomDTO createRoom(RoomCreateDTO roomCreateDTO, UserPrincipal currentUser);

    RoomDTO updateRoom(Long roomId, RoomUpdateDTO roomUpdateDTO, UserPrincipal currentUser);

    CommonResponseDTO deleteRoom(Long roomId);

    CommonResponseDTO deleteRoomPermanently(Long roomId);

    CommonResponseDTO restoreRoom(Long roomId);

    void deleteRoomByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);
}
