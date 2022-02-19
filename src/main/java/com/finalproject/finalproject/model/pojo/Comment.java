package com.finalproject.finalproject.model.pojo;

import com.finalproject.finalproject.model.dto.UserWithoutPasswordDTO;
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
    @Column(name = "id")
    private int id;
    private String text;
    private LocalDateTime postedDate;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User ownerId;
    @ManyToOne
    @JoinColumn(name = "workman_id")
    private User workmanId;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "parent_comment_id"))
    private Comment parentComment;
}
