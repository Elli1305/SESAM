package com.gpse.sesam;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SesamUserControllerIT {
    @Autowired
    private MockMvc mvc;

    @Test
    @Disabled
    void signUp() throws Exception {
        // TODO: How can I prevent the sign up from persisting?
        final String body = "{\"firstName\": \"Example\",\"lastName\": \"User\",\"email\": \"test@example.com\",\"password\": \"password\", \"requestedRoles\": [\"ISSUER\"]}";

        this.mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Example"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[0].role").value("ISSUER"))
                .andExpect(jsonPath("$.roles[0].granted").value(false));
    }

    @Test
    void signUpWithMissingFirstName() throws Exception {
        final String body = "{\"lastName\": \"User\",\"email\": \"test@example.com\",\"password\": \"password\", \"requestedRoles\": [\"ISSUER\"]}";

        this.mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void signUpWithInvalidEmail() throws Exception {
        final String body = "{\"firstName\": \"Example\",\"lastName\": \"User\",\"email\": \"this is not an email\",\"password\": \"password\", \"requestedRoles\": [\"ISSUER\"]}";

        this.mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnprocessableEntity());
    }
}
