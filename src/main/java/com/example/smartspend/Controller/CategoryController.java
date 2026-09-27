package com.example.smartspend.Controller;

import com.example.smartspend.ApiResponse.ApiResponse;
import com.example.smartspend.Model.Category;
import com.example.smartspend.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<?> get(){
        List<Category> categories = categoryService.get();

        if (categories.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("The list is empty"));
        }

        return ResponseEntity.status(200).body(categories);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Category category, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (categoryService.add(category)){
            return ResponseEntity.status(200).body(new ApiResponse("Category added successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Category already exists"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if (categoryService.update(id,category)){
            return ResponseEntity.status(200).body(new ApiResponse("Category updated successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        if (categoryService.delete(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
    }
}