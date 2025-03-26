package com.mongs.book_social_network.controller;

import com.mongs.book_social_network.book.BookRequest;
import com.mongs.book_social_network.book.BookResponse;
import com.mongs.book_social_network.book.BorrowedBookResponse;
import com.mongs.book_social_network.book.PageResponse;
import com.mongs.book_social_network.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@Valid @RequestBody BookRequest request, Authentication authenticatedUser) {
        return ResponseEntity.ok(service.saveBook(request, authenticatedUser));
    }

    @GetMapping("{bookId}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable Integer bookId) {
        return ResponseEntity.ok(service.findById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   Authentication authenticatedUser) {
        return ResponseEntity.ok(service.findAllBooks(page, size, authenticatedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   Authentication authenticatedUser) {
        return ResponseEntity.ok(service.findAllBooksByOwner(page, size, authenticatedUser));
    }
    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(@RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size,
                                                                                   Authentication authenticatedUser) {
        return ResponseEntity.ok(service.findAllBorrowedBooks(page, size, authenticatedUser));
    }

}
