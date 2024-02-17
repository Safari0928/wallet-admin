package com.mdd.mobile.service.impl;

import com.mdd.common.util.PayUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.mobile.service.ICodeService;
import com.mdd.mobile.validate.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CodeServiceImpl implements ICodeService {

    private static final Logger logger = LoggerFactory.getLogger(CodeServiceImpl.class);

    @Override
    public Boolean sendMailVerificationCode(String email) {
        try{
            String key =Constants.LOGIN_CODES+email;
            Integer verificationCode = PayUtils.generateVerificationCode();
            RedisUtils.set(key, String.valueOf(verificationCode), 60);
            logger.info("E mail sent");
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public Boolean sendPhoneVerificationCode(String phone) {
        Integer verificationCode = PayUtils.generateVerificationCode();
        String key =Constants.LOGIN_CODES+phone;
        RedisUtils.set(key, String.valueOf(verificationCode), 60);
        logger.info("Sms sent");
        return true;
    }

    @Override
    public Boolean codeCheck(String value, String code) {

        String key =Constants.LOGIN_CODES+value;
        String redisCode = (String) RedisUtils.get(key);

        if (code.equals(redisCode))
            return true;
        else
            return false;
    }

    @Override
    public Boolean saveVerificationCode(String key, String code) {
        try {
            RedisUtils.set(key, code,  60);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public Boolean forgetPasswordCode(String phone) {
        Integer verificationCode = PayUtils.generateVerificationCode();
        String key =Constants.LOGIN_CODES+phone;
        RedisUtils.set(key, String.valueOf(verificationCode), 600);
        logger.info("Forget Password Code sent");
        return true;
    }

}





