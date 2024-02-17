package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("admin info vo")
public class AdminProfileVo {

    @ApiModelProperty(value = "管理员ID")
    private String uuid;

    @ApiModelProperty("用户账号")
    private String nickname;

    @ApiModelProperty("用户昵称")
    private String username;

    @ApiModelProperty("角色主键")
    private String roleIds;
}
