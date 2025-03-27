package com.mongs.book_social_network.repository;

import com.mongs.book_social_network.history.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {
    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id = :id
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Integer id, Pageable pageable);

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :id
           """)
    Page<BookTransactionHistory> findAllReturnedBooks(Integer id, Pageable pageable);

    @Query("""
           SELECT
           (COUNT(*) > 0) AS isBorrowed
           FROM BookTransactionHistory bookTransactionHistory
           WHERE bookTransactionHistory.user.id = :userId
           AND bookTransactionHistory.book.id = :bookId
           AND bookTransactionHistory.returnApproved = false
           """)
    boolean isAlreadyBorrowed(Integer bookId, Integer userId);

    @Query("""
            SELECT transaction
            FROM BookTransactionHistory transaction
            WHERE transaction.book.id = :bookId
            AND transaction.user.id = :userId
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId);
}
