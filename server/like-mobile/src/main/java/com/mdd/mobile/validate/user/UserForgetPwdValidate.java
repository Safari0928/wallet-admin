package com.mdd.mobile.validate.user;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserForgetPwdValidate {

    @NotNull(message = "Missing phone number parameters")
    @NotEmpty(message = "Mobile phone number cannot be empty")
    @Length(min = 10, max = 11, message = "Wrong phone number")
    @ApiModelProperty(value = "Mobile Telephone Number", required = true)
    private String phone;

    @ApiModelProperty(value = "code", required = true,notes = "This code is the code sent to the mail ")
    private String code;

    @NotNull(message = "Missing password parameters")
    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 6, message ="Password must be greater than 6 characters")
    @ApiModelProperty(value = "Password", required = true)
    private String password;
}
