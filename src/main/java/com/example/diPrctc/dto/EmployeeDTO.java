package com.example.diPrctc.dto;

import com.example.diPrctc.annotations.AgeValidation;
import com.example.diPrctc.annotations.EmployeeRoleValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;

    @NotNull(message = "Name can't be null")
    private String name;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Put valid email")
    private String email;

//    @Max(message = "Age must be up to 80", value = 80)
//    @Min(message = "Age must be at least 18", value = 18)
    @AgeValidation
    private int age;

//    @Pattern(regexp = "^(ADMIN|USER)$", message = "Role of employee can be ADMIN or USER")
    @EmployeeRoleValidation
    private String role;

    @Digits(integer = 3, fraction = 2)
    @DecimalMin(value = "00.01")
    @DecimalMax(value = "100.00")
    @Positive(message = "Salary hike must be Positive")
    private Double salaryHike;

    @PastOrPresent(message = "Please! Enter valid date of joining")
    private LocalDate dateOfjoining;

    @JsonProperty("isActive")
    @AssertTrue(message = "Active must be true")
    private Boolean isActive;
}

/*

*/