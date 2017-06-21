package com.saran.api.usermanagement.service;

import com.saran.api.usermanagement.dto.NewRoleDTO;
import com.saran.api.usermanagement.exception.ResourceExistsException;
import com.saran.api.usermanagement.exception.ResourceNotFoundException;
import com.saran.api.usermanagement.model.Role;
import com.saran.api.usermanagement.model.User;
import com.saran.api.usermanagement.repository.RoleRepository;
import com.saran.api.usermanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class DefaultRoleService implements RoleService
{

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Role> findAll()
    {
        log.info("Fetching all role");
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Integer id) throws ResourceNotFoundException
    {
        log.info("Role to be searched for id is [{}]", id);
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Role not found for id %s", id)));
    }

    @Override
    public Role findByName(String name) throws ResourceNotFoundException
    {
        log.info("Role to be searched for name is [{}]", name);
        return roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(String.format("Role not found for name %s", name)));
    }

    @Override
    public Role create(NewRoleDTO dto) throws ResourceExistsException
    {
        log.info("Role to be created is [{}]", dto);
        // Check if role already exists
        roleRepository.findByName(dto.getName()).ifPresent(role ->
        {
            log.info("Role [{}] already exists", dto.getName());
            throw new ResourceExistsException(String.format("Role [%s] already exists", dto.getName()));
        });
        Role role = Role.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        roleRepository.saveAndFlush(role);
        log.info("Role [{}] created successfully", dto.getName());
        return role;
    }

    @Override
    public User mapRoles(Integer userId, List<Integer> roleIds)
    {
        log.info("Roles Ids [{}] to be mapped for used id [{}]", roleIds, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found for id %s", userId)));
        Set<Role> roles = new HashSet<>();
        roleIds.forEach(roleId -> roles.add(findById(roleId)));
        user.setRoles(roles);
        return userRepository.saveAndFlush(user);
    }

}
