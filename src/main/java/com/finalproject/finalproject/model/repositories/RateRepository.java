package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Rates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RateRepository extends JpaRepository<Rates,Integer> {

    @Query(value = "SELECT AVG(rating) FROM user_rated_user WHERE user_rated_id = :id",nativeQuery = true)
    double getAverageRatingForUser(@Param("id") int id);
    @Query(value = "SELECT * FROM user_rated_user WHERE user_rated_id = :ratedId AND user_id = :raterId LIMIT 1",nativeQuery = true)
    Rates findByUserIdAndRatedWorkmanId(@Param("raterId") int userId,@Param("ratedId") int ratedWorkmanId);

}
