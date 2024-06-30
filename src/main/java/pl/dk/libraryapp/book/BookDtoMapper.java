package pl.dk.libraryapp.book;

import pl.dk.libraryapp.book.dtos.BookDto;

import java.util.Objects;

class BookDtoMapper {

    public static BookDto map(Book book) {
        return BookDto.builder()
                .id(book.id())
                .title(book.title())
                .author(book.author())
                .publisher(book.publisher())
                .isbn(book.isbn())
                .available(setAvailableProperty(book.available()))
                .build();
    }

    public static Book map(BookDto bookDto) {
        return Book.builder()
                .id(bookDto.id())
                .title(bookDto.title())
                .author(bookDto.author())
                .publisher(bookDto.publisher())
                .isbn(bookDto.isbn())
                .available(setAvailableProperty(bookDto.available()))
                .build();
    }

    private static boolean setAvailableProperty(Boolean current) {
        return Objects.requireNonNullElse(current, true);
    }
}
