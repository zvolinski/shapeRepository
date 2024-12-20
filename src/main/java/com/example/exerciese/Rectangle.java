package com.example.exerciese;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Component("Rectangle")
@DiscriminatorValue("Rectangle")
public class Rectangle extends Shape {

    @Override
    public Shape clone() {
        return new Rectangle();
    }


    @Override
    public double calculateArea() {
        return getPerimeters().get(0) * getPerimeters().get(1);
    }

    @Override
    public double calculatePerimeter() {
        return 2 * getPerimeters().get(0) + 2 * getPerimeters().get(1);
    }


}
