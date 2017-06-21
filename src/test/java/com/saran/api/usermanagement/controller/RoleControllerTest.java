package com.saran.api.usermanagement.controller;

import com.saran.api.usermanagement.dto.NewRoleDTO;
import com.saran.api.usermanagement.model.Role;
import com.saran.api.usermanagement.repository.RoleRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RoleControllerTest extends BaseControllerTest
{
    private static final String PATH = "roles";

    @Autowired
    private RoleRepository roleRepository;

    private Integer id;

    @Before
    public void setUp() throws Exception
    {
        Role mockRole = Role.builder()
                .name("ADMIN")
                .description("description")
                .build();
        id = roleRepository.save(mockRole).getId();
    }

    @After
    public void tearDown() throws Exception
    {
        roleRepository.deleteAll();
    }

    @Test
    public void shouldFindAllRoles() throws Exception
    {
        this.mockMvc
                .perform(get("/" + PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("ADMIN"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$[0].updatedAt").isEmpty())
                .andExpect(jsonPath("$[0].version").isNumber());
    }

    @Test
    public void shouldFindRoleById() throws Exception
    {
        this.mockMvc
                .perform(get("/" + PATH + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADMIN"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isEmpty())
                .andExpect(jsonPath("$.version").isNumber());
    }

    @Test
    public void shouldNotFindRoleById() throws Exception
    {
        this.mockMvc
                .perform(get("/" + PATH + "/" + Integer.MAX_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Role not found for id " + Integer.MAX_VALUE));
    }

    @Test
    public void shouldCreateRole() throws Exception
    {
        this.mockMvc
                .perform(post("/" + PATH)
                        .content(toJson(NewRoleDTO.builder()
                                .name("ROLE_ONE")
                                .description("description")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotCreateRole() throws Exception
    {
        this.mockMvc
                .perform(post("/" + PATH)
                        .content(toJson(NewRoleDTO.builder()
                                .name("ADMIN")
                                .description("description")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("EXISTS_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Role [ADMIN] already exists"));
    }

}