package com.example.smartspend.Service;

import com.example.smartspend.DTO.ReceiptAIResponse;
import com.example.smartspend.Model.Category;
import com.example.smartspend.Model.Receipt;
import com.example.smartspend.Model.User;
import com.example.smartspend.Repository.CategoryRepository;
import com.example.smartspend.Repository.ReceiptRepository;
import com.example.smartspend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AIService aiService;

    public List<Receipt> get(){
        return receiptRepository.findAll();
    }


    public boolean add(MultipartFile image, Integer user_id) throws Exception {

        User user = userRepository.findUserById(user_id);

        if (user == null){
            return false;
        }

        ReceiptAIResponse aiResponse = aiService.analyzeReceipt(image);

        if (aiResponse == null || aiResponse.getStoreName() == null || aiResponse.getTotalAmount() == null || aiResponse.getPurchaseDate() == null || aiResponse.getCategory() == null){
            return false;
        }

        Category category = categoryRepository.findCategoryByNameIgnoreCase(aiResponse.getCategory());

        if (category == null){
            return false;
        }

        Receipt receipt = new Receipt();

        receipt.setStoreName(aiResponse.getStoreName());
        receipt.setTotalAmount(aiResponse.getTotalAmount());
        receipt.setPurchaseDate(aiResponse.getPurchaseDate());
        receipt.setUser_id(user_id);
        receipt.setCategory_id(category.getId());

        receiptRepository.save(receipt);

        return true;
    }


    public boolean update(Integer id, Receipt receipt){

        Receipt oldReceipt = receiptRepository.findReceiptById(id);

        if (oldReceipt == null){
            return false;
        }

        User user = userRepository.findUserById(receipt.getUser_id());

        Category category =
                categoryRepository.findCategoryById(receipt.getCategory_id());

        if (user == null || category == null){
            return false;
        }

        oldReceipt.setStoreName(receipt.getStoreName());
        oldReceipt.setTotalAmount(receipt.getTotalAmount());
        oldReceipt.setPurchaseDate(receipt.getPurchaseDate());
        oldReceipt.setUser_id(receipt.getUser_id());
        oldReceipt.setCategory_id(receipt.getCategory_id());

        receiptRepository.save(oldReceipt);

        return true;
    }


    public boolean delete(Integer id){

        Receipt receipt = receiptRepository.findReceiptById(id);

        if (receipt == null){
            return false;
        }

        receiptRepository.delete(receipt);

        return true;
    }
}