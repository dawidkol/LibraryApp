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
        underTest = new BookServiceImpl(bookRepository, objectMapper);
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
                .available(true)
                .build();

        Book book = Book.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .publisher("Addison-Wesley")
                .isbn("978-1-56619-909-4")
                .available(true)
                .build();

        Book bookWithId = Book.builder()
                .id("667dc41cb4579216604fdd63")
                .title("Effective Java")
                .author("Joshua Bloch")
                .publisher("Addison-Wesley")
                .isbn("978-1-56619-909-4")
                .available(true)
                .build();

        when(bookRepository.save(any())).thenReturn(bookWithId);

        // When
        BookDto bookDto = underTest.saveBook(dto);

        // Then
        assertAll(
                () -> assertNotNull(bookDto),
                () -> assertEquals(dto.title(), bookDto.title()),
                () -> assertEquals(dto.author(), bookDto.author()),
                () -> assertEquals(dto.publisher(), bookDto.publisher()),
                () -> assertEquals(dto.author(), bookDto.author()),
                () -> assertEquals(dto.available(), bookDto.available()),
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
                .available(true)
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
                () -> assertEquals(book.available(), bookById.available()),
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
                .available(true)
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
                .available(true)
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
    @DisplayName("It should retrieve all books")
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
                .available(true)
                .build();
        List<Book> books = List.of(book);
        PageImpl<Book> pageImpl = new PageImpl<>(books);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        when(bookRepository.findAll(pageRequest)).thenReturn(pageImpl);

        // When
        List<BookDto> allBooks = underTest.findAllBooks(page, size);

        // Then
        assertAll(
                () -> verify(bookRepository, times(1)).findAll(pageRequest),
                () -> assertEquals(1, allBooks.size())
        );
    }

    @Test
    @DisplayName("It should find book by given ISBN")
    void itShouldFindBookByGivenISBN() {
        // Given
        String ISBN = "978-1-56619-909-4";

        Book book = Book.builder()
                .id("1")
                .title("Effective Java")
                .author("Joshua Bloch")
                .publisher("Addison-Wesley")
                .isbn(ISBN)
                .available(true)
                .build();

        when(bookRepository.findBookByIsbn(ISBN)).thenReturn(Optional.of(book));

        // When
        BookDto result = underTest.findBookByISBN(ISBN);

        // Then
        assertAll(
                () -> verify(bookRepository, times(1)).findBookByIsbn(ISBN),
                () -> assertEquals(book.id(), result.id()),
                () -> assertEquals(book.title(), result.title()),
                () -> assertEquals(book.author(), result.author()),
                () -> assertEquals(book.publisher(), result.publisher()),
                () -> assertEquals(book.isbn(), result.isbn()),
                () -> assertEquals(book.available(), result.available())
        );
    }

    @Test
    @DisplayName("It should throw BookNotFoundException when user tries to find book with non existing ISBN")
    void itShouldThrowBookNotFoundExceptionWhenUserTriesToFindBookWithNonExistingISBN() {
        // Given
        String ISBN = "978-1-56619-909-4";

        when(bookRepository.findBookByIsbn(ISBN)).thenReturn(Optional.empty());

        // When & Then
        assertAll(
                () -> assertThrows(BookNotFoundException.class, () -> underTest.findBookByISBN(ISBN)),
                () -> verify(bookRepository, times(1)).findBookByIsbn(ISBN)
        );
    }

    @Test
    @DisplayName(("It should find all available books"))
    void itShouldFindAllAvailableBooks() {
        // Given
        int pageNumber = 1;
        int pageSize = 25;

        Book book = Book.builder()
                .id("1")
                .title("Effective Java")
                .author("Joshua Bloch")
                .publisher("Addison-Wesley")
                .isbn("978-1-56619-909-4")
                .available(true)
                .build();

        List<Book> books = List.of(book);
        PageImpl<Book> page = new PageImpl<>(books);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);

        when(bookRepository.findAllByAvailableIsTrue(pageRequest)).thenReturn(page);

        // When
        List<BookDto> allAvailableBooks = underTest.findAllAvailableBooks(pageNumber, pageSize);

        // Then
        assertAll(
                () -> assertEquals(1, allAvailableBooks.size())
        );
    }

}

