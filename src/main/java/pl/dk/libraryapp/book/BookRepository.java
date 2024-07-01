package pl.dk.libraryapp.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    Page<Book> findAllByAvailableIsTrue(Pageable pageable);

    Optional<Book> findBookByIsbn(String isbn);

}
