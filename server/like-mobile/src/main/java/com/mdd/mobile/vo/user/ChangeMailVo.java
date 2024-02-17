package com.mdd.mobile.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChangeMailVo implements Serializable {

    @ApiModelProperty("Status")
    private String status;

    @ApiModelProperty("New Mail")
    private String newMail;

}
