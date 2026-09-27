package com.example.smartspend.Repository;

import com.example.smartspend.Model.ManualExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ManualExpenseRepository extends JpaRepository<ManualExpense, Integer> {
    ManualExpense findManualExpenseById(Integer id);
    @Query("select m from ManualExpense m where m.user_id = ?1")
    List<ManualExpense> findManualExpensesByUserId(Integer user_id);
    @Query("select m from ManualExpense m where m.user_id = ?1 and m.expenseDate between ?2 and ?3")
    List<ManualExpense> findManualExpensesByUserIdAndDate(Integer user_id, LocalDate startDate, LocalDate endDate);
    @Query("select m from ManualExpense m where m.user_id = ?1 and m.category_id = ?2")
    List<ManualExpense> findManualExpensesByUserIdAndCategoryId(Integer user_id, Integer category_id);
    @Query("select m from ManualExpense m where m.user_id = ?1 order by m.amount desc")
    List<ManualExpense> findManualExpensesOrderByAmount(Integer user_id);
    @Query("select m from ManualExpense m where m.user_id = ?1 and year(m.expenseDate) = ?2 and month(m.expenseDate) = ?3")
    List<ManualExpense> findManualExpensesByMonth(Integer user_id, Integer year, Integer month);
}