package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    public boolean existsByCategoryName(String categoryName);
    public int removeCategoriesByCategoryName(String categoryName);
    public Category findByCategoryName( String categoryName);
    void deleteAllByCategoryName(String categoryName);
    public Category getByCategoryName(String categoryName);
}
