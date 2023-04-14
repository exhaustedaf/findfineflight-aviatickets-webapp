package com.aerosales.aviaticketsspring.pojo;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer user_id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;
    
    @Column(name="role")
    private String role;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name="user_flight_list",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="flight_id"))
    private Set<FlightDetails> flightDetailsList;

    public Set<FlightDetails> getFlightDetailsList() {
        return flightDetailsList;
    }

    public void setFlightDetailsList(Set<FlightDetails> flightDetailsList) {
        this.flightDetailsList = flightDetailsList;
    }

    public Users() { }

    public Users(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Integer getUsers_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
