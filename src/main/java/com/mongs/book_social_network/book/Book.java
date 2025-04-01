package com.mongs.book_social_network.book;

import com.mongs.book_social_network.common.BaseEntity;
import com.mongs.book_social_network.feedback.Feedback;
import com.mongs.book_social_network.history.BookTransactionHistory;
import com.mongs.book_social_network.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Book extends BaseEntity {
    private String title;
    private String author;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;


    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;
    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> transactionHistories;

    @Transient
    public double getRate(){
        if (feedbacks != null && !feedbacks.isEmpty()){
            var rate =  this.feedbacks.stream()
                    .mapToDouble(Feedback::getRate)
                    .average()
                    .orElse(0);
            return Math.round(rate * 100) / 100.0;
        }
        return 0.0;
    }
}
