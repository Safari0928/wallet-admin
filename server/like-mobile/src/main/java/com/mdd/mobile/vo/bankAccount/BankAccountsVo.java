package com.mdd.mobile.vo.bankAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "Bank Accounts Vo")
public class BankAccountsVo implements Serializable {

    @ApiModelProperty("Card Name")
    private String bankCardName;

    @ApiModelProperty("Card Type")
    private String cardType;

    @ApiModelProperty("Bank Name")
    private String bankName;

    @ApiModelProperty("Iban")
    private String iban;

}
