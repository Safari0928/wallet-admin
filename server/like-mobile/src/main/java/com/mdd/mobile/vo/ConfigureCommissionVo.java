package com.mdd.mobile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Configure Update Vo")
public class ConfigureCommissionVo implements Serializable {

    @NotNull(message = "Amount Not Null")
    @ApiModelProperty("Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Commision")
    private Double payCommission ;

    @ApiModelProperty("Channel Commission")
    private Double channelCommission;

}
