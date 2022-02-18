package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
