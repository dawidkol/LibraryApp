package pl.dk.libraryapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookHasAlreadyBeenReturnedException extends RuntimeException {

    public BookHasAlreadyBeenReturnedException(String message) {
        super(message);
    }
}
