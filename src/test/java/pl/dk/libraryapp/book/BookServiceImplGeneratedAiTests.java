package pl.dk.libraryapp.book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.libraryapp.book.dtos.BookDto;
import pl.dk.libraryapp.exceptions.BookNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplGeneratedAiTests {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBook() {
        // GIVEN
        BookDto bookDto = BookDto.builder().title("The Lord of the Rings").author("J.R.R. Tolkien").publisher("HarperCollins").isbn("978-0-618-05327-4").available(true).build();

        // WHEN
        BookDto savedBook = bookService.saveBook(bookDto);

        // THEN
        assertNotNull(savedBook);
        assertEquals("The Lord of the Rings", savedBook.title());
    }

    @Test
    void testFindBookById() {
        // GIVEN
        String id = "123";
        Book expectedBook = BookDto.builder().id(id).title("The Hobbit").author("J.R.R. Tolkien").publisher("HarperCollins").isbn("978-0-618-05328-1").available(true).build();

        when(bookRepository.findById(id)).thenReturn(Optional.of(expectedBook));

        // WHEN
        BookDto foundBook = bookService.findBookById(id);

        // THEN
        assertEquals(expectedBook, foundBook);
    }

    @Test
    void testFindBookById_NotFound() {
        // GIVEN
        String id = "456";

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        assertThrows(BookNotFoundException.class, () -> bookService.findBookById(id));
    }


    @Test
    void testUpdateBook() {
        // GIVEN
        String id = "123";
        BookDto existingBook = BookDto.builder().id(id).title("The Hobbit").author("J.R.R. Tolkien").publisher("HarperCollins").isbn("978-0-618-05328-1").available(true).build();

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        // WHEN
        bookService.updateBook(id, null);

        // THEN
        verify(bookRepository, times(1)).save(any());
    }


    @Test
    void testDeleteBookById() {
        // GIVEN
        String id = "123";

        when(bookRepository.findById(id)).thenReturn(Optional.of(null));

        // WHEN
        bookService.deleteBookById(id);

        // THEN
        verify(bookRepository, times(1)).deleteById(id);
    }


    @Test
    void testDeleteBookById_NotFound() {
        // GIVEN
        String id = "456";

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBookById(id));
    }


}