package com.mdd.mobile.validate.bankAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@ApiModel("Iban parameter")
public class IbanValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "Iban", required = true)
    @Size(min = 24, max = 24, message = "IBAN must be exactly 24 characters")
    private String iban;

}
