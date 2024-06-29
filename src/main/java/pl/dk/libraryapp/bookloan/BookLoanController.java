package pl.dk.libraryapp.bookloan;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.libraryapp.bookloan.dtos.BookLoanDto;
import pl.dk.libraryapp.bookloan.dtos.SaveBookLoanDto;

import java.net.URI;

@RestController
@RequestMapping("/borrowBook")
@AllArgsConstructor
class BookLoanController {

    private final BookLoanService bookLoanService;

    @PostMapping
    public ResponseEntity<BookLoanDto> borrowBook(@Valid @RequestBody SaveBookLoanDto saveBookLoanDto) {
        BookLoanDto bookLoanDto = bookLoanService.saveBookLoan(saveBookLoanDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookLoanDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(bookLoanDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> returnBook(@PathVariable String id) {
        bookLoanService.setBookLoanReturnedTime(id);
        return ResponseEntity.noContent().build();
    }

}
