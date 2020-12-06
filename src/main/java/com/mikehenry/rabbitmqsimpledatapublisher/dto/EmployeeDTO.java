package com.mikehenry.rabbitmqsimpledatapublisher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EmployeeDTO {

    String employeeName;
    int salary;
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


