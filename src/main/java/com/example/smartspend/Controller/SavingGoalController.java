package com.example.smartspend.Controller;

import com.example.smartspend.ApiResponse.ApiResponse;
import com.example.smartspend.Model.SavingGoal;
import com.example.smartspend.Service.SavingGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/saving-goal")
@RequiredArgsConstructor
public class SavingGoalController {

    private final SavingGoalService savingGoalService;

    @GetMapping("/get")
    public ResponseEntity<?> get(){
        List<SavingGoal> savingGoals = savingGoalService.get();

        if (savingGoals.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("The list is empty"));
        }

        return ResponseEntity.status(200).body(savingGoals);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid SavingGoal savingGoal, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (savingGoalService.add(savingGoal)){
            return ResponseEntity.status(200).body(new ApiResponse("Saving goal added successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid SavingGoal savingGoal, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (savingGoalService.update(id,savingGoal)){
            return ResponseEntity.status(200).body(new ApiResponse("Saving goal updated successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Saving goal or user not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        if (savingGoalService.delete(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Saving goal deleted successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Saving goal not found"));
    }

    @PutMapping("/add-money/{id}/{amount}")
    public ResponseEntity<?> addMoney(
            @PathVariable Integer id,
            @PathVariable BigDecimal amount){

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            return ResponseEntity.status(400).body(new ApiResponse("Amount must be greater than 0"));
        }

        if (savingGoalService.addMoney(id, amount)){
            return ResponseEntity.status(200).body(new ApiResponse("Money added successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Saving goal not found"));
    }

    @GetMapping("/progress/{id}")
    public ResponseEntity<?> getProgress(@PathVariable Integer id){

        String result = savingGoalService.getProgress(id);

        if (result == null){
            return ResponseEntity.status(400).body(new ApiResponse("Saving goal not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse(result));
    }

    @GetMapping("/required-saving-per-month/{id}")
    public ResponseEntity<?> getRequiredSavingPerMonth(@PathVariable Integer id){

        BigDecimal amount = savingGoalService.getRequiredSavingPerMonth(id);

        if (amount == null){
            return ResponseEntity.status(400).body(new ApiResponse("Saving goal not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Required saving per month: " + amount));
    }
}