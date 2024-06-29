package pl.dk.libraryapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookLoanNotFoundException extends RuntimeException{

    public BookLoanNotFoundException(String message) {
        super(message);
    }
}
