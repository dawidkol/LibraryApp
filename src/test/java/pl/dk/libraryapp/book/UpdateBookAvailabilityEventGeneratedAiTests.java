package pl.dk.libraryapp.book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class UpdateBookAvailabilityEventGeneratedAiTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testConstructor() {
        GIVEN String bookId = "12345";;
        GIVEN boolean availability = true;;
        WHEN UpdateBookAvailabilityEvent event = new UpdateBookAvailabilityEvent(bookId, availability); 
        THEN assertEquals(bookId, event.getBookId());
        THEN assertEquals(availability, event.isAvailability());
    }
}