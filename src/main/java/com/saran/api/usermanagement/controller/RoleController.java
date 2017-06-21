package com.saran.api.usermanagement.controller;

import com.saran.api.usermanagement.dto.NewRoleDTO;
import com.saran.api.usermanagement.model.Role;
import com.saran.api.usermanagement.service.RoleService;
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
@RequestMapping("/roles")
@Api(tags = {"Role"}, consumes = "application/json", produces = "application/json")
public class RoleController
{

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "List all the roles", produces = "application/json", response = Role.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Role> findAll()
    {
        log.info("Find all the roles");
        return roleService.findAll();
    }

    @ApiOperation(value = "Find a role by Id", produces = "application/json", response = Role.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            }
    )
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Role findById(@PathVariable Integer id)
    {
        log.info("Id to be searched is [{}]", id);
        return roleService.findById(id);
    }

    @ApiOperation(value = "Create a new role", consumes = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Role created successfully"),
                    @ApiResponse(code = 400, message = "Validation errors"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            }
    )
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody NewRoleDTO role)
    {
        log.info("Role to be updated is [{}]", role);
        roleService.create(role);
    }

}
