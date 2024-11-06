package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.MediaDTO;
import com.example.projectbase.domain.dto.MediaDetailDTO;
import com.example.projectbase.domain.entity.Media;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    Media toMedia(MediaDTO mediaDTO);

    List<Media> toMedias(List<MediaDTO> mediaDTO);

    MediaDTO toMediaDTO(Media media);

    List<MediaDTO> toMediaDTOs(List<Media> medias);

    List<MediaDetailDTO> toMediaDetailDTOs(List<Media> medias);

}
