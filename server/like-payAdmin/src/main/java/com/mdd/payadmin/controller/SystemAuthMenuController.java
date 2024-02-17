package com.mdd.payadmin.controller;

import com.alibaba.fastjson2.JSONArray;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.service.ISystemAuthMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/system/menu")
@Api(tags = "系统菜单管理")
public class SystemAuthMenuController {

    @Resource
    ISystemAuthMenuService systemAuthMenuService;

    @NotPower
    @GetMapping("/route")
    @ApiOperation(value="获取菜单路由")
    public AjaxResult<JSONArray> route() {
        List<Integer> roleIds = LikePayAdminThreadLocal.getRoleIds();
        JSONArray lists = systemAuthMenuService.selectMenuByRoleId(roleIds);
        return AjaxResult.success(lists);
    }


}
