package com.mdd.mobile.validate.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("Withdraw Code Validate")
public class WithdrawCodeValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty("UUId")
    private String uuid;

    @NotNull
    @ApiModelProperty("Code")
    private String code;
}
