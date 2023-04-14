package com.aerosales.aviaticketsspring.pojo;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="flight_details")
public class FlightDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="flight_id")
    private Integer flightId;

    @Column(name="flight_name")
    private String flightName;

    @Column(name="flight_duration")
    private Integer flightDuration;

    @Column(name="from_place")
    private String fromPlace;

    @Column(name="to_place")
    private String toPlace;

    @Column(name="dept_date")
    private String deptDate;

    @Column(name="arrival_date")
    private String arrivalDate;

    @Column(name="travel_class")
    private String travelClass;

    @Column(name="tickets_amount")
    private Integer ticketsAmount;

    @Column(name="price")
    private Integer price;

    @ManyToMany(mappedBy = "flightDetailsList", fetch = FetchType.EAGER)
    private Set<Users> userList;

    public Set<Users> getUserList() {
        return userList;
    }

    public void setUserList(Set<Users> userList) {
        this.userList = userList;
    }

    public FlightDetails(String flightName, Integer flightDuration, String fromPlace, String toPlace,
                         String deptDate, String arrivalDate, String travelClass, Integer ticketsAmount, Integer price) {
        this.flightName = flightName;
        this.flightDuration = flightDuration;
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.deptDate = deptDate;
        this.arrivalDate = arrivalDate;
        this.travelClass = travelClass;
        this.ticketsAmount = ticketsAmount;
        this.price = price;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public String getDeptDate() {
        return deptDate;
    }

    public Integer getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Integer flightDuration) {
        this.flightDuration = flightDuration;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDeptDate(String deptDate) {
        this.deptDate = deptDate;
    }

    public String getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }

    public Integer getTicketsAmount() {
        return ticketsAmount;
    }

    public void setTicketsAmount(Integer ticketsAmount) {
        this.ticketsAmount = ticketsAmount;
    }

    public FlightDetails() { }
}
