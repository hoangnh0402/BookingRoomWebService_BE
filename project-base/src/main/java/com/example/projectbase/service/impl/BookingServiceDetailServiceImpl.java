package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.BookingServiceDTO;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.BookingServiceDetail;
import com.example.projectbase.domain.entity.Service;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.repository.BookingServiceDetailRepository;
import com.example.projectbase.repository.ServiceRepository;
import com.example.projectbase.service.BookingServiceDetailService;
import lombok.RequiredArgsConstructor;

import java.util.*;


@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class BookingServiceDetailServiceImpl implements BookingServiceDetailService {

    private final BookingServiceDetailRepository bookingServiceDetailRepository;

    private final ServiceRepository serviceRepository;

    @Override
    public Set<BookingServiceDetail> getBookingServiceDetailsByBooking(Long bookingId) {
        return bookingServiceDetailRepository.findAllByBookingId(bookingId);
    }

    @Override
    public Set<BookingServiceDetail> createBookingServiceDetails(Booking booking, List<BookingServiceDTO> bookingService) {
        Set<BookingServiceDetail> bookingServiceDetails = new LinkedHashSet<>(Math.max((int) (bookingService.size() / .75f) + 1, 16));
        // services ordered by the customer
        List<Service> services = getServicesFromBookingServiceDTOs(bookingService);
        for(BookingServiceDTO bookingServiceDTO : bookingService) {
            BookingServiceDetail serviceDetail = new BookingServiceDetail();
            for(Service service : services) {
                if(service.getId().equals(bookingServiceDTO.getServiceId())) {
                    serviceDetail.setPrice(service.getPrice());
                    serviceDetail.setAmount(bookingServiceDTO.getAmount());
                    serviceDetail.setBooking(booking);
                    serviceDetail.setService(service);
                }
            }
            bookingServiceDetailRepository.save(serviceDetail);
            bookingServiceDetails.add(serviceDetail);
        }
        return bookingServiceDetails;
    }

    private List<Service> getServicesFromBookingServiceDTOs(List<BookingServiceDTO> bookingServiceDTOs) {
        List<Service> services = new LinkedList<>();
        for(BookingServiceDTO bookingServiceDTO : bookingServiceDTOs) {
            Optional<Service> service = serviceRepository.findById(bookingServiceDTO.getServiceId());
            checkNotFoundServiceById(service, bookingServiceDTO.getServiceId());
            services.add(service.get());
        }
        return services;
    }

    private void checkNotFoundServiceById(Optional<Service> service, Long serviceId) {
        if (service.isEmpty()) {
            throw new NotFoundException(String.format(ErrorMessage.Service.ERR_NOT_FOUND_ID, serviceId));
        }
    }

}
