package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    boolean existsByCategoryName(String categoryName);
    int removeCategoriesByCategoryName(String categoryName);
    Optional<Category> findByCategoryName(String categoryName);
    void deleteAllByCategoryName(String categoryName);
    Category getByCategoryName(String categoryName);
}
