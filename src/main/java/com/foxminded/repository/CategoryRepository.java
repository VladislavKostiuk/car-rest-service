package com.foxminded.repository;

import com.foxminded.model.Car;
import com.foxminded.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
//    List<Category> findAll();
    Page<Category> findAll(Pageable pageable);
}
