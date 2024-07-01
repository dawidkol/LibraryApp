package pl.dk.libraryapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookBorrowedException extends RuntimeException {

    public BookBorrowedException(String message) {
        super(message);
    }
}
