package pl.dk.libraryapp.bookloan.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BookLoanDto(String id,
                          @NotBlank
                          LocalDateTime borrowedAt,
                          @NotBlank
                          LocalDateTime returnedAt,
                          @NotBlank
                          String bookId,
                          @NotBlank
                          String customerId) {
}
