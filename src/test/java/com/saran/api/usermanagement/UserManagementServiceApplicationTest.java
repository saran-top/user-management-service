package com.saran.api.usermanagement;

import com.saran.api.usermanagement.controller.RegistrationController;
import com.saran.api.usermanagement.controller.RoleController;
import com.saran.api.usermanagement.controller.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserManagementServiceApplication.class)
@ActiveProfiles("test")
public class UserManagementServiceApplicationTest
{

    @Autowired
    private UserController userController;

    @Autowired
    private RoleController roleController;

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void contextLoads() throws Exception
    {
        assertThat(userController).isNotNull();
        assertThat(registrationController).isNotNull();
        assertThat(roleController).isNotNull();
        assertThat(passwordEncoder).isNotNull();
    }

}