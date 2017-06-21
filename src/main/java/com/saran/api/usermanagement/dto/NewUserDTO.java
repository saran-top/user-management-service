package com.saran.api.usermanagement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Getter
@ToString
@ApiModel(value = "Create User")
public class NewUserDTO implements Serializable
{

    private static final long serialVersionUID = -7360062748218611071L;

    @NotNull(message = "Email can not be null")
    @Email(message = "Invalid email")
    @ApiModelProperty(required = true, value = "Unique email id of a user")
    private String email;

    @NotNull(message = "First name can not be null")
    @ApiModelProperty(required = true, notes = "First name of a user")
    private String firstName;

    @NotNull(message = "Last name can not be null")
    @ApiModelProperty(required = true, notes = "Last name of a user")
    private String lastName;

    @NotNull(message = "Password can not be null")
    @Length(min = 6, max = 12, message = "Password should of minimum 6 & maximum 12 characters")
    @ApiModelProperty(required = true, notes = "Password of a user, should be minimum 6 & maximum 12 characters")
    private String password;

}
