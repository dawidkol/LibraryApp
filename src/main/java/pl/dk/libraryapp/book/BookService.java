package pl.dk.libraryapp.book;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import pl.dk.libraryapp.book.dtos.BookDto;
import pl.dk.libraryapp.book.dtos.BookInventoryDto;

import java.util.List;

public interface BookService {

    BookDto saveBook(BookDto bookDto);

    BookDto findBookById(String id);

    void updateBook(String id, JsonMergePatch jsonMergePatch);

    void deleteBookById(String id);

    List<BookInventoryDto> findAllBooks(int page, int size);

    BookDto findBookByISBN(String isbn);
}
