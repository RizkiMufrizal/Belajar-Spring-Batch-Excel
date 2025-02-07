package org.rizki.mufrizal.belajar.spring.batch.excel.object;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Employee implements Serializable {
    private String firstName;
    private String lastName;
    private String department;
    private Double salary;
}