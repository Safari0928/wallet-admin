package com.mdd.mobile.validate.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChangePhoneValidate {
    @ApiModelProperty(value = "code", required = true,notes = "This code is the code sent to the mail ")
    private String code;

    @NotNull(message = "Missing phone number parameters")
    @NotEmpty(message = "Mobile phone number cannot be empty")
    @Length(min = 10, max = 11, message = "Wrong phone number")
    @ApiModelProperty(value = "Mobile Telephone Number", required = true)
    private String phone;

    @NotNull(message = "Missing phone number parameters")
    @NotEmpty(message = "Mobile phone number cannot be empty")
    @Length(min = 10, max = 11, message = "Wrong phone number")
    @ApiModelProperty(value = "Mobile Telephone Number", required = true)
    private String newPhone;
}
