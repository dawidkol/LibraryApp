package pl.dk.libraryapp.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "books")
@Builder
record Customer(
        @MongoId
        String id,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Indexed(unique = true)
        @Email
        String email
) {
}
