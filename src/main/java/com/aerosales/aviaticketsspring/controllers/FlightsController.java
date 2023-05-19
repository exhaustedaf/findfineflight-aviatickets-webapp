package com.aerosales.aviaticketsspring.controllers;

import com.aerosales.aviaticketsspring.pojo.Flight;
import com.aerosales.aviaticketsspring.pojo.User;
import com.aerosales.aviaticketsspring.repos.FlightsRepository;
import com.aerosales.aviaticketsspring.repos.UsersRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FlightsController {
    private static final String path = "src/main/resources/flights.json";
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private FlightsRepository flightsRepository;

    @GetMapping("/flights-list")
    public String flightsList(Model model) {
        return "flights-list";
    }

    @GetMapping("/flights/search")
    public String flights(Model model) {
        return "flights";
    }

    @PostMapping("/flights/search")
    public String searchFlights(@RequestParam String origCity, @RequestParam String destCity,
                                @RequestParam String travelClass, @RequestParam String departDate,
                                @RequestParam String returnDate, @RequestParam Integer adultCount,
                                @RequestParam Integer childrenCount, Model model, Principal principal) throws Exception {
        User user = usersRepository.findByUsername(principal.getName());
        HttpResponse<JsonNode> cityCodes = Unirest.get("https://www.travelpayouts.com/widgets_suggest_params?q=Из%20" +
                origCity + "%20в%20" + destCity)
                .asJson();
        JSONObject cityCodesJson = cityCodes.getBody().getObject();
        String origCode = cityCodesJson.getJSONObject("origin").getString("iata");
        String destCode = cityCodesJson.getJSONObject("destination").getString("iata");

        departDate = departDate.replace('.', '-');
        boolean isOneWay = true;
        if (!returnDate.equals("")) {
            isOneWay = false;
            String oldOrigCode = origCode;
            origCode = origCode + "%2C" + destCode;
            destCode = destCode + "%2C" + oldOrigCode;
            returnDate = returnDate.replace('.', '-');
            departDate = departDate + "%2C" + returnDate;
        }

        final String getResponse;
        if (childrenCount == 0) {
            getResponse = "https://priceline-com-provider.p.rapidapi.com/v2/flight/roundTrip?sid=iSiX639&adults="
                + adultCount + "&departure_date=" + departDate + "&destination_airport_code=" + destCode
                + "&cabin_class=" + travelClass + "&origin_airport_code=" + origCode + "&number_of_itineraries=10";
        } else {
            getResponse = "https://priceline-com-provider.p.rapidapi.com/v2/flight/roundTrip?sid=iSiX639&adults="
                + adultCount + "&departure_date=" + departDate + "&destination_airport_code=" + destCode
                + "&cabin_class=" + travelClass + "&origin_airport_code=" + origCode + "&children=" + childrenCount
                + "&number_of_itineraries=10";
        }

        HttpResponse<JsonNode> flightsResponse = Unirest.get(getResponse)
            .header("X-RapidAPI-Key", "c27b3226d8mshdab182b14d39cb0p176a8ejsn130f3c39ffbd")
            .header("X-RapidAPI-Host", "priceline-com-provider.p.rapidapi.com")
            .asJson();
        JSONObject flightsJson = flightsResponse.getBody().getObject();
        JSONObject resultsJson = flightsJson.getJSONObject("getAirFlightRoundTrip").getJSONObject("results");

        if (resultsJson.getInt("status_code") == 100) {
            List<Flight> flightList = new ArrayList<>();
            JSONObject saveFlightJson = new JSONObject();
            saveFlightJson.put("is_one_way", isOneWay);
            saveFlightJson.put("tickets_amount", adultCount +  childrenCount);
            saveFlightJson.put("flight_name", origCity + " - " +  destCity);
            saveFlightJson.put("flight_date", departDate);
            saveFlightJson.put("travel_class", travelClass);
            JSONArray flightsArray = new JSONArray();
            if (!isOneWay)
                saveFlightJson.put("return_date", returnDate);
            JSONObject flightsData = resultsJson.getJSONObject("result").getJSONObject("itinerary_data");
            for (int i = 0; i < 10; i++) {
                JSONObject singleFlightJson = new JSONObject();
                JSONObject flightJson = flightsData.getJSONObject("itinerary_" + i);
                double ticketPrice = flightJson.getJSONObject("price_details").getDouble("display_total_fare");
                if(!isOneWay)
                    ticketPrice = Math.round(ticketPrice / (adultCount + childrenCount));
                singleFlightJson.put("ticket_price", ticketPrice);
                JSONObject flightData = flightJson.getJSONObject("slice_data").getJSONObject("slice_0");
                String flightDuration = flightData.getJSONObject("info").getString("duration");
                singleFlightJson.put("duration", flightDuration);
                int transferCount = flightData.getJSONObject("info").getInt("connection_count");
                singleFlightJson.put("transfer_count", transferCount);
                String departAirport = flightData.getJSONObject("departure").getJSONObject("airport").getString("code");
                singleFlightJson.put("depart_airport", departAirport);
                String arrivalAirport = flightData.getJSONObject("arrival").getJSONObject("airport").getString("code");
                singleFlightJson.put("arrival_airport", arrivalAirport);
                String departTime = flightData.getJSONObject("departure").getJSONObject("datetime").getString("time_24h");
                singleFlightJson.put("depart_time", departTime);
                String arrivalTime = flightData.getJSONObject("arrival").getJSONObject("datetime").getString("time_24h");
                singleFlightJson.put("arrival_time", arrivalTime);
                String airlineName = flightData.getJSONObject("airline").getString("name");
                singleFlightJson.put("airline_name", airlineName);

                flightList.add(new Flight(origCity+" - "+destCity, isOneWay, flightDuration, origCity, destCity, departDate,
                    travelClass, transferCount, adultCount+childrenCount, departAirport, arrivalAirport, departTime,
                    arrivalTime, ticketPrice, airlineName, user.getFlightsList()));
                if (!isOneWay) {
                    singleFlightJson.put("return_flight_name", destCity + " - " + origCity);
                    JSONObject returnFlightData = flightJson.getJSONObject("slice_data").getJSONObject("slice_1");
                    String returnFlightDuration = returnFlightData.getJSONObject("info").getString("duration");
                    singleFlightJson.put("return_duration", returnFlightDuration);
                    int returnTransferCount = returnFlightData.getJSONObject("info").getInt("connection_count");
                    singleFlightJson.put("return_transfer_count", returnTransferCount);
                    String returnDepartAirport = returnFlightData.getJSONObject("departure").getJSONObject("airport").getString("code");
                    singleFlightJson.put("return_depart_airport", returnDepartAirport);
                    String returnArrivalAirport = returnFlightData.getJSONObject("arrival").getJSONObject("airport").getString("code");
                    singleFlightJson.put("return_arrival_airport", returnArrivalAirport);
                    String returnDepartTime = returnFlightData.getJSONObject("departure").getJSONObject("datetime").getString("time_24h");
                    singleFlightJson.put("return_depart_time", returnDepartTime);
                    String returnArrivalTime = returnFlightData.getJSONObject("arrival").getJSONObject("datetime").getString("time_24h");
                    singleFlightJson.put("return_arrival_time", returnArrivalTime);
                    String returnAirlineName = returnFlightData.getJSONObject("airline").getString("name");
                    singleFlightJson.put("return_airline_name", returnAirlineName);

                    flightList.add(new Flight(destCity+" - "+origCity, false, returnFlightDuration, destCity, origCity, returnDate,
                        travelClass, returnTransferCount, adultCount+childrenCount, returnDepartAirport, returnArrivalAirport,
                        returnDepartTime, returnArrivalTime, ticketPrice, returnAirlineName, user.getFlightsList()));
                }
                flightsArray.put(singleFlightJson);
            }
            saveFlightJson.put("flights", flightsArray);
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(saveFlightJson.toString());
            writer.close();
            model.addAttribute("flights", flightList);
            return "flights-list";
        } else return "redirect:/error";
    }
}
