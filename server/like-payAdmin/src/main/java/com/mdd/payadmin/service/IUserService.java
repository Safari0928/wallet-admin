package com.mdd.payadmin.service;

import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.validate.user.UserListValidate;
import com.mdd.payadmin.validate.user.UserStatusValidate;
import com.mdd.payadmin.vo.user.UserDetailVo;
import com.mdd.payadmin.vo.user.UserListVo;

import java.util.List;

public interface IUserService {
    PageResult<UserListVo> userList(PageValidate pageValidate, UserListValidate userListValidate);

    UserDetailVo detail(String uuid);

    boolean updateUserStatus(UserStatusValidate validate);

    boolean updateUserVerify(UserStatusValidate validate);

    List<Object> getCountry();
}
