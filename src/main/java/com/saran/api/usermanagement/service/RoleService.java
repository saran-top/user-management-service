package com.saran.api.usermanagement.service;

import com.saran.api.usermanagement.dto.NewRoleDTO;
import com.saran.api.usermanagement.exception.ResourceExistsException;
import com.saran.api.usermanagement.exception.ResourceNotFoundException;
import com.saran.api.usermanagement.model.Role;
import com.saran.api.usermanagement.model.User;

import java.util.List;

public interface RoleService
{

    List<Role> findAll();

    Role findById(Integer id) throws ResourceNotFoundException;

    Role findByName(String name) throws ResourceNotFoundException;

    Role create(NewRoleDTO dto) throws ResourceExistsException;

    User mapRoles(Integer userId, List<Integer> roleIds);

}
