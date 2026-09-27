package com.example.smartspend.Service;

import com.example.smartspend.Model.Category;
import com.example.smartspend.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> get(){
        return categoryRepository.findAll();
    }

    public boolean add(Category category){

        Category oldCategory = categoryRepository.findCategoryByNameIgnoreCase(category.getName());

        if (oldCategory != null){
            return false;
        }

        categoryRepository.save(category);
        return true;
    }

    public boolean update(Integer id, Category category){
        Category oldCategory = categoryRepository.findCategoryById(id);

        if (oldCategory == null){
            return false;
        }

        oldCategory.setName(category.getName());

        categoryRepository.save(oldCategory);
        return true;
    }

    public boolean delete(Integer id){
        Category category = categoryRepository.findCategoryById(id);

        if (category == null){
            return false;
        }

        categoryRepository.delete(category);
        return true;
    }
}