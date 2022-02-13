package com.finalproject.finalproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String categoryName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="workman_qualification" ,
            joinColumns = { @JoinColumn(name ="workman_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    private List<User> users;


}
