package com.saran.api.usermanagement.service;

import com.saran.api.usermanagement.dto.NewUserDTO;
import com.saran.api.usermanagement.dto.UpdateUserDTO;
import com.saran.api.usermanagement.dto.UpdateUserPasswordDTO;
import com.saran.api.usermanagement.dto.UpdateUserStatusDTO;
import com.saran.api.usermanagement.exception.ResourceExistsException;
import com.saran.api.usermanagement.exception.ResourceNotFoundException;
import com.saran.api.usermanagement.model.User;
import com.saran.api.usermanagement.model.UserStatus;
import com.saran.api.usermanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DefaultUserService implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll()
    {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public User findById(final Integer id) throws ResourceNotFoundException
    {
        log.info("User to be searched for id is [{}]", id);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found for id %s", id)));
    }

    @Override
    public List<User> findByStatues(List<UserStatus> userStatuses)
    {
        log.info("Users to be searched for statuses are [{}]", userStatuses);
        return userRepository.findByStatuses(userStatuses);
    }

    @Override
    public User findByEmail(final String email) throws ResourceNotFoundException
    {
        log.info("User to be searched for email is [{}]", email);
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found for email %s", email)));
    }

    @Override
    @Transactional
    public User create(final NewUserDTO dto) throws ResourceExistsException
    {
        log.info("User to be created is [{}]", dto);
        // Check if user already exists
        userRepository.findByEmail(dto.getEmail()).ifPresent(user ->
        {
            log.info("User [{}] already exists", dto.getEmail());
            throw new ResourceExistsException(String.format("User [%s] already exists", dto.getEmail()));
        });
        // Encrypt password and save user
        User user = User.builder()
                .email(dto.getEmail())
                .passwordDigest(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
        userRepository.saveAndFlush(user);
        log.info("User [{}] created successfully", dto.getEmail());
        return user;
    }

    @Override
    public User update(final Integer id, final UpdateUserDTO dto) throws ResourceNotFoundException
    {
        log.info("User id to be updated is [{}]", id);
        log.info("User details is [{}]", dto);
        User retrievedUser = findById(id);
        User updatableUser = User.builder()
                .id(id)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .description(dto.getDescription())
                .passwordDigest(retrievedUser.getPasswordDigest())
                .status(retrievedUser.getStatus())
                .createdAt(retrievedUser.getCreatedAt())
                .version(dto.getVersion())
                .build();
        return userRepository.saveAndFlush(updatableUser);
    }

    @Override
    public User updatePassword(final Integer id, final UpdateUserPasswordDTO dto) throws ResourceNotFoundException
    {
        log.info("Update password for user id is [{}]", id);
        log.info("User password details is [{}]", dto);
        User retrievedUser = findById(id);
        User updatableUser = User.builder()
                .id(id)
                .firstName(retrievedUser.getFirstName())
                .lastName(retrievedUser.getLastName())
                .description(retrievedUser.getDescription())
                .passwordDigest(dto.getPassword())
                .status(retrievedUser.getStatus())
                .createdAt(retrievedUser.getCreatedAt())
                .version(dto.getVersion())
                .status(retrievedUser.getStatus())
                .build();
        return userRepository.save(updatableUser);
    }

    @Override
    public User updateStatus(final Integer id, final UpdateUserStatusDTO dto) throws ResourceNotFoundException
    {
        log.info("Update status for user id is [{}]", id);
        log.info("User status details is [{}]", dto);
        User retrievedUser = findById(id);
        User updatableUser = User.builder()
                .id(id)
                .firstName(retrievedUser.getFirstName())
                .lastName(retrievedUser.getLastName())
                .description(retrievedUser.getDescription())
                .passwordDigest(retrievedUser.getPasswordDigest())
                .createdAt(retrievedUser.getCreatedAt())
                .version(dto.getVersion())
                .status(dto.getUserStatus())
                .build();
        return userRepository.saveAndFlush(updatableUser);
    }

}
