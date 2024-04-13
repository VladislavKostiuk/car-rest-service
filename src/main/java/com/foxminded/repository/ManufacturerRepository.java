package com.foxminded.repository;

import com.foxminded.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
//    List<Manufacturer> findAll();
    Page<Manufacturer> findAll(Pageable pageable);
    Optional<Manufacturer> findByName(String name);
}
