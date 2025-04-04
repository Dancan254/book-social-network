package com.mongs.book_social_network.book;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Tag(name = "Book", description = "Book API")
public class BookController {

    private final BookService service;

    @PostMapping
    public ResponseEntity<String> saveBook(@Valid @RequestBody BookRequest request, Authentication authenticatedUser) {
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

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(@RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size,
                                                                                   Authentication authenticatedUser) {
        return ResponseEntity.ok(service.findAllReturnedBooks(page, size, authenticatedUser));
    }

    @PatchMapping("/shareable/{bookId}")
    public ResponseEntity<Integer> updateShareableStatus(@PathVariable Integer bookId, Authentication authenticatedUser) {
        return ResponseEntity.ok(service.updateShareableStatus(bookId, authenticatedUser));
    }

    @PatchMapping("/archive/{bookId}")
    public ResponseEntity<Integer> updateArchiveStatus(@PathVariable Integer bookId, Authentication authenticatedUser) {
        return ResponseEntity.ok(service.updateArchiveStatus(bookId, authenticatedUser));
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<Integer> borrowBook(@PathVariable Integer bookId, Authentication authenticatedUser) {
        return ResponseEntity.ok(service.borrowBook(bookId, authenticatedUser));
    }

    @PatchMapping("/return/{bookId}")
    public ResponseEntity<Integer> returnBook(@PathVariable Integer bookId, Authentication authenticatedUser) {
        return ResponseEntity.ok(service.returnBook(bookId, authenticatedUser));
    }

    @PatchMapping("/return/approve/{bookId}")
    public ResponseEntity<Integer> approveReturnedBook(@PathVariable Integer bookId, Authentication authenticatedUser) {
        return ResponseEntity.ok(service.approveReturnedBook(bookId, authenticatedUser));
    }

    @PostMapping(value = "/cover/upload/{bookId}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadCoverPicture(@PathVariable Integer bookId, Authentication authenticatedUser,
                                         @Parameter @RequestPart("file") MultipartFile file) {
        service.uploadCoverPicture(bookId, file, authenticatedUser);
        return ResponseEntity.ok("File uploaded successfully");
    }
}
