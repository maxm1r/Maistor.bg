package com.finalproject.finalproject.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isWorkman;
    private String profilePicture;
    private String phoneNumber;
    @ManyToMany
    @JoinTable(
            name = "workman_qualification",
            joinColumns = @JoinColumn(name = "workman_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
    @OneToMany
    @JoinColumn(name = "owner_id")
    private Set<Post> posts = new HashSet<>();
    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;
    @Column(name = "email_verification_code")
    private String emailVerificationCode;
    @Column(name = "phone_verification_code")
    private String phoneVerificationCode;
    @Column(name = "email_enabled")
    private volatile boolean emailEnabled;
    @Column(name = "phone_enabled")
    private volatile boolean phoneEnabled;
    @Column(name = "creation_date")
    private LocalDate creationDate;




}
