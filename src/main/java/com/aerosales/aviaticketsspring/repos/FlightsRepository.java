package com.aerosales.aviaticketsspring.repos;

import com.aerosales.aviaticketsspring.pojo.FlightDetails;
import org.springframework.data.repository.CrudRepository;

public interface FlightsRepository extends CrudRepository<FlightDetails, Integer> {
}
