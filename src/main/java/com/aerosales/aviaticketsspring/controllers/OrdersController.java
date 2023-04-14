package com.aerosales.aviaticketsspring.controllers;

import com.aerosales.aviaticketsspring.pojo.FlightDetails;
import com.aerosales.aviaticketsspring.pojo.Users;
import com.aerosales.aviaticketsspring.repos.FlightsRepository;
import com.aerosales.aviaticketsspring.repos.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
public class OrdersController {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FlightsRepository flightsRepository;

    @GetMapping("/orders")
    public ModelAndView orders(Principal principal) {
        ModelAndView mv = new ModelAndView("/orders");
        Users user = usersRepository.findByUsername(principal.getName());
        mv.addObject("user", user);
        return mv;
    }

    @GetMapping("/addOrder/{id}")
    public String addOrder(@PathVariable("id")Integer flightId, Principal principal) {
        ModelAndView mv = new ModelAndView("/orders");
        Users user = usersRepository.findByUsername(principal.getName());
        FlightDetails flight = flightsRepository.findById(flightId).get();
        Set<FlightDetails> flightDetailsList = new LinkedHashSet<>();
        flightDetailsList.add(flight);
        user.setFlightDetailsList(flightDetailsList);
        Set<Users> userList = new LinkedHashSet<>();
        userList.add(user);
        flight.setUserList(userList);
        Set<FlightDetails> newFlightDetailsList = user.getFlightDetailsList();
        Set<FlightDetails> oldFlightDetailsList = (usersRepository.findByUsername(user.getUsername())).getFlightDetailsList();
        newFlightDetailsList.addAll(oldFlightDetailsList);
        user.setFlightDetailsList(newFlightDetailsList);
        usersRepository.save(user);
        flightsRepository.save(flight);
        mv.addObject("user", user);
        return "redirect:/orders";
    }
}
