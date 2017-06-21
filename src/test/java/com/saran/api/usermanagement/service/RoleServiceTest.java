package com.saran.api.usermanagement.service;

import com.saran.api.usermanagement.UserManagementServiceApplication;
import com.saran.api.usermanagement.dto.NewRoleDTO;
import com.saran.api.usermanagement.exception.ResourceExistsException;
import com.saran.api.usermanagement.exception.ResourceNotFoundException;
import com.saran.api.usermanagement.model.Role;
import com.saran.api.usermanagement.model.User;
import com.saran.api.usermanagement.repository.RoleRepository;
import com.saran.api.usermanagement.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserManagementServiceApplication.class)
@ActiveProfiles("test")
public class RoleServiceTest
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Role createdRole;

    private User createdUser;

    @Before
    public void setUp() throws Exception
    {
        Role mockRole = getMockRole();
        createdRole = roleRepository.save(mockRole);
        createdUser = userRepository.save(getMockUser());
    }

    @After
    public void tearDown() throws Exception
    {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }


    @Test
    public void shouldFindRoleById() throws Exception
    {
        Role role = roleService.findById(createdRole.getId());
        assertThat(role.getName()).isEqualTo("USER");
        assertThat(role.getCreatedAt()).isNotNull();
        assertThat(role.getDescription()).isEqualTo("Normal user role");
        assertThat(role.getUpdatedAt()).isNull();
        assertThat(role.getVersion()).isGreaterThanOrEqualTo(0);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldNotFindRoleById() throws Exception
    {
        roleService.findById(Integer.MIN_VALUE);
    }

    @Test
    public void shouldFindByName() throws Exception
    {
        Role role = roleService.findByName("USER");
        assertThat(role.getName()).isEqualTo("USER");
        assertThat(role.getCreatedAt()).isNotNull();
        assertThat(role.getDescription()).isEqualTo("Normal user role");
        assertThat(role.getUpdatedAt()).isNull();
        assertThat(role.getVersion()).isGreaterThanOrEqualTo(0);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldNotFindRoleByName() throws Exception
    {
        roleService.findByName("");
    }

    @Test
    public void shouldFindAllRoles() throws Exception
    {
        roleRepository.save(getMockRoles());
        assertThat(roleService.findAll().size()).isGreaterThanOrEqualTo(4);
    }


    @Test
    public void shouldCreateRole() throws Exception
    {
        NewRoleDTO dto = NewRoleDTO.builder()
                .name("ADMIN")
                .description("Admin role")
                .build();
        Role role = roleService.create(dto);
        assertThat(role.getName()).isEqualTo("ADMIN");
        assertThat(role.getCreatedAt()).isNotNull();
        assertThat(role.getDescription()).isEqualTo("Admin role");
        assertThat(role.getUpdatedAt()).isNull();
        assertThat(role.getVersion()).isGreaterThanOrEqualTo(0);
    }

    @Test(expected = ResourceExistsException.class)
    public void shouldNotCreateRole() throws Exception
    {
        NewRoleDTO dto = NewRoleDTO.builder()
                .name("USER")
                .build();
        roleService.create(dto);
    }

    @Test
    public void shouldMapRolesToUser()
    {
        User mappedUser = roleService.mapRoles(createdUser.getId(), Collections.singletonList(createdRole.getId()));
        assertThat(mappedUser.getRoles().size()).isEqualTo(1);
    }

    private Role getMockRole()
    {
        return Role.builder()
                .name("USER")
                .description("Normal user role")
                .build();
    }

    private List<Role> getMockRoles()
    {
        return Arrays.asList(
                Role.builder().name("ROLE_ONE").description("Role one").build(),
                Role.builder().name("ROLE_TWO").description("Role Two").build(),
                Role.builder().name("ROLE_THREE").description("Role Three").build(),
                Role.builder().name("ROLE_FOUR").description("Role Four").build()
        );
    }

    private User getMockUser()
    {
        return User.builder()
                .email("user-one@app.com")
                .passwordDigest(passwordEncoder.encode("passwordDigest"))
                .firstName("user")
                .lastName("one")
                .description("description")
                .build();
    }

}