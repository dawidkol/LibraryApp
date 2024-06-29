package pl.dk.libraryapp.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    int countAllByTitleAndAuthorAndPublisher(String title, String author, String publisher);
    Optional<Book> findBookByIsbn(String isbn);

}
