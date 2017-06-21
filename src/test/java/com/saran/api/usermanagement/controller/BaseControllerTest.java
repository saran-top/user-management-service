package com.saran.api.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saran.api.usermanagement.UserManagementServiceApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserManagementServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseControllerTest
{

    @Autowired
    protected MockMvc mockMvc;

    protected static String toJson(final Object obj)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }
}
