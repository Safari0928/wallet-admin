package com.mdd.mobile.vo.payLogin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayLoginVo {


    @ApiModelProperty(value = "UUID")
    private String uuid;

    @ApiModelProperty(value = "Account Number")
    private String accountNumber;

    @ApiModelProperty(value = "Token", notes="login token")
    private String token;

    @ApiModelProperty(value = "status", notes="User Status")
    private Integer status;
}
