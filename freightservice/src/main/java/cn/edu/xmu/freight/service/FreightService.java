package cn.edu.xmu.freight.service;

import cn.edu.xmu.freight.dao.FreightDao;
import cn.edu.xmu.freight.model.bo.FreightModelBo;
import cn.edu.xmu.freight.model.vo.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.awt.*;
import java.util.List;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 1:24
 */
@Service
public class FreightService {

    @Autowired
    private FreightDao freightDao;

    public ReturnObject getFreghtModelsInShop(FreightModelVo freightModelVo, Integer page, Integer pageSize) {

        ReturnObject returnObject = freightDao.getFreghtModelsInShop(freightModelVo,page,pageSize);
        return returnObject;
    }

    /**
     * 店家或管理员为店铺定义默认运费模板
     * @author 王薪蕾
     * @date 2020/12/7
     */

    public ReturnObject postFreightModelToShop(Long shopId,Long id) {

        ReturnObject returnObject = freightDao.postFreightModelToShop(shopId,id);
        return returnObject;
    }

    /**
     * 定义店铺中商品的运费模板
     * @author issyu 30320182200070
     * @date 2020/12/8 1:38
     */
    public ReturnObject defineFreightModel(FreightModelVo freightModelVo,Long id,Long departId){
        return freightDao.defineFreightModel(freightModelVo,id,departId);
    }
    /**
     * 管理员定义重量模板明细
     * @author 王薪蕾
     * @date 2020/12/8
     */
    public ReturnObject postWeightItems(WeightModelInfoVo vo, Long shopId, Long id) {
        ReturnObject returnObject = freightDao.postWeightItems(vo,shopId,id);
        return returnObject;
    }
    /**
     * 管理员删除运费模板
     * @author 王薪蕾
     * @date 2020/12/9
     */

    public ReturnObject deleteFreightModel(Long shopId, Long id) {
        return freightDao.deleteFreightModel(shopId,id);
    }
    /**
     * 管理员克隆店铺的运费模板
     * @author 史韬韬
     * @parameter shopId 店铺id
     * @parameter id 运费模板id
     */
    public ReturnObject<FreightModelRetVo> cloneFreightModel(Long id, Long shopId){
        return freightDao.cloneFreightModel(id,shopId);
    }

    /**
     * 获得运费模板概要
     * @author 史韬韬
     * @parameter id 运费模板id
     */
    public ReturnObject<FreightModelSimpleInfoRetVo> getFreightModelSimpleInfo(Long id){
        return  freightDao.getFreightModelSimpleInfo(id);
    }
    /**
     * 管理员修改店铺运费模板
     * @author 史韬韬
     * created in 2020/12/7
     */
    public ReturnObject<VoObject> changeFreightModel(Long id, Long shopId, FreightModelInfoVo freightModelInfoVo){
        return freightDao.chanegFreightModel(id,shopId,freightModelInfoVo);
    }

    /**
     * 管理员定义件数模板明细
     * @author 陈星如
     * @date 2020/12/9 9:13
     */
    public ReturnObject postPieceItems(PieceModelInfoVo vo, Long shopId, Long id,Long departId) {
        ReturnObject returnObject = freightDao.postPieceItems(vo,shopId,id,departId);
        return returnObject;
    }

    /**
     * 店家或管理员修改件数模板明细
     * @author 王子扬
     * @date 2020/12/10 9:13
     */
    public ReturnObject putPieceItems(PieceModelInfoVo vo, Long shopId, Long id, Long departId) {
        ReturnObject returnObject = freightDao.putPieceItems(vo,shopId,id,departId);
        return returnObject;
    }

    /**
     * 店家或管理员修改重量模板明细
     * @author 王子扬
     * @date 2020/12/10 9:13
     */
    public ReturnObject putWeightItems(WeightModelInfoVo vo, Long shopId, Long id,Long departId) {
        ReturnObject returnObject = freightDao.putWeightItems(vo,shopId,id,departId);
        return returnObject;
    }

    /**
     * 店家或管理员删除件数模板明细
     * @author 王子扬
     * @date 2020/12/10 9:13
     */
    public ReturnObject deletePieceItems(Long shopId, Long id,Long departId) {
        ReturnObject returnObject = freightDao.deletePieceItems(shopId,id,departId);
        return returnObject;
    }

    /**
     * 店家或管理员删除重量模板明细
     * @author 王子扬
     * @date 2020/12/10 9:13
     */
    public ReturnObject deleteWeightItems(Long shopId, Long id, Long departId) {
        ReturnObject returnObject = freightDao.deleteWeightItems(shopId,id,departId);
        return returnObject;
    }
    /**
     * 店家或管理员查询运费模板明细
     * @author 陈星如
     * @date 2020/12/8 13:33
     */
    public ReturnObject<java.util.List> getFreightModelsWeightItems(Long shopId, Long id,Long departId) {
        ReturnObject returnObject = freightDao.getFreightModelsWeightItems(shopId,id,departId);
        return returnObject;
    }
    /**
     *店家或管理员查询件数运费模板的明细
     *@author 陈星如
     *@date 2020/12/8 14:13
     */
    public ReturnObject<List> getFreightModelsPieceItems(Long shopId, Long id,Long departId) {
        ReturnObject returnObject = freightDao.getFreightModelsPieceItems(shopId,id,departId);
        return returnObject;
    }

    /**
     * @author issyu 30320182200070
     * @date 2020/12/18 15:58
     * 买家使用运费模板计算一批运费
     * @param userId
     * @param departId
     * @param rid
     * @param itemInfoVoList
     * @return
     */
    public ReturnObject calculateFreightPrice(
            Long userId, Long departId,
            Long rid, List<ItemInfoVo> itemInfoVoList) {
        return freightDao.calculateFreightPrice(userId,departId,rid,itemInfoVoList);
    }
}
