package com.example.smartspend.Controller;

import com.example.smartspend.ApiResponse.ApiResponse;
import com.example.smartspend.Model.Budget;
import com.example.smartspend.Service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping("/get")
    public ResponseEntity<?> get(){
        List<Budget> budgets = budgetService.get();

        if (budgets.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("The list is empty"));
        }

        return ResponseEntity.status(200).body(budgets);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Budget budget, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (budgetService.add(budget)){
            return ResponseEntity.status(200).body(new ApiResponse("Budget added successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid Budget budget, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (budgetService.update(id,budget)){
            return ResponseEntity.status(200).body(new ApiResponse("Budget updated successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Budget or user not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        if (budgetService.delete(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Budget deleted successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Budget not found"));
    }

    @GetMapping("/remaining/{user_id}")
    public ResponseEntity<?> getRemainingBudget(@PathVariable Integer user_id){

        BigDecimal remaining = budgetService.getRemainingBudget(user_id);

        if (remaining == null){
            return ResponseEntity.status(400).body(new ApiResponse("User or budget not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Remaining budget: " + remaining));
    }

    @GetMapping("/usage-percentage/{user_id}")
    public ResponseEntity<?> getBudgetUsagePercentage(@PathVariable Integer user_id){

        BigDecimal percentage = budgetService.getBudgetUsagePercentage(user_id);

        if (percentage == null){
            return ResponseEntity.status(400).body(new ApiResponse("User or budget not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Budget usage: " + percentage + "%"));
    }

    @GetMapping("/check-exceeded/{user_id}")
    public ResponseEntity<?> checkBudgetExceeded(@PathVariable Integer user_id){

        String result = budgetService.checkBudgetExceeded(user_id);

        if (result == null){
            return ResponseEntity.status(400).body(new ApiResponse("User or budget not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse(result));
    }

    @GetMapping("/forecast/{user_id}")
    public ResponseEntity<?> forecastBudgetSpending(@PathVariable Integer user_id){

        BigDecimal forecast = budgetService.forecastBudgetSpending(user_id);

        if (forecast == null){
            return ResponseEntity.status(400).body(new ApiResponse("User or budget not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Forecasted spending: " + forecast));
    }

    @PostMapping("/send-budget-status/{user_id}")
    public ResponseEntity<?> sendBudgetStatus(@PathVariable Integer user_id){

        String result = budgetService.sendBudgetStatus(user_id);

        if (result == null){
            return ResponseEntity.status(400).body(new ApiResponse("User or budget not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse(result));
    }
}