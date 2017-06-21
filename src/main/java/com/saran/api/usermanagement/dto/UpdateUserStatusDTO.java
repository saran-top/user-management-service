package com.saran.api.usermanagement.dto;

import com.saran.api.usermanagement.model.UserStatus;
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
@ApiModel(value = "Update User Status")
public class UpdateUserStatusDTO implements Serializable
{

    private static final long serialVersionUID = 5627417806187474948L;

    @NotNull(message = "Status can not be null")
    @ApiModelProperty(required = true, notes = "Status of a user")
    private UserStatus userStatus;

    @NotNull(message = "Version can not be null")
    @ApiModelProperty(required = true, notes = "Version of a user to be updated", dataType = "Integer")
    private Integer version;

}
