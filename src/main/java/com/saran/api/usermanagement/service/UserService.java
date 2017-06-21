package com.saran.api.usermanagement.service;

import com.saran.api.usermanagement.dto.*;
import com.saran.api.usermanagement.exception.ResourceExistsException;
import com.saran.api.usermanagement.exception.ResourceNotFoundException;
import com.saran.api.usermanagement.model.User;
import com.saran.api.usermanagement.model.UserStatus;

import java.util.List;

public interface UserService
{

    List<User> findAll();

    List<User> findByStatues(List<UserStatus> userStatuses);

    User findByEmail(String email) throws ResourceNotFoundException;

    User findById(Integer id) throws ResourceNotFoundException;

    User create(NewUserDTO dto) throws ResourceExistsException;

    User update(Integer id, UpdateUserDTO dto) throws ResourceNotFoundException;

    User updatePassword(Integer id, UpdateUserPasswordDTO dto) throws ResourceNotFoundException;

    User updateStatus(Integer id, UpdateUserStatusDTO dto) throws ResourceNotFoundException;

}
