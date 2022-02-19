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
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade ={ CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private double pricePerHour;
    private LocalDateTime offer_date;
    private int hoursNeeded;
    private int daysNeeded;
    @OneToOne(mappedBy = "acceptedOffer")
    private Post acceptedBy;

}
