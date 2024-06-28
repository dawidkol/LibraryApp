package pl.dk.libraryapp.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.libraryapp.book.dtos.BookDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    private BookService underTest;
    private AutoCloseable autoCloseable;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        underTest = new BookService(bookRepository, objectMapper);
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
                () -> assertEquals(bookDto.author(), dto.author()),
                () -> verify(bookRepository, times(1)).save(book)
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
                () -> assertEquals(bookById.isbn(), book.isbn()),
                () -> verify(bookRepository, times(1)).findById(bookId)
        );
    }

    @Test
    @DisplayName("It should update book author and publisher")
    void itShouldUpdateBookAuthorAndPublisher() throws JsonProcessingException {
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
        when(bookRepository.save(any(Book.class))).thenReturn(any(Book.class));
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        String jsonMergePatchString = """
                {
                    "title": "Effective Java - Updated",
                    "author": "Addison-Wesley - Updated"
                }
                """;

        JsonMergePatch jsonMergePatch = objectMapper.readValue(jsonMergePatchString, JsonMergePatch.class);

        // When
        underTest.updateBook(bookId, jsonMergePatch);

        // Then
        assertAll(
                () -> verify(bookRepository, times(1)).findById(bookId),
                () -> verify(bookRepository, times(1)).save(bookArgumentCaptor.capture()),
                () -> assertEquals(bookArgumentCaptor.getValue().title(), "Effective Java - Updated"),
                () -> assertEquals(bookArgumentCaptor.getValue().author(), "Addison-Wesley - Updated")
        );
    }
}

