package com.mdd.mobile.validate.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@ApiModel("Nick Name Parameter")
public class NickNameValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "Nick Name", required = true)
    @Size(max = 14, message = "Nick name can contain a maximum of 14 characters")
    private String nickName;
}
