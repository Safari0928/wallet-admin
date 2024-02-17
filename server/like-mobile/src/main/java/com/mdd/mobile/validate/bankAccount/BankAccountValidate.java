package com.mdd.mobile.validate.bankAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@ApiModel("Bank Account")
public class BankAccountValidate implements Serializable {

    @NotNull(message = "userID Not Null")
    @ApiModelProperty("iban")
    @Size(min = 24, max = 24, message = "IBAN must be exactly 24 characters")
    private String iban;

    @ApiModelProperty("Optional Bank Card Name")
    private String bankCardName;

    @ApiModelProperty("Card Type")
    @NotNull
    private Integer cardType;

    @ApiModelProperty("Verify Code")
    @NotNull
    private String code;

}
