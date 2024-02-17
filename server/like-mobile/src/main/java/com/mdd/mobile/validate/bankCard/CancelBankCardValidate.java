package com.mdd.mobile.validate.bankCard;

import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("文章收藏参数")
public class CancelBankCardValidate implements Serializable {

    @NotNull(message = "UserId Not Null")
    @ApiModelProperty(value = "userId", required = true)
    private String userId;

    @NotNull(message = "Bank Card Id Not Null")
    @ApiModelProperty(value = "Bank Card Id", required = true)
    private String  bankCardId;


}
