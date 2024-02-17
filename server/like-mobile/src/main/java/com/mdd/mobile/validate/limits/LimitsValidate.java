package com.mdd.mobile.validate.limits;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@ApiModel("Limits Validate")
public class LimitsValidate implements Serializable {

    @NotNull(message = "UserId Not Null")
    @ApiModelProperty("UserId")
    private String userId;

    @NotNull(message = "User status Not Null")
    @ApiModelProperty("User status")
    private Integer verifyAccount;

}
