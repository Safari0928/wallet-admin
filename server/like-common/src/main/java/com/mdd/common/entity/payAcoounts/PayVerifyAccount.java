package com.mdd.common.entity.payAcoounts;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Verifiy Acoount")
public class PayVerifyAccount {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty("UUID")
    private String uuid;

    @ApiModelProperty("User id")
    private String UserId;

    @ApiModelProperty("Real Name")
    private String realName;

    @ApiModelProperty("Identity No")
    private String identityNo;

    @ApiModelProperty("BirthDate")
    private String birthDate;

    @ApiModelProperty("Image Identity Front")
    private String imageIdentityFront;

    @ApiModelProperty("Image Identity Back")
    private String imageIdentityBack;

    @ApiModelProperty(value="Verify Account ", notes="-1=Unverified 1=Verified 2=Pending  Default=-1")
    private Integer verifyAccount;

    @ApiModelProperty("Creation Time")
    private String creationTime;


}
