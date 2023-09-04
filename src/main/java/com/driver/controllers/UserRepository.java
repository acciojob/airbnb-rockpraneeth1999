package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {

    public class HotelComparator implements Comparator<Hotel> {
        @Override
        public int compare(Hotel hotel1, Hotel hotel2) {
            int facilityComparison = Integer.compare(hotel2.getFacilities().size(), hotel1.getFacilities().size());
            if (facilityComparison == 0) {
                return hotel1.getHotelName().compareTo(hotel2.getHotelName());
            }
            return facilityComparison;
        }
    }

    TreeMap<String, Hotel> hotelDataBase = new TreeMap<>();
    HashMap<Integer, User> userDataBase = new HashMap<>();
    HashMap<String,Booking> bookingDatabase = new HashMap<>();
    HashMap<Integer, List<Booking>> userBookingsDatabase = new HashMap<>();

    public String addHotel(Hotel hotel) {
        if(hotelDataBase.containsKey(hotel.getHotelName())){
            return "FAILURE";
        }
        hotelDataBase.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        userDataBase.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
       /* Map.Entry<String, Hotel> entry = hotelDataBase.firstEntry();
        if(entry!=null && entry.getValue().getFacilities().size()!=0)
            return entry.getKey();
        return "";
        */

        String hotelWithMostFacilities = "";
        int maxFacilities = 0;

        for (Map.Entry<String, Hotel> entry : hotelDataBase.entrySet()) {
            Hotel hotel = entry.getValue();
            int numFacilities = hotel.getFacilities().size();

            if (numFacilities > maxFacilities) {
                maxFacilities = numFacilities;
                hotelWithMostFacilities = entry.getKey();
            } else if (numFacilities == maxFacilities && entry.getKey().compareTo(hotelWithMostFacilities) < 0) {
                hotelWithMostFacilities = entry.getKey();
            }
        }

        return hotelWithMostFacilities;
    }

    public int bookARoom(Booking booking) {

        String hotelName = booking.getHotelName();
        int noOfRoomsNeeded = booking.getNoOfRooms();
        int noOfRoomsAvailable = hotelDataBase.get(hotelName).getAvailableRooms();

        int bill = -1;
        if(noOfRoomsNeeded > noOfRoomsAvailable){
            booking.setAmountToBePaid(bill);
        }
        else{
            bill = noOfRoomsNeeded * hotelDataBase.get(hotelName).getPricePerNight();
            booking.setAmountToBePaid(bill);
        }

        bookingDatabase.put(booking.getBookingId(),booking);
        int aadhar = booking.getBookingAadharCard();

        List<Booking> bookingsOfUser;
        if(!userBookingsDatabase.containsKey(aadhar)){
            bookingsOfUser = new ArrayList<>();
            bookingsOfUser.add(booking);
            userBookingsDatabase.put(aadhar,bookingsOfUser);
        }
        else{
            bookingsOfUser=userBookingsDatabase.get(aadhar);
            bookingsOfUser.add(booking);
        }

        return bill;
    }

    public int getBookings(Integer aadharCard) {
        return userBookingsDatabase.get(aadharCard).size();
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelDataBase.get(hotelName);
        List<Facility> oldFacilities = hotel.getFacilities();
        LinkedHashSet<Facility> uniqueFacilities = new LinkedHashSet<>();

        for(int i=0;i<oldFacilities.size();i++){
            uniqueFacilities.add(oldFacilities.get(i));
        }
        for(int i=0;i<newFacilities.size();i++){
            uniqueFacilities.add(newFacilities.get(i));
        }

        List<Facility> facilitiesToAdd = new ArrayList<>();
        for (Facility facility : uniqueFacilities) {
            facilitiesToAdd.add(facility);
        }

        hotel.setFacilities(facilitiesToAdd);

        return hotel;
    }
}
