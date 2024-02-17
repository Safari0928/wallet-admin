package com.mdd.payadmin.validate.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("USER STATUS VALİDATE")
public class UserStatusValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("UserStatus ")
    private Integer status;

    @ApiModelProperty("StatusOrVerifyUpdateType")
    private Integer type;

    @ApiModelProperty("verified UUId")
    private String userId ;

}
