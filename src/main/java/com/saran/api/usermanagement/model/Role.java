package com.saran.api.usermanagement.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "ROLE")
@ApiModel(value = "Role")
public class Role implements Serializable
{

    private static final long serialVersionUID = 5832732566903068282L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @ApiModelProperty(required = true, value = "Generated ID of a role", dataType = "Integer")
    private Integer id;

    @Column(name = "NAME", nullable = false, length = 64, unique = true)
    @ApiModelProperty(required = true, value = "Unique name of a role")
    private String name;

    @Setter
    @Column(name = "DESCRIPTION", length = 1024)
    @ApiModelProperty(value = "Description of a role")
    private String description;

    @Column(name = "CREATED_AT")
    @ApiModelProperty(required = true, value = "Created timestamp of a role", dataType = "LocalDateTime")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    @ApiModelProperty(value = "Last updated timestamp of a role", dataType = "LocalDateTime")
    private LocalDateTime updatedAt;

    @Setter
    @Version
    @Column(name = "VERSION")
    @ApiModelProperty(required = true, value = "Version of a role", dataType = "Integer")
    private Integer version;

    @PrePersist
    public void prePersist()
    {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate()
    {
        updatedAt = LocalDateTime.now();
    }

}