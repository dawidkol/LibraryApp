package pl.dk.libraryapp.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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


    @Test
    @DisplayName("It should add book to database")
    void itShouldAddBookToDatabase() throws Exception {
        // Given
        String bookJson = """
                {
                    "title": "Effective Java",
                    "author": "Joshua Bloch",
                    "publisher": "Addison-Wesley",
                    "isbn": "978-1-56619-909-4"
                }
                """.trim();

        // When & Then
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String location = resultActions
                .andReturn()
                .getResponse()
                .getHeader("Location");

        Assertions.assertNotNull(location);
    }
}