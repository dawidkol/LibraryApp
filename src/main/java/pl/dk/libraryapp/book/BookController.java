package pl.dk.libraryapp.book;

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

}
