package org.lessons.spring_library.repository;

import org.lessons.spring_library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Integer> {

    public Category findByCategory(String category);
}
