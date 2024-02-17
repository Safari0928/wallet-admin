package com.mdd.payadmin.validate.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Update Withdraw Aipay Handling Fee")
public class UpdateWithdrawConfigureValidate implements Serializable {

    @NotNull(message = "Uuid Not Null")
    @ApiModelProperty("Uuid")
    private String  uuid;

    @ApiModelProperty("Commission")
    private Double payCommission ;

    @ApiModelProperty("Unverified User Fee")
    private BigDecimal unverifiedUserFee;

    @ApiModelProperty("Verified User Fee ")
    private BigDecimal verifiedUserFee;

    @ApiModelProperty("Amount Audited")
    private BigDecimal amountAudited;

    @NotNull
    @ApiModelProperty("Admin Password")
    private String  password;
}
