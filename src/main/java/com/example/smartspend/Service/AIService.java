package com.example.smartspend.Service;

import com.example.smartspend.DTO.GeminiContent;
import com.example.smartspend.DTO.GeminiInlineData;
import com.example.smartspend.DTO.GeminiPart;
import com.example.smartspend.DTO.GeminiRequest;
import com.example.smartspend.DTO.ReceiptAIResponse;
import com.example.smartspend.Model.Category;
import com.example.smartspend.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final CategoryRepository categoryRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();


    public String convertImageToBase64(MultipartFile image) throws IOException {
        return Base64.getEncoder().encodeToString(image.getBytes());
    }


    public ReceiptAIResponse analyzeReceipt(MultipartFile image) throws Exception {
        String base64Image = convertImageToBase64(image);

        String imageType = image.getContentType();

        List<Category> categories = categoryRepository.findAll();

        String categoryNames = "";

        for (Category category : categories){
            categoryNames += category.getName() + ", ";
        }

        String prompt =
                """
                Analyze this receipt and return ONLY JSON.

                Return exactly these fields:
                {
                  "storeName": "store name",
                  "totalAmount": 0.00,
                  "purchaseDate": "yyyy-MM-dd",
                  "category": "category name"
                }

                The category must be one of these categories:
                """ + categoryNames + """

                Choose only one category from the provided list.
                Do not create a new category.
                Do not return any explanation outside the JSON.
                """;

        GeminiInlineData inlineData = new GeminiInlineData(imageType, base64Image);

        GeminiPart textPart = new GeminiPart(prompt, null);

        GeminiPart imagePart = new GeminiPart(null, inlineData);

        GeminiContent content = new GeminiContent(List.of(textPart, imagePart));

        GeminiRequest body = new GeminiRequest(List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiRequest> request = new HttpEntity<>(body, headers);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent?key=" + apiKey;

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return extractAIResult(response.getBody());
    }


    public ReceiptAIResponse extractAIResult(String response) throws Exception {
        JsonNode root = objectMapper.readTree(response);

        String result = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();


        result = result.replace("```json", "").replace("```", "").trim();


        return objectMapper.readValue(result, ReceiptAIResponse.class);
    }
}