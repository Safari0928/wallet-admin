package com.mdd.payadmin.validate.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@ApiModel("Cancel Withdraw Validate")
public class CancelValidate implements Serializable {

    @ApiModelProperty("Detail Id")
    private String detailId;

    @NotEmpty
    @ApiModelProperty("Failed description")
    private String description;
}
