package com.mdd.common.entity.payBankAccounts;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("Bank Account Entity ")
public class PayBankAccount {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("Id")
    private Integer id;

    @ApiModelProperty("uuid")
    private String uuid;

    @ApiModelProperty("Iban")
    private String iban;

    @ApiModelProperty("User Id")
    private String  userId;

    @ApiModelProperty("Card Name")
    private String bankCardName;

    @ApiModelProperty("Card Type")
    private Integer cardType;

    @ApiModelProperty("Creation Time")
    private String createTime;

    @ApiModelProperty("Delete Time")
    private String deleteTime;

    @ApiModelProperty("Currency Id")
    private String currencyId;

    @ApiModelProperty("Pay Id")
    private String accountId;

    @ApiModelProperty("Is this bank account deleted? -1:Deleted, 1:Not Deleted")
    private Integer isDelete;

    @ApiModelProperty("Balance")
    private BigDecimal total;

}
