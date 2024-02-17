package com.mdd.common.entity.payAdmin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Pay Daily Metrics")
public class PayDailyMetrics implements Serializable {

    @ApiModelProperty("ID")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty("UUID")
    private String uuid;

    @ApiModelProperty("Daily revenue")
    private BigDecimal revenue;

    @ApiModelProperty("Daily transaction")
    private BigDecimal transaction;

    @ApiModelProperty("Daily users")
    private BigDecimal users;

    @ApiModelProperty("Daily orders")
    private BigDecimal orders;

    @ApiModelProperty("Daily withdrawal")
    private BigDecimal withdrawal;

    @ApiModelProperty("Daily deposit")
    private BigDecimal deposit;

    @ApiModelProperty("Daily transfer")
    private BigDecimal transfer;

    @ApiModelProperty("Daily exchanges")
    private BigDecimal exchanges;

    @ApiModelProperty("date")
    private String date;

}
