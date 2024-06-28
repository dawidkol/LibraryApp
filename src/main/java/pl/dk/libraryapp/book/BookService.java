package pl.dk.libraryapp.book;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.libraryapp.book.dtos.BookDto;

@Service
@AllArgsConstructor
class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public BookDto saveBook(BookDto bookDto) {
        Book bookToSave = BookDtoMapper.map(bookDto);
        Book savedBook = bookRepository.save(bookToSave);
        return BookDtoMapper.map(savedBook);
    }

}
