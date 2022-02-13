package com.finalproject.finalproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;
    private LocalDateTime postedDate;
    @ManyToOne
    @JoinColumn(name = "owner_id",nullable = false)
    private User ownerId;
    @ManyToOne
    @JoinColumn(name = "workman_id",nullable = false)
    private User workmanId;
    @ManyToOne
    @JoinColumn(name = "id")
    private Comment parentComment;
}
