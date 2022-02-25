package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Post;
import com.finalproject.finalproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
    void deleteById(int id);
    List<Post> findAllByOwner(User User);
}
