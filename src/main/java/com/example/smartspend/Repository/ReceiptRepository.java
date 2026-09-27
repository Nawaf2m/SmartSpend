package com.example.smartspend.Repository;

import com.example.smartspend.Model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    Receipt findReceiptById(Integer id);
    @Query("select r from Receipt r where r.user_id = ?1")
    List<Receipt> findReceiptsByUserId(Integer user_id);
    @Query("select r from Receipt r where r.user_id = ?1 and r.purchaseDate between ?2 and ?3")
    List<Receipt> findReceiptsByUserIdAndDate(Integer user_id, LocalDate startDate, LocalDate endDate);
    @Query("select r from Receipt r where r.user_id = ?1 and r.category_id = ?2")
    List<Receipt> findReceiptsByUserIdAndCategoryId(Integer user_id, Integer category_id);
    @Query("select r from Receipt r where r.user_id = ?1 order by r.totalAmount desc")
    List<Receipt> findReceiptsOrderByAmount(Integer user_id);
    @Query("select r from Receipt r where r.user_id = ?1 and year(r.purchaseDate) = ?2 and month(r.purchaseDate) = ?3")
    List<Receipt> findReceiptsByMonth(Integer user_id, Integer year, Integer month);
}