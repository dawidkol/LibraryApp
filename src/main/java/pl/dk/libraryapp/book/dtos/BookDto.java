package pl.dk.libraryapp.book.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.data.mongodb.core.index.Indexed;

@Builder
public record BookDto(
        String id,
        @NotBlank
        String title,
        @NotBlank
        String author,
        @NotBlank
        String publisher,
        @ISBN
        @Indexed(unique = true)
        String isbn
) {

}
