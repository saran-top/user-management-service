package com.saran.api.usermanagement.controller;

import com.saran.api.usermanagement.dto.NewUserDTO;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationControllerTest extends BaseControllerTest
{
    private static final String PATH = "register";

    @Test
    public void shouldNotRegisterUserWithInvalidInput() throws Exception
    {
        this.mockMvc
                .perform(post("/" + PATH)
                        .content(toJson(NewUserDTO.builder().build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Validation errors. Refer errors for details"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.*", hasSize(4)));
    }

    @Test
    public void shouldNotRegisterUserWithInvalidEmail() throws Exception
    {
        this.mockMvc
                .perform(post("/" + PATH)
                        .content(toJson(NewUserDTO.builder()
                                .email("user-one@.cm")
                                .firstName("user")
                                .lastName("one")
                                .password("password")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Validation errors. Refer errors for details"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("Invalid email"));
    }

    @Test
    public void shouldNotRegisterUserWithInvalidPassword() throws Exception
    {
        this.mockMvc
                .perform(post("/" + PATH)
                        .content(toJson(NewUserDTO.builder()
                                .email("user-one@app.cm")
                                .firstName("user")
                                .lastName("one")
                                .password("p")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Validation errors. Refer errors for details"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("Password should of minimum 6 & maximum 12 characters"));
    }

    @Test
    public void shouldRegisterUser() throws Exception
    {
        this.mockMvc
                .perform(post("/" + PATH)
                        .content(toJson(NewUserDTO.builder()
                                .email("user-two@app.c0m")
                                .firstName("user")
                                .lastName("one")
                                .password("password")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

}