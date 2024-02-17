package com.mdd.mobile.validate.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("Mail Validate")
public class MailValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "Old Mail", required = true)
    private String oldMail;

    @NotNull
    @ApiModelProperty(value = "New Mail",required = true)
    private String newMail;

    @NotNull
    @ApiModelProperty(value = "Verification Code",required = true)
    private String code;
}
