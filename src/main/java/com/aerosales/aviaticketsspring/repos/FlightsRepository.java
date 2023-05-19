package com.aerosales.aviaticketsspring.repos;

import com.aerosales.aviaticketsspring.pojo.Flight;
import org.springframework.data.repository.CrudRepository;

public interface FlightsRepository extends CrudRepository<Flight, Integer> {
}
