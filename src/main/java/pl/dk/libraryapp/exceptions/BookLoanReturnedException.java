package pl.dk.libraryapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookLoanReturnedException extends RuntimeException {

    public BookLoanReturnedException(String message) {
        super(message);
    }
}
