package com.mongs.book_social_network.feedback;

import com.mongs.book_social_network.book.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@Tag(name = "Feedback", description = "Feedback API")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    @PostMapping()
    public ResponseEntity<Integer> saveFeedBack(@Valid @RequestBody FeedBackRequest request, Authentication authentication) {
        return ResponseEntity.ok(feedBackService.saveFeedBack(request, authentication));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<FeedbackResponse>> getFeedBackByBookId(
            @PathVariable Integer bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication authentication
    ) {
        return ResponseEntity.ok(feedBackService.getAllFeedbackByBookId(bookId, page, size, authentication));
    }
}
