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
@Component("Circle")
@DiscriminatorValue("CIRCLE")
public class Circle extends Shape {

    @Override
    public double calculateArea() {
        return Math.PI * Math.pow(getPerimeters().get(0), 2);
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * getPerimeters().get(0);
    }


}
