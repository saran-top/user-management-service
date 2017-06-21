package com.saran.api.usermanagement.repository;

import com.saran.api.usermanagement.model.User;
import com.saran.api.usermanagement.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.status in :statuses")
    List<User> findByStatuses(@Param("statuses") List<UserStatus> statuses);
}
