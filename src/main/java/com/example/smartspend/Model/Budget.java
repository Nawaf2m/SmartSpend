package com.example.smartspend.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    @Column(columnDefinition = "decimal(12,2) not null")
    private BigDecimal amount;

    @NotEmpty(message = "Period cannot be empty")
    @Pattern(
            regexp = "^(MONTHLY|YEARLY)$",
            message = "Period must be MONTHLY or YEARLY"
    )
    @Column(columnDefinition = "varchar(10) not null")
    private String period;

    @NotNull(message = "Start date cannot be null")
    @Column(columnDefinition = "date not null")
    private LocalDate startDate;

    @NotNull(message = "User id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer user_id;
}
