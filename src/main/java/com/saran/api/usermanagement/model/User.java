package com.saran.api.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString(exclude = "passwordDigest")
@Table(name = "USER")
@ApiModel(value = "User")
public class User implements Serializable
{

    private static final long serialVersionUID = -1298911440354098707L;

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @ApiModelProperty(required = true, value = "Generated ID of a user", dataType = "Integer")
    private Integer id;

    @Setter
    @Column(name = "EMAIL", length = 64, nullable = false, updatable = false, unique = true)
    @ApiModelProperty(required = true, value = "Unique email of a user")
    private String email;

    @JsonIgnore
    @Setter
    @Column(name = "PASSWORD_DIGEST", length = 128, nullable = false)
    @ApiModelProperty(hidden = true)
    private String passwordDigest;

    @Setter
    @Column(name = "FIRST_NAME", length = 64, nullable = false)
    @ApiModelProperty(required = true, value = "First name of a user")
    private String firstName;

    @Setter
    @Column(name = "LAST_NAME", length = 64, nullable = false)
    @ApiModelProperty(required = true, value = "Last name of a user")
    private String lastName;

    @Setter
    @Column(name = "DESCRIPTION", length = 1024)
    @ApiModelProperty(value = "Description of a user")
    private String description;

    @Setter
    @Column(name = "STATUS", length = 32, nullable = false)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(required = true, value = "Status of a user")
    private UserStatus status;

    @Column(name = "CREATED_AT")
    @ApiModelProperty(required = true, value = "Created date of a user", dataType = "LocalDateTime")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    @ApiModelProperty(value = "Last updated date of a user", dataType = "LocalDateTime")
    private LocalDateTime updatedAt;

    @Setter
    @Version
    @Column(name = "VERSION")
    @ApiModelProperty(required = true, value = "Version of a user", dataType = "Integer")
    private Integer version;

    @Setter
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    @ApiModelProperty(value = "List of roles of a user", dataType = "List")
    private Set<Role> roles;

    @PrePersist
    public void prePersist()
    {
        createdAt = LocalDateTime.now();
        if (Objects.isNull(this.status))
        {
            this.status = UserStatus.NEW;
        }
    }

    @PreUpdate
    public void preUpdate()
    {
        updatedAt = LocalDateTime.now();
    }

}
