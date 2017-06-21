package com.saran.api.usermanagement.controller;

import com.saran.api.usermanagement.dto.UpdateUserDTO;
import com.saran.api.usermanagement.dto.UpdateUserPasswordDTO;
import com.saran.api.usermanagement.dto.UpdateUserStatusDTO;
import com.saran.api.usermanagement.model.User;
import com.saran.api.usermanagement.service.RoleService;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Api(tags = {"User"}, consumes = "application/json", produces = "application/json")
public class UserController
{

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "List all the users", produces = "application/json", response = User.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll()
    {
        log.info("Find all the users");
        return userService.findAll();
    }

    @ApiOperation(value = "Find a user by Id", produces = "application/json", response = User.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User findById(@PathVariable Integer id)
    {
        log.info("Id to be searched is [{}]", id);
        return userService.findById(id);
    }

    @ApiOperation(value = "Update a user profile", consumes = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "User profile updated successfully"),
                    @ApiResponse(code = 400, message = "Validation errors"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 409, message = "Stale update")
            }
    )
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @Valid @RequestBody UpdateUserDTO dto)
    {
        log.info("Id to be updated is [{}]", id);
        log.info("User details to be updated is [{}]", dto);
        userService.update(id, dto);
    }

    @ApiOperation(value = "Update a user password", consumes = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "User password updated successfully"),
                    @ApiResponse(code = 400, message = "Validation errors"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 409, message = "Stale update")
            }
    )
    @RequestMapping(value = "/{id}/password", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable Integer id, @Valid @RequestBody UpdateUserPasswordDTO dto)
    {
        log.info("Id to be updated is [{}]", id);
        userService.updatePassword(id, dto);
    }

    @ApiOperation(value = "Update a user status", consumes = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "User status updated successfully"),
                    @ApiResponse(code = 400, message = "Validation errors"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 409, message = "Stale update")
            }
    )
    @RequestMapping(value = "/{id}/status", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id, @Valid @RequestBody UpdateUserStatusDTO dto)
    {
        log.info("Id to be updated is [{}]", id);
        userService.updateStatus(id, dto);
    }

    @ApiOperation(value = "Add or remove role(s) from a user", consumes = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "User/Role mapping done successfully"),
                    @ApiResponse(code = 400, message = "Validation errors"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 409, message = "Stale update")
            }
    )
    @RequestMapping(value = "/{id}/roles", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void mapRoles(@PathVariable Integer id, @Valid @RequestBody List<Integer> roleIds)
    {
        log.info("User Id to be updated is [{}]", id);
        log.info("Role Ids to be mapped is [{}]", roleIds);
        roleService.mapRoles(id, roleIds);
    }

}
