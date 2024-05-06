package com.foxminded.dal.repository;

import com.foxminded.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    Page<Model> findAll(Pageable pageable);
    Optional<Model> findByName(String name);
}
