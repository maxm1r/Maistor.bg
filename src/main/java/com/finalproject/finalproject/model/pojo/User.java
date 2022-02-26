package com.finalproject.finalproject.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
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
    @Column(name = "verification_code")
    private String verificationCode;
    private volatile boolean enabled;




}
