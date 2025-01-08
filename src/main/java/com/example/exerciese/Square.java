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
@Component("Square")
@Entity
@DiscriminatorValue("Square")

public class Square extends Shape {

    @Override
    public Shape clone() {
        return new Square();
    }

@DiscriminatorValue("SQUARE")
public class Square extends Shape {

    @Override
    public double calculateArea() {
        return getPerimeters().get(0) * getPerimeters().get(0);
    }

    @Override
    public double calculatePerimeter() {
        return 4 * getPerimeters().get(0);

    }

}
