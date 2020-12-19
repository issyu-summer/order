package cn.edu.xmu.external.service;


import cn.edu.xmu.external.bo.GoodSkuBo;
import cn.edu.xmu.external.bo.TimeSegInfo;
import cn.edu.xmu.external.model.MyReturn;
import cn.edu.xmu.ooad.util.ReturnObject;


/** 测试用的，别骂了别骂了 **/
public interface IGoodsService {
    ReturnObject<GoodSkuBo> getGoodSkuById(Long skuId);
    MyReturn<Boolean> resetFlashTimeSeg(Long segId);
}
