package pl.dk.libraryapp.bookloan;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
interface BookLoanRepository extends MongoRepository<BookLoan, String> {

    List<BookLoan> findAllByCustomer_Id(String customerId);

}
