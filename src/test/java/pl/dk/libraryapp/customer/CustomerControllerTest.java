package pl.dk.libraryapp.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import pl.dk.libraryapp.customer.dtos.CustomerDto;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test CRUD operations for Customer")
    void testCrudOperationsForCustomer() throws Exception {
        // 1. User wants to register new Customer
        String customerJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john.doe@test.pl"
                }
                """.trim();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        String location = response.getHeader("Location");
        assertNotNull(location);

        // 2. User wants to retrieve Customer
        String contentAsString = response.getContentAsString();
        CustomerDto customerDto = objectMapper.readValue(contentAsString, CustomerDto.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{id}", customerDto.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 3. User wants to retrieve all Customers
        mockMvc.perform(MockMvcRequestBuilders.get("/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))));

        // 4. User wants to update Customer
        String jsonMergePatchUpdate = """
                {
                "firstName": "John - updated",
                "lastName": "Doe - updated"
                }
                """.trim();
        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/{id}",customerDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMergePatchUpdate))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}