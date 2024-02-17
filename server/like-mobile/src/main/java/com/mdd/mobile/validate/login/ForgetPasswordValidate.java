package com.mdd.mobile.validate.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ForgetPasswordValidate implements Serializable {

    @NotNull(message = "Missing phone number parameters")
    @NotEmpty(message = "Mobile phone number cannot be empty")
    @Length(min = 10, max = 11, message = "Wrong phone number")
    @ApiModelProperty(value = "Mobile Telephone Number", required = true)
    private String phone;

    @NotNull(message = "Missing password parameters")
    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 6, message ="Password must be greater than 6 characters")
    @ApiModelProperty(value = "Password", required = true)
    private String newPassword;


}
