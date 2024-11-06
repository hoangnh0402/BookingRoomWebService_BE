package com.example.projectbase.domain.dto;

import com.example.projectbase.constant.CommonConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleSummaryDTO {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime dayStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime dayEnd;

    private Integer salePercent;

}
