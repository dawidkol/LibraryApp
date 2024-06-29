package pl.dk.libraryapp.error;

import com.mongodb.MongoWriteException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
class BookControllerErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ConstraintViolationWrapper> handleConstraintViolationException(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .map(fieldError -> new ConstraintViolationWrapper(
                        fieldError.getPropertyPath().toString().split("\\.")[1],
                        fieldError.getInvalidValue().toString(),
                        fieldError.getMessage()))
                .toList();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(MongoWriteException.class)
    public MongoWriteExceptionWrapper handleMongoWriteException(MongoWriteException ex) {
        return new MongoWriteExceptionWrapper(ex.getError().getCategory().name(), ex.getError().getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<MethodArgumentNotValidWrapper> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new MethodArgumentNotValidWrapper(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
    }

    record ConstraintViolationWrapper(String parameter, String value, String message) {
    }

    record MongoWriteExceptionWrapper(String value, String message) {
    }

    record MethodArgumentNotValidWrapper(String field, String message) {
    }
}
