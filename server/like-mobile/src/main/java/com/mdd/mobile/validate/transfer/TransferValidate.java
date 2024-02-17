package com.mdd.mobile.validate.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Transfer Validate")
public class TransferValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty("currency type : 1:TRY 2:USD 3:EUR")
    private Integer currencyType;

    @NotNull
    @ApiModelProperty("Receiver Pay ID")
   private String toPayId;

    @NotNull
    @ApiModelProperty("Transfer Amount")
   private BigDecimal amount;

}
