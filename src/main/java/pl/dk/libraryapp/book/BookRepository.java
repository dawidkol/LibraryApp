package pl.dk.libraryapp.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BookRepository extends MongoRepository<Book, String> {
}
