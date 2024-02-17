package com.mdd.payadmin.validate.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
 @Data
 @ApiModel("User List Validate")
public class UserListValidate implements Serializable {

    @ApiModelProperty("Airpay id")
    private String payId;

    @ApiModelProperty(value="Status",notes="-1=Disabled 1=Normal")
    private Integer status;

    @ApiModelProperty(value="Verify Account ", notes="-1=Unverified 1=Verified 2=Pending  Default=-1")
    private Integer verifyAccount;

    @ApiModelProperty("Phone Number")
    private String phoneNumber;

    @ApiModelProperty("Country")
    private String country;

    @ApiModelProperty("Email")
    private String email;

}
