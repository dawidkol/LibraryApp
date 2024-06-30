package pl.dk.libraryapp.bookloan;

import pl.dk.libraryapp.bookloan.dtos.BookLoanDto;
import pl.dk.libraryapp.bookloan.dtos.SaveBookLoanDto;

import java.util.List;

public interface BookLoanService {

    BookLoanDto saveBookLoan(SaveBookLoanDto saveBookLoanDto);

    void setBookLoanReturnedTime(String id);

    List<BookLoanDto> findCustomerBookLoans(String userId, int pageNumber, int pageSize);
}
