package com.mdd.payadmin.vo.user;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

@Data
@ApiModel("User List Vo")
public class UserListVo implements Serializable {

    @ApiModelProperty("Phone Number")
    private String phoneNumber;

    @ApiModelProperty("Country")
    private String country;

    @ApiModelProperty("Email")
    private String email;

    @ApiModelProperty(value ="Pay_id")
    private String payId;

    @ApiModelProperty(value = "Status",notes = "-1=Disabled 1=Normal")
    private Integer status;

    @ApiModelProperty(value = "Status",notes = "-1=Disabled 1=Normal")
    private String StatusMSG;

    @ApiModelProperty(value = "Verify Account",notes = "-1=Unverified 1=Verified 2=Pending  Default=-1")
    private Integer verifyAccount;

    @ApiModelProperty(value = "Verify Account",notes = "-1=Unverified 1=Verified 2=Pending  Default=-1")
    private String verifyAccountMSG;

    @ApiModelProperty("UUID")
    private String uuid;

    @ApiModelProperty("Creation time")
    private String createTime;
}
