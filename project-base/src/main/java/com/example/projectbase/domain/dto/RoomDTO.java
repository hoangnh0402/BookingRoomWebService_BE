package com.example.projectbase.domain.dto;

import com.example.projectbase.domain.dto.common.CreatedByDTO;
import com.example.projectbase.domain.dto.common.LastModifiedByDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomDTO {
    private Long id;

    private String name;

    private Long price;

    private String type;

    private String bed;

    private Integer size;

    private Integer capacity;

    private String services;

    private String description;

    private SaleSummaryDTO sale;

    private CreatedByDTO createdBy;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private LastModifiedByDTO lastModifiedBy;

    private List<MediaDTO> medias;
}
