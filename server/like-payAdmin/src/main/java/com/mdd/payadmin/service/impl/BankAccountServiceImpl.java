package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payBankAccounts.PayBankAccount;
import com.mdd.common.mapper.payBankAccounts.PayBankAccountsMapper;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.MappingUtils;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.service.IBankAccountService;
import com.mdd.payadmin.vo.BankAccountVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class BankAccountServiceImpl implements IBankAccountService {

    @Resource
    PayBankAccountsMapper payBankAccountsMapper;

    @Override
    public PageResult<BankAccountVo> list(PageValidate pageValidate, String userId){
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();

        QueryWrapper<PayBankAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constants.USERID,userId);
        queryWrapper.eq("is_delete",1);
        queryWrapper.orderByDesc("create_time");

        IPage<PayBankAccount> iPage = payBankAccountsMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);

        List<BankAccountVo> list = new LinkedList<>();
        for (PayBankAccount bankAccount : iPage.getRecords()) {
            BankAccountVo vo = new BankAccountVo();
            vo.setCountry(bankAccount.getIban().substring(0,2));
            vo.setBankName("İş Bankası");
            vo.setCardType(MappingUtils.mapCardType(bankAccount.getCardType()));
            BeanUtils.copyProperties(bankAccount, vo);
            list.add(vo);
        }

        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);

    }
}
