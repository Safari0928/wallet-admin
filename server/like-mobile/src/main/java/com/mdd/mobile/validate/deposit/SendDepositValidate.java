package com.mdd.mobile.validate.deposit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SendDepositValidate implements Serializable {

    @NotNull(message = "deposit Id Not Null")
    @ApiModelProperty("deposit Id")
    private String bankCardId;

    @NotNull(message = "Amount  Not Null")
    @ApiModelProperty("Amount")
    private BigDecimal amount;

}
