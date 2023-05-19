package com.aerosales.aviaticketsspring.pojo;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="flight_details")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="flight_id")
    private Integer flightId;

    @Column(name="flight_name")
    private String flightName;

    @Column(name="is_one_way")
    private boolean isOneWay;

    @Column(name="flight_duration")
    private String flightDuration;

    @Column(name="origin_city")
    private String originCity;

    @Column(name="destination_city")
    private String destinationCity;

    @Column(name="depart_date")
    private String departDate;

    @Column(name="travel_class")
    private String travelClass;

    @Column(name="tranfer_count")
    private Integer transferCount;

    @Column(name="tickets_amount")
    private Integer ticketsAmount;

    @Column(name="depart_airport")
    private String departAirport;

    @Column(name="arrival_airport")
    private String arrivalAirport;

    @Column(name="depart_time")
    private String departTime;

    @Column(name="arrival_time")
    private String arrivalTime;

    @Column(name="ticket_price")
    private Double ticketPrice;

    @Column(name="airline_name")
    private String airlineName;

    @ManyToMany(mappedBy = "flightsList", fetch = FetchType.EAGER)
    private List<User> userList;

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    public String getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(String flightDuration) {
        this.flightDuration = flightDuration;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }

    public Integer getTransferCount() {
        return transferCount;
    }

    public void setTransferCount(Integer transferCount) {
        this.transferCount = transferCount;
    }

    public Integer getTicketsAmount() {
        return ticketsAmount;
    }

    public void setTicketsAmount(Integer ticketsAmount) {
        this.ticketsAmount = ticketsAmount;
    }

    public String getDepartAirport() {
        return departAirport;
    }

    public void setDepartAirport(String departAirport) {
        this.departAirport = departAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Flight() {}

    public Flight(String flightName, boolean isOneWay, String flightDuration, String originCity, String destinationCity, String departDate,
        String travelClass, Integer transferCount, Integer ticketsAmount, String departAirport, String arrivalAirport, String departTime, String arrivalTime,
        Double ticketPrice, String airlineName, List userList) {
        this.flightName = flightName;
        this.isOneWay = isOneWay;
        this.flightDuration = flightDuration;
        this.originCity = originCity;
        this.destinationCity = destinationCity;
        this.departDate = departDate;
        this.travelClass = travelClass;
        this.transferCount = transferCount;
        this.ticketsAmount = ticketsAmount;
        this.departAirport = departAirport;
        this.arrivalAirport = arrivalAirport;
        this.departTime = departTime;
        this.arrivalTime = arrivalTime;
        this.ticketPrice = ticketPrice;
        this.airlineName = airlineName;
        this.userList = userList;
    }
}
