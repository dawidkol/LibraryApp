package pl.dk.libraryapp.bookloan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.libraryapp.TestcontainersConfiguration;
import pl.dk.libraryapp.book.dtos.BookDto;
import pl.dk.libraryapp.bookloan.dtos.BookLoanDto;
import pl.dk.libraryapp.customer.dtos.CustomerDto;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookLoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testBorrowAndReturnOperations() throws Exception {
        // 1. User register Customer
        String customerJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john.doe@test.pl"
                }
                """.trim();

        String customerAsString = mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        CustomerDto customerDto = objectMapper.readValue(customerAsString, CustomerDto.class);

        // 2. User add book
        String bookJson = """
                {
                    "title": "Effective Java",
                    "author": "Joshua Bloch",
                    "publisher": "Addison-Wesley",
                    "isbn": "978-1-56619-909-4"
                }
                """.trim();

        String bookAsString = mockMvc.perform(MockMvcRequestBuilders
                        .post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDto bookDto = objectMapper.readValue(bookAsString, BookDto.class);

        // 3. User wants to borrow book
        String borrowBookJson = """
                {
                    "customerId": "%s",
                    "bookId": "%s"
                }
                """.trim().formatted(customerDto.id(), bookDto.id());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/borrowBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(borrowBookJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        String location = response.getHeader("Location");
        assertNotNull(location);

        // 4. User wants to return book
        String bookLoanAsString = response.getContentAsString();
        BookLoanDto bookLoanDto = objectMapper.readValue(bookLoanAsString, BookLoanDto.class);

        mockMvc.perform(MockMvcRequestBuilders.patch("/borrowBook/{id}", bookLoanDto.id()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


}