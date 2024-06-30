package pl.dk.libraryapp.bookloan;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.libraryapp.bookloan.dtos.BookLoanDto;
import pl.dk.libraryapp.bookloan.dtos.SaveBookLoanDto;

import java.net.URI;
import java.util.List;

import static pl.dk.libraryapp.constants.PaginationConstants.PAGE_DEFAULT;
import static pl.dk.libraryapp.constants.PaginationConstants.PAGE_SIZE_DEFAULT;

@RestController
@RequestMapping("/borrow")
@AllArgsConstructor
@Validated
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

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<BookLoanDto>> retrieveUserHistory(
            @PathVariable String userId,
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT) @Positive int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT) @Positive int size) {
        List<BookLoanDto> usersBookLoans = bookLoanService.findCustomerBookLoans(userId, page, size);
        return ResponseEntity.ok(usersBookLoans);
    }

}
