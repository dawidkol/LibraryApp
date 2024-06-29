package pl.dk.libraryapp.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.dk.libraryapp.book.dtos.BookDto;
import pl.dk.libraryapp.book.dtos.BookInventoryDto;
import pl.dk.libraryapp.exceptions.BookNotFoundException;

import java.util.List;
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
                () -> assertEquals(dto.title(), bookDto.title()),
                () -> assertEquals(dto.author(), bookDto.author()),
                () -> assertEquals(dto.publisher(), bookDto.publisher()),
                () -> assertEquals(dto.author(), bookDto.author()),
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
                () -> assertEquals(bookId, bookById.id()),
                () -> assertEquals(book.title(), bookById.title()),
                () -> assertEquals(book.author(), bookById.author()),
                () -> assertEquals(book.publisher(), bookById.publisher()),
                () -> assertEquals(book.isbn(), bookById.isbn()),
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
                () -> assertEquals("Effective Java - Updated", bookArgumentCaptor.getValue().title()),
                () -> assertEquals("Addison-Wesley - Updated", bookArgumentCaptor.getValue().author())
        );
    }

    @Test
    @DisplayName("It should delete book by given id")
    void itShouldDeleteBookByGivenId() {
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
        underTest.deleteBookById(bookId);

        // Then
        assertAll(
                () -> verify(bookRepository, times(1)).findById(bookId),
                () -> verify(bookRepository, times(1)).delete(book)
        );
    }

    @Test
    @DisplayName("It should throw BookNotFoundException when user tries to delete book with non existing ID")
    void itShouldThrowBookNotFoundExceptionWhenUserTriesToDeleteBookWithNonExistingId() {
        // Given
        String nonExistingId = "1";

        // When & Then
        assertAll(
                () -> assertThrows(BookNotFoundException.class, () -> underTest.deleteBookById(nonExistingId)),
                () -> verify(bookRepository, times(1)).findById(nonExistingId)
        );

    }

    @Test
    void itShouldRetrieveAllBooks() {
        // Given
        int page = 1;
        int size = 25;
        Book book = Book.builder()
                .id("1")
                .title("Effective Java")
                .author("Joshua Bloch")
                .publisher("Addison-Wesley")
                .isbn("978-1-56619-909-4")
                .build();
        List<Book> books = List.of(book);
        PageImpl<Book> pageImpl = new PageImpl<>(books);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        when(bookRepository.findAll(pageRequest)).thenReturn(pageImpl);

        // When
        List<BookInventoryDto> allBooks = underTest.findAllBooks(page, size);

        // Then
        assertAll(
                () -> verify(bookRepository, times(1)).findAll(pageRequest),
                () -> assertEquals(books.size(), pageImpl.getTotalElements()),
                () -> assertEquals(page, pageImpl.getTotalPages())
        );
    }
}

