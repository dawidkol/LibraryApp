package pl.dk.libraryapp.customer.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.data.mongodb.core.index.Indexed;

@Builder
public record CustomerDto(
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
