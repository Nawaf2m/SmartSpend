package com.example.smartspend.Service;

import com.example.smartspend.Model.Budget;
import com.example.smartspend.Model.ManualExpense;
import com.example.smartspend.Model.Receipt;
import com.example.smartspend.Model.User;
import com.example.smartspend.Repository.BudgetRepository;
import com.example.smartspend.Repository.ManualExpenseRepository;
import com.example.smartspend.Repository.ReceiptRepository;
import com.example.smartspend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final ManualExpenseRepository manualExpenseRepository;
    private final ReceiptRepository receiptRepository;
    private final EmailService emailService;

    public List<Budget> get(){
        return budgetRepository.findAll();
    }

    public boolean add(Budget budget){
        User user = userRepository.findUserById(budget.getUser_id());

        if (user == null){
            return false;
        }

        budgetRepository.save(budget);
        return true;
    }

    public boolean update(Integer id, Budget budget){
        Budget oldBudget = budgetRepository.findBudgetById(id);
        User user = userRepository.findUserById(budget.getUser_id());

        if (oldBudget == null || user == null){
            return false;
        }

        oldBudget.setAmount(budget.getAmount());
        oldBudget.setPeriod(budget.getPeriod());
        oldBudget.setStartDate(budget.getStartDate());
        oldBudget.setUser_id(budget.getUser_id());

        budgetRepository.save(oldBudget);
        return true;
    }

    public boolean delete(Integer id){
        Budget budget = budgetRepository.findBudgetById(id);

        if (budget == null){
            return false;
        }

        budgetRepository.delete(budget);
        return true;
    }

    public BigDecimal getRemainingBudget(Integer user_id){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        List<Budget> budgets = budgetRepository.findBudgetsByUserId(user_id);

        if (budgets.isEmpty()){
            return null;
        }

        Budget budget = budgets.get(0);

        LocalDate today = LocalDate.now();

        LocalDate cycleStart = budget.getStartDate();
        LocalDate cycleEnd;

        if (budget.getPeriod().equals("MONTHLY")){

            while (!cycleStart.plusMonths(1).isAfter(today)){
                cycleStart = cycleStart.plusMonths(1);
            }

            cycleEnd = cycleStart.plusMonths(1).minusDays(1);

        } else {

            while (!cycleStart.plusYears(1).isAfter(today)){
                cycleStart = cycleStart.plusYears(1);
            }

            cycleEnd = cycleStart.plusYears(1).minusDays(1);
        }


        List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesByUserIdAndDate(user_id, cycleStart, cycleEnd);

        List<Receipt> receipts = receiptRepository.findReceiptsByUserIdAndDate(user_id, cycleStart, cycleEnd);


        BigDecimal totalSpending = BigDecimal.ZERO;

        for (ManualExpense manualExpense : manualExpenses){
            totalSpending = totalSpending.add(manualExpense.getAmount());
        }

        for (Receipt receipt : receipts){
            totalSpending = totalSpending.add(receipt.getTotalAmount());
        }


        return budget.getAmount().subtract(totalSpending);
    }

    public BigDecimal getBudgetUsagePercentage(Integer user_id){

        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        List<Budget> budgets = budgetRepository.findBudgetsByUserId(user_id);

        if (budgets.isEmpty()){
            return null;
        }

        Budget budget = budgets.get(0);

        LocalDate today = LocalDate.now();

        LocalDate cycleStart = budget.getStartDate();
        LocalDate cycleEnd;

        if (budget.getPeriod().equals("MONTHLY")){

            while (!cycleStart.plusMonths(1).isAfter(today)){
                cycleStart = cycleStart.plusMonths(1);
            }

            cycleEnd = cycleStart.plusMonths(1).minusDays(1);

        } else {

            while (!cycleStart.plusYears(1).isAfter(today)){
                cycleStart = cycleStart.plusYears(1);
            }

            cycleEnd = cycleStart.plusYears(1).minusDays(1);
        }

        List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesByUserIdAndDate(user_id, cycleStart, cycleEnd);

        List<Receipt> receipts = receiptRepository.findReceiptsByUserIdAndDate(user_id, cycleStart, cycleEnd);

        BigDecimal totalSpending = BigDecimal.ZERO;

        for (ManualExpense manualExpense : manualExpenses){
            totalSpending = totalSpending.add(manualExpense.getAmount());
        }

        for (Receipt receipt : receipts){
            totalSpending = totalSpending.add(receipt.getTotalAmount());
        }

        BigDecimal percentage = totalSpending.divide(budget.getAmount(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        return percentage.setScale(2, RoundingMode.HALF_UP);
    }

    public String checkBudgetExceeded(Integer user_id){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        List<Budget> budgets = budgetRepository.findBudgetsByUserId(user_id);

        if (budgets.isEmpty()){
            return null;
        }

        Budget budget = budgets.get(0);

        LocalDate today = LocalDate.now();

        LocalDate cycleStart = budget.getStartDate();
        LocalDate cycleEnd;

        if (budget.getPeriod().equals("MONTHLY")){

            while (!cycleStart.plusMonths(1).isAfter(today)){
                cycleStart = cycleStart.plusMonths(1);
            }

            cycleEnd = cycleStart.plusMonths(1).minusDays(1);

        } else {

            while (!cycleStart.plusYears(1).isAfter(today)){
                cycleStart = cycleStart.plusYears(1);
            }

            cycleEnd = cycleStart.plusYears(1).minusDays(1);
        }

        List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesByUserIdAndDate(user_id, cycleStart, cycleEnd);

        List<Receipt> receipts = receiptRepository.findReceiptsByUserIdAndDate(user_id, cycleStart, cycleEnd);

        BigDecimal totalSpending = BigDecimal.ZERO;

        for (ManualExpense manualExpense : manualExpenses){
            totalSpending = totalSpending.add(manualExpense.getAmount());
        }

        for (Receipt receipt : receipts){
            totalSpending = totalSpending.add(receipt.getTotalAmount());
        }

        if (totalSpending.compareTo(budget.getAmount()) > 0){

            BigDecimal exceededAmount = totalSpending.subtract(budget.getAmount());

            return "Budget exceeded by: " + exceededAmount;
        }

        BigDecimal remaining = budget.getAmount().subtract(totalSpending);

        return "Budget not exceeded. Remaining: " + remaining;
    }

    public BigDecimal forecastBudgetSpending(Integer user_id){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        List<Budget> budgets = budgetRepository.findBudgetsByUserId(user_id);

        if (budgets.isEmpty()){
            return null;
        }

        Budget budget = budgets.get(0);

        LocalDate today = LocalDate.now();

        LocalDate cycleStart = budget.getStartDate();
        LocalDate cycleEnd;

        if (budget.getPeriod().equals("MONTHLY")){

            while (!cycleStart.plusMonths(1).isAfter(today)){
                cycleStart = cycleStart.plusMonths(1);
            }

            cycleEnd = cycleStart.plusMonths(1).minusDays(1);

        } else {

            while (!cycleStart.plusYears(1).isAfter(today)){
                cycleStart = cycleStart.plusYears(1);
            }

            cycleEnd = cycleStart.plusYears(1).minusDays(1);
        }

        List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesByUserIdAndDate(user_id, cycleStart, today);

        List<Receipt> receipts = receiptRepository.findReceiptsByUserIdAndDate(user_id, cycleStart, today);

        BigDecimal totalSpending = BigDecimal.ZERO;

        for (ManualExpense manualExpense : manualExpenses){
            totalSpending = totalSpending.add(manualExpense.getAmount());
        }

        for (Receipt receipt : receipts){
            totalSpending = totalSpending.add(receipt.getTotalAmount());
        }

        long daysPassed = ChronoUnit.DAYS.between(cycleStart, today) + 1;

        long totalDays = ChronoUnit.DAYS.between(cycleStart, cycleEnd) + 1;

        BigDecimal averageDaily = totalSpending.divide(BigDecimal.valueOf(daysPassed), 2, RoundingMode.HALF_UP);

        BigDecimal forecast = averageDaily.multiply(BigDecimal.valueOf(totalDays));

        return forecast.setScale(2, RoundingMode.HALF_UP);
    }

    public String sendBudgetStatus(Integer user_id){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        BigDecimal remaining = getRemainingBudget(user_id);

        if (remaining == null){
            return null;
        }

        String message;

        if (remaining.compareTo(BigDecimal.ZERO) < 0){

            BigDecimal exceededAmount = remaining.abs();

            message = "You have exceeded your budget by " + exceededAmount + " SAR.";

        } else {
            message = "Your budget is in good status. Remaining amount: " + remaining + " SAR.";
        }

        emailService.sendBudgetStatus(user.getEmail(), user.getName(), message);

        return "Budget status email sent successfully";
    }
}