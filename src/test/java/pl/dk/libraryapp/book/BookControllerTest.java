package pl.dk.libraryapp.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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


@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Test CRUD operations for Book")
    void testCrudOperationsForBook() throws Exception {
        // 1. User wants to add book to the database
        String bookJson = """
                {
                    "title": "Effective Java",
                    "author": "Joshua Bloch",
                    "publisher": "Addison-Wesley",
                    "isbn": "978-1-56619-909-4"
                }
                """.trim();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        MockHttpServletResponse response = resultActions
                .andReturn()
                .getResponse();

        String location = response.getHeader("Location");

        Assertions.assertNotNull(location);


        // 2. User wants to find book by given id
        String bookString = response.getContentAsString();
        Book book = objectMapper.readValue(bookString, Book.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", book.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 3. User wants to update book
        String jsonMergePatchString = """
                {
                    "title": "Effective Java - Updated",
                    "author": "Addison-Wesley - Updated"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/books/{id}", book.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMergePatchString))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

}