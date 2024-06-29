package pl.dk.libraryapp.bookloan.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SaveBookLoanDto(
        String id,
        @NotBlank
        String bookId,
        @NotBlank
        String customerId
) {
}
