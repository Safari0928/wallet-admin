package com.mdd.mobile.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

@Data
@ApiModel(value = "Bank Card Vo")
public class BankCardVo implements Serializable {

    @ApiModelProperty(value = "Deposit uuid")
    private String  uuid;

    @ApiModelProperty(value = "User uuid")
    private String userId;

    @ApiModelProperty("fullName ")
    private String  fullName;

    @ApiModelProperty("fullName ")
    private String  bankType;

    @ApiModelProperty("Bank Name ")
    private String  bankName;

    @ApiModelProperty("Bank Name ")
    private String  cartNumber;

    @ApiModelProperty(value = "Create Time")
    private String createTime;

}
