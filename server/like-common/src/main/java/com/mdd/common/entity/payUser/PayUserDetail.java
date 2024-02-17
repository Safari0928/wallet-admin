package com.mdd.common.entity.payUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Pay User Detail")
public class PayUserDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty("UUID")
    private String uuid;

    @ApiModelProperty(value="User id")
    private String userId;

    @ApiModelProperty("Phone Number")
    private String phoneNumber;

    @ApiModelProperty("Country")
    private String country;

    @ApiModelProperty("Email")
    private String email;

    @ApiModelProperty("Airpay id")
    private String payId;

    @ApiModelProperty(value="Verify Account ", notes="-1=Unverified 1=Verified 2=Pending  Default=-1")
    private Integer verifyAccount;

    @ApiModelProperty(value="Status",notes="-1=Disabled 1=Normal")
    private Integer status;

    @ApiModelProperty("Nick Name")
    private String nickName;

    @ApiModelProperty("iban")
    private String iban;

    @ApiModelProperty("Credit Cards")
    private String creditCards;

    @ApiModelProperty("Avatar")
    private String avatar;

    @ApiModelProperty("Creation Time")
    private Integer createTime;

    @ApiModelProperty("Delete Time")
    private Integer deleteTime;

}
