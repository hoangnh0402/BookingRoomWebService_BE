package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.RoomRatingCreateDTO;
import com.example.projectbase.domain.dto.RoomRatingUpdateDTO;
import com.example.projectbase.security.AuthorizationInfo;
import com.example.projectbase.security.CurrentUserLogin;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.RoomRatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestApiV1
public class RoomRatingController {

    private final RoomRatingService roomRatingService;

    @Operation(summary = "API get room rating by id")
    @GetMapping(UrlConstant.RoomRating.GET_ROOM_RATING)
    public ResponseEntity<?> getRoomRatingById(@PathVariable Long roomRatingId) {
        return VsResponseUtil.ok(roomRatingService.getRoomRating(roomRatingId));
    }

    @Operation(summary = "API create room rating")
    @AuthorizationInfo(role = { RoleConstant.USER })
    @PostMapping(value = UrlConstant.RoomRating.CREATE_ROOM_RATING)
    public ResponseEntity<?> createRoom(@PathVariable Long roomId,
                                        @Valid @RequestBody RoomRatingCreateDTO roomRatingCreateDTO,
                                        @Parameter(name = "principal", hidden = true)
                                        @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(roomRatingService.createRoomRating(roomId, roomRatingCreateDTO, principal));
    }

    @Operation(summary = "API update room rating by id")
    @AuthorizationInfo(role = { RoleConstant.USER })
    @PutMapping(value = UrlConstant.RoomRating.UPDATE_ROOM_RATING)
    public ResponseEntity<?> updateRoomRatingById(@PathVariable Long roomRatingId,
                                                  @Valid @RequestBody RoomRatingUpdateDTO roomRatingUpdateDTO,
                                                  @Parameter(name = "principal", hidden = true)
                                                  @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(roomRatingService.updateRoomRating(roomRatingId, roomRatingUpdateDTO, principal));
    }

    @Operation(summary = "API delete room rating by id")
    @AuthorizationInfo(role = { RoleConstant.USER, RoleConstant.ADMIN })
    @DeleteMapping(UrlConstant.RoomRating.DELETE_ROOM_RATING)
    public ResponseEntity<?> deleteRoomRatingById(@PathVariable Long roomRatingId,
                                                  @Parameter(name = "principal", hidden = true)
                                                  @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(roomRatingService.deleteRoomRating(roomRatingId, principal));
    }

}
