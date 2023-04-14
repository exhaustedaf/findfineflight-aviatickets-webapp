package com.aerosales.aviaticketsspring.controllers;

import com.aerosales.aviaticketsspring.pojo.FlightDetails;
import com.aerosales.aviaticketsspring.repos.FlightsRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FlightsController {
    @Autowired
    private FlightsRepository flightsRepository;

    @GetMapping("/flightslist")
    public String flightsList(Model model) {
        Iterable<FlightDetails> flights = flightsRepository.findAll();
        model.addAttribute("flights", flights);
        return "flights-list";
    }

    @GetMapping("/flights/search")
    public String flights(Model model) {
        return "flights";
    }

    @PostMapping("/flights/search")
    public String searchFlights(@RequestParam String dept_city, @RequestParam String dest_city,
                                @RequestParam String depart_time, @RequestParam String arrival_time,
                                @RequestParam String travel_class, @RequestParam Integer tickets_amount,
                                Model model) throws Exception {
        HttpResponse<JsonNode> deptResponse = Unirest.get("https://priceline-com-provider.p.rapidapi.com/v1/flights/locations?name=" + dept_city)
                .header("x-rapidapi-key", "YOUR_API")
                .header("x-rapidapi-host", "priceline-com-provider.p.rapidapi.com")
                .asJson();
        JSONArray deptCityAirports = deptResponse.getBody().getArray();
        String deptAirportName = deptCityAirports.getJSONObject(0).getString("cityCode");
        HttpResponse<JsonNode> destResponse = Unirest.get("https://priceline-com-provider.p.rapidapi.com/v1/flights/locations?name=" + dest_city)
                .header("x-rapidapi-key", "YOUR_API")
                .header("x-rapidapi-host", "priceline-com-provider.p.rapidapi.com")
                .asJson();
        JSONArray destCityAirports = destResponse.getBody().getArray();
        String destAirportName = destCityAirports.getJSONObject(0).getString("cityCode");
        depart_time = depart_time.replace('/', '-');
        if (!arrival_time.equals("")) {
            arrival_time = arrival_time.replace('/', '-');
               HttpResponse<JsonNode> fullResponse = Unirest.get("https://priceline-com-provider.p.rapidapi.com/v1/flights/search?date_departure=" + depart_time +
                        "&itinerary_type=ROUND_TRIP&class_type=" + travel_class + "&location_departure=" + deptAirportName +
                        "&sort_order=PRICE&location_arrival=" + destAirportName + "&date_departure_return=" + arrival_time +
                        "&number_of_passengers=" + tickets_amount)
                        .header("x-rapidapi-key", "YOUR_API")
                        .header("x-rapidapi-host", "priceline-com-provider.p.rapidapi.com")
                        .asJson();
                JSONObject flightInfo = fullResponse.getBody().getObject();
                int minTotalFare = flightInfo.getJSONObject("filteredTripSummary").getInt("minTotalFare");
                int maxTotalFare = flightInfo.getJSONObject("filteredTripSummary").getInt("maxTotalFare");
                int minDuration = flightInfo.getJSONObject("filteredTripSummary").getInt("minDuration");
                int maxDuration = flightInfo.getJSONObject("filteredTripSummary").getInt("maxDuration");
                JSONArray times = flightInfo.getJSONArray("sliceSummary");
                String minTakeoffTime = times.getJSONObject(0).getString("minTakeoffTime");
                String maxTakeoffTime = times.getJSONObject(0).getString("maxTakeoffTime");
                String minLandingTime = times.getJSONObject(0).getString("minLandingTime");
                String maxLandingTime = times.getJSONObject(0).getString("maxLandingTime");
                minDuration /= 60;
                maxDuration /= 60;
                FlightDetails firstFlight = new FlightDetails(dept_city + "-" + dest_city, minDuration, dept_city,
                        dest_city, minTakeoffTime, minLandingTime, travel_class, tickets_amount, minTotalFare);
                FlightDetails secondFlight = new FlightDetails(dept_city + "-" + dest_city, maxDuration, dept_city,
                        dest_city, maxTakeoffTime, maxLandingTime, travel_class, tickets_amount, maxTotalFare);
                flightsRepository.save(firstFlight);
                flightsRepository.save(secondFlight);
        } else {
            HttpResponse<JsonNode> fullResponse = Unirest.get("https://priceline-com-provider.p.rapidapi.com/v1/flights/search?date_departure=" + depart_time +
                        "&itinerary_type=ONE_WAY&class_type=" + travel_class + "&location_departure=" + deptAirportName +
                        "&sort_order=PRICE&location_arrival=" + destAirportName + "&number_of_passengers=" + tickets_amount)
                        .header("x-rapidapi-key", "YOUR_API")
                        .header("x-rapidapi-host", "priceline-com-provider.p.rapidapi.com")
                        .asJson();
                JSONObject flightInfo = fullResponse.getBody().getObject();
                int minTotalFare = flightInfo.getJSONObject("filteredTripSummary").getInt("minTotalFare");
                int maxTotalFare = flightInfo.getJSONObject("filteredTripSummary").getInt("maxTotalFare");
                int minDuration = flightInfo.getJSONObject("filteredTripSummary").getInt("minDuration");
                int maxDuration = flightInfo.getJSONObject("filteredTripSummary").getInt("maxDuration");
                JSONArray times = flightInfo.getJSONArray("sliceSummary");
                String minTakeoffTime = times.getJSONObject(0).getString("minTakeoffTime");
                String maxTakeoffTime = times.getJSONObject(0).getString("maxTakeoffTime");
                String minLandingTime = times.getJSONObject(0).getString("minLandingTime");
                String maxLandingTime = times.getJSONObject(0).getString("maxLandingTime");
                minDuration /= 60;
                maxDuration /= 60;
                FlightDetails firstFlight = new FlightDetails(dept_city + "-" + dest_city, minDuration, dept_city,
                        dest_city, minTakeoffTime, minLandingTime, travel_class, tickets_amount, minTotalFare);
                FlightDetails secondFlight = new FlightDetails(dept_city + "-" + dest_city, maxDuration, dept_city,
                        dest_city, maxTakeoffTime, maxLandingTime, travel_class, tickets_amount, maxTotalFare);
                flightsRepository.save(firstFlight);
                flightsRepository.save(secondFlight);
        }
        return "redirect:/flightslist";
    }
}
