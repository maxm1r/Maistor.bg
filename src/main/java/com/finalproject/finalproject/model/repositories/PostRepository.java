package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
    void deleteById(int id);
    Post findById(int id);
}
