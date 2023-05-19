package com.aerosales.aviaticketsspring.controllers;

import com.aerosales.aviaticketsspring.pojo.Flight;
import com.aerosales.aviaticketsspring.pojo.User;
import com.aerosales.aviaticketsspring.repos.FlightsRepository;
import com.aerosales.aviaticketsspring.repos.UsersRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
public class TrackController {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FlightsRepository flightsRepository;

    @GetMapping("/tracks")
    public String tracks(Principal principal, Model model) {
        User user = usersRepository.findByUsername(principal.getName());
        List<Flight> flightList = user.getFlightsList();
        model.addAttribute("user", user);
        model.addAttribute("userFlights", flightList);
        return "tracks";
    }

    @GetMapping("flights/add-flight/{id}")
    public String addFlight(@PathVariable("id")Integer flightNumber, Principal principal, Model model) throws Exception {
        User user = usersRepository.findByUsername(principal.getName());
        List<Flight> flightsList = user.getFlightsList();
        String path = "src/main/resources/flights.json";
        File file = new File(path);
        String stringFlights = new String(Files.readAllBytes(Paths.get(file.toURI())));
        JSONObject jsonFlights = new JSONObject(stringFlights);
        boolean isOneWay = jsonFlights.getBoolean("is_one_way");
        int ticketsAmount = jsonFlights.getInt("tickets_amount");
        String flightName = jsonFlights.getString("flight_name");
        String flightDate = jsonFlights.getString("flight_date");
        String travelClass = jsonFlights.getString("travel_class");
        JSONArray flightsArray = jsonFlights.getJSONArray("flights");
        JSONObject flightJson = flightsArray.getJSONObject(flightNumber);
        double ticketPrice = flightJson.getDouble("ticket_price");
        String flightDuration = flightJson.getString("duration");
        int transferCount = flightJson.getInt("transfer_count");
        String departAirport = flightJson.getString("depart_airport");
        String arrivalAirport = flightJson.getString("arrival_airport");
        String departTime = flightJson.getString("depart_time");
        String arrivalTime = flightJson.getString("arrival_time");
        String airlineName = flightJson.getString("airline_name");
        if (!isOneWay)
            flightDate = flightDate.substring(0, flightDate.indexOf('%'));
        Flight flight = new Flight(flightName, isOneWay, flightDuration, flightName.substring(0, flightName.indexOf(' ')),
            flightName.substring(flightName.lastIndexOf(' ')+1), flightDate, travelClass, transferCount,
            ticketsAmount, departAirport, arrivalAirport, departTime, arrivalTime, ticketPrice, airlineName, user.getFlightsList());
        flightsRepository.save(flight);
        flightsList.add(flight);
        if (!isOneWay) {
            String returnFlightName = flightJson.getString("return_flight_name");
            String returnDate = jsonFlights.getString("return_date");
            String returnFlightDuration = flightJson.getString("return_duration");
            int returnTransferCount = flightJson.getInt("return_transfer_count");
            String returnDepartAirport = flightJson.getString("return_depart_airport");
            String returnArrivalAirport = flightJson.getString("return_arrival_airport");
            String returnDepartTime = flightJson.getString("return_depart_time");
            String returnArrivalTime = flightJson.getString("return_arrival_time");
            String returnAirlineName = flightJson.getString("return_airline_name");
            Flight returnFlight = new Flight(returnFlightName, false, returnFlightDuration, flightName.substring(flightName.lastIndexOf(' ')+1),
                flightName.substring(0, flightName.indexOf(' ')), returnDate, travelClass, returnTransferCount, ticketsAmount, returnDepartAirport,
                returnArrivalAirport, returnDepartTime, returnArrivalTime, ticketPrice, returnAirlineName, user.getFlightsList());
            flightsRepository.save(returnFlight);
            flightsList.add(returnFlight);
        }
        new PrintWriter(path).close();
        user.setFlightsList(flightsList);
        model.addAttribute("user", user);
        model.addAttribute("userFlights", flightsList);
        return "tracks";
    }

    @GetMapping("tracks/delete-flight/{id}")
    public String deleteFlight(@PathVariable("id")Integer flightId, Principal principal) {
        User user = usersRepository.findByUsername(principal.getName());
        List<Flight> flightList = user.getFlightsList();
        flightList.remove(flightId);
        flightsRepository.deleteById(flightId);
        user.setFlightsList(flightList);
        return "tracks";
    }
}
