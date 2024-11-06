package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.constant.RoomType;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.RoomCreateDTO;
import com.example.projectbase.domain.dto.RoomFilterDTO;
import com.example.projectbase.domain.dto.RoomUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSortRequestDTO;
import com.example.projectbase.security.AuthorizationInfo;
import com.example.projectbase.security.CurrentUserLogin;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.RoomRatingService;
import com.example.projectbase.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestApiV1
public class RoomController {

    private final RoomService roomService;

    private final RoomRatingService roomRatingService;

    @Operation(summary = "API get room by id")
    @GetMapping(UrlConstant.Room.GET_ROOM)
    public ResponseEntity<?> getRoomById(@PathVariable Long roomId) {
        return VsResponseUtil.ok(roomService.getRoom(roomId));
    }

    @Tag(name = "room-controller-admin")
    @Operation(summary = "API get all room")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @GetMapping(UrlConstant.Room.GET_ROOMS)
    public ResponseEntity<?> getRooms(@Valid @ParameterObject PaginationSearchSortRequestDTO pagination,
                                      @RequestParam(required = false) RoomType roomType,
                                      @RequestParam Boolean deleteFlag) {
        String type = roomType == null ? null : roomType.getValue();
        return VsResponseUtil.ok(roomService.getRooms(pagination, type, deleteFlag));
    }

    @Operation(summary = "API get all room available")
    @GetMapping(UrlConstant.Room.GET_ROOMS_AVAILABLE)
    public ResponseEntity<?> getRoomsAvailable(@Valid @ParameterObject PaginationSearchSortRequestDTO pagination,
                                               @ParameterObject RoomFilterDTO roomFilterDTO) {
        return VsResponseUtil.ok(roomService.getRoomsAvailable(pagination, roomFilterDTO));
    }

    @Operation(summary = "API get all room rating by room id")
    @GetMapping(UrlConstant.Room.GET_ROOM_RATINGS_BY_ROOM)
    public ResponseEntity<?> getRoomRatingsByRoom(@PathVariable Long roomId,
                                                  @Valid @ParameterObject PaginationSortRequestDTO requestDTO,
                                                  @RequestParam(required = false) Integer star) {
        return VsResponseUtil.ok(roomRatingService.getRoomRatingsByRoom(requestDTO, star, roomId));
    }

    @Tag(name = "room-controller-admin")
    @Operation(summary = "API create room")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PostMapping(value = UrlConstant.Room.CREATE_ROOM, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRoom(@Valid @ModelAttribute RoomCreateDTO roomCreateDTO,
                                        @Parameter(name = "principal", hidden = true)
                                        @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(roomService.createRoom(roomCreateDTO, principal));
    }

    @Tag(name = "room-controller-admin")
    @Operation(summary = "API update room by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PutMapping(value = UrlConstant.Room.UPDATE_ROOM, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRoomById(@PathVariable Long roomId,
                                            @Valid @ModelAttribute RoomUpdateDTO roomUpdateDTO,
                                            @Parameter(name = "principal", hidden = true)
                                            @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(roomService.updateRoom(roomId, roomUpdateDTO, principal));
    }

    @Tag(name = "room-controller-admin")
    @Operation(summary = "API delete room by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @DeleteMapping(UrlConstant.Room.DELETE_ROOM)
    public ResponseEntity<?> deleteRoomById(@PathVariable Long roomId) {
        return VsResponseUtil.ok(roomService.deleteRoom(roomId));
    }

    @Tag(name = "room-controller-admin")
    @Operation(summary = "API delete room permanently by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @DeleteMapping(UrlConstant.Room.DELETE_ROOM_PERMANENTLY)
    public ResponseEntity<?> deleteRoomPermanentlyById(@PathVariable Long roomId) {
        return VsResponseUtil.ok(roomService.deleteRoomPermanently(roomId));
    }

    @Tag(name = "room-controller-admin")
    @Operation(summary = "API restore room by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PostMapping(UrlConstant.Room.RESTORE_ROOM)
    public ResponseEntity<?> restoreRoomById(@PathVariable Long roomId) {
        return VsResponseUtil.ok(roomService.restoreRoom(roomId));
    }

}

