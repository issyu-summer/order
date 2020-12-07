package cn.edu.xmu.freight.dao;

import cn.edu.xmu.freight.mapper.FreightModelPoMapper;
import cn.edu.xmu.freight.model.bo.FreightModelBo;
import cn.edu.xmu.freight.model.po.FreightModelPo;
import cn.edu.xmu.freight.model.po.FreightModelPoExample;
import cn.edu.xmu.freight.model.vo.FreightModelVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

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
}
