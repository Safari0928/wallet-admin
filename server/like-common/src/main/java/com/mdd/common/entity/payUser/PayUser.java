package com.mdd.common.entity.payUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Pay User")
public class PayUser implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("ID")
        @TableId(value="id", type= IdType.AUTO)
        private Integer id;

        @ApiModelProperty("UUID")
        private String uuid;

        @ApiModelProperty("Phone Number")
        private String phoneNumber;

        @ApiModelProperty("Country")
        private String country;

        @ApiModelProperty("Email")
        private String email;

        @ApiModelProperty("Airpay id")
        private String payId;

        @ApiModelProperty("Avatar")
        private String avatar;

        @ApiModelProperty("Nick Name")
        private String nickName;

        @ApiModelProperty(value="Verify Account ", notes="-1=Unverified 1=Verified 2=Pending  Default=-1")
        private Integer verifyAccount;

        @ApiModelProperty("Password")
        private String password;

        @ApiModelProperty("Password")
        private String salt;

        @ApiModelProperty(value="Status",notes="-1=Disabled 1=Normal")
        private Integer status;

        @ApiModelProperty("Last Login Time")
        private String lastLoginTime;

        @ApiModelProperty("Last Login IP")
        private String lastLoginIp;

        @ApiModelProperty("Creation Time")
        private String createTime;

        @ApiModelProperty("Delete Time")
        private String updateTime;
}
