package com.mdd.payadmin.validate.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Transfer Search Validate")
public class TransferSearchValidate {

    @ApiModelProperty("Transact Number")
    private String transactionNumber;

    @ApiModelProperty("PayId")
    private String payId;

    @ApiModelProperty("Transfer Status:[-1:Fail 1:Success]]")
    private Integer status;

    @ApiModelProperty("Transfer Type:[1:Deposit  2:Withdraw  3:Transfer-Deposit  4:Transfer-Withdraw]")
    private Integer typeDetail;

    @ApiModelProperty("Currency Type::[1=TRY 2=USD 3=EUR  Default=1")
    private Integer currencyType;

    private String userId;
}
