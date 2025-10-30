package pl.dk.libraryapp.book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pl.dk.libraryapp.book.dtos.BookDto; 

class BookDtoMapperGeneratedAiTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void map_shouldReturnBookDtoWithCorrectValues() {
        // GIVEN
        Book book = Book.builder()
                .id("123")
                .title("The Lord of the Rings")
                .author("J.R.R. Tolkien")
                .publisher("HarperCollins")
                .isbn("978-0-618-05323-4")
                .available(true)
                .build();

        // WHEN
        BookDto bookDto = BookDtoMapper.map(book);

        // THEN
        assertEquals("123", bookDto.id());
        assertEquals("The Lord of the Rings", bookDto.title());
        assertEquals("J.R.R. Tolkien", bookDto.author());
        assertEquals("HarperCollins", bookDto.publisher());
        assertEquals("978-0-618-05323-4", bookDto.isbn());
        assertTrue(bookDto.available());
    }

    @Test
    void map_shouldReturnBookWithCorrectValues() {
        // GIVEN
        BookDto bookDto = BookDto.builder()
                .id("123")
                .title("The Lord of the Rings")
                .author("J.R.R. Tolkien")
                .publisher("HarperCollins")
                .isbn("978-0-618-05323-4")
                .available(true)
                .build();

        // WHEN
        Book book = BookDtoMapper.map(bookDto);

        // THEN
        assertEquals("123", book.id());
        assertEquals("The Lord of the Rings", book.title());
        assertEquals("J.R.R. Tolkien", book.author());
        assertEquals("HarperCollins", book.publisher());
        assertEquals("978-0-618-05323-4", book.isbn());
        assertTrue(book.available());
    }

}