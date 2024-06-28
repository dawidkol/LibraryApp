package pl.dk.libraryapp.book;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.libraryapp.book.dtos.BookDto;

import java.net.URI;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
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

}
