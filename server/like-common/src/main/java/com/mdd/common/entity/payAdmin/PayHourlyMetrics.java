package com.mdd.common.entity.payAdmin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@ApiModel("Pay Daily Metrics")
public class PayHourlyMetrics implements Serializable {

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
    private BigInteger users;

    @ApiModelProperty("Daily orders")
    private BigInteger orders;

    @ApiModelProperty("Daily withdrawal")
    private BigDecimal withdrawal;

    @ApiModelProperty("Daily deposit")
    private BigDecimal deposit;

    @ApiModelProperty("Daily transfer")
    private BigDecimal transfer;

    @ApiModelProperty("Daily exchanges")
    private BigDecimal exchanges;

    @ApiModelProperty("createTime")
    private String  createTime;

}
