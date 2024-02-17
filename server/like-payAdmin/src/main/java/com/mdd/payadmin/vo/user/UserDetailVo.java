package com.mdd.payadmin.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@ApiModel("User Detail Vo")
public class UserDetailVo implements Serializable {

    @ApiModelProperty("Avatar")
    private String avatar;

    @ApiModelProperty("Pay Id")
    private String payId;

    @ApiModelProperty("Country")
    private String country;

    @ApiModelProperty("UUID")
    private String uuid;

    @ApiModelProperty("Phone Number")
    private String phoneNumber;

    @ApiModelProperty(value="Verify Account ", notes="-1=Unverified 1=Verified 2=Pending  Default=-1")
    private Integer verifyAccount;

    @ApiModelProperty(value = "Verify Account",notes = "-1=Unverified 1=Verified 2=Pending  Default=-1")
    private String verifyAccountMSG;

    @ApiModelProperty(value="Status",notes="-1=Disabled 1=Normal")
    private Integer status;

    @ApiModelProperty(value = "Status",notes = "-1=Disabled 1=Normal")
    private String statusMSG;

    @ApiModelProperty("Nick Name")
    private String nickName;

    @ApiModelProperty("Email")
    private String email;

    @ApiModelProperty(value="Currency Type", notes="1=TRY")
    private String currencyTRY;

    @ApiModelProperty(value="Currency Type", notes=" 2=USD")
    private String currencyUSD;

    @ApiModelProperty(value="Currency Type", notes="3=EUR")
    private String currencyEUR;

    @ApiModelProperty(value = "Balance TRY")
    private BigDecimal balanceForTry;

    @ApiModelProperty(value = "Balance USD")
    private BigDecimal balanceForUsd;

    @ApiModelProperty(value = "Balance EUR")
    private BigDecimal balanceForEur;

    @ApiModelProperty("Creation Time")
    private String createTime;
}
