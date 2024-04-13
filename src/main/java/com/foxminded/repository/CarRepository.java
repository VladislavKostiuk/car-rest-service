package com.foxminded.repository;

import com.foxminded.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
//    List<Car> findAll();
    Page<Car> findAll(Pageable pageable);
    Optional<Car> findByModel_NameAndYear(String modelName, int year);
}
