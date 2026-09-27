package com.example.smartspend.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @NotNull(message = "Target amount cannot be null")
    @Positive(message = "Target amount must be positive")
    @Column(columnDefinition = "decimal(12,2) not null")
    private BigDecimal targetAmount;

    @NotNull(message = "Saved amount cannot be null")
    @PositiveOrZero(message = "Saved amount cannot be negative")
    @Column(columnDefinition = "decimal(12,2) not null")
    private BigDecimal savedAmount;

    @NotNull(message = "Target date cannot be null")
    @Future(message = "Target date must be in the future")
    @Column(columnDefinition = "date not null")
    private LocalDate targetDate;

    @NotNull(message = "User id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer user_id;
}
