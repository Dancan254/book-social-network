package com.mongs.book_social_network.services;

import com.mongs.book_social_network.book.Book;
import com.mongs.book_social_network.book.BookRequest;
import com.mongs.book_social_network.config.BookMapper;
import com.mongs.book_social_network.repository.BookRepository;
import com.mongs.book_social_network.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public Integer saveBook(BookRequest request, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }
}
