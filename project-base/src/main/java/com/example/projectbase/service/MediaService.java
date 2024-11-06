package com.example.projectbase.service;

import com.example.projectbase.domain.dto.MediaDetailDTO;
import com.example.projectbase.domain.dto.PostUpdateDTO;
import com.example.projectbase.domain.dto.RoomUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationRequestDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.domain.entity.Media;
import com.example.projectbase.domain.entity.Post;
import com.example.projectbase.domain.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface MediaService {

    PaginationResponseDTO<MediaDetailDTO> getMediasInTrash(PaginationRequestDTO requestDTO, Boolean deleteFlag);

    CommonResponseDTO deleteMediaPermanently(Long mediaId);

    CommonResponseDTO restoreMedia(Long mediaId);

    void deleteMediaByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

    //room
    List<Media> getMediaByRoom(Long roomId);

    List<Media> getMediaByRoomAndIsDeleteFlag(Long roomId);

    Set<Media> createMediasForRoom(Room room, List<MultipartFile> files);

    //Delete media if not found MediaDTO in RoomUpdateDTO
    Room deleteMediaFromRoomUpdate(Room room, RoomUpdateDTO roomUpdateDTO);

    //post
    List<Media> getMediaByPost(Long postId);

    List<Media> getMediaByPostAndIsDeleteFlag(Long postId);

    Set<Media> createMediasForPost(Post post, List<MultipartFile> files);

    //Delete media if not found MediaDTO in PostUpdateDTO
    Post deleteMediaFromPostUpdate(Post post, PostUpdateDTO postUpdateDTO);

}