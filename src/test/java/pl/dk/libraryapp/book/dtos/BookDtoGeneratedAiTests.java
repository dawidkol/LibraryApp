package pl.dk.libraryapp.book.dtos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BookDtoGeneratedAiTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testConstructor() {
        // GIVEN
        String id = "123";
        String title = "The Lord of the Rings";
        String author = "J.R.R. Tolkien";
        String publisher = "HarperCollins";
        String isbn = "978-0-618-64450-0";
        Boolean available = true;

        // WHEN
        BookDto bookDto = new BookDto(id, title, author, publisher, isbn, available);

        // THEN
        assertEquals(id, bookDto.id());
        assertEquals(title, bookDto.title());
        assertEquals(author, bookDto.author());
        assertEquals(publisher, bookDto.publisher());
        assertEquals(isbn, bookDto.isbn());
        assertEquals(available, bookDto.available());
    }

}