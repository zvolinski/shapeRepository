package com.example.exerciese;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder

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
