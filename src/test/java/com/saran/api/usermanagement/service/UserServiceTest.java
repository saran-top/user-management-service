package com.saran.api.usermanagement.service;

import com.saran.api.usermanagement.UserManagementServiceApplication;
import com.saran.api.usermanagement.dto.NewUserDTO;
import com.saran.api.usermanagement.dto.UpdateUserDTO;
import com.saran.api.usermanagement.dto.UpdateUserPasswordDTO;
import com.saran.api.usermanagement.dto.UpdateUserStatusDTO;
import com.saran.api.usermanagement.exception.ResourceExistsException;
import com.saran.api.usermanagement.exception.ResourceNotFoundException;
import com.saran.api.usermanagement.model.User;
import com.saran.api.usermanagement.model.UserStatus;
import com.saran.api.usermanagement.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserManagementServiceApplication.class)
@ActiveProfiles("test")
public class UserServiceTest
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Integer id;

    @Before
    public void setUp() throws Exception
    {
        User mockUser = getMockUser();
        id = userRepository.save(mockUser).getId();
    }

    @After
    public void tearDown() throws Exception
    {
        userRepository.deleteAll();
    }

    @Test
    public void shouldFindAllUsers() throws Exception
    {
        List<User> users = userService.findAll();
        assertThat(users.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void shouldFindAllUsersByStatuses() throws Exception
    {
        userRepository.save(getMockUsers());
        List<User> users = userService.findByStatues(Arrays.asList(UserStatus.ACTIVE, UserStatus.DELETED, UserStatus.BLOCKED, UserStatus.EXPIRED));
        assertThat(users.size()).isEqualTo(4);
    }

    @Test
    public void shouldFindUserById() throws Exception
    {
        User user = userService.findById(id);
        assertThat(user.getEmail()).isEqualTo("user-one@app.com");
        assertThat(user.getFirstName()).isEqualTo("user");
        assertThat(user.getLastName()).isEqualTo("one");
        assertThat(user.getPasswordDigest()).isNotNull();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getDescription()).isEqualTo("description");
        assertThat(user.getUpdatedAt()).isNull();
        assertThat(user.getVersion()).isGreaterThanOrEqualTo(0);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldNotFindUserById() throws Exception
    {
        userService.findById(Integer.MIN_VALUE);
    }

    @Test
    public void shouldFindUserByEmail() throws Exception
    {
        User user = userService.findByEmail("user-one@app.com");
        assertThat(user.getEmail()).isEqualTo("user-one@app.com");
        assertThat(user.getFirstName()).isEqualTo("user");
        assertThat(user.getLastName()).isEqualTo("one");
        assertThat(user.getPasswordDigest()).isNotNull();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getDescription()).isEqualTo("description");
        assertThat(user.getUpdatedAt()).isNull();
        assertThat(user.getVersion()).isGreaterThanOrEqualTo(0);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldNotFindUserByEmail() throws Exception
    {
        userService.findByEmail("");
    }

    @Test
    public void shouldCreateUser() throws Exception
    {
        NewUserDTO newUserDTO = NewUserDTO.builder()
                .email("user-two@app.com")
                .firstName("user")
                .lastName("two")
                .password("password")
                .build();
        User user = userService.create(newUserDTO);
        assertThat(user.getEmail()).isEqualTo("user-two@app.com");
        assertThat(user.getFirstName()).isEqualTo("user");
        assertThat(user.getLastName()).isEqualTo("two");
        assertThat(user.getPasswordDigest()).isNotNull();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getDescription()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
        assertThat(user.getVersion()).isGreaterThanOrEqualTo(0);
        assertThat(user.getStatus()).isEqualTo(UserStatus.NEW);
    }

    @Test(expected = ResourceExistsException.class)
    public void shouldNotCreateUserIfAlreadyExists() throws Exception
    {
        userService.create(NewUserDTO.builder().email("user-one@app.com").build());
    }

    @Test
    public void shouldUpdateUserPassword() throws Exception
    {
        User retrievedUser = userService.findById(id);
        UpdateUserPasswordDTO dto = UpdateUserPasswordDTO.builder()
                .password("password")
                .version(retrievedUser.getVersion())
                .build();
        User updatedUser = userService.updatePassword(id, dto);
        assertThat(updatedUser.getPasswordDigest()).isNotNull();
        assertThat(updatedUser.getUpdatedAt()).isNotNull();
        assertThat(updatedUser.getVersion()).isGreaterThanOrEqualTo(1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldNotUpdateUserPassword() throws Exception
    {
        userService.updatePassword(Integer.MIN_VALUE, UpdateUserPasswordDTO.builder().build());
    }

    @Test
    public void shouldUpdateUserStatus() throws Exception
    {
        User retrievedUser = userService.findById(id);
        UpdateUserStatusDTO dto = UpdateUserStatusDTO.builder()
                .userStatus(UserStatus.INACTIVE)
                .version(retrievedUser.getVersion())
                .build();
        User updatedUser = userService.updateStatus(id, dto);
        assertThat(updatedUser.getStatus()).isEqualTo(UserStatus.INACTIVE);
        assertThat(updatedUser.getUpdatedAt()).isNotNull();
        assertThat(updatedUser.getVersion()).isGreaterThanOrEqualTo(1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldNotUpdateUserStatus() throws Exception
    {
        userService.updateStatus(Integer.MIN_VALUE, UpdateUserStatusDTO.builder().build());
    }

    @Test
    public void shouldUpdateUser() throws Exception
    {
        User retrievedUser = userService.findById(id);
        UpdateUserDTO dto = UpdateUserDTO.builder()
                .firstName("firstName")
                .lastName("lastName")
                .description("newDescription")
                .version(retrievedUser.getVersion())
                .build();
        User updatedUser = userService.update(id, dto);
        assertThat(updatedUser.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(updatedUser.getLastName()).isEqualTo(dto.getLastName());
        assertThat(updatedUser.getDescription()).isEqualTo(dto.getDescription());
        assertThat(updatedUser.getUpdatedAt()).isNotNull();
        assertThat(updatedUser.getVersion()).isGreaterThanOrEqualTo(1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldNotUpdateUser() throws Exception
    {
        userService.update(Integer.MIN_VALUE, UpdateUserDTO.builder().build());
    }

    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void shouldNotUpdateUserWithIncorrectVersion() throws Exception
    {
        UpdateUserDTO dto = UpdateUserDTO.builder()
                .firstName("firstName")
                .lastName("lastName")
                .description("newDescription")
                .version(Integer.MAX_VALUE)
                .build();
        userService.update(id, dto);
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

    private List<User> getMockUsers()
    {
        return Arrays.asList(
                User.builder().email("abc@app.com").firstName("firstName").lastName("lastName").passwordDigest("password").status(UserStatus.ACTIVE).build(),
                User.builder().email("def@app.com").firstName("firstName").lastName("lastName").passwordDigest("password").status(UserStatus.DELETED).build(),
                User.builder().email("igl@app.com").firstName("firstName").lastName("lastName").passwordDigest("password").status(UserStatus.BLOCKED).build(),
                User.builder().email("lmn@app.com").firstName("firstName").lastName("lastName").passwordDigest("password").status(UserStatus.EXPIRED).build()
        );
    }

}