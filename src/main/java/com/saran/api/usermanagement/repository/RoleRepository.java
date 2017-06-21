package com.saran.api.usermanagement.repository;

import com.saran.api.usermanagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>
{

    Optional<Role> findById(Integer id);

    Optional<Role> findByName(String email);

}
