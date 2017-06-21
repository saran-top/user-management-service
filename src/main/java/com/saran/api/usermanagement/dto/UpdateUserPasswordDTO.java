package com.saran.api.usermanagement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Getter
@ToString
@ApiModel(value = "Update User Password")
public class UpdateUserPasswordDTO implements Serializable
{

    private static final long serialVersionUID = 5627417806187474948L;

    @NotNull(message = "Password can not be null")
    @Length(min = 6, max = 12, message = "Password should of minimum 6 & maximum 12 characters")
    @ApiModelProperty(required = true, notes = "Password of a user, should be minimum 6 & maximum 12 characters")
    private String password;

    @NotNull(message = "Version can not be null")
    @ApiModelProperty(required = true, notes = "Version of a user to be updated", dataType = "Integer")
    private Integer version;

}
