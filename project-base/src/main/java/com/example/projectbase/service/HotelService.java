package com.example.projectbase.service;

import com.example.projectbase.domain.dto.ServiceCreateDTO;
import com.example.projectbase.domain.dto.ServiceDTO;
import com.example.projectbase.domain.dto.ServiceUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.security.UserPrincipal;

public interface HotelService {

    ServiceDTO getServiceById(Long serviceId);

    PaginationResponseDTO<ServiceDTO> getServices(PaginationSearchSortRequestDTO requestDTO, Boolean deleteFlag);

    ServiceDTO createService(ServiceCreateDTO serviceCreateDTO, UserPrincipal principal);

    ServiceDTO updateService(Long serviceId, ServiceUpdateDTO serviceUpdateDTO, UserPrincipal principal);

    CommonResponseDTO deleteService(Long serviceId);

    CommonResponseDTO deleteServicePermanently(Long serviceId);

    CommonResponseDTO restoreService(Long serviceId);

    void deleteServiceByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

}

