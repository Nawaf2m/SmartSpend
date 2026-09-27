package com.example.smartspend.Controller;

import com.example.smartspend.ApiResponse.ApiResponse;
import com.example.smartspend.Model.User;
import com.example.smartspend.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> get(){
        List<User> users = userService.get();

        if (users.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("The list is empty"));
        }

        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid User user, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (userService.add(user)){
            return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Email already exists"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid User user, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (userService.update(id,user)){
            return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        if (userService.delete(id)){
            return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

    @GetMapping("/total-spending/{user_id}")
    public ResponseEntity<?> getTotalSpending(@PathVariable Integer user_id){
        BigDecimal total = userService.getTotalSpending(user_id);

        if (total == null){
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Total spending: " + total));
    }

    @GetMapping("/total-spending-between-dates/{user_id}/{startDate}/{endDate}")
    public ResponseEntity<?> getTotalSpendingBetweenDates(@PathVariable Integer user_id, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate){
        if (endDate.isBefore(startDate)){
            return ResponseEntity.status(400).body(new ApiResponse("End date must be after start date"));
        }

        BigDecimal total = userService.getTotalSpendingBetweenDates(user_id, startDate, endDate);

        if (total == null){
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Total spending between dates: " + total));
    }

    @GetMapping("/spending-by-category/{user_id}/{category_id}")
    public ResponseEntity<?> getSpendingByCategory(@PathVariable Integer user_id, @PathVariable Integer category_id){
        BigDecimal total = userService.getSpendingByCategory(user_id, category_id);

        if (total == null){
            return ResponseEntity.status(400).body(new ApiResponse("User or category not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Total spending in category: " + total));
    }

    @GetMapping("/highest-spending-category/{user_id}")
    public ResponseEntity<?> getHighestSpendingCategory(@PathVariable Integer user_id){
        String result = userService.getHighestSpendingCategory(user_id);

        if (result == null){
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse(result));
    }

    @GetMapping("/highest-expense/{user_id}")
    public ResponseEntity<?> getHighestExpense(@PathVariable Integer user_id){
        String result = userService.getHighestExpense(user_id);

        if (result == null){
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse(result));
    }

    @GetMapping("/average-daily-spending/{user_id}/{startDate}/{endDate}")
    public ResponseEntity<?> getAverageDailySpending(@PathVariable Integer user_id, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate){
        if (endDate.isBefore(startDate)){
            return ResponseEntity.status(400).body(new ApiResponse("End date must be after start date"));
        }

        BigDecimal average = userService.getAverageDailySpending(user_id, startDate, endDate);

        if (average == null){
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Average daily spending: " + average));
    }

    @GetMapping("/monthly-spending/{user_id}/{year}/{month}")
    public ResponseEntity<?> getMonthlySpending(@PathVariable Integer user_id, @PathVariable Integer year, @PathVariable Integer month){
        if (month < 1 || month > 12){
            return ResponseEntity.status(400).body(new ApiResponse("Month must be between 1 and 12"));
        }

        BigDecimal total = userService.getMonthlySpending(user_id, year, month);

        if (total == null){
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Monthly spending: " + total));
    }

    @GetMapping("/compare-monthly-spending/{user_id}/{firstYear}/{firstMonth}/{secondYear}/{secondMonth}")
    public ResponseEntity<?> compareMonthlySpending(@PathVariable Integer user_id, @PathVariable Integer firstYear, @PathVariable Integer firstMonth, @PathVariable Integer secondYear, @PathVariable Integer secondMonth){
        if (firstMonth < 1 || firstMonth > 12 || secondMonth < 1 || secondMonth > 12){
            return ResponseEntity.status(400).body(new ApiResponse("Month must be between 1 and 12"));
        }

        String result = userService.compareMonthlySpending(user_id, firstYear, firstMonth, secondYear, secondMonth);

        if (result == null){
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse(result));
    }
}