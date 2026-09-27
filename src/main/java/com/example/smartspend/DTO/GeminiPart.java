package com.example.smartspend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeminiPart {

    private String text;

    private GeminiInlineData inline_data;
}