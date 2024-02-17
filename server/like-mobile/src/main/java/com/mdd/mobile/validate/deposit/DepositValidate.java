package com.mdd.mobile.validate.deposit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Deposit Validate")
public class DepositValidate implements Serializable {

    @NotNull(message = "UserId Not Null")
    @ApiModelProperty("UserId")
    private String userId;

    @NotNull(message = "Bank Card Id Not Null")
    @ApiModelProperty("Bank Card Id")
    private String bankCardId;

    @NotNull(message = "Amount Not Null")
    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @NotNull(message = "PayId Not Null")
    @ApiModelProperty("PayId")
    private String payId;

}
