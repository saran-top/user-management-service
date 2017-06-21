package com.saran.api.usermanagement.controller;

import com.saran.api.usermanagement.dto.NewUserDTO;
import com.saran.api.usermanagement.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/register")
@Api(tags = {"Registration"}, consumes = "application/json")
public class RegistrationController
{
    @Autowired
    private UserService userService;

    @ApiOperation(value = "Register a new user", consumes = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Registration successful"),
                    @ApiResponse(code = 400, message = "Validation errors"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            }
    )
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody NewUserDTO user)
    {
        log.info("User to be created is [{}]", user);
        userService.create(user);
    }

}
