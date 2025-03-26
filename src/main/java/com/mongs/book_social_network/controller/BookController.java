package com.mongs.book_social_network.controller;

import com.mongs.book_social_network.book.BookRequest;
import com.mongs.book_social_network.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@Valid @RequestBody BookRequest request, Authentication authenticatedUser) {
        return ResponseEntity.ok(service.saveBook(request, authenticatedUser));
    }
}
