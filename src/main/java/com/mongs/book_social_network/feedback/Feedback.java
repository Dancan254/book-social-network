package com.mongs.book_social_network.feedback;

import com.mongs.book_social_network.book.Book;
import com.mongs.book_social_network.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Feedback extends BaseEntity {
    private Double rate;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}
