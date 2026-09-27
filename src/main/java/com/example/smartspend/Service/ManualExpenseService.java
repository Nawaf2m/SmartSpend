package com.example.smartspend.Service;

import com.example.smartspend.Model.Category;
import com.example.smartspend.Model.ManualExpense;
import com.example.smartspend.Model.User;
import com.example.smartspend.Repository.CategoryRepository;
import com.example.smartspend.Repository.ManualExpenseRepository;
import com.example.smartspend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManualExpenseService {

    private final ManualExpenseRepository manualExpenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<ManualExpense> get(){
        return manualExpenseRepository.findAll();
    }

    public boolean add(ManualExpense manualExpense){
        User user = userRepository.findUserById(manualExpense.getUser_id());
        Category category = categoryRepository.findCategoryById(manualExpense.getCategory_id());

        if (user == null || category == null){
            return false;
        }

        manualExpenseRepository.save(manualExpense);
        return true;
    }

    public boolean update(Integer id, ManualExpense manualExpense){
        ManualExpense oldManualExpense =
                manualExpenseRepository.findManualExpenseById(id);

        User user = userRepository.findUserById(manualExpense.getUser_id());
        Category category = categoryRepository.findCategoryById(manualExpense.getCategory_id());

        if (oldManualExpense == null || user == null || category == null){
            return false;
        }

        oldManualExpense.setDescription(manualExpense.getDescription());
        oldManualExpense.setAmount(manualExpense.getAmount());
        oldManualExpense.setExpenseDate(manualExpense.getExpenseDate());
        oldManualExpense.setUser_id(manualExpense.getUser_id());
        oldManualExpense.setCategory_id(manualExpense.getCategory_id());

        manualExpenseRepository.save(oldManualExpense);
        return true;
    }

    public boolean delete(Integer id){
        ManualExpense manualExpense =
                manualExpenseRepository.findManualExpenseById(id);

        if (manualExpense == null){
            return false;
        }

        manualExpenseRepository.delete(manualExpense);
        return true;
    }
}