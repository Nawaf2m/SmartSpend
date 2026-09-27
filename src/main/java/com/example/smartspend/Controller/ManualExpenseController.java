package com.example.smartspend.Controller;

import com.example.smartspend.ApiResponse.ApiResponse;
import com.example.smartspend.Model.ManualExpense;
import com.example.smartspend.Service.ManualExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manual-expense")
@RequiredArgsConstructor
public class ManualExpenseController {

    private final ManualExpenseService manualExpenseService;

    @GetMapping("/get")
    public ResponseEntity<?> get(){
        List<ManualExpense> manualExpenses = manualExpenseService.get();

        if (manualExpenses.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("The list is empty"));
        }

        return ResponseEntity.status(200).body(manualExpenses);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid ManualExpense manualExpense, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (manualExpenseService.add(manualExpense)){
            return ResponseEntity.status(200).body(new ApiResponse("Manual expense added successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("User or category not found"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid ManualExpense manualExpense, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (manualExpenseService.update(id,manualExpense)){
            return ResponseEntity.status(200).body(new ApiResponse("Manual expense updated successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Manual expense, user or category not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        if (manualExpenseService.delete(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Manual expense deleted successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Manual expense not found"));
    }
}