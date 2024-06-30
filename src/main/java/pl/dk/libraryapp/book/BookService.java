package pl.dk.libraryapp.book;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import pl.dk.libraryapp.book.dtos.BookDto;

import java.util.List;

public interface BookService {

    BookDto saveBook(BookDto bookDto);

    BookDto findBookById(String id);

    void updateBook(String id, JsonMergePatch jsonMergePatch);

    void deleteBookById(String id);

    List<BookDto> findAllBooks(int page, int size);

    BookDto findBookByISBN(String isbn);

    void setBookAvailability(UpdateBookAvailabilityEvent event);

    List<BookDto> findAllAvailableBooks(int pageNumber, int pageSize);
}
