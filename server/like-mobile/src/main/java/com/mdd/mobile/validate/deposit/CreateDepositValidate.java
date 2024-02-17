package com.mdd.mobile.validate.deposit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Deposit Validate")
public class CreateDepositValidate implements Serializable {

    @Length(min = 0, max = 16, message = "Wrong Card number")
    @NotNull(message = "Card Number Not Null")
    @ApiModelProperty(value = "Card Number")
    private String cardNumber;

    @NotNull(message = "Expiry Date Not Null")
    @ApiModelProperty(value = "Expiry Date")
    private String expiryDate;

    @Length(min = 0, max = 3, message = "Wrong phone number")
    @ApiModelProperty(value = "CVV Number")
    private String cvv;

    @ApiModelProperty(value = "Full Name on Card")
    private String fullName;

    @NotNull(message = "Bank Name Not Null")
    @ApiModelProperty(value = "Bank Name")
    private String bankName;

    @NotNull(message = "Card Type Not Null")
    @ApiModelProperty(value = "Card Type")
    private String cardType;

    @NotNull(message = "Amount Not Null")
    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @NotNull(message = "save Not Null")
    @ApiModelProperty("Save :[1:save , 2: Dont Save")
    private Integer save;
}
