package com.example.projectbase.service;

import com.example.projectbase.domain.dto.SaleCreateDTO;
import com.example.projectbase.domain.dto.SaleDTO;
import com.example.projectbase.domain.dto.SaleUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.UserPrincipal;

import java.util.List;

public interface SaleService {

    SaleDTO getSale(Long saleId);

    PaginationResponseDTO<SaleDTO> getSales(PaginationSearchSortRequestDTO requestDTO, Boolean deleteFlag);

    SaleDTO createSale(SaleCreateDTO createDTO, UserPrincipal principal);

    SaleDTO updateSale(Long saleId, SaleUpdateDTO updateDTO, UserPrincipal principal);

    CommonResponseDTO addSalesToRooms(Long saleId, List<Long> roomIds);

    CommonResponseDTO removeSaleFromRoom(Long saleId, Long roomId);

    CommonResponseDTO removeSaleFromRooms(List<Long> roomId);

    CommonResponseDTO deleteSale(Long saleId);

    CommonResponseDTO deleteSalePermanently(Long saleId);

    CommonResponseDTO restoreSale(Long saleId);

    void deleteSaleByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

}
