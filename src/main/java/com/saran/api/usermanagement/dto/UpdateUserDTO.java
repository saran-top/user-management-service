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
@ApiModel(value = "Update User")
public class UpdateUserDTO implements Serializable
{

    private static final long serialVersionUID = -7360062748218611071L;

    @NotNull(message = "First name can not be null")
    @ApiModelProperty(required = true, notes = "First name of a user")
    private String firstName;

    @NotNull(message = "Last name can not be null")
    @ApiModelProperty(required = true, notes = "Last name of a user")
    private String lastName;

    @ApiModelProperty(notes = "Description of a user")
    private String description;

    @NotNull(message = "Version can not be null")
    @ApiModelProperty(required = true, notes = "Version of a user to be updated", dataType = "Integer")
    private Integer version;

}
