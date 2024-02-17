package com.mdd.payadmin.service;

import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.validate.transfer.TransferSearchValidate;
import com.mdd.payadmin.vo.TransferDefaultVo;
import com.mdd.payadmin.vo.TransferDetailVo;

public interface ITransferService {

    PageResult<TransferDefaultVo> list(PageValidate pageValidate , TransferSearchValidate validate);

    TransferDetailVo getDetail(String uuid);

    Long getCount(PageValidate pageValidate);
}
