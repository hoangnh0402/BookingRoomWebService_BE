package com.example.projectbase.domain.dto.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class UserDateAuditingDto extends DateAuditingDTO {

  private String createdBy;

  private String lastModifiedBy;

}
