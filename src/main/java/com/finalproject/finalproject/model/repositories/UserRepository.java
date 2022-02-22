package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmail(String email);
    Set<User> getAllByCategoriesContaining(Category category);
    @Query(value = "SELECT * FROM user WHERE is_workman = true",nativeQuery = true)
    Set<User> findAllWorkmans();
    User findUserByPhoneNumber(String phoneNumber);


}
