package pl.dk.libraryapp.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.libraryapp.book.dtos.BookDto;
import pl.dk.libraryapp.book.exceptions.BookNotFoundException;
import pl.dk.libraryapp.book.exceptions.ServerException;

@Service
@AllArgsConstructor
class BookService {

    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;

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
}

