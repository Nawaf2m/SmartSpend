package com.example.smartspend.Repository;

import com.example.smartspend.Model.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingGoalRepository extends JpaRepository<SavingGoal, Integer> {
    SavingGoal findSavingGoalById(Integer id);
}