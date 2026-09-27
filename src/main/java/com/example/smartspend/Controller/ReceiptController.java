package com.example.smartspend.Controller;

import com.example.smartspend.ApiResponse.ApiResponse;
import com.example.smartspend.Model.Receipt;
import com.example.smartspend.Service.ReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;


    @GetMapping("/get")
    public ResponseEntity<?> get(){

        List<Receipt> receipts = receiptService.get();

        if (receipts.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("The list is empty"));
        }

        return ResponseEntity.status(200).body(receipts);
    }


    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam MultipartFile image,
                                 @RequestParam Integer user_id){

        try {

            if (image.isEmpty()){
                return ResponseEntity.status(400).body(new ApiResponse("Image cannot be empty"));
            }

            if (receiptService.add(image, user_id)){
                return ResponseEntity.status(200).body(new ApiResponse("Receipt added successfully"));
            }

            return ResponseEntity.status(400).body(new ApiResponse("User, category or receipt data not found"));

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(new ApiResponse(e.getMessage()));
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody @Valid Receipt receipt,
                                    Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (receiptService.update(id, receipt)){
            return ResponseEntity.status(200).body(new ApiResponse("Receipt updated successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Receipt, user or category not found"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){

        if (receiptService.delete(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Receipt deleted successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Receipt not found"));
    }
}