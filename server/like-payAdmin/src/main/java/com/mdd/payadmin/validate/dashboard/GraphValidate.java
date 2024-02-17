package com.mdd.payadmin.validate.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Graph Validate")
public class GraphValidate implements Serializable {

    @ApiModelProperty("Transaction Type")
    private Integer type;

    @ApiModelProperty("Start Date")
    private String startDate;

    @ApiModelProperty("End Date")
    private String endDate;
}
