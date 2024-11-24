package com.example.exerciese;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@DiscriminatorValue("RECTANGLE")

public class Rectangle extends Shape {

    @Override
    public double calculateArea() {
        return getPerimeters().get(0) * getPerimeters().get(1);
    }

    @Override
    public double calculatePerimeter() {
        return 2 * getPerimeters().get(0) + 2 * getPerimeters().get(1);
    }


}