package com.mdd.payadmin.service;

import com.alibaba.fastjson2.JSONArray;

import java.util.List;

public interface ISystemAuthMenuService {
    JSONArray selectMenuByRoleId(List<Integer> roleIds);
}
