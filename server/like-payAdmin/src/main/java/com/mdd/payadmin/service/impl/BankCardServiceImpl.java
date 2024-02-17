package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payBankCard.PayBankCards;
import com.mdd.common.mapper.payBankCard.PayBankCardMapper;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.service.IBankCardService;
import com.mdd.payadmin.vo.bankCard.BankCardVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class BankCardServiceImpl implements IBankCardService {

    @Resource
    PayBankCardMapper payBankCardMapper;
    @Override
    public PageResult<BankCardVo> list(PageValidate pageValidate, String userId) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();

        QueryWrapper<PayBankCards> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",1);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.eq("user_id",userId);

        IPage<PayBankCards> iPage = payBankCardMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);

        List<BankCardVo> list = new LinkedList<>();
        for (PayBankCards bankCards : iPage.getRecords()) {
            BankCardVo vo = new BankCardVo();
            BeanUtils.copyProperties(bankCards, vo);
            list.add(vo);
        }

        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }
}
