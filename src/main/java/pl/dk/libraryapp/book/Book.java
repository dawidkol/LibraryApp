package pl.dk.libraryapp.book;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "books")
@Builder
record Book(
        @MongoId
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
