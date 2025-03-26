package com.mongs.book_social_network.services;

import com.mongs.book_social_network.book.*;
import com.mongs.book_social_network.config.BookMapper;
import com.mongs.book_social_network.exceptions.OperationNotPermitted;
import com.mongs.book_social_network.history.BookTransactionHistory;
import com.mongs.book_social_network.repository.BookRepository;
import com.mongs.book_social_network.repository.BookTransactionHistoryRepository;
import com.mongs.book_social_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    public Integer saveBook(BookRequest request, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream().map(bookMapper::toBookResponse).toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                (int)books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponses = books.stream().map(bookMapper::toBookResponse).toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                (int)books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookTransactionHistory> bookTransactionHistories = bookTransactionHistoryRepository.findAllBorrowedBooks(user.getId(), pageable);
        List<BorrowedBookResponse> borrowedBookResponses = bookTransactionHistories.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                borrowedBookResponses,
                bookTransactionHistories.getNumber(),
                bookTransactionHistories.getSize(),
                (int)bookTransactionHistories.getTotalElements(),
                bookTransactionHistories.getTotalPages(),
                bookTransactionHistories.isFirst(),
                bookTransactionHistories.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookTransactionHistory> bookTransactionHistories = bookTransactionHistoryRepository.findAllReturnedBooks(user.getId(), pageable);
        List<BorrowedBookResponse> borrowedBookResponses = bookTransactionHistories.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                borrowedBookResponses,
                bookTransactionHistories.getNumber(),
                bookTransactionHistories.getSize(),
                (int)bookTransactionHistories.getTotalElements(),
                bookTransactionHistories.getTotalPages(),
                bookTransactionHistories.isFirst(),
                bookTransactionHistories.isLast()
        );
    }


    public Integer updateShareableStatus(Integer bookId, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        User user = (User) authenticatedUser.getPrincipal();
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermitted("You are not the owner of this book");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return book.getId();
    }
}
