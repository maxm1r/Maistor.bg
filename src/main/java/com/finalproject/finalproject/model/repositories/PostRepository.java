package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.Post;
import com.finalproject.finalproject.model.pojo.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
    void deleteById(int id);
    List<Post> findAllByOwner(User User);
    List<Post> findAllByCategory(Category category, Pageable pageable);
}
