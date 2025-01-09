package com.example.exerciese;


import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@NonNullApi
public interface ShapeRepository extends JpaRepository<Shape, Long> {
    @Query(value = "SELECT * FROM shape WHERE type = :type", nativeQuery = true)
    List<Shape> findByType(@Param("type") String type);
}

