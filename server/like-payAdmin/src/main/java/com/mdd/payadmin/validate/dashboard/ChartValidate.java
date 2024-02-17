package com.mdd.payadmin.validate.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Chart Validate")
public class ChartValidate implements Serializable {

    @ApiModelProperty("User's Type")
    private Integer type;
}
