package pl.dk.libraryapp.book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.libraryapp.book.dtos.BookDto;
import pl.dk.libraryapp.book.service.BookService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;


class BookControllerGeneratedAiTests {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBook() {
        // GIVEN
        BookDto bookDto = BookDto.builder().id("123").title("Title").author("Author").publisher("Publisher").isbn("ISBN").available(true).build();
        when(bookService.saveBook(bookDto)).thenReturn(bookDto);

        // WHEN
        ResponseEntity<BookDto> response = bookController.addBook(bookDto);

        // THEN
        verify(bookService, times(1)).saveBook(bookDto);
        assertEquals(CREATED, response.getStatusCode());
        assertEquals(bookDto, response.getBody());
    }

    @Test
    void testGetBookById() {
        // GIVEN
        String id = "123";
        BookDto bookDto = BookDto.builder().id("123").title("Title").author("Author").publisher("Publisher").isbn("ISBN").available(true).build();
        when(bookService.findBookById(id)).thenReturn(bookDto);

        // WHEN
        ResponseEntity<BookDto> response = bookController.getBookById(id);

        // THEN
        verify(bookService, times(1)).findBookById(id);
        assertEquals(ok(), response);
        assertEquals(bookDto, response.getBody());
    }

    @Test
    void testUpdateBook() {
        // GIVEN
        String id = "123";
        when(bookService.updateBook(id, any())).thenReturn(null);

        // WHEN
        ResponseEntity<?> response = bookController.updateBook(id, null);

        // THEN
        verify(bookService, times(1)).updateBook(id, any());
        assertEquals(ok(), response);
    }

    @Test
    void testDeleteBook() {
        // GIVEN
        String id = "123";
        when(bookService.deleteBookById(id)).thenReturn(null);

        // WHEN
        ResponseEntity<?> response = bookController.deleteBook(id);

        // THEN
        verify(bookService, times(1)).deleteBookById(id);
        assertEquals(ok(), response);
    }

    @Test
    void testGetAllBooks() {
        // GIVEN
        List<BookDto> books = List.of(BookDto.builder().id("123").title("Title").author("Author").publisher("Publisher").isbn("ISBN").available(true).build());
        when(bookService.findAllAvailableBooks(0, 10)).thenReturn(books);

        // WHEN
        ResponseEntity<List<BookDto>> response = bookController.getAllBooks(0, 10, true);

        // THEN
        verify(bookService, times(1)).findAllAvailableBooks(0, 10);
        assertEquals(ok(), response);
        assertEquals(books, response.getBody());
    }


}