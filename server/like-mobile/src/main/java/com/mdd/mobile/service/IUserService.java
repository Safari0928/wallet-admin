package com.mdd.mobile.service;

import com.mdd.mobile.validate.user.MailValidate;
import com.mdd.mobile.validate.user.NickNameValidate;
import com.mdd.mobile.vo.user.ChangeMailVo;
import com.mdd.mobile.vo.user.UserInfoVo;

public interface IUserService {

    UserInfoVo info(String userId);

    boolean changeNickName(String userId , NickNameValidate nickNameValidate);

    boolean changeAvatar(String userId, String avatar);

    boolean changePhone(String userId, String newPhone, String phone);

    boolean changePassword(String userId, String oldPassword, String newPassword, String newPasswordAgain);

    ChangeMailVo changeMail (String userId,MailValidate mailValidate);
}
