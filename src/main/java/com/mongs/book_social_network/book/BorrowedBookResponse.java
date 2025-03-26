package com.mongs.book_social_network.book;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BorrowedBookResponse {

    private Integer bookId;
    private String title;
    private String author;
    private String isbn;
    private double rate;
    private boolean returned;
    private boolean returnApproved;
}
