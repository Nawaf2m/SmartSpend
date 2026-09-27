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
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Store name cannot be empty")
    @Size(min = 2, max = 50, message = "Store name must be between 2 and 50 characters")
    @Column(columnDefinition = "varchar(50) not null")
    private String storeName;

    @NotNull(message = "Total amount cannot be null")
    @Positive(message = "Total amount must be positive")
    @Column(columnDefinition = "decimal(12,2) not null")
    private BigDecimal totalAmount;

    @NotNull(message = "Purchase date cannot be null")
    @Column(columnDefinition = "date not null")
    private LocalDate purchaseDate;

    @NotNull(message = "User id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer user_id;

    @NotNull(message = "Category id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer category_id;
}