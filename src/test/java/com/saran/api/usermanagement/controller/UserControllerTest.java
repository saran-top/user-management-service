package com.saran.api.usermanagement.controller;

import com.saran.api.usermanagement.dto.UpdateUserDTO;
import com.saran.api.usermanagement.dto.UpdateUserPasswordDTO;
import com.saran.api.usermanagement.dto.UpdateUserStatusDTO;
import com.saran.api.usermanagement.model.Role;
import com.saran.api.usermanagement.model.User;
import com.saran.api.usermanagement.model.UserStatus;
import com.saran.api.usermanagement.repository.RoleRepository;
import com.saran.api.usermanagement.repository.UserRepository;
import com.saran.api.usermanagement.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends BaseControllerTest
{
    private static final String PATH = "users";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    private Integer userId;

    private Integer roleId;

    @Before
    public void setUp() throws Exception
    {
        User mockUser = User.builder()
                .email("user-one@app.com")
                .passwordDigest("passwordDigest")
                .firstName("user")
                .lastName("one")
                .description("description")
                .build();
        Role mockRole = Role.builder()
                .name("USER")
                .description("Normal user role")
                .build();
        userId = userRepository.save(mockUser).getId();
        roleId = roleRepository.save(mockRole).getId();
    }

    @After
    public void tearDown() throws Exception
    {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void shouldFindAllUsers() throws Exception
    {
        this.mockMvc
                .perform(get("/" + PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("user-one@app.com"))
                .andExpect(jsonPath("$[0].firstName").value("user"))
                .andExpect(jsonPath("$[0].lastName").value("one"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$[0].updatedAt").isEmpty())
                .andExpect(jsonPath("$[0].version").isNumber());
    }

    @Test
    public void shouldFindUserById() throws Exception
    {
        this.mockMvc
                .perform(get("/" + PATH + "/" + userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user-one@app.com"))
                .andExpect(jsonPath("$.firstName").value("user"))
                .andExpect(jsonPath("$.lastName").value("one"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isEmpty())
                .andExpect(jsonPath("$.version").isNumber());
    }

    @Test
    public void shouldNotFindUserById() throws Exception
    {
        this.mockMvc
                .perform(get("/" + PATH + "/" + Integer.MIN_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("User not found for id " + Integer.MIN_VALUE));
    }

    @Test
    public void shouldUpdateUser() throws Exception
    {
        User user = userService.findById(userId);

        this.mockMvc
                .perform(put("/" + PATH + "/" + userId)
                        .content(toJson(UpdateUserDTO.builder()
                                .firstName("updatedFirstName")
                                .lastName("updatedLastName")
                                .description("updatedDescription")
                                .version(user.getVersion())
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        this.mockMvc
                .perform(get("/" + PATH + "/" + userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user-one@app.com"))
                .andExpect(jsonPath("$.firstName").value("updatedFirstName"))
                .andExpect(jsonPath("$.lastName").value("updatedLastName"))
                .andExpect(jsonPath("$.description").value("updatedDescription"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.version").value(user.getVersion() + 1));
    }

    @Test
    public void shouldNotUpdateUser() throws Exception
    {
        this.mockMvc
                .perform(put("/" + PATH + "/" + userId)
                        .content(toJson(UpdateUserDTO.builder()
                                .firstName("updatedFirstName")
                                .lastName("updatedLastName")
                                .description("updatedDescription")
                                .version(Integer.MIN_VALUE)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("OPTIMISTIC_LOCKING_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Entity was updated or deleted by another transaction. Try after the refreshing the entity."));
    }

    @Test
    public void shouldUpdateUserPassword() throws Exception
    {
        User user = userService.findById(userId);

        this.mockMvc
                .perform(patch("/" + PATH + "/" + userId + "/password")
                        .content(toJson(UpdateUserPasswordDTO.builder()
                                .password("upPassword")
                                .version(user.getVersion())
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        this.mockMvc
                .perform(get("/" + PATH + "/" + userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.description").value(user.getDescription()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.version").value(user.getVersion() + 1));
    }

    @Test
    public void shouldNotUpdateUserPassword() throws Exception
    {
        this.mockMvc
                .perform(patch("/" + PATH + "/" + userId + "/password")
                        .content(toJson(UpdateUserPasswordDTO.builder()
                                .password("upPassword")
                                .version(Integer.MIN_VALUE)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldUpdateUserStatus() throws Exception
    {
        User user = userService.findById(userId);

        this.mockMvc
                .perform(patch("/" + PATH + "/" + userId + "/status")
                        .content(toJson(UpdateUserStatusDTO.builder()
                                .userStatus(UserStatus.DELETED)
                                .version(user.getVersion())
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        this.mockMvc
                .perform(get("/" + PATH + "/" + userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.status").value(UserStatus.DELETED.name()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.description").value(user.getDescription()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.version").value(user.getVersion() + 1));
    }

    @Test
    public void shouldNotUpdateUserStatus() throws Exception
    {
        this.mockMvc
                .perform(patch("/" + PATH + "/" + userId + "/status")
                        .content(toJson(UpdateUserStatusDTO.builder()
                                .userStatus(UserStatus.DELETED)
                                .version(Integer.MIN_VALUE)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldMapRolesToUser() throws Exception
    {
        this.mockMvc
                .perform(put("/" + PATH + "/" + userId + "/roles")
                        .content(toJson(Collections.singletonList(roleId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        this.mockMvc
                .perform(get("/" + PATH + "/" + userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user-one@app.com"))
                .andExpect(jsonPath("$.firstName").value("user"))
                .andExpect(jsonPath("$.lastName").value("one"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.version").isNumber());
    }

}