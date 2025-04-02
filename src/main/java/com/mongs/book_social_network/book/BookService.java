package com.mongs.book_social_network.book;

import com.mongs.book_social_network.exceptions.OperationNotPermitted;
import com.mongs.book_social_network.file.FileStorageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

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

    public Integer updateArchiveStatus(Integer bookId, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        User user = (User) authenticatedUser.getPrincipal();
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermitted("You are not the owner of this book");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return book.getId();
    }

    public Integer borrowBook(Integer bookId, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (!book.isShareable() || book.isArchived()) {
            throw new OperationNotPermitted("This book is not shareable or is archived");
        }
        User user = (User) authenticatedUser.getPrincipal();
        if (book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermitted("You cannot borrow your own book");
        }
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowed(book.getId(), user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermitted("This book is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .book(book)
                .user(user)
                .returned(false)
                .returnApproved(false)
                .build();

        bookTransactionHistoryRepository.save(bookTransactionHistory);
        return book.getId();
    }

    public Integer returnBook(Integer bookId, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (!book.isShareable() || book.isArchived()) {
            throw new OperationNotPermitted("This book is not shareable or is archived");
        }
        User user = (User) authenticatedUser.getPrincipal();
        if (book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermitted("You cannot borrow your own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found in your borrowed list"));
        bookTransactionHistory.setReturned(true);
        bookTransactionHistoryRepository.save(bookTransactionHistory);
        return book.getId();
    }

    public Integer approveReturnedBook(Integer bookId, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (!book.isShareable() || book.isArchived()) {
            throw new OperationNotPermitted("This book is not shareable or is archived");
        }
        User user = (User) authenticatedUser.getPrincipal();
        if (book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermitted("You cannot borrow your own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book is not returned yet"));
        bookTransactionHistory.setReturnApproved(true);
        bookTransactionHistoryRepository.save(bookTransactionHistory);
        return book.getId();
    }

    public void uploadCoverPicture(Integer bookId, MultipartFile file, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (!book.isShareable() || book.isArchived()) {
            throw new OperationNotPermitted("This book is not shareable or is archived");
        }
        User user = (User) authenticatedUser.getPrincipal();
        var bookCover = fileStorageService.saveFile(file, book, user.getId());
    }
}
