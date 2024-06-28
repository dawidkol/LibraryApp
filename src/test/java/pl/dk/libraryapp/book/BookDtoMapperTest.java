package pl.dk.libraryapp.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.dk.libraryapp.book.dtos.BookDto;

import static org.junit.jupiter.api.Assertions.*;

class BookDtoMapperTest {


    @Test
    @DisplayName("It should map Book to Book Dto")
    void itShouldMapBookToBookDto() {
        // Given
        Book book = Book.builder()
                .id("667dc41cb4579216604fdd63")
                .title("Effective Java")
                .author("Joshua Bloch")
                .publisher("Addison-Wesley")
                .isbn("978-1-56619-909-4")
                .build();

        // When
        BookDto bookDto = BookDtoMapper.map(book);

        // Then
        assertAll(
                () -> assertEquals(bookDto.id(), book.id()),
                () -> assertEquals(bookDto.title(), book.title()),
                () -> assertEquals(bookDto.author(), book.author()),
                () -> assertEquals(bookDto.publisher(), book.publisher()),
                () -> assertEquals(bookDto.isbn(), book.isbn())
        );
    }
}