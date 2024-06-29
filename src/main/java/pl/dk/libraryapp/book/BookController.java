package pl.dk.libraryapp.book;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.libraryapp.book.dtos.BookDto;

import java.net.URI;
import java.util.List;

import static pl.dk.libraryapp.book.constants.PaginationConstants.PAGE_DEFAULT;
import static pl.dk.libraryapp.book.constants.PaginationConstants.PAGE_SIZE_DEFAULT;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
@Validated
class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody BookDto bookDto) {
        BookDto savedBookDto = bookService.saveBook(bookDto);
        URI bookUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBookDto.id())
                .toUri();
        return ResponseEntity.created(bookUri).body(savedBookDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable String id) {
        BookDto optionBookDto = bookService.findBookById(id);
        return ResponseEntity.ok(optionBookDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable String id, @RequestBody JsonMergePatch jsonMergePatch) {
        bookService.updateBook(id, jsonMergePatch);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT) @Positive int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT) @Positive int size) {
        List<BookDto> allBooks = bookService.findAllBooks(page, size);
        return ResponseEntity.ok(allBooks);
    }
}
