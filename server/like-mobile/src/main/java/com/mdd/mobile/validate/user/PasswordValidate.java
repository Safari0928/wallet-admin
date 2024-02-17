package com.mdd.mobile.validate.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@ApiModel("Password Parameter")
public class PasswordValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "Old Password", required = true)
    private String oldPassword;

    @NotNull
    @ApiModelProperty(value = "New Password", required = true)
    @Size(min = 7, message = "Password must be greater than 6 characters")
    private String newPassword;

    @NotNull
    @ApiModelProperty(value = "New Password Again", required = true)
    @Size(min = 7, message = "Password must be greater than 6 characters")
    private String newPasswordAgain;
}
