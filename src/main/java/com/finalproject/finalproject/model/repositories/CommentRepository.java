package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.dto.commentDTOS.ReplyDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByParentComment(Comment comment);
}
