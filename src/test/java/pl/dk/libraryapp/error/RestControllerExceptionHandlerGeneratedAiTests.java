package pl.dk.libraryapp.error;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class RestControllerExceptionHandlerGeneratedAiTests {

    @Test
    void handleConstraintViolationException_ReturnsExpectedResult() {
        // GIVEN
        ConstraintViolationException ex = new ConstraintViolationException(List.of());
        // WHEN
        List<ConstraintViolationWrapper> result = new RestControllerExceptionHandler().handleConstraintViolationException(ex);
        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, result.get(0).getStatus());
    }

    @Test
    void handleMongoWriteException_ReturnsExpectedResult() {
        // GIVEN
        MongoWriteException ex = new MongoWriteException();
        // WHEN
        MongoWriteExceptionWrapper result = new RestControllerExceptionHandler().handleMongoWriteException(ex);
        // THEN
        assertEquals(HttpStatus.CONFLICT, HttpStatus.valueOf(result.getClass().getName()));
    }

    @Test
    void handleMethodArgumentNotValid_ReturnsExpectedResult() {
        // GIVEN
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException();
        // WHEN
        List<MethodArgumentNotValidWrapper> result = new RestControllerExceptionHandler().handleMethodArgumentNotValid(ex);
        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, result.get(0).getStatus());
    }

}