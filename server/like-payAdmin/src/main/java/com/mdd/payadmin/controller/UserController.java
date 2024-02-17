package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.StatusEnums;
import com.mdd.payadmin.enums.UserVerifyEnum;
import com.mdd.payadmin.service.IUserService;
import com.mdd.payadmin.validate.user.UserListValidate;
import com.mdd.payadmin.validate.user.UserStatusValidate;
import com.mdd.payadmin.vo.user.UserDetailVo;
import com.mdd.payadmin.vo.user.UserListVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    IUserService iUserService;

    @GetMapping("/list")
    @ApiOperation(value = "User List Api")
    public AjaxResult<PageResult<UserListVo>> userList(@Validated PageValidate pageValidate,
                                                       @Validated  UserListValidate userListValidate) {
        PageResult<UserListVo> list = iUserService.userList(pageValidate, userListValidate);
            return AjaxResult.success(list);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "User Detail")
    public AjaxResult<UserDetailVo> detail(@RequestParam String uuid) {
        UserDetailVo vo = iUserService.detail(uuid);
        return AjaxResult.success(vo);
    }
  @PostMapping("/update")
  @ApiOperation(value = "Update User Status or Verify")
  public AjaxResult updateUserStatusOrVerify(@Validated @RequestBody UserStatusValidate userStatusValidate ){
        Integer type=userStatusValidate.getType();
        if(type.equals(0)) {
            if (iUserService.updateUserStatus(userStatusValidate)) {
                return AjaxResult.success(StatusEnums.SUCCESS.getCode(), StatusEnums.SUCCESS.getMsg());
            }
            return AjaxResult.failed(StatusEnums.FAIL.getMsg());
        }
        else if(type.equals(1)) {
             if (iUserService.updateUserVerify(userStatusValidate)) {
                 return AjaxResult.success(UserVerifyEnum.VERIFIED.getCode(), UserVerifyEnum.VERIFIED.getMsg());
             }
         }return AjaxResult.failed(UserVerifyEnum.UNVERIFIED.getMsg());
      }

    @GetMapping("/country")
    @ApiOperation(value = "User Detail")
    public AjaxResult<List<Object>> detail() {
        List<Object> country = (List<Object>) RedisUtils.get("like:c:");
        if (country == null) {
            country = iUserService.getCountry();
            RedisUtils.set("like:c:", country,86400);
        }
        return AjaxResult.success(country);
    }
}

