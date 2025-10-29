package pl.dk.libraryapp.bookloan;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.dk.libraryapp.book.Book;
import pl.dk.libraryapp.customer.Customer;

import java.time.LocalDateTime;

@Document(value = "bookLoans")
@Builder
record BookLoan(
        @MongoId
        String id,
        @NotBlank
        LocalDateTime borrowedAt,
        @NotBlank
        LocalDateTime returnedAt,
        @DBRef
        @NotBlank
        Book book,
        @DBRef
        @NotBlank
        Customer customer
) {

}
