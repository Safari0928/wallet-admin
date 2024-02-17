package com.mdd.payadmin.service;

import com.mdd.payadmin.validate.ProfileValidate;
import com.mdd.payadmin.vo.AdminProfileVo;
import com.mdd.payadmin.vo.SystemAuthAdminInformVo;
import com.mdd.payadmin.vo.SystemAuthAdminSelvesVo;

public interface ISystemAuthAdminService {
    SystemAuthAdminSelvesVo self(String adminId);

    AdminProfileVo profile(String adminId);

    boolean update(ProfileValidate validate);
}
