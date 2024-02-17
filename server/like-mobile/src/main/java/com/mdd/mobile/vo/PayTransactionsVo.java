package com.mdd.mobile.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class PayTransactionsVo {

    @ApiModelProperty("Details : Last 4 digit, Nickname")
    private String detail;

    @ApiModelProperty("Transfer Amount")
    private BigDecimal amount;

    @ApiModelProperty("Transfer Type:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private Integer typeDetail;

    @ApiModelProperty("Transfer Type MSG:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private String typeMSG;


    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Transfer UUID")
    private String detailId;

}
