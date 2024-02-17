package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.PayAdmin;
import com.mdd.common.entity.payCurrency.PayCurrency;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayAdminMapper;
import com.mdd.common.mapper.payCurrency.PayCurrencyMapper;
import com.mdd.common.util.ToolUtils;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.enums.StatusEnums;
import com.mdd.payadmin.service.ICurrencyConfigureService;
import com.mdd.payadmin.validate.currency.CurrencyValidate;
import com.mdd.payadmin.vo.CurrencyConfigureVo;
import com.mdd.payadmin.vo.CurrencyEditVo;
import com.mdd.payadmin.vo.deposit.DepositListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyConfigureServiceImpl implements ICurrencyConfigureService {

    @Resource
    PayCurrencyMapper payCurrencyMapper;

    @Resource
    PayAdminMapper payAdminMapper;

    private void checkPassword(String password){
        String username = LikePayAdminThreadLocal.getUsername();
        PayAdmin admin = payAdminMapper.selectOne(new QueryWrapper<PayAdmin>()
                .eq(Constants.USERNAME, username));
        String newPWd = password + admin.getSalt();
        String md5Pwd = ToolUtils.makeMd5(newPWd);
        if (!md5Pwd.equals(admin.getPassword())) {
            throw new LoginException(AdminEnum.WRONG_PASSWORD.getCode(),AdminEnum.WRONG_PASSWORD.getMsg());
        }
    }

    @Override
    public CurrencyEditVo detail(String currencyID){
        PayCurrency payCurrency = payCurrencyMapper.selectOne(new QueryWrapper<PayCurrency>()
                .eq(Constants.UUID,currencyID));
        Assert.notNull(payCurrency, Constants.NO_RECORD);

        CurrencyEditVo vo = new CurrencyEditVo();

        vo.setCurrencyName(payCurrency.getCurrencyName());
        vo.setCurrencyCode(payCurrency.getCurrencyCode());
        vo.setSymbol(payCurrency.getSymbol());
        vo.setOperate(payCurrency.getIsActive());
        BeanUtils.copyProperties(payCurrency,vo);
        return vo;
    }

    @Override
    public boolean editCurrency( CurrencyValidate currencyValidate){
        checkPassword(currencyValidate.getPassword());
        PayCurrency payCurrency = payCurrencyMapper.selectOne(new QueryWrapper<PayCurrency>()
                .eq(Constants.UUID,currencyValidate.getUuid()));
        Assert.notNull(payCurrency, Constants.NO_RECORD);

        payCurrency.setCurrencyName(currencyValidate.getCurrencyName());
        payCurrency.setCurrencyCode(currencyValidate.getCurrencyCode());
        payCurrency.setSymbol(currencyValidate.getCurrencySymbol());
        payCurrency.setImage(currencyValidate.getImage());
        if(currencyValidate.getOperate()== StatusEnums.FAIL.getCode()){
            payCurrency.setIsActive(StatusEnums.FAIL.getCode());
        }else if(currencyValidate.getOperate()== StatusEnums.SUCCESS.getCode()){
            payCurrency.setIsActive(StatusEnums.SUCCESS.getCode());
        }
        int result = payCurrencyMapper.updateById(payCurrency);
        if(result<=0){
            int code = AdminEnum.FAILED.getCode();
            String msg = AdminEnum.FAILED.getMsg();
            throw new OperateException(msg,code);
        }
        return true;
    }

    @Override
    public List<CurrencyConfigureVo> list() {

        List<PayCurrency> payCurrency = payCurrencyMapper.selectList(new QueryWrapper<PayCurrency>()
                .orderByDesc("id")
        );
        Assert.notNull(payCurrency, Constants.NO_RECORD);

        List<CurrencyConfigureVo> list = new ArrayList<>();
        for (PayCurrency currency : payCurrency){
            CurrencyConfigureVo vo =new CurrencyConfigureVo();
            BeanUtils.copyProperties(currency, vo);
            vo.setCurrencyId(currency.getUuid());
            vo.setOperate(currency.getIsActive());
            list.add(vo);
        }

        return list;
    }
}
