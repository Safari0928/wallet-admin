package com.mdd.mobile.validate.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("Verify Account")
public class VerifyAccountValidate {

    @NotNull
    @ApiModelProperty("user Id")
    private String userId;

    @NotNull
    @ApiModelProperty(value="Identity No",required = true)
    private String identityNo;

    @NotNull
    @ApiModelProperty(value="BirthDate",required = true)
    private String birthDate;

    @NotNull
    @ApiModelProperty(value="Real Name",required = true)
    private String realName;

    @ApiModelProperty("Image Identity Front")
    private String imageIdentityFront;

    @ApiModelProperty("Image Identity Back")
    private String imageIdentityBack;


}
