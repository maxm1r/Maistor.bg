package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City,Integer> {

    Optional<City> findByCityName(String cityName);
}
