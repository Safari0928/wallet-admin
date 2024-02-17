package com.mdd.payadmin.validate.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("Currency Validate")
public class CurrencyValidate implements Serializable {

    @NotNull
    @ApiModelProperty("Currency Uuid")
    private String uuid;

    @NotNull
    @ApiModelProperty("Currency Name")
    private String currencyName;

    @NotNull
    @ApiModelProperty("Currency Code")
    private String currencyCode;

    @NotNull
    @ApiModelProperty("Currency Symbol")
    private String currencySymbol;

    @NotNull
    @ApiModelProperty("Operate")
    private Integer operate;

    @NotNull
    @ApiModelProperty("Image")
    private String image;

    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 6, max = 50, message = "password can not be shorter then 6 digit")
    @ApiModelProperty(value = "login password", required = true)
    private String password;
}
