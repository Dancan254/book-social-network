package com.mongs.book_social_network.config;

import com.mongs.book_social_network.book.Book;
import com.mongs.book_social_network.book.BookRequest;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .author(request.author())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }
}
