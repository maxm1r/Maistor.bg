package com.finalproject.finalproject.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finalproject.finalproject.model.dto.CategoryDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    private Set<User> users = new HashSet<>();

}
