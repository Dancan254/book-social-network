package com.mongs.book_social_network.config;

import com.mongs.book_social_network.book.Book;
import com.mongs.book_social_network.book.BookRequest;
import com.mongs.book_social_network.book.BookResponse;
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

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .synopsis(book.getSynopsis())
                .shareable(book.isShareable())
                .archived(book.isArchived())
                .owner(book.getOwner().getUsername())
                .rate(book.getRate())
                .isbn(book.getIsbn())
                .build();
    }
}
