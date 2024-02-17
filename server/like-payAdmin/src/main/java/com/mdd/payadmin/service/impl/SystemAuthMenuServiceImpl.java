package com.mdd.payadmin.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.system.SystemAuthMenu;
import com.mdd.common.mapper.system.SystemAuthMenuMapper;
import com.mdd.common.util.ListUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.payadmin.service.ISystemAuthMenuService;
import com.mdd.payadmin.vo.SystemAuthMenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class SystemAuthMenuServiceImpl implements ISystemAuthMenuService {


    @Resource
    SystemAuthMenuMapper systemAuthMenuMapper;
    @Override
    public JSONArray selectMenuByRoleId(List<Integer> roleIds) {

        QueryWrapper<SystemAuthMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("menu_type", Arrays.asList("M", "C"));
        queryWrapper.eq("is_disable", 0);
        queryWrapper.orderByDesc("menu_sort");
        queryWrapper.orderByAsc("id");

        List<SystemAuthMenu> systemAuthMenus = systemAuthMenuMapper.selectList(queryWrapper);

        List<SystemAuthMenuVo> lists = new ArrayList<>();
        for (SystemAuthMenu systemAuthMenu : systemAuthMenus) {
            SystemAuthMenuVo vo = new SystemAuthMenuVo();
            BeanUtils.copyProperties(systemAuthMenu, vo);

            vo.setUpdateTime(TimeUtils.timestampToDate(systemAuthMenu.getUpdateTime()));
            vo.setCreateTime(TimeUtils.timestampToDate(systemAuthMenu.getCreateTime()));
            lists.add(vo);
        }

        JSONArray jsonArray = JSONArray.parseArray(JSONArray.toJSONString(lists));
        return ListUtils.listToTree(jsonArray, "id", "pid", "children");
    }
}
