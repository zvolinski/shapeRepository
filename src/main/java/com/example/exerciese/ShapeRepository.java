package com.example.exerciese;


import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
@NonNullApi
public interface ShapeRepository extends JpaRepository<Shape, Long> {

}
