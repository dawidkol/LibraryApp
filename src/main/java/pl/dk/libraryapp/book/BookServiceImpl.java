package pl.dk.libraryapp.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.libraryapp.book.dtos.BookDto;
import pl.dk.libraryapp.exceptions.BookNotFoundException;
import pl.dk.libraryapp.exceptions.ServerException;

import java.util.List;

@Service
@AllArgsConstructor
class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public BookDto saveBook(BookDto bookDto) {
        Book bookToSave = BookDtoMapper.map(bookDto);
        Book savedBook = bookRepository.save(bookToSave);
        return BookDtoMapper.map(savedBook);
    }

    public BookDto findBookById(String id) {
        return bookRepository.findById(id)
                .map(BookDtoMapper::map)
                .orElseThrow(() -> new BookNotFoundException("Book with id = %s not found".formatted(id)));
    }

    @Override
    @Transactional
    public void updateBook(String id, JsonMergePatch jsonMergePatch) {
        BookDto bookById = this.findBookById(id);
        try {
            BookDto bookDto = this.applyPatch(bookById, jsonMergePatch);
            Book bookToUpdate = BookDtoMapper.map(bookDto);
            bookRepository.save(bookToUpdate);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ServerException();
        }
    }

    private BookDto applyPatch(BookDto bookDto, JsonMergePatch jsonMergePatch) throws JsonPatchException, JsonProcessingException {
        JsonNode jsonNode = objectMapper.valueToTree(bookDto);
        JsonNode bookDtoPatchedNode = jsonMergePatch.apply(jsonNode);
        return objectMapper.treeToValue(bookDtoPatchedNode, BookDto.class);
    }

    @Override
    @Transactional
    public void deleteBookById(String id) {
        bookRepository.findById(id)
                .ifPresentOrElse(bookRepository::delete, () -> {
                    throw new BookNotFoundException("Book with id = %s not found".formatted(id));
                });
    }

    @Override
    public List<BookDto> findAllBooks(int page, int size) {
        return bookRepository.findAll(PageRequest.of(--page, size))
                .stream()
                .map(BookDtoMapper::map)
                .toList();
    }

    @Override
    public BookDto findBookByISBN(String isbn) {
        return bookRepository.findBookByIsbn(isbn)
                .map(BookDtoMapper::map)
                .orElseThrow(() -> new BookNotFoundException("Book with ISBN = %s not found".formatted(isbn)));
    }

    @Override
    @EventListener
    @Async
    @Transactional
    public void setBookAvailability(UpdateBookAvailabilityEvent event) {
        Book book = bookRepository.findById(event.bookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id = %s not found".formatted(event.bookId())));
        Book updatedBook = Book.builder()
                .id(book.id())
                .title(book.title())
                .author(book.author())
                .publisher(book.publisher())
                .isbn(book.isbn())
                .available(event.availability())
                .build();
        bookRepository.save(updatedBook);
    }

    @Override
    public List<BookDto> findAllAvailableBooks(int pageNumber, int pageSize) {
        return bookRepository.findAllByAvailableIsTrue(PageRequest.of(--pageNumber, pageSize))
                .stream()
                .map(BookDtoMapper::map)
                .toList();
    }

}

