package com.example.smartspend.Repository;

import com.example.smartspend.Model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    Budget findBudgetById(Integer id);
    @Query("select b from Budget b where b.user_id = ?1")
    List<Budget> findBudgetsByUserId(Integer user_id);
}