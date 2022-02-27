package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);
    Set<User> getAllByCategoriesContaining(Category category);
    @Query(value = "SELECT * FROM user WHERE is_workman = true",nativeQuery = true)
    Set<User> findAllWorkmans();
    User findUserByPhoneNumber(String phoneNumber);
    @Query(value = "SELECT * FROM user WHERE verification_code = :code",nativeQuery = true)
    Optional<User> findByVerificationCode(String code);
    Optional<User> findByEmailVerificationCode(String code);
    Optional<User> findByPhoneVerificationCode(String code);
    List<User> findAllByEmailEnabled( boolean emailEnabled);
    List<User> findAllByPhoneEnabled(boolean phoneEnabled);
    @Query("select u from User u where u.creationDate < ?1 and (u.emailEnabled = ?2 OR u.phoneEnabled = ?3)")
    List<User> findAllForDelete(LocalDate date, boolean emailEnabled, boolean phoneEnabled);
}
