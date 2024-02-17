package com.mdd.mobile.validate.limits;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Update Limits Validate")
public class UpdateLimitsValidate implements Serializable {

    @NotNull(message = "UserId Not Null")
    @ApiModelProperty("UserId")
    private String userId;

    @NotNull(message = "Type Detail Not Null")
    @ApiModelProperty("Transfer limit  type")
    private Integer typeDetail;

    @NotNull(message = "Amount Not Null")
    @ApiModelProperty("Amount")
    private BigDecimal amount;

}
