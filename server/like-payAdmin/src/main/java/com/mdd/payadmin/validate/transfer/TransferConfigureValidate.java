package com.mdd.payadmin.validate.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
@ApiModel("Update Transfer Configure Validate")
public class TransferConfigureValidate {

    @NotEmpty(message = "edit uuid cant be empty")
    @ApiModelProperty(value = "uuid", required = true)
    private String editUuid;

    @ApiModelProperty("Commission")
    private Double payCommission ;

    @ApiModelProperty("Unverified User Fee")
    private BigDecimal unverifiedUserFee;

    @ApiModelProperty("Verified User Fee ")
    private BigDecimal verifiedUserFee;

    @ApiModelProperty("enable/disable Transfer limit")
    private Integer currentStatus;

    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 6, max = 50, message = "password can not be shorter then 6 digit")
    private String  password;
}
