package pl.dk.libraryapp.bookloan;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.libraryapp.book.Book;
import pl.dk.libraryapp.book.BookRepository;
import pl.dk.libraryapp.book.UpdateBookAvailabilityEvent;
import pl.dk.libraryapp.bookloan.dtos.BookLoanDto;
import pl.dk.libraryapp.bookloan.dtos.SaveBookLoanDto;
import pl.dk.libraryapp.customer.Customer;
import pl.dk.libraryapp.customer.CustomerRepository;
import pl.dk.libraryapp.exceptions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public BookLoanDto saveBookLoan(SaveBookLoanDto saveBookLoanDto) {
        Book book = validateAndReturnBook(saveBookLoanDto);
        BookLoan bookToSave = this.map(saveBookLoanDto, book);
        BookLoan savedBook = bookLoanRepository.save(bookToSave);
        BookLoanDto bookLoanDto = this.map(savedBook);
        applicationEventPublisher.publishEvent(
                new UpdateBookAvailabilityEvent(bookLoanDto.bookId(), false));
        return bookLoanDto;
    }

    private Book validateAndReturnBook(SaveBookLoanDto saveBookLoanDto) {
        String bookId = saveBookLoanDto.bookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id %s not found".formatted(bookId)));

        if (!book.available()) {
            throw new BookBorrowedException("Book with id %s has already been borrowed".formatted(bookId));
        }
        return book;
    }

    private BookLoan map(SaveBookLoanDto saveBookLoanDto, Book book) {
        String customerId = saveBookLoanDto.customerId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id %s not found".formatted(customerId)));

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

    @Override
    @Transactional
    public void setBookLoanReturnedTime(String id) {
        BookLoan bookLoan = bookLoanRepository.findById(id)
                .orElseThrow(() -> new BookLoanNotFoundException("Book loan with id %s not found".formatted(id)));

        if (bookLoan.returnedAt() != null) {
            throw new BookLoanReturnedException(
                    "Book loan with id %s has already been returned".formatted(id));
        }

        BookLoan bookLoanToUpdate = createBookLoanUpdateRecord(bookLoan);
        BookLoan savedBookLoan = bookLoanRepository.save(bookLoanToUpdate);

        applicationEventPublisher.publishEvent(
                new UpdateBookAvailabilityEvent(savedBookLoan.book().id(), true));
    }

    private BookLoan createBookLoanUpdateRecord(BookLoan bookLoan) {
        return BookLoan.builder()
                .id(bookLoan.id())
                .borrowedAt(bookLoan.borrowedAt())
                .returnedAt(LocalDateTime.now())
                .customer(bookLoan.customer())
                .book(bookLoan.book())
                .build();
    }

    @Override
    public List<BookLoanDto> findCustomerBookLoans(String customerId, int pageNumber, int pageSize) {
        return bookLoanRepository.findAllByCustomer_Id(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

}
