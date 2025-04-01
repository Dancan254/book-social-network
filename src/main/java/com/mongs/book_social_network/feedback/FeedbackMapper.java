package com.mongs.book_social_network.feedback;

import com.mongs.book_social_network.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {

    public Feedback toFeedback(FeedBackRequest feedBackRequest) {
        return Feedback.builder()
                .comment(feedBackRequest.comment())
                .book(Book.builder().
                        id(feedBackRequest.bookId())
                        .archived(false)
                        .shareable(false)
                        .build())
                .rate(feedBackRequest.rate())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        return FeedbackResponse.builder()
                .comment(feedback.getComment())
                .rate(feedback.getRate())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), id))
                .build();
    }
}
