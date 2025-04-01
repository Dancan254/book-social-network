package com.mongs.book_social_network.feedback;

import com.mongs.book_social_network.book.Book;
import com.mongs.book_social_network.book.PageResponse;
import com.mongs.book_social_network.exceptions.OperationNotPermitted;
import com.mongs.book_social_network.repository.BookRepository;
import com.mongs.book_social_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedBackService {
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;
    public Integer saveFeedBack(@Valid FeedBackRequest request, Authentication authentication) {
        Book book = bookRepository.findById(request.bookId()).
                orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.isArchived() || !book.isShareable()){
            throw new OperationNotPermitted("You cannot leave feedback for this book, it is archived or not shareable");
        }
        User user = (User) authentication.getPrincipal();
        if (Objects.equals(user.getId(), book.getOwner().getId())){
            throw new OperationNotPermitted("You cannot leave feedback for your own book");
        }
        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> getAllFeedbackByBookId(Integer bookId, int page, int size, Authentication authentication) {
        PageRequest pageRequest = PageRequest.of(page, size);
        User user = (User) authentication.getPrincipal();
        Page<Feedback> feedbackPage = feedbackRepository.findAllByBookId(bookId, pageRequest);

        List<FeedbackResponse> feedbackResponses = feedbackPage.getContent().stream()
                .map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
                .toList();

        return new PageResponse<>(
                feedbackResponses,
                feedbackPage.getNumber(),
                feedbackPage.getSize(),
                (int) feedbackPage.getTotalElements(),
                feedbackPage.getTotalPages(),
                feedbackPage.isFirst(),
                feedbackPage.isLast()
        );
    }
}
