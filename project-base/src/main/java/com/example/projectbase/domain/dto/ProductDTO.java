package com.example.projectbase.domain.dto;

import com.example.projectbase.domain.dto.common.CreatedByDTO;
import com.example.projectbase.domain.dto.common.DateAuditingDTO;
import com.example.projectbase.domain.dto.common.LastModifiedByDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO extends DateAuditingDTO {

    private Long id;

    private String name;

    private String thumbnail;

    private String description;

    private ServiceSummaryDTO service;

    private CreatedByDTO createdBy;

    private LastModifiedByDTO lastModifiedBy;

}
