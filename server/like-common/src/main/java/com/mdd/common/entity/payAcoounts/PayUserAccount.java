package com.mdd.common.entity.payAcoounts;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@ApiModel("User Account")
public class PayUserAccount implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("ID")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty("UUID")
    private String uuid;

    @ApiModelProperty("Airpay id")
    private String payId;

    @ApiModelProperty(value="User id", notes="This user.id = PayUser.uuid")
    private String userId;

    @ApiModelProperty("Balance")
    private BigDecimal balance;

    @ApiModelProperty(value="Currency Type", notes="1=TRY 2=USD 3=EUR  Default=1")
    private Integer currencyType;

    @ApiModelProperty("Nick Name")
    private String nickName;

    @ApiModelProperty(value="Verify Account ", notes="-1=Unverified 1=Verified 2=Pending  Default=-1")
    private Integer verifyAccount;

    @ApiModelProperty("Deposit Limit")
    private BigDecimal depositLimit;

    @ApiModelProperty("Transfer Limit")
    private BigDecimal transferLimit;

    @ApiModelProperty("Withdrawal Limit")
    private BigDecimal withdrawalLimit;

    @ApiModelProperty("Creation Time")
    private String createTime;

    @ApiModelProperty("Delete Time")
    private String updateTime;


}
