package com.mdd.mobile.validate.deposit;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DepositCompleteValidate implements Serializable {

    @NotNull(message = "deposit Id Not Null")
    @ApiModelProperty("deposit Id")
    private String depositId;

    @NotNull(message = "code Id Not Null")
    @ApiModelProperty("code")
    private String code;
}
