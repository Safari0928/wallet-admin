package com.mdd.mobile.service;

import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.entity.payTransfer.PayTransfer;
import com.mdd.common.entity.payTransfer.PayTransferDetails;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.validate.transfer.TransferValidate;
import com.mdd.mobile.vo.PayTransferDetailVo;
import com.mdd.mobile.vo.PayTransferDetailsVo;
import com.mdd.mobile.vo.PayTransferVo;

import java.math.BigDecimal;

public interface IPayTransferService {

    PayTransferVo makeTransfer(TransferValidate validate);

    PayTransfer createPayTransferEntry(PayUserAccount sender, PayUserAccount receiver,Integer type);

    PayTransferDetails saveTransferDetails(String transferId);

    PayTransferDetailVo getDetailByTransferId(String transferId);

    PageResult<PayTransferDetailsVo> list(PageValidate pageValidate);
}
