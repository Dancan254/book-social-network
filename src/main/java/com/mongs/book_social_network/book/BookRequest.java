package com.mongs.book_social_network.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest(Integer id,
                          @NotNull(message = "100")
                          @NotEmpty(message = "101")
                          String title,
                          @NotNull(message = "100")
                          @NotEmpty(message = "101")
                          String author,
                          @NotNull(message = "100")
                          @NotEmpty(message = "101")
                          String synopsis,
                          boolean shareable) {
}
