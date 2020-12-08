package com.mikehenry.rabbitmqsimpledatapublisher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
public class EmployeeDTO {

    @NotEmpty
    String employeeName;

    @Min(value = 1, message = "salary should not be less than 1")
    double salary;

    @NotEmpty
    @Min(value = 15, message = "age of employee should be greater than 16")
    @Max(value = 90, message = "employee is too old")
    int age;

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "employeeName='" + employeeName + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                '}';
    }
}


