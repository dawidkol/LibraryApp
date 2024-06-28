package pl.dk.libraryapp.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.libraryapp.book.dtos.BookDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    private BookService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new BookService(bookRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }


    @Test
    @DisplayName("It should save book in database")
    void itShouldSaveBookInDatabase() {
        // Given
        BookDto dto = BookDto.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .publisher("Addison-Wesley")
                .isbn("978-1-56619-909-4")
                .build();

        Book book = Book.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .publisher("Addison-Wesley")
                .isbn("978-1-56619-909-4")
                .build();

        Book bookWithId = Book.builder()
                .id("667dc41cb4579216604fdd63")
                .title("Effective Java")
                .author("Joshua Bloch")
                .publisher("Addison-Wesley")
                .isbn("978-1-56619-909-4")
                .build();

        when(bookRepository.save(book)).thenReturn(bookWithId);

        // When
        BookDto bookDto = underTest.saveBook(dto);

        // Then
        assertAll(
                () -> assertNotNull(bookDto),
                () -> assertEquals(bookDto.title(), dto.title()),
                () -> assertEquals(bookDto.author(), dto.author()),
                () -> assertEquals(bookDto.publisher(), dto.publisher()),
                () -> assertEquals(bookDto.author(), dto.author())
        );
    }

    @Test
    @DisplayName("It should find book by given id")
    void itShouldFindBookByGivenId() {
        // Given
        String bookId = "1";

        Book book = Book.builder()
                .id(bookId)
                .title("Effective Java")
                .author("Joshua Bloch")
                .publisher("Addison-Wesley")
                .isbn("978-1-56619-909-4")
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // When
        BookDto bookById = underTest.findBookById(bookId);

        // Then
        assertAll(
                () -> assertEquals(bookById.id(), bookId),
                () -> assertEquals(bookById.title(), book.title()),
                () -> assertEquals(bookById.author(), book.author()),
                () -> assertEquals(bookById.publisher(), book.publisher()),
                () -> assertEquals(bookById.isbn(), book.isbn())
        );

    }
}

