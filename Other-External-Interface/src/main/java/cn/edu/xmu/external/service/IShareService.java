package cn.edu.xmu.external.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;

public interface IShareService {
    ReturnObject<Long> updateBeShare(Long customerId, Long goodsSkuId, Long sharerId);
    void startGiveRebate();
}
