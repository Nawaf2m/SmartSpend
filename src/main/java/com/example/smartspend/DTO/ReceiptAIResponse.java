package com.example.smartspend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptAIResponse {

    private String storeName;

    private BigDecimal totalAmount;

    private LocalDate purchaseDate;

    private String category;
}