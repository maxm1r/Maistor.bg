package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City,Integer> {

    public City findByCityName(String cityName);
}
