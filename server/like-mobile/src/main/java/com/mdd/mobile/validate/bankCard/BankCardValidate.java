package com.mdd.mobile.validate.bankCard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("Bank card")
public class BankCardValidate implements Serializable {

    @NotNull(message = "Card Number Not Null")
    @ApiModelProperty(value = "Card Number")
    private String cardNumber;

    @NotNull(message = "Expiry Date Not Null")
    @ApiModelProperty(value = "Expiry Date")
    private String expiryDate;

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

    @NotNull(message = "Verify Code Not Null")
    @ApiModelProperty(value = "Verify Code")
    private String code;
}
