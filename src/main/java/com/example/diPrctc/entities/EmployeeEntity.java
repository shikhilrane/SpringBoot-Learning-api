package com.example.diPrctc.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private int age;
    private String role;
    private Double salaryHike;
    private LocalDate dateOfjoining;
    @JsonProperty("isActive")
    private Boolean isActive;
}
