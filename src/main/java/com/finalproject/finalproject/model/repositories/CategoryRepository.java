package com.finalproject.finalproject.model.repositories;

import com.finalproject.finalproject.model.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    public boolean existsByCategoryName(String name);
    public Category removeCategoriesByCategoryName(String categoryName);
    public Category getByCategoryName(String categoryName);

}
