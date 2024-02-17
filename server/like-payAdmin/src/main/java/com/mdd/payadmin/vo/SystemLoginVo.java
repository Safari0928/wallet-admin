package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Login Vo")
public class SystemLoginVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "管理员ID")
    private String uuid;

    @ApiModelProperty(value = "登录令牌")
    private String token;
}
