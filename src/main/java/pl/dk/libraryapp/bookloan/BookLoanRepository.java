package pl.dk.libraryapp.bookloan;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BookLoanRepository extends MongoRepository<BookLoan, String> {
}
