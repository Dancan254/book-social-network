package com.mongs.book_social_network.book;

import com.mongs.book_social_network.file.FileUtils;
import com.mongs.book_social_network.history.BookTransactionHistory;
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
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponse.builder()
                .bookId(history.getBook().getId())
                .title(history.getBook().getTitle())
                .author(history.getBook().getAuthor())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
