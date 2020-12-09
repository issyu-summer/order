package cn.edu.xmu.freight.dao;

import cn.edu.xmu.freight.mapper.FreightModelPoMapper;
import cn.edu.xmu.freight.mapper.WeightFreightModelPoMapper;
import cn.edu.xmu.freight.model.bo.FreightModelBo;
import cn.edu.xmu.freight.model.bo.WeightModelInfoBo;
import cn.edu.xmu.freight.model.po.FreightModelPo;
import cn.edu.xmu.freight.model.po.FreightModelPoExample;
import cn.edu.xmu.freight.model.po.WeightFreightModelPo;
import cn.edu.xmu.freight.model.po.WeightFreightModelPoExample;
import cn.edu.xmu.freight.model.vo.FreightModelVo;
import cn.edu.xmu.freight.model.vo.WeightModelInfoRetVo;
import cn.edu.xmu.freight.model.vo.WeightModelInfoVo;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 1:24
 */
@Repository
public class FreightDao {

    @Autowired
    private FreightModelPoMapper freightModelPoMapper;
    @Autowired
    private WeightFreightModelPoMapper weightFreightModelPoMapper;

    /**
     * 获取店铺中商品的运费模板
     * @author issyu 30320182200070
     * @date 2020/12/5 14:58
     */
    public ReturnObject getFreghtModelsInShop(FreightModelVo freightModelVo, Integer page, Integer pageSize){

        FreightModelPoExample freightModelPoExample = new FreightModelPoExample();
        FreightModelPoExample.Criteria criteria = freightModelPoExample.createCriteria();

        criteria.andShopIdEqualTo(freightModelVo.getShopId());

        if(freightModelVo.getName()!=null)
        {
            criteria.andNameEqualTo(freightModelVo.getName());
        }

        try{
            PageHelper.startPage(page,pageSize);

            List<FreightModelPo> freightModelPos = freightModelPoMapper.selectByExample(freightModelPoExample);

            PageInfo<FreightModelPo> pages = new PageInfo<>(freightModelPos);

            List<VoObject> operationOrders = pages.getList()
                    .stream()
                    .map(FreightModelBo::new)
                    //this Bo implements VoObject
                    .collect(Collectors.toList());

            PageInfo<VoObject> returnObject = new PageInfo<>(operationOrders);
            returnObject.setPages(pages.getPages());
            returnObject.setPageNum(pages.getPageNum());
            returnObject.setPageSize(pages.getPageSize());
            returnObject.setTotal(pages.getTotal());

            return new ReturnObject<>(returnObject);

        }catch (DataAccessException e){
            return  new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+ e.getMessage()));
        }
    }

    /**
     * 定义店铺中商品的运费模板
     * @author issyu 30320182200070
     * @date 2020/12/8 1:33
     */
    public ReturnObject<VoObject> defineFreightModel(FreightModelVo freightModelVo,Long id,Long departId){

        FreightModelPo freightModelPo = freightModelVo.createFreightModePo(Boolean.TRUE,id);

        try{
            int ret = freightModelPoMapper.insertSelective(freightModelPo);
            FreightModelBo freightModelBo  = new FreightModelBo(freightModelPo);

            if(ret == 0){
                return  new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误"));
            }else{
                return new ReturnObject<>(freightModelBo);
            }

        }catch (DataAccessException e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库内部错误："+e.getMessage()));
        }

    }
    /**
     * 店家或管理员为店铺定义默认运费模板
     * @author 王薪蕾
     * @date 2020/12/7
     */

    public ReturnObject<Object> postFreightModelToShop(Long userId,Long shopId,Long id){

        FreightModelPo freightModelPo = freightModelPoMapper.selectByPrimaryKey(id);
        ReturnObject<Object> retObj = null;
        //资源不存在
        if (freightModelPo == null) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在：id=" + id));
            return retObj;
        }
        //不是本店铺模板
        if (freightModelPo.getShopId()!=shopId) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + id));
            return retObj;
        }
        //用户无权限，暂无
        //设为默认
        freightModelPo.setDefaultModel((byte)1);
        try {
            int ret = freightModelPoMapper.updateByPrimaryKey(freightModelPo);
            if (ret == 0) {
                //设置失败
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库更新不成功"));
            } else {
                //设置成功
                retObj = new ReturnObject<>(ResponseCode.OK,String.format("成功"));
            }
        } catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
        return retObj;
    }
    /**
     * 管理员定义重量模板明细
     * @author 王薪蕾
     * @date 2020/12/9
     */
    public ReturnObject<Object> postWeightItems(WeightModelInfoVo vo, Long shopId, Long id){

        ReturnObject<Object> retObj = null;
        try {
            WeightModelInfoBo weightModelInfoBo=vo.createWeightModelInfoBo();
            weightModelInfoBo.setFreightModelId(id);
            weightModelInfoBo.setGmtCreate(LocalDateTime.now());
            weightModelInfoBo.setGmtModified(LocalDateTime.now());

            //获得运费模板
            FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(id);
            //运费模板不存在
            if(freightModelPo==null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
            }
            //运费模板不是本店铺的
            if(freightModelPo.getId()!=shopId){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
            }
            //运费模板模式不是重量运费模板
            if(freightModelPo.getType()!=(byte)0){
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("运费模板模式不是重量运费模板"));
            }
            //获得数据库中此运费模板的所有明细
            WeightFreightModelPoExample weightFreightModelPoExample=new WeightFreightModelPoExample();
            WeightFreightModelPoExample.Criteria criteria=weightFreightModelPoExample.createCriteria();
            criteria.andFreightModelIdEqualTo(id);
            List<WeightFreightModelPo> weightFreightPos = weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample);
            if (!weightFreightPos.isEmpty())
                for (WeightFreightModelPo po : weightFreightPos) {
                    //如果找到一样的地区定义，则返回错误
                    if (po.getRegionId().equals(weightModelInfoBo.getRegionId())) {
                        return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("运费模板中该地区已定义"));
                    }
                }
            //表中插入明细数据
            WeightFreightModelPo weightFreightModelPo=weightModelInfoBo.getweightFreightModelPo();
            int retweightFreightModel = weightFreightModelPoMapper.insertSelective(weightFreightModelPo);
            WeightModelInfoRetVo weightModelInfoRetVo=new WeightModelInfoRetVo(weightModelInfoBo);
            weightModelInfoRetVo.setId(weightFreightModelPo.getId());
            //更新运费模板时间
            freightModelPo.setGmtModified(LocalDateTime.now());
            freightModelPoMapper.updateByPrimaryKey(freightModelPo);
            if (retweightFreightModel == 0) {
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：新增失败"));
            }
            else {
                retObj = new ReturnObject<>(weightModelInfoRetVo);
            }
        }catch (DataAccessException e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误"));
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
        return retObj;
    }
}
