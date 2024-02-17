package com.mdd.payadmin.commons;

import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.payadmin.validate.withdraw.ApproveValidate;
import com.mdd.payadmin.vo.TransactionsDefaultVo;
import com.mdd.payadmin.vo.WithdrawVo;

import java.util.List;
import java.util.Set;

public class AdminUtils {

    public static void updateRedis(String detailId,Integer status,String completeTime){
        Set<String> catchKeys = RedisUtils.keys(Constants.TRANSACTION_PREFIX + "*");
        for (int i = 0; i < catchKeys.size(); i++) {
            String catchKey = (String) catchKeys.toArray()[i];
            String keyWithoutPrefix = catchKey.substring("like:".length());
            PageResult<TransactionsDefaultVo> list = (PageResult<TransactionsDefaultVo>) RedisUtils.get(keyWithoutPrefix);
            List<TransactionsDefaultVo> lists = list.getLists();
            TransactionsDefaultVo resultVo = lists.stream()
                    .filter(item -> item.getDetailId().equals(detailId))
                    .findFirst()
                    .orElse(null);

            if (resultVo != null) {
                resultVo.setStatus(status);
                resultVo.setStatusMSG(MappingUtils.mapStatus(status));
                resultVo.setCompleteTime(completeTime);
                RedisUtils.set(keyWithoutPrefix, list, 604800);
            }
        }
    }

    public static void updateWithdrawRedis(String detailId,Integer status,String completeTime){
        Set<String> catchKeys = RedisUtils.keys(Constants.WITHDRAW_PREFIX + "*");
        for (int i = 0; i < catchKeys.size(); i++) {
            String catchKey = (String) catchKeys.toArray()[i];
            String keyWithoutPrefix = catchKey.substring("like:".length());
            List<WithdrawVo> list = (List<WithdrawVo>) RedisUtils.get(keyWithoutPrefix);
            WithdrawVo resultVo = list.stream()
                    .filter(item -> item.getUuid().equals(detailId))
                    .findFirst()
                    .orElse(null);

            if (resultVo != null) {
                resultVo.setStatus(status);
                resultVo.setStatusMessage(MappingUtils.mapStatus(status));
                resultVo.setCompleteTime(completeTime);
                RedisUtils.set(keyWithoutPrefix, list, 604800);
            }
        }
    }
}
