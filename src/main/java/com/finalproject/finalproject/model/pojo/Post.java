package com.finalproject.finalproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "accepted_offer_id")
    private Offer acceptedOffer;
    private LocalDate postedDate;
    private LocalDate assignedDate;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    private String description;
    @OneToMany(cascade = {CascadeType.PERSIST},mappedBy = "post")
    private Set<Offer> offers = new HashSet<>();
}
