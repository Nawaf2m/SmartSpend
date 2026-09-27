package com.example.smartspend.Service;

import com.example.smartspend.Model.SavingGoal;
import com.example.smartspend.Model.User;
import com.example.smartspend.Repository.SavingGoalRepository;
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
public class SavingGoalService {

    private final SavingGoalRepository savingGoalRepository;
    private final UserRepository userRepository;

    public List<SavingGoal> get(){
        return savingGoalRepository.findAll();
    }

    public boolean add(SavingGoal savingGoal){
        User user = userRepository.findUserById(savingGoal.getUser_id());

        if (user == null){
            return false;
        }

        savingGoalRepository.save(savingGoal);
        return true;
    }

    public boolean update(Integer id, SavingGoal savingGoal){
        SavingGoal oldSavingGoal = savingGoalRepository.findSavingGoalById(id);
        User user = userRepository.findUserById(savingGoal.getUser_id());

        if (oldSavingGoal == null || user == null){
            return false;
        }

        oldSavingGoal.setName(savingGoal.getName());
        oldSavingGoal.setTargetAmount(savingGoal.getTargetAmount());
        oldSavingGoal.setSavedAmount(savingGoal.getSavedAmount());
        oldSavingGoal.setTargetDate(savingGoal.getTargetDate());
        oldSavingGoal.setUser_id(savingGoal.getUser_id());

        savingGoalRepository.save(oldSavingGoal);
        return true;
    }

    public boolean delete(Integer id){
        SavingGoal savingGoal = savingGoalRepository.findSavingGoalById(id);

        if (savingGoal == null){
            return false;
        }

        savingGoalRepository.delete(savingGoal);
        return true;
    }

    public boolean addMoney(Integer id, BigDecimal amount){
        SavingGoal savingGoal = savingGoalRepository.findSavingGoalById(id);

        if (savingGoal == null){
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            return false;
        }

        BigDecimal newSavedAmount = savingGoal.getSavedAmount().add(amount);

        savingGoal.setSavedAmount(newSavedAmount);

        savingGoalRepository.save(savingGoal);

        return true;
    }

    public String getProgress(Integer id){
        SavingGoal savingGoal = savingGoalRepository.findSavingGoalById(id);

        if (savingGoal == null){
            return null;
        }

        BigDecimal percentage = savingGoal.getSavedAmount().divide(savingGoal.getTargetAmount(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        BigDecimal remaining = savingGoal.getTargetAmount().subtract(savingGoal.getSavedAmount());

        if (remaining.compareTo(BigDecimal.ZERO) < 0){
            remaining = BigDecimal.ZERO;
        }

        return "Progress: " + percentage.setScale(2, RoundingMode.HALF_UP) + "% - Remaining: " + remaining;
    }

    public BigDecimal getRequiredSavingPerMonth(Integer id){
        SavingGoal savingGoal = savingGoalRepository.findSavingGoalById(id);

        if (savingGoal == null){
            return null;
        }

        BigDecimal remaining = savingGoal.getTargetAmount().subtract(savingGoal.getSavedAmount());

        if (remaining.compareTo(BigDecimal.ZERO) <= 0){
            return BigDecimal.ZERO;
        }

        LocalDate today = LocalDate.now();

        long months = ChronoUnit.MONTHS.between(today.withDayOfMonth(1), savingGoal.getTargetDate().withDayOfMonth(1));

        if (months <= 0){
            return remaining;
        }

        return remaining.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP
        );
    }
}