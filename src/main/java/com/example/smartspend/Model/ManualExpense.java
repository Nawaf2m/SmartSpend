package com.example.smartspend.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManualExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Description cannot be empty")
    @Size(min = 3, max = 100, message = "Description must be between 3 and 100 characters")
    @Column(columnDefinition = "varchar(100) not null")
    private String description;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    @Column(columnDefinition = "decimal(12,2) not null")
    private BigDecimal amount;

    @NotNull(message = "Expense date cannot be null")
    @Column(columnDefinition = "date not null")
    private LocalDate expenseDate;

    @NotNull(message = "User id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer user_id;

    @NotNull(message = "Category id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer category_id;
}
