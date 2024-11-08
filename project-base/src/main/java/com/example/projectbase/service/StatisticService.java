package com.example.projectbase.service;

import com.example.projectbase.domain.dto.BookingStatisticDTO;
import com.example.projectbase.domain.dto.RevenueMonthDTO;
import com.example.projectbase.domain.dto.RevenueRequestDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;

import java.util.List;
import java.util.Map;

public interface StatisticService {

    PaginationResponseDTO<Map<String, Object>> statisticRoomBookedForMonth(PaginationSearchSortRequestDTO request, Integer month, Integer year);

    List<Map<String, Object>> statisticCustomerTopBooking();

    List<RevenueMonthDTO> statisticRevenue(RevenueRequestDTO revenueRequestDTO);

    BookingStatisticDTO statisticBookingForMonth(Integer month, Integer year);

}
