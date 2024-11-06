package com.example.projectbase.domain.dto;

import com.example.projectbase.constant.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MediaDetailDTO {

    private Long id;

    private String url;

    private MediaType type;

    private Long postId;

    private Long roomId;

}
