package com.mdd.payadmin.validate.configure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Update Deposit Configure Validate")
public class UpdateDepositConfigureValidate implements Serializable {

    @NotNull(message = "Uuid Not Null")
    @ApiModelProperty("Admin Password")
    private String  uuid;

    @ApiModelProperty("Pay Commission")
    private Double payCommission;

    @ApiModelProperty("Unverified User Fee")
    private BigDecimal unverifiedUserFee;

    @ApiModelProperty("Verified User Fee ")
    private BigDecimal verifiedUserFee;

    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 6, max = 50, message = "password can not be shorter then 6 digit")
    private String  password;

}
