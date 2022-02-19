package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
