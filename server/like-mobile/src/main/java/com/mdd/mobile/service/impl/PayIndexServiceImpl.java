package com.mdd.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.entity.payAcoounts.PayVerifyAccount;
import com.mdd.common.entity.payUser.PayUser;
import com.mdd.common.mapper.payAccount.PayUserAccountMapper;
import com.mdd.common.mapper.payAccount.PayVerifyAccountMapper;
import com.mdd.common.mapper.payUser.PayUserMapper;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.mobile.enums.StatusEnums;
import com.mdd.mobile.service.IPayIndexService;
import com.mdd.mobile.validate.account.VerifyAccountValidate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PayIndexServiceImpl implements IPayIndexService {

    @Resource
    PayUserAccountMapper payUserAccountMapper;

    @Resource
    PayVerifyAccountMapper payVerifyAccountMapper;

    @Resource
    PayUserMapper payUserMapper;

    @Override
    public Map<String, Object> userAccount(String userId){
        Map<String, Object> response = new LinkedHashMap<>();
        PayUserAccount account = payUserAccountMapper.selectOne( new QueryWrapper<PayUserAccount>()
                .eq("user_id", userId)
                .eq("currency_type",1)
        );
        Assert.notNull(account,"user account not found");
        response.put("currencyType",account.getCurrencyType());
        response.put("balance",account.getBalance());
        response.put("payId",account.getPayId());
        return response;
    }

    @Override
    public Boolean verifyAccount(VerifyAccountValidate verifyAccountValidate) {
        String userId=verifyAccountValidate.getUserId();
        PayUser payUser= payUserMapper.selectOne( new QueryWrapper<PayUser>()
                .eq("uuid",userId)
                .last("limit 1"));
        Assert.notNull(payUser,"user not found");

        PayVerifyAccount verifyAccount = new PayVerifyAccount();
        verifyAccount.setUuid(ToolUtils.makeUUID());
        verifyAccount.setUserId(userId);
        verifyAccount.setIdentityNo(verifyAccountValidate.getIdentityNo());
        verifyAccount.setBirthDate(PayUtils.createDate(verifyAccount.getBirthDate()));
        verifyAccount.setRealName(verifyAccountValidate.getRealName());
        verifyAccount.setImageIdentityBack(verifyAccountValidate.getImageIdentityBack());
        verifyAccount.setImageIdentityFront(verifyAccountValidate.getImageIdentityFront());
        verifyAccount.setCreationTime(PayUtils.createDateTime());
        int err=payVerifyAccountMapper.insert(verifyAccount);
        assert err<=0 : "Failed to verify account";

        payUser.setVerifyAccount(StatusEnums.PENDING.getCode());
        payUserMapper.updateById(payUser);

        PayUserAccount userAccount=payUserAccountMapper.selectOne( new QueryWrapper<PayUserAccount>()
                .eq("user_id",userId)
                .last("limit 1"));
        Assert.notNull(userAccount,"user account not found");
        userAccount.setVerifyAccount(StatusEnums.PENDING.getCode());
        payUserAccountMapper.updateById(userAccount);
        if(err<=0)  return false;
        else
            return true;
    }

}
