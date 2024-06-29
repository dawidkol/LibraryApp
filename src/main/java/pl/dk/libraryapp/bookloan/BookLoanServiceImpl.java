package pl.dk.libraryapp.bookloan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.libraryapp.book.Book;
import pl.dk.libraryapp.book.BookRepository;
import pl.dk.libraryapp.bookloan.dtos.BookLoanDto;
import pl.dk.libraryapp.bookloan.dtos.SaveBookLoanDto;
import pl.dk.libraryapp.customer.Customer;
import pl.dk.libraryapp.customer.CustomerRepository;
import pl.dk.libraryapp.exceptions.BookNotFoundException;
import pl.dk.libraryapp.exceptions.CustomerNotFoundException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public BookLoanDto saveBookLoan(SaveBookLoanDto saveBookLoanDto) {
        BookLoan bookToSave = this.map(saveBookLoanDto);
        BookLoan savedBook = bookLoanRepository.save(bookToSave);
        return this.map(savedBook);
    }

    private BookLoan map(SaveBookLoanDto saveBookLoanDto) {
        String customerId = saveBookLoanDto.customerId();
        String bookId = saveBookLoanDto.bookId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id %s not found".formatted(customerId)));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id %s not found".formatted(bookId)));
        return BookLoan.builder()
                .id(saveBookLoanDto.id())
                .borrowedAt(LocalDateTime.now())
                .returnedAt(null)
                .customer(customer)
                .book(book)
                .build();
    }

    private BookLoanDto map(BookLoan bookLoan) {
        return BookLoanDto.builder()
                .id(bookLoan.id())
                .borrowedAt(bookLoan.borrowedAt())
                .returnedAt(bookLoan.returnedAt())
                .customerId(bookLoan.customer().id())
                .bookId(bookLoan.book().id())
                .build();
    }
}
