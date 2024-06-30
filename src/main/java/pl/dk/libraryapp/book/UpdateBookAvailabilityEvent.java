package pl.dk.libraryapp.book;

public record UpdateBookAvailabilityEvent(
        String bookId,
        boolean availability
) {
}
