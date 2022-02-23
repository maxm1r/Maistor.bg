package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Offer;
import com.finalproject.finalproject.model.pojo.Post;
import com.finalproject.finalproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer,Integer> {
    List<Offer> findAllByUser(User user);
    List<Offer> findAllByPost(Post post);
}
