package com.mdd.payadmin.vo.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Chart Panel Vo")
public class ChartVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Turkish Users")
    private String turkishUsers;

    @ApiModelProperty("Chinese Users")
    private String chineseUsers;

    @ApiModelProperty("Other Users")
    private String otherUsers;

}
