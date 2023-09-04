package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public String addHotel(Hotel hotel) {
        if (hotel == null || hotel.getHotelName() == null) {
            return "FAILURE";
        }
        return userRepository.addHotel(hotel);
    }

    public Integer addUser(User user) {
        return userRepository.addUser(user);
    }

    public String getHotelWithMostFacilities() {
        return userRepository.getHotelWithMostFacilities();
    }

    public int bookARoom(Booking booking) {
        booking.setBookingId(UUID.randomUUID().toString());
        return userRepository.bookARoom(booking);
    }

    public int getBookings(Integer aadharCard) {
        return userRepository.getBookings(aadharCard);
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        return userRepository.updateFacilities(newFacilities,hotelName);
    }
}
