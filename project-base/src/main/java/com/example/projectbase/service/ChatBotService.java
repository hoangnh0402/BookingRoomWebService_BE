package com.example.projectbase.service;

public interface ChatBotService {

    Object getRoomsByType(String typeRoom);

    Object getRoomById(Long id);

    Object getHotelServices();

    Object getHotelServiceById(Long id);

    Object getProductsById(Long id);

}
