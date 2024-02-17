package com.mdd.payadmin.validate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
@Data
@ApiModel("Login Validate")
public class SystemLoginValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "账号不能为空")
    @Length(min = 2, max = 20, message = "账号或密码错误")
    @ApiModelProperty(value = "登录账号", required = true)
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 50, message = "账号或密码错误")
    @ApiModelProperty(value = "登录密码", required = true)
    private String password;
}
