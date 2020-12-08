package com.mikehenry.rabbitmqsimpledatapublisher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Setter
@Getter
@AllArgsConstructor
public class EmployeeDTO {

    @NotEmpty(message = "Employee name is required and should not be empty")
    String employeeName;

    @NotEmpty(message = "Salary must not be empty")
    @Min(value = 1, message = "salary should be greater than 1")
    @Digits(integer = 11, message = "Enter a valid salary, up to 2 dp", fraction = 2)
    String salary;

    @NotEmpty(message = "Age must not be empty")
    @Min(value = 15, message = "age of employee should be greater than 16")
    @Max(value = 90, message = "employee is too old")
    @Digits(integer = 5, message = "Enter a valid age in numeric value", fraction = 0)
    String age;

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "employeeName='" + employeeName + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                '}';
    }
}


