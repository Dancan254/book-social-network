package com.mongs.book_social_network.book;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BookResponse {

    private Integer bookId;;
    private String title;
    private String author;
    private String synopsis;
    private String owner;
    private String isbn;
    private byte[] cover;
    private double rate;
    private boolean shareable;
    private boolean archived;
}
