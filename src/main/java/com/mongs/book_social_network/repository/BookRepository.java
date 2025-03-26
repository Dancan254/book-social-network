package com.mongs.book_social_network.repository;

import com.mongs.book_social_network.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
