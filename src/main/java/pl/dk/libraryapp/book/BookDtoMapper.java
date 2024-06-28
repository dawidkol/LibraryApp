package pl.dk.libraryapp.book;

import pl.dk.libraryapp.book.dtos.BookDto;

class BookDtoMapper {

    public static BookDto map(Book book) {
        return BookDto.builder()
                .id(book.id())
                .title(book.title())
                .author(book.author())
                .publisher(book.publisher())
                .isbn(book.isbn())
                .build();
    }

    public static Book map(BookDto bookDto) {
        return Book.builder()
                .id(bookDto.id())
                .title(bookDto.title())
                .author(bookDto.author())
                .publisher(bookDto.publisher())
                .isbn(bookDto.isbn())
                .build();
    }
}
