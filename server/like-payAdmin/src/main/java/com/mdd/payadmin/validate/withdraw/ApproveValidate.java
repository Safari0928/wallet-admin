package com.mdd.payadmin.validate.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Approve Withdraw Validate")
public class ApproveValidate implements Serializable {

    @ApiModelProperty("Detail Id")
    private String detailId;

}
