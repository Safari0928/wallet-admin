package com.mdd.common.entity.payBankCard;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("Bank card")
public class PayBankCards {

    @TableId(value="id", type= IdType.AUTO)
    @ApiModelProperty(value = "Card ID")
    private Integer id;

    @ApiModelProperty(value = "Card UUID")
    private String  uuid;

    @ApiModelProperty(value = "User ID")
    private String  userId;

    @ApiModelProperty(value = "Card Number")
    private String cardNumber;

    @ApiModelProperty(value = "Expiry Date")
    private String expiryDate;

    @ApiModelProperty(value = "CVV Number")
    private String cvv;

    @ApiModelProperty(value = "Full Name on Card")
    private String fullName;

    @ApiModelProperty(value = "Bank Name")
    private String bankName;

    @ApiModelProperty(value = "Card Type")
    private String cardType;

    @ApiModelProperty(value = "Is delete: -1: Yes ,1:No")
    private Integer isDelete;

    @ApiModelProperty(value = "Creation Time")
    private String createTime;

    @ApiModelProperty(value = "Update Time")
    private String updateTime;

}
