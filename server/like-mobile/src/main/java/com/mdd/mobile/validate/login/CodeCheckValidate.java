package com.mdd.mobile.validate.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CodeCheckValidate implements Serializable {
    @ApiModelProperty(value = "code", required = true,notes = "This code is the code sent to the mail ")
    private String code;

    @ApiModelProperty(value = "Check value", required = true)
    private String value;

}
