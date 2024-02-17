package com.mdd.payadmin.validate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel("change profile validate")
public class ProfileValidate {

    @ApiModelProperty("nickname")
    private String nickname;

    @ApiModelProperty("username")
    private String username;

    @ApiModelProperty("password")
    private String  oldPassword;

    @Length(min = 6, max = 50, message = "账号或密码错误")
    @ApiModelProperty("password")
    private String  newPassword;
}
