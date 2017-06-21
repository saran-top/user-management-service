package com.saran.api.usermanagement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Getter
@ToString
@ApiModel(value = "Create Role")
public class NewRoleDTO implements Serializable
{

    private static final long serialVersionUID = -1074632610943548356L;

    @NotNull(message = "Name can not be null")
    @ApiModelProperty(required = true, value = "Unique name of a role")
    private String name;

    @ApiModelProperty(value = "Description of a role")
    private String description;

}
