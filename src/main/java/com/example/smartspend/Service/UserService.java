package com.example.smartspend.Service;

import com.example.smartspend.Model.Category;
import com.example.smartspend.Model.ManualExpense;
import com.example.smartspend.Model.Receipt;
import com.example.smartspend.Model.User;
import com.example.smartspend.Repository.CategoryRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final ManualExpenseRepository manualExpenseRepository;
    private final ReceiptRepository receiptRepository;
    private final CategoryRepository categoryRepository;
    public List<User> get(){
        return userRepository.findAll();
    }

    public boolean add(User user){

        User oldUser = userRepository.findUserByEmail(user.getEmail());

        if (oldUser != null){
            return false;
        }

        user.setCreatedAt(LocalDate.now());
        userRepository.save(user);

        return true;
    }

    public boolean update(Integer id, User user){
        User oldUser = userRepository.findUserById(id);

        if (oldUser == null){
            return false;
        }

        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());

        userRepository.save(oldUser);
        return true;
    }

    public boolean delete(Integer id){
        User user = userRepository.findUserById(id);

        if (user == null){
            return false;
        }

        userRepository.delete(user);
        return true;
    }

    public BigDecimal getTotalSpending(Integer user_id){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesByUserId(user_id);

        List<Receipt> receipts = receiptRepository.findReceiptsByUserId(user_id);

        BigDecimal total = BigDecimal.ZERO;

        for (ManualExpense manualExpense : manualExpenses){
            total = total.add(manualExpense.getAmount());
        }

        for (Receipt receipt : receipts){
            total = total.add(receipt.getTotalAmount());
        }

        return total;
    }

    public BigDecimal getTotalSpendingBetweenDates(Integer user_id, LocalDate startDate, LocalDate endDate){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesByUserIdAndDate(user_id, startDate, endDate);

        List<Receipt> receipts = receiptRepository.findReceiptsByUserIdAndDate(user_id, startDate, endDate);

        BigDecimal total = BigDecimal.ZERO;

        for (ManualExpense manualExpense : manualExpenses){
            total = total.add(manualExpense.getAmount());
        }

        for (Receipt receipt : receipts){
            total = total.add(receipt.getTotalAmount());
        }

        return total;
    }

    public BigDecimal getSpendingByCategory(Integer user_id, Integer category_id){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        Category category = categoryRepository.findCategoryById(category_id);

        if (category == null){
            return null;
        }

        List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesByUserIdAndCategoryId(user_id, category_id);

        List<Receipt> receipts = receiptRepository.findReceiptsByUserIdAndCategoryId(user_id, category_id);

        BigDecimal total = BigDecimal.ZERO;

        for (ManualExpense manualExpense : manualExpenses){
            total = total.add(manualExpense.getAmount());
        }

        for (Receipt receipt : receipts){
            total = total.add(receipt.getTotalAmount());
        }

        return total;
    }

    public String getHighestSpendingCategory(Integer user_id){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        List<Category> categories = categoryRepository.findAll();

        BigDecimal highestTotal = BigDecimal.ZERO;
        String highestCategory = null;

        for (Category category : categories){

            List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesByUserIdAndCategoryId(user_id, category.getId());

            List<Receipt> receipts = receiptRepository.findReceiptsByUserIdAndCategoryId(user_id, category.getId());

            BigDecimal total = BigDecimal.ZERO;

            for (ManualExpense manualExpense : manualExpenses){
                total = total.add(manualExpense.getAmount());
            }

            for (Receipt receipt : receipts){
                total = total.add(receipt.getTotalAmount());
            }

            if (total.compareTo(highestTotal) > 0){
                highestTotal = total;
                highestCategory = category.getName();
            }
        }

        if (highestCategory == null){
            return "No spending found";
        }

        return highestCategory + " - Total: " + highestTotal;
    }

    public String getHighestExpense(Integer user_id){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesOrderByAmount(user_id);

        List<Receipt> receipts = receiptRepository.findReceiptsOrderByAmount(user_id);

        ManualExpense highestManualExpense = null;
        Receipt highestReceipt = null;

        if (!manualExpenses.isEmpty()){
            highestManualExpense = manualExpenses.get(0);
        }

        if (!receipts.isEmpty()){
            highestReceipt = receipts.get(0);
        }

        if (highestManualExpense == null && highestReceipt == null){
            return "No spending found";
        }

        if (highestManualExpense == null){
            return "Receipt - " + highestReceipt.getStoreName() + " - Amount: " + highestReceipt.getTotalAmount();
        }

        if (highestReceipt == null){
            return "Manual Expense - " + highestManualExpense.getDescription() + " - Amount: " + highestManualExpense.getAmount();
        }

        if (highestManualExpense.getAmount().compareTo(highestReceipt.getTotalAmount()) > 0){

            return "Manual Expense - " + highestManualExpense.getDescription() + " - Amount: " + highestManualExpense.getAmount();
        }

        return "Receipt - " + highestReceipt.getStoreName() + " - Amount: " + highestReceipt.getTotalAmount();
    }

    public BigDecimal getAverageDailySpending(Integer user_id, LocalDate startDate, LocalDate endDate){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesByUserIdAndDate(user_id, startDate, endDate);

        List<Receipt> receipts = receiptRepository.findReceiptsByUserIdAndDate(user_id, startDate, endDate);

        BigDecimal total = BigDecimal.ZERO;

        for (ManualExpense manualExpense : manualExpenses){
            total = total.add(manualExpense.getAmount());
        }

        for (Receipt receipt : receipts){
            total = total.add(receipt.getTotalAmount());
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        return total.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getMonthlySpending(Integer user_id, Integer year, Integer month){

        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        List<ManualExpense> manualExpenses = manualExpenseRepository.findManualExpensesByMonth(user_id, year, month);

        List<Receipt> receipts = receiptRepository.findReceiptsByMonth(user_id, year, month);

        BigDecimal total = BigDecimal.ZERO;

        for (ManualExpense manualExpense : manualExpenses){
            total = total.add(manualExpense.getAmount());
        }

        for (Receipt receipt : receipts){
            total = total.add(receipt.getTotalAmount());
        }

        return total;
    }

    public String compareMonthlySpending(Integer user_id, Integer firstYear, Integer firstMonth, Integer secondYear, Integer secondMonth){
        User user = userRepository.findUserById(user_id);

        if (user == null){
            return null;
        }

        BigDecimal firstTotal = getMonthlySpending(user_id, firstYear, firstMonth);

        BigDecimal secondTotal = getMonthlySpending(user_id, secondYear, secondMonth);

        BigDecimal difference = secondTotal.subtract(firstTotal);

        if (difference.compareTo(BigDecimal.ZERO) > 0){
            return "Spending increased by: " + difference;
        }

        if (difference.compareTo(BigDecimal.ZERO) < 0){
            return "Spending decreased by: " + difference.abs();
        }

        return "Spending is the same";
    }
}