package com.mdd.mobile.vo.bankAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "All Bank Accounts Vo")
public class AllBankAccountsVo implements Serializable {

    @ApiModelProperty("Card Name")
    private String bankCardName;

    @ApiModelProperty("Bank Name")
    private String bankName;

    @ApiModelProperty("Iban")
    private String iban;
}
