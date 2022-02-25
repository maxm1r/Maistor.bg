package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByParentComment(Comment comment);
    List<Comment> findAllByOwnerId(User user);
}
