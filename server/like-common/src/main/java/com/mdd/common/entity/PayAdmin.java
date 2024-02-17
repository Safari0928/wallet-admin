package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Admin entity")
public class PayAdmin implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty(value = "管理员ID")
    private String uuid;

    @ApiModelProperty("用户账号")
    private String nickname;

    @ApiModelProperty("用户昵称")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("加密盐巴")
    private String salt;

    @ApiModelProperty("角色主键")
    private String roleIds;

    @ApiModelProperty("是否禁用: [0=否, 1=是]")
    private Integer isDisable;

    @ApiModelProperty("是否删除: [0=否, 1=是]")
    private Integer isDelete;

    @ApiModelProperty("最后登录IP")
    private String lastLoginIp;

    @ApiModelProperty("最后登录时间")
    private String lastLoginTime;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("更新时间")
    private String updateTime;

    @ApiModelProperty("删除时间")
    private String deleteTime;
}
