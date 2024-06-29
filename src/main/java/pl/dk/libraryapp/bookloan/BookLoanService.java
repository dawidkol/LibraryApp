package pl.dk.libraryapp.bookloan;

import pl.dk.libraryapp.bookloan.dtos.BookLoanDto;
import pl.dk.libraryapp.bookloan.dtos.SaveBookLoanDto;

public interface BookLoanService {

    BookLoanDto saveBookLoan(SaveBookLoanDto saveBookLoanDto);
}
