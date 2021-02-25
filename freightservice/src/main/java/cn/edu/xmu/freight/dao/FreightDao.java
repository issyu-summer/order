package cn.edu.xmu.freight.dao;

import cn.edu.xmu.freight.mapper.FreightModelPoMapper;
import cn.edu.xmu.freight.mapper.PieceFreightModelPoMapper;
import cn.edu.xmu.freight.mapper.WeightFreightModelPoMapper;
import cn.edu.xmu.freight.model.bo.*;
import cn.edu.xmu.freight.model.po.*;
import cn.edu.xmu.freight.model.vo.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.AuthVerify;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.GoodInfo;
import cn.edu.xmu.otherinterface.service.GoodsModuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

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
    @Autowired
    private PieceFreightModelPoMapper pieceFreightModelPoMapper;

    /**
     * 获取店铺中商品的运费模板
     * @author issyu 30320182200070
     * @date 2020/12/5 14:58
     */
    public ReturnObject getFreghtModelsInShop(Long departId,FreightModelVo freightModelVo, Integer page, Integer pageSize){

        if(!AuthVerify.shopAdminAuth(departId)&&!AuthVerify.adminAuth(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        FreightModelPoExample freightModelPoExample = new FreightModelPoExample();
        FreightModelPoExample.Criteria criteria = freightModelPoExample.createCriteria();

        criteria.andShopIdEqualTo(freightModelVo.getShopId());

        if(freightModelVo.getName()!=null)
        {
            criteria.andNameEqualTo(freightModelVo.getName());
        }
        if(freightModelVo.getUnit()!=null){
            criteria.andUnitEqualTo(freightModelVo.getUnit());
        }
        if(freightModelVo.getType()!=null){
            criteria.andTypeEqualTo(freightModelVo.getType());
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

        if(AuthVerify.shopAdminAuth(departId)) {
            FreightModelPoExample freightModelPoExample = new FreightModelPoExample();
            FreightModelPoExample.Criteria criteria = freightModelPoExample.createCriteria();
            if (freightModelVo.getName() != null) {
                criteria.andNameEqualTo(freightModelVo.getName());
            }

//            if (freightModelVo.getType() != null) {
//                criteria.andTypeEqualTo(freightModelVo.getType());
//            }

            if (freightModelVo.getUnit() != null) {
                criteria.andUnitEqualTo(freightModelVo.getUnit());
            }
            try {
                List<FreightModelPo> freightModelPos = freightModelPoMapper.selectByExample(freightModelPoExample);
                if (freightModelPos.isEmpty()) {
                    FreightModelPo freightModelPo = freightModelVo.createFreightModePo(Boolean.TRUE, id);

                    try {
                        int ret = freightModelPoMapper.insertSelective(freightModelPo);
                        FreightModelBo freightModelBo = new FreightModelBo(freightModelPo);

                        if (ret == 0) {
                            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误"));
                        } else {
                            return new ReturnObject<>(freightModelBo);
                        }

                    } catch (DataAccessException e) {
                        return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库内部错误：" + e.getMessage()));
                    }
                }else{
                    return new ReturnObject<>(ResponseCode.FREIGHTNAME_SAME);
                }
            } catch (DataAccessException e) {
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            }
        }else{
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
    }
    /**
     * 店家或管理员为店铺定义默认运费模板
     * @author 王薪蕾
     * @date 2020/12/7
     */

    public ReturnObject<Object> postFreightModelToShop(Long shopId,Long id,Long departId){
        ReturnObject<Object> retObj = null;
        //效验
        //路径不合法
        //获得运费模板
        FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(id);
        //运费模板不存在
        if(freightModelPo==null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
        }

        //运费模板不是本店铺的
        if(!freightModelPo.getShopId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象"));
        }
//        //无权限
//        //管理员不是该店铺管理员
//        if(departId!=0L){
//            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
//        }
        //效验结束
        //设为默认
        freightModelPo.setDefaultModel((byte)1);
        freightModelPo.setGmtModified(LocalDateTime.now());
        try {
            int ret = freightModelPoMapper.updateByPrimaryKey(freightModelPo);
            if (ret == 0) {
                //设置失败
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            } else {
                //设置成功
                retObj = new ReturnObject<>(ResponseCode.OK);
            }
        } catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
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
    public ReturnObject<Object> postWeightItems(WeightModelInfoVo vo, Long shopId, Long id,Long departId){

        ReturnObject<Object> retObj = null;
        try {
            //路径不合法
            //获得运费模板
            FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(id);

            //运费模板不存在
            if(freightModelPo==null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
            }
            //运费模板模式不是重量运费模板
//            if(freightModelPo.getType()!=(byte)0){
//                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板模式不是重量运费模板"));
//            }
            //无权限
            //管理员不是该店铺管理员
            if(!shopId.equals(departId) &&departId!=0L){
                return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
            }
            //运费模板不是本店铺的
            if(!freightModelPo.getShopId().equals(shopId)){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象"));
            }
            //字段不合法
            WeightModelInfoBo weightModelInfoBo;
            weightModelInfoBo=vo.createWeightModelInfoBo(id);
            //获得数据库中此运费模板的所有明细
            WeightFreightModelPoExample weightFreightModelPoExample=new WeightFreightModelPoExample();
            WeightFreightModelPoExample.Criteria criteria=weightFreightModelPoExample.createCriteria();
            criteria.andFreightModelIdEqualTo(id);
            List<WeightFreightModelPo> weightFreightPos = weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample);
            if (!weightFreightPos.isEmpty()) {
                for (WeightFreightModelPo po : weightFreightPos) {
                    //如果找到一样的地区定义，则返回错误
                    if (po.getRegionId().equals(weightModelInfoBo.getRegionId())) {
                        return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("运费模板中该地区已定义"));
                    }
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

    /**
     * 管理员删除运费模板
     * @author 王薪蕾
     * @date 2020/12/9
     */
    public ReturnObject deleteFreightModel(Long shopId, Long id,Long departId) {
        try{
            //效验
            //路径不合法
            //获得运费模板
            FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(id);
            //运费模板不存在
            if(freightModelPo==null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
            }
            //无权限
            //管理员不是该店铺管理员
            if(!shopId.equals(departId) &&departId!=0L){
                return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
            }
            //运费模板不是本店铺的
            if(!freightModelPo.getShopId().equals(shopId)){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象"));
            }
            //未实现级联删除与商品关联的
            //获得数据库中此运费模板的所有明细,删除之
            WeightFreightModelPoExample weightFreightModelPoExample=new WeightFreightModelPoExample();
            WeightFreightModelPoExample.Criteria criteria=weightFreightModelPoExample.createCriteria();
            criteria.andFreightModelIdEqualTo(id);
            List<WeightFreightModelPo> weightFreightPos = weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample);
            if (!weightFreightPos.isEmpty()){
                for (WeightFreightModelPo po : weightFreightPos) {
                    weightFreightModelPoMapper.deleteByPrimaryKey(po.getId());
                    }
                }

            //删除运费模板
            int ret=freightModelPoMapper.deleteByPrimaryKey(id);
            if(ret==0)
            {
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误"));
            }
            else {
                return new ReturnObject<>();
            }
        }catch (DataAccessException e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误:删除失败"));
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }
    /**
     * 管理员克隆店铺的运费模板
     * @author 史韬韬
     * @parameter shopId 店铺id
     * @parameter id 运费模板id
     */
    public ReturnObject<FreightModelRetVo> cloneFreightModel(Long departId,Long id, Long shopId){
//        if(!AuthVerify.shopAdminAuth(departId)){
//            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
//        }
        if(departId!=0L) {
            if (!shopId.equals(departId)) {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        }

//        if(!departId.equals(0L)){
//            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW,"无管理员权限");
//        }
        try{

            FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(id);
            if(freightModelPo==null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"运费模板id不存在");
            }
            if(!shopId.equals(freightModelPo.getShopId())){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,"shopid与运费模板所对应的shopid不同");
            }
            FreightModelBo freightModelBo=new FreightModelBo(freightModelPo);
            FreightModelPo freightModelPo1=freightModelBo.createClonePo(shopId);
            freightModelPoMapper.insertSelective(freightModelPo1);
            Long freightModelId=freightModelPo1.getId();
            //若模板为按重计费模板
            if(freightModelPo.getType()!=null) {
                if (freightModelPo.getType().equals((byte) 0)) {
                    WeightFreightModelPoExample weightFreightModelPoExample = new WeightFreightModelPoExample();
                    WeightFreightModelPoExample.Criteria criteria = weightFreightModelPoExample.createCriteria();
                    criteria.andFreightModelIdEqualTo(id);
                    List<WeightFreightModelPo> weightFreightModelPoList = weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample);
                    if(!weightFreightModelPoList.isEmpty()) {
                        WeightFreightModelPo weightFreightModelPo = weightFreightModelPoList.get(0);
                        WeightFreightModelBo weightFreightModelBo = new WeightFreightModelBo(weightFreightModelPo);
                        WeightFreightModelPo weightFreightModelPo1 = weightFreightModelBo.createClonePo(shopId);
                        weightFreightModelPo1.setFreightModelId(freightModelId);
                        weightFreightModelPoMapper.insertSelective(weightFreightModelPo1);
                    }
                } else {
                    PieceFreightModelPoExample pieceFreightModelPoExample = new PieceFreightModelPoExample();
                    PieceFreightModelPoExample.Criteria criteria = pieceFreightModelPoExample.createCriteria();
                    criteria.andFreightModelIdEqualTo(id);
                    List<PieceFreightModelPo> pieceFreightModelPoList = pieceFreightModelPoMapper.selectByExample(pieceFreightModelPoExample);
                    if(!pieceFreightModelPoList.isEmpty()) {
                        PieceFreightModelPo pieceFreightModelPo = pieceFreightModelPoList.get(0);
                        PieceFreightModelBo pieceFreightModelBo = new PieceFreightModelBo(pieceFreightModelPo);
                        PieceFreightModelPo pieceFreightModelPo1 = pieceFreightModelBo.createClonePo();
                        pieceFreightModelPo1.setFreightModelId(freightModelId);
                        pieceFreightModelPoMapper.insertSelective(pieceFreightModelPo1);
                    }
                }
            }
            FreightModelBo freightModelBo1=new FreightModelBo(freightModelPo1);
            FreightModelRetVo freightModelRetVo=new FreightModelRetVo(freightModelBo1);
            return new ReturnObject<FreightModelRetVo>(freightModelRetVo);
        }catch (DataAccessException e){
            return  new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+ e.getMessage()));
        }

    }

    /**
     * 获得运费模板概要
     * @author 史韬韬
     * @parameter id 运费模板id
     */
    public ReturnObject<FreightModelSimpleInfoRetVo> getFreightModelSimpleInfo(Long id,Long shopId,Long departId,Long userId){
        try {
            FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(id);
            if(freightModelPo==null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
            }
            if(!shopId.equals(freightModelPo.getShopId())){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,"shopid与运费模板所对应的shopid不同");
            }
            FreightModelSimpleInfoBo freightModelSimpleInfoBo=new FreightModelSimpleInfoBo(freightModelPo);
            FreightModelSimpleInfoRetVo freightModelSimpleInfoRetVo=freightModelSimpleInfoBo.createfreightModelSimpleInfoRetVo();
            return new ReturnObject<>(freightModelSimpleInfoRetVo);
        }
        catch (DataAccessException e){
            return  new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+ e.getMessage()));
        }
    }

    /**
     * 管理员修改店铺运费模板
     * @author 史韬韬
     * created in 2020/12/7
     */
    public ReturnObject<VoObject> chanegFreightModel(Long id, Long shopId, FreightModelInfoVo freightModelInfoVo,Long departId){
        if(!departId.equals(0L)&&!shopId.equals(departId)){
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW,"无管理员权限");
        }
        try {
            FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(id);
            if(freightModelPo==null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
            }
            if(!shopId.equals(freightModelPo.getShopId())){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,"shopid与运费模板所对应的shopid不同");
            }
            {
                //校验重复模板名
                if(freightModelInfoVo.getName()!=null){
                    FreightModelPoExample freightModelPoExample = new FreightModelPoExample();
                    FreightModelPoExample.Criteria criteria = freightModelPoExample.createCriteria();
                    criteria.andNameEqualTo(freightModelInfoVo.getName());
                    List<FreightModelPo> freightModelPos=freightModelPoMapper.selectByExample(freightModelPoExample);
                    if(freightModelPos.size()>0){
                        return new ReturnObject<>(ResponseCode.FREIGHTNAME_SAME);
                    }
                }
            }
            freightModelPo.setName(freightModelInfoVo.getName());
            freightModelPo.setUnit(freightModelInfoVo.getUnit());
            freightModelPo.setShopId(shopId);
            freightModelPoMapper.updateByPrimaryKeySelective(freightModelPo);
            return new ReturnObject<VoObject>();
        }
        catch (DataAccessException e){
            return  new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+ e.getMessage()));
        }

    }
    /**
     * 管理员定义件数模板明细
     * @author 陈星如
     * @date 2020/12/9 9:13
     */

    public ReturnObject<Object> postPieceItems(PieceModelInfoVo vo, Long shopId, Long id, Long departId) {

        ReturnObject<Object> retObj = null;
        try {
            PieceModelInfoBo pieceModelInfoBo = vo.createPieceModelInfoBo();
            pieceModelInfoBo.setId(id);
            pieceModelInfoBo.setGmtCreate(LocalDateTime.now());
            pieceModelInfoBo.setGmtModified(LocalDateTime.now());

            //获得运费模板
            FreightModelPo freightModelPo = freightModelPoMapper.selectByPrimaryKey(id);
            //运费模板不存在
            if (freightModelPo == null) {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
            }
            //该用户不是管理员
//            if (!AuthVerify.adminAuth(departId)) {
//                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("该用户不是管理员"));
//                return retObj;
//            }
            //运费模板不是本店铺的
            if (!freightModelPo.getShopId().equals(shopId)) {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
            }
            //运费模板模式不是件数运费模板
            if (freightModelPo.getType() != (byte) 1) {
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("运费模板模式不是件数运费模板"));
            }
            //获得数据库中此运费模板的所有明细
//            PieceFreightModelPoExample pieceFreightModelPoExample = new PieceFreightModelPoExample();
//            PieceFreightModelPoExample.Criteria criteria = pieceFreightModelPoExample.createCriteria();
//            criteria.andFreightModelIdEqualTo(id);
//            List<PieceFreightModelPo> pieceFreightPos = pieceFreightModelPoMapper.selectByExample(pieceFreightModelPoExample);
//            if (!pieceFreightPos.isEmpty()) {
//                for (PieceFreightModelPo po : pieceFreightPos) {
//                    //如果找到一样的地区定义，则返回错误
//                    if (po.getRegionId().equals(pieceModelInfoBo.getRegionId())) {
//                        return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("运费模板中该地区已定义"));
//                    }
//                }
//            }
            //表中插入明细数据
            PieceFreightModelPo pieceFreightModelPo = pieceModelInfoBo.getpieceFreightModelPo();
            pieceFreightModelPo.setFreightModelId(id);
            int retpieceFreightModel = pieceFreightModelPoMapper.insertSelective(pieceFreightModelPo);
            PieceModelInfoRetVo pieceModelInfoRetVo = new PieceModelInfoRetVo(pieceModelInfoBo);
            pieceModelInfoRetVo.setId(pieceFreightModelPo.getId());
            //更新运费模板时间
            freightModelPo.setGmtModified(LocalDateTime.now());
            freightModelPoMapper.updateByPrimaryKey(freightModelPo);
            if (retpieceFreightModel == 0) {
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：新增失败"));
            } else {
                retObj = new ReturnObject<>(pieceModelInfoRetVo);
            }
        } catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误"));
        } catch (Exception e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
        return retObj;
    }
    /**
     * 店家或管理员修改件数模板明细(未校验regionId是否合法、未校验Vo中负数数据是否合法)
     * @author 王子扬
     * @date 2020/12/10 9:13
     */
    public ReturnObject<Object> putPieceItems(PieceModelInfoVo vo, Long shopId, Long id,Long departId){
        try {
            if(departId==0){
                //管理员

                //获得运费模板
                PieceFreightModelPo pieceFreightModelPo=pieceFreightModelPoMapper.selectByPrimaryKey(id);
                if(pieceFreightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("件数运费模板不存在"));
                }

                FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(pieceFreightModelPo.getFreightModelId());
                if(freightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                if(!freightModelPo.getShopId().equals(shopId)){//运费模板不是本店铺的
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }
                if(freightModelPo.getType()!=(byte)1){//运费模板模式不是件数运费模板
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板模式不是件数运费模板"));
                }

                PieceModelInfoBo pieceModelInfoBo=vo.createPieceModelInfoBo();
                pieceModelInfoBo.setId(id);
                pieceModelInfoBo.setGmtCreate(pieceFreightModelPo.getGmtCreate());
                pieceModelInfoBo.setGmtModified(LocalDateTime.now());

                //校验重复地区
                PieceFreightModelPoExample pieceFreightModelPoExample=new PieceFreightModelPoExample();
                PieceFreightModelPoExample.Criteria criteria=pieceFreightModelPoExample.createCriteria();
                criteria.andIdEqualTo(id);
                List<PieceFreightModelPo> pieceFreightPos = pieceFreightModelPoMapper.selectByExample(pieceFreightModelPoExample);
                if (!pieceFreightPos.isEmpty()) {
                    for (PieceFreightModelPo po : pieceFreightPos) {
                        //如果找到一样的地区定义，则返回错误
                        if (po.getRegionId()!=null && po.getRegionId().equals(pieceModelInfoBo.getRegionId())) {
                            return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("运费模板中该地区已定义"));
                        }
                    }
                }
                {//check duplicate region 2
                    if(vo.getRegionId()!=null){
                        PieceFreightModelPoExample pieceFreightModelPoExample1 = new PieceFreightModelPoExample();
                        PieceFreightModelPoExample.Criteria criteria1 = pieceFreightModelPoExample1.createCriteria();
                        if(pieceFreightModelPo.getFreightModelId()!=null){
                            criteria1.andFreightModelIdEqualTo(pieceFreightModelPo.getFreightModelId());
                        }
                        criteria1.andRegionIdEqualTo(vo.getRegionId());
                        if(id!=null){
                            criteria1.andIdNotEqualTo(id);
                        }
                        List<PieceFreightModelPo> pieceFreightModelPos1 = pieceFreightModelPoMapper.selectByExample(pieceFreightModelPoExample1);
                        if(pieceFreightModelPos1.size()>0){
                            return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("重复地区定义"));
                        }
                    }
                }
                PieceFreightModelPo pieceFreightModelPoUpdate=pieceModelInfoBo.getpieceFreightModelPo();
                pieceFreightModelPoUpdate.setFreightModelId(freightModelPo.getId());
                int ret=pieceFreightModelPoMapper.updateByPrimaryKey(pieceFreightModelPoUpdate);
                if(ret == 0){
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：修改失败"));
                }else{
                    return new ReturnObject<>();
                }
            }else if(departId>0){//店家
                if(!departId.equals(shopId)){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("请求的店铺和登陆的店铺不符"));
                }
                //获得运费模板
                PieceFreightModelPo pieceFreightModelPo=pieceFreightModelPoMapper.selectByPrimaryKey(id);
                if(pieceFreightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("件数运费模板不存在"));
                }

                FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(pieceFreightModelPo.getFreightModelId());
                if(freightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                if(!freightModelPo.getShopId().equals(shopId)){//运费模板不是本店铺的
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }
                if(freightModelPo.getType()!=(byte)1){//运费模板模式不是件数运费模板
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板模式不是件数运费模板"));
                }
                PieceModelInfoBo pieceModelInfoBo=vo.createPieceModelInfoBo();
                pieceModelInfoBo.setId(id);
                pieceModelInfoBo.setGmtCreate(pieceFreightModelPo.getGmtCreate());
                pieceModelInfoBo.setGmtModified(LocalDateTime.now());

                //校验重复地区
                PieceFreightModelPoExample pieceFreightModelPoExample=new PieceFreightModelPoExample();
                PieceFreightModelPoExample.Criteria criteria=pieceFreightModelPoExample.createCriteria();
                criteria.andIdEqualTo(id);
                List<PieceFreightModelPo> pieceFreightPos = pieceFreightModelPoMapper.selectByExample(pieceFreightModelPoExample);
                if (!pieceFreightPos.isEmpty()) {
                    for (PieceFreightModelPo po : pieceFreightPos) {
                        //如果找到一样的地区定义，则返回错误
                        if (po.getRegionId()!=null && po.getRegionId().equals(pieceModelInfoBo.getRegionId())) {
                            return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("运费模板中该地区已定义"));
                        }
                    }
                }
                {//check duplicate region 2
                    if(vo.getRegionId()!=null){
                        PieceFreightModelPoExample pieceFreightModelPoExample1 = new PieceFreightModelPoExample();
                        PieceFreightModelPoExample.Criteria criteria1 = pieceFreightModelPoExample1.createCriteria();
                        if(pieceFreightModelPo.getFreightModelId()!=null){
                            criteria1.andFreightModelIdEqualTo(pieceFreightModelPo.getFreightModelId());
                        }
                        criteria1.andRegionIdEqualTo(vo.getRegionId());
                        if(id!=null){
                            criteria1.andIdNotEqualTo(id);
                        }
                        List<PieceFreightModelPo> pieceFreightModelPos1 = pieceFreightModelPoMapper.selectByExample(pieceFreightModelPoExample1);
                        if(pieceFreightModelPos1.size()>0){
                            return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("重复地区定义"));
                        }
                    }
                }

                PieceFreightModelPo pieceFreightModelPoUpdate=pieceModelInfoBo.getpieceFreightModelPo();
                pieceFreightModelPoUpdate.setFreightModelId(freightModelPo.getId());
                int ret=pieceFreightModelPoMapper.updateByPrimaryKey(pieceFreightModelPoUpdate);
                if(ret == 0){
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：修改失败"));
                }else{
                    return new ReturnObject<>();
                }
            }else{
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("请求的用户不是店家或者管理员"));
            }
        }catch (DataAccessException e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误"));
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }


    /**
     * 店家或管理员修改重量模板明细(未校验regionId是否合法、未校验Vo中负数数据是否合法)
     * @author 王子扬
     * @date 2020/12/10 9:13
     */
    public ReturnObject<Object> putWeightItems(WeightModelInfoVo vo, Long shopId, Long id,Long departId){
        try {
            if(departId==0){//管理员
                //获得运费模板
                WeightFreightModelPo weightFreightModelPo=weightFreightModelPoMapper.selectByPrimaryKey(id);
                if(weightFreightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("重量运费模板不存在"));
                }

                FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(weightFreightModelPo.getFreightModelId());
                if(freightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                if(!freightModelPo.getShopId().equals(shopId)){//运费模板不是本店铺的
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }
                if(freightModelPo.getType()!=(byte)0){//运费模板模式不是重量运费模板
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板模式不是重量运费模板"));
                }

                WeightModelInfoBo weightModelInfoBo = vo.createWeightModelInfoBo();
                weightModelInfoBo.setId(id);
                weightModelInfoBo.setGmtCreate(weightFreightModelPo.getGmtCreate());
                weightModelInfoBo.setGmtModified(LocalDateTime.now());

                //校验重复地区
                WeightFreightModelPoExample weightFreightModelPoExample=new WeightFreightModelPoExample();
                WeightFreightModelPoExample.Criteria criteria=weightFreightModelPoExample.createCriteria();
                criteria.andIdEqualTo(id);
                List<WeightFreightModelPo> weightFreightPos = weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample);
                if (!weightFreightPos.isEmpty()){
                    for (WeightFreightModelPo po : weightFreightPos) {
                        //如果找到一样的地区定义，则返回错误
                        if (po.getRegionId()!=null && po.getRegionId().equals(weightModelInfoBo.getRegionId())) {
                            return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("运费模板中该地区已定义"));
                        }
                    }
                }
                {//check duplicate region 2
                    if(vo.getRegionId()!=null){
                        WeightFreightModelPoExample weightFreightModelPoExample1 = new WeightFreightModelPoExample();
                        WeightFreightModelPoExample.Criteria criteria1 = weightFreightModelPoExample1.createCriteria();
                        if(weightFreightModelPo.getFreightModelId()!=null){
                            criteria1.andFreightModelIdEqualTo(weightFreightModelPo.getFreightModelId());
                        }
                        criteria1.andRegionIdEqualTo(vo.getRegionId());
                        if(id!=null){
                            criteria1.andIdNotEqualTo(id);
                        }
                        List<WeightFreightModelPo> weightFreightModelPos1=weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample1);
                        if(weightFreightModelPos1.size()>0){
                            return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("重复地区定义"));
                        }
                    }
                }

                WeightFreightModelPo weightFreightModelPoUpdate = weightModelInfoBo.getweightFreightModelPo();
                weightFreightModelPoUpdate.setFreightModelId(freightModelPo.getId());
                weightFreightModelPoUpdate.setId(weightModelInfoBo.getId());
                int ret=weightFreightModelPoMapper.updateByPrimaryKey(weightFreightModelPoUpdate);
                if(ret == 0){
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：修改失败"));
                }else{
                    return new ReturnObject<>();
                }
            }else if(departId>0){//店家
                if(!departId.equals(shopId)){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("请求的店铺和登陆的店铺不符"));
                }
                //获得运费模板
                WeightFreightModelPo weightFreightModelPo=weightFreightModelPoMapper.selectByPrimaryKey(id);
                if(weightFreightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("重量运费模板不存在"));
                }

                FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(weightFreightModelPo.getFreightModelId());
                if(freightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                if(!freightModelPo.getShopId().equals(shopId)){//运费模板不是本店铺的
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }
                if(freightModelPo.getType()!=(byte)0){//运费模板模式不是重量运费模板
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板模式不是重量运费模板"));
                }

                WeightModelInfoBo weightModelInfoBo = vo.createWeightModelInfoBo();
                weightModelInfoBo.setId(id);
                weightModelInfoBo.setGmtCreate(weightFreightModelPo.getGmtCreate());
                weightModelInfoBo.setGmtModified(LocalDateTime.now());

                //校验重复地区
                WeightFreightModelPoExample weightFreightModelPoExample=new WeightFreightModelPoExample();
                WeightFreightModelPoExample.Criteria criteria=weightFreightModelPoExample.createCriteria();
                criteria.andIdEqualTo(id);
                List<WeightFreightModelPo> weightFreightPos = weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample);
                if (!weightFreightPos.isEmpty()){
                    for (WeightFreightModelPo po : weightFreightPos) {
                        //如果找到一样的地区定义，则返回错误
                        if (po.getRegionId()!=null && po.getRegionId().equals(weightModelInfoBo.getRegionId())) {
                            return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("运费模板中该地区已定义"));
                        }
                    }
                }
                {//check duplicate region 2
                    if(vo.getRegionId()!=null){
                        WeightFreightModelPoExample weightFreightModelPoExample1 = new WeightFreightModelPoExample();
                        WeightFreightModelPoExample.Criteria criteria1 = weightFreightModelPoExample1.createCriteria();
                        if(weightFreightModelPo.getFreightModelId()!=null){
                            criteria1.andFreightModelIdEqualTo(weightFreightModelPo.getFreightModelId());
                        }
                        criteria1.andRegionIdEqualTo(vo.getRegionId());
                        if(id!=null){
                            criteria1.andIdNotEqualTo(id);
                        }
                        List<WeightFreightModelPo> weightFreightModelPos1=weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample1);
                        if(weightFreightModelPos1.size()>0){
                            return new ReturnObject<>(ResponseCode.REGION_SAME, String.format("重复地区定义"));
                        }
                    }
                }

                WeightFreightModelPo weightFreightModelPoUpdate = weightModelInfoBo.getweightFreightModelPo();
                weightFreightModelPoUpdate.setFreightModelId(freightModelPo.getId());
                weightFreightModelPoUpdate.setId(weightModelInfoBo.getId());
                int ret=weightFreightModelPoMapper.updateByPrimaryKey(weightFreightModelPoUpdate);
                if(ret == 0){
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：修改失败"));
                }else{
                    return new ReturnObject<>();
                }
            }else{
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("请求的用户不是店家或者管理员"));
            }
        }catch (DataAccessException e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误"));
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }

    /**
     * 店家或管理员删除件数模板明细
     * @author 王子扬
     * @date 2020/12/10 9:13
     */
    public ReturnObject<Object> deletePieceItems(Long shopId, Long id,Long departId){
        try {
            if(departId==0){//管理员
                //获得运费模板
                PieceFreightModelPo pieceFreightModelPo=pieceFreightModelPoMapper.selectByPrimaryKey(id);
                if(pieceFreightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("件数运费模板不存在"));
                }

                FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(pieceFreightModelPo.getFreightModelId());
                if(freightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                if(!freightModelPo.getShopId().equals(shopId)){//运费模板不是本店铺的
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }
                if(freightModelPo.getType()!=(byte)1){//运费模板模式不是件数运费模板
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板模式不是件数运费模板"));
                }

                int ret=pieceFreightModelPoMapper.deleteByPrimaryKey(id);
                if(ret == 0){
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：删除失败"));
                }else{
                    return new ReturnObject<>();
                }
            }else if(departId>0){//店家
                if(!departId.equals(shopId)){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("请求的店铺和登陆的店铺不符"));
                }
                //获得运费模板
                PieceFreightModelPo pieceFreightModelPo=pieceFreightModelPoMapper.selectByPrimaryKey(id);
                if(pieceFreightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("件数运费模板不存在"));
                }

                FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(pieceFreightModelPo.getFreightModelId());
                if(freightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                if(!freightModelPo.getShopId().equals(shopId)){//运费模板不是本店铺的
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }
                if(freightModelPo.getType()!=(byte)1){//运费模板模式不是件数运费模板
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板模式不是件数运费模板"));
                }

                int ret=pieceFreightModelPoMapper.deleteByPrimaryKey(id);
                if(ret == 0){
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：删除失败"));
                }else{
                    return new ReturnObject<>();
                }
            }else{
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("请求的用户不是店家或者管理员"));
            }

        }catch (DataAccessException e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误"));
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }

    /**
     * 店家或管理员删除重量模板明细
     * @author 王子扬
     * @date 2020/12/10 9:13
     */
    public ReturnObject<Object> deleteWeightItems(Long shopId, Long id, Long departId){
        try {
            if(departId==0){//管理员
                //获得运费模板
                WeightFreightModelPo weightFreightModelPo=weightFreightModelPoMapper.selectByPrimaryKey(id);
                if(weightFreightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("重量运费模板不存在"));
                }

                FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(weightFreightModelPo.getFreightModelId());
                if(freightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                if(!freightModelPo.getShopId().equals(shopId)){//运费模板不是本店铺的
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }
                if(freightModelPo.getType()!=(byte)0){//运费模板模式不是重量运费模板
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板模式不是重量运费模板"));
                }

                int ret=weightFreightModelPoMapper.deleteByPrimaryKey(id);
                if(ret == 0){
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：删除失败"));
                }else{
                    return new ReturnObject<>();
                }
            }else if(departId>0){
                if(!departId.equals(shopId)){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("请求的店铺和登陆的店铺不符"));
                }
                //获得运费模板
                WeightFreightModelPo weightFreightModelPo=weightFreightModelPoMapper.selectByPrimaryKey(id);
                if(weightFreightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("重量运费模板不存在"));
                }

                FreightModelPo freightModelPo=freightModelPoMapper.selectByPrimaryKey(weightFreightModelPo.getFreightModelId());
                if(freightModelPo==null){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                if(!freightModelPo.getShopId().equals(shopId)){//运费模板不是本店铺的
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }
                if(freightModelPo.getType()!=(byte)0){//运费模板模式不是重量运费模板
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板模式不是重量运费模板"));
                }

                int ret=weightFreightModelPoMapper.deleteByPrimaryKey(id);
                if(ret == 0){
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：删除失败"));
                }else{
                    return new ReturnObject<>();
                }
            }else{
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("请求的用户不是店家或者管理员"));
            }

        }catch (DataAccessException e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误"));
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }

    /**
     * 店家或管理员查询重量运费模板明细
     * @author 陈星如
     * @date 2020/12/8 13:33
     */
    public ReturnObject getFreightModelsWeightItems(Long shopId, Long id, Long departId) {
        try {
            //该用户不是管理员且不是有店铺的店家
            if (!AuthVerify.adminAuth(departId)&&AuthVerify.noShopAdminAuth(departId)) {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("该用户不是管理员且不是有店铺的店家"));

            }
            //该用户是管理员
            else if (AuthVerify.adminAuth(departId)) {
                //获得运费模板
                FreightModelPo freightModelPo = freightModelPoMapper.selectByPrimaryKey(id);
                //运费模板不存在
                if (freightModelPo == null) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                //运费模板不是本店铺的
                if (!freightModelPo.getShopId().equals(shopId)) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }

                //运费模板模式不是重量运费模板
                if (freightModelPo.getType() != (byte) 0) {
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("运费模板模式不是重量运费模板"));
                }
                //获得数据库中此运费模板的所有明细
                WeightFreightModelPoExample weightFreightModelPoExample = new WeightFreightModelPoExample();
                WeightFreightModelPoExample.Criteria criteria = weightFreightModelPoExample.createCriteria();
                criteria.andFreightModelIdEqualTo(id);

                List<WeightFreightModelPo> weightFreightPos = weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample);
                List<WeightModelInfoRetVo> ret = new ArrayList<>(weightFreightPos.size());
                if (!weightFreightPos.isEmpty()) {
                    for (WeightFreightModelPo po : weightFreightPos) {
                        WeightModelInfoRetVo weightFreightModelVo = new WeightModelInfoRetVo(po);
                        ret.add(weightFreightModelVo);
                    }
                }
                //返回运费模板明细列表
                return new ReturnObject<>(ret);
            }
            //该用户是店家
            else if (departId > 0L) {
                //获得运费模板
                FreightModelPo freightModelPo = freightModelPoMapper.selectByPrimaryKey(id);
                //请求店铺和登录店铺不一样
                if (!departId.equals(shopId)) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("请求店铺和登录店铺不一样"));

                }
                //运费模板不存在
                if (freightModelPo == null) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                //运费模板不是本店铺的
                if (!freightModelPo.getShopId().equals(shopId)) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }

                //运费模板模式不是重量运费模板
                if (freightModelPo.getType() != (byte) 0) {
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("运费模板模式不是重量运费模板"));
                }
                //获得数据库中此运费模板的所有明细
                WeightFreightModelPoExample weightFreightModelPoExample = new WeightFreightModelPoExample();
                WeightFreightModelPoExample.Criteria criteria = weightFreightModelPoExample.createCriteria();
                criteria.andFreightModelIdEqualTo(id);

                List<WeightFreightModelPo> weightFreightPos = weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample);
                List<WeightModelInfoRetVo> ret = new ArrayList<>(weightFreightPos.size());
                if (!weightFreightPos.isEmpty()) {
                    for (WeightFreightModelPo po : weightFreightPos) {
                        WeightModelInfoRetVo weightFreightModelVo = new WeightModelInfoRetVo(po);
                        ret.add(weightFreightModelVo);
                    }
                }
                //返回运费模板明细列表
                return new ReturnObject<>(ret);
            }
            //该用户是未创建店铺的店家或者用户
            else{
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("该用户是未创建店铺的店家或者买家"));
            }

        } catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误"));
        } catch (Exception e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }

    /**
     * 店家或管理员查询件数运费模板的明细
     * @author 陈星如
     * @date 2020/12/8 14:13
     **/
    public ReturnObject getFreightModelsPieceItems(Long shopId, Long id, Long departId){
        try {
            //该用户不是管理员且不是店家
            if (!AuthVerify.adminAuth(departId)&&AuthVerify.noShopAdminAuth(departId)) {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("该用户不是管理员且不是店家"));

            }
            //该用户是管理员
            else if (AuthVerify.adminAuth(departId)) {
                //获得运费模板
                FreightModelPo freightModelPo = freightModelPoMapper.selectByPrimaryKey(id);
                //运费模板不存在
                if (freightModelPo == null) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                //运费模板不是本店铺的
                if (!freightModelPo.getShopId().equals(shopId)) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }

                //运费模板模式不是件数运费模板
                if (freightModelPo.getType() != (byte) 1) {
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("运费模板模式不是件数运费模板"));
                }
                //获得数据库中此运费模板的所有明细
                PieceFreightModelPoExample pieceFreightModelPoExample = new PieceFreightModelPoExample();
                PieceFreightModelPoExample.Criteria criteria = pieceFreightModelPoExample.createCriteria();
                criteria.andFreightModelIdEqualTo(id);
                List<PieceFreightModelPo> pieceFreightPos = pieceFreightModelPoMapper.selectByExample(pieceFreightModelPoExample);
                List<PieceModelInfoRetVo> ret = new ArrayList<>(pieceFreightPos.size());
                if (!pieceFreightPos.isEmpty()) {
                    for (PieceFreightModelPo po : pieceFreightPos) {
                        PieceModelInfoRetVo pieceModelInfoRetVo = new PieceModelInfoRetVo(po);
                        ret.add(pieceModelInfoRetVo);

                    }
                }
                //返回运费模板明细列表
                return new ReturnObject<>(ret);
            }
            //该用户是店家
            else if (departId > 0L) {
                //请求店铺和登录店铺不一样
                if (!departId.equals(shopId)) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("请求店铺和登录店铺不一样"));
                }
                //获得运费模板
                FreightModelPo freightModelPo = freightModelPoMapper.selectByPrimaryKey(id);
                //运费模板不存在
                if (freightModelPo == null) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("运费模板不存在"));
                }
                //运费模板不是本店铺的
                if (!freightModelPo.getShopId().equals(shopId)) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("运费模板不是本店铺的对象：运费模板id=" + freightModelPo.getId()));
                }

                //运费模板模式不是件数运费模板
                if (freightModelPo.getType() != (byte) 1) {
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("运费模板模式不是件数运费模板"));
                }
                //获得数据库中此运费模板的所有明细
                PieceFreightModelPoExample pieceFreightModelPoExample = new PieceFreightModelPoExample();
                PieceFreightModelPoExample.Criteria criteria = pieceFreightModelPoExample.createCriteria();
                criteria.andFreightModelIdEqualTo(id);
                List<PieceFreightModelPo> pieceFreightPos = pieceFreightModelPoMapper.selectByExample(pieceFreightModelPoExample);
                List<PieceModelInfoRetVo> ret = new ArrayList<>(pieceFreightPos.size());
                if (!pieceFreightPos.isEmpty()) {
                    for (PieceFreightModelPo po : pieceFreightPos) {
                        PieceModelInfoRetVo pieceModelInfoRetVo = new PieceModelInfoRetVo(po);
                        ret.add(pieceModelInfoRetVo);

                    }
                }
                //返回运费模板明细列表
                return new ReturnObject<>(ret);
            }
            //该用户是未创建店铺的店家或者用户
            else{
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("该用户是未创建店铺的店家或者买家"));
            }

        }
        catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误"));
        } catch (Exception e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }

    }

    /**
     * 计算运费
     * @author 薪蕾
     */
    @DubboReference(version = "0.0.1")
    private GoodsModuleService goodsModuleService;

    public ReturnObject calculateFreightPrice(
            Long userId, Long departId,
            Long rid, List<ItemInfoVo> itemInfoVoList) {
        //递归查询到省
        //不是买家用户
        if (!AuthVerify.customerAuth(departId)) {
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW, "不是买家用户，departId=" + departId);
        }
        //商品不存在
        if (itemInfoVoList.isEmpty()) {
            return new ReturnObject(ResponseCode.FIELD_NOTVALID, "没有输入");
        }
        //所有的运费
        List<Long> freightModelIdList = new ArrayList<>();
        Long totalWeight = 0L;
        Long totalPieces = 0L;
        //计算总重量和总件数
        for (ItemInfoVo itemInfoVo : itemInfoVoList) {
            //字段不合法
            if(itemInfoVo.getCount()==null||itemInfoVo.getSkuId()==null){
                return new ReturnObject(ResponseCode.FIELD_NOTVALID, "没有输入");
            }
            totalPieces = totalPieces + itemInfoVo.getCount();
            System.out.println("begin");
            GoodInfo goodInfo = goodsModuleService.getFreightModelIdBySkuId(itemInfoVo.getSkuId()).getData();
            System.out.println("end");
            Long freightModelId = goodInfo.getFreightModelId();
            totalWeight = totalWeight + goodInfo.getWeight()*itemInfoVo.getCount();

//            if(freightModelId==null){
//                System.out.println("dao");
//                FreightModelPoExample freightModelPoExample_ = new FreightModelPoExample();
//                FreightModelPoExample.Criteria criteria_ = freightModelPoExample_.createCriteria();
//                criteria_.andShopIdEqualTo(goodInfo.getShopId());
//                criteria_.andDefaultModelEqualTo((byte)1);
//                List<FreightModelPo> freightModelPos = freightModelPoMapper.selectByExample(freightModelPoExample_);
//                if(freightModelPos.size()!=0){
//                    freightModelId=freightModelPos.get(0).getId();
//                }
//            }
            if (!freightModelIdList.contains(freightModelId)) {
                freightModelIdList.add(freightModelId);
            }
        }
        FreightModelPoExample freightModelPoExample = new FreightModelPoExample();
        FreightModelPoExample.Criteria criteria = freightModelPoExample.createCriteria();
        criteria.andIdIn(freightModelIdList);
        List<FreightModelPo> freightModelPos = freightModelPoMapper.selectByExample(freightModelPoExample);
        if (freightModelPos.isEmpty()) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "商品未绑定任何运费模板");
        }
        Long maxresult=0L;
        List<Long> freightPriceList = new ArrayList<>();
        for (FreightModelPo freightModelPo : freightModelPos) {
            if (freightModelPo.getType() != null) {
                if (freightModelPo.getType().equals((byte) 0)) {
                    //重量单位
                    int unit=freightModelPo.getUnit();
                    WeightFreightModelPoExample weightFreightModelPoExample = new WeightFreightModelPoExample();
                    WeightFreightModelPoExample.Criteria criteria1 = weightFreightModelPoExample.createCriteria();
                    //运费模板明细
                    criteria1.andFreightModelIdEqualTo(freightModelPo.getId());
                    //地区码
                    criteria1.andRegionIdEqualTo(rid);
                    List<WeightFreightModelPo> weightFreightModelPoList = weightFreightModelPoMapper.selectByExample(weightFreightModelPoExample);
                    if( weightFreightModelPoList.size()==0)
                    {
                        return new ReturnObject(ResponseCode.REGION_NOT_REACH);
                    }
                    //获得唯一的明细
                    WeightFreightModelPo po=weightFreightModelPoList.get(0);
                    //首重
                    Long result=po.getFirstWeightFreight();
                    //1-9
                    if(totalWeight>po.getFirstWeight()) {
                        if(totalWeight<10*1000){
                            result+=po.getTenPrice()*((totalWeight-po.getFirstWeight())/unit);
                            if((totalWeight-po.getFirstWeight())%unit!=0)
                                result+=po.getTenPrice();
                        }
                        else{
                            result+=po.getTenPrice()*((10*1000-po.getFirstWeight())/unit);
                            if((10*1000-po.getFirstWeight())%unit!=0)
                                result+=po.getTenPrice();
                        }

                    }
                    //10-49
                    if(totalWeight>10*1000) {
                        if(totalWeight<50*1000){
                            result+=po.getFiftyPrice()*((totalWeight-10*1000)/unit);
                            if((totalWeight-10*1000)%unit!=0)
                                result+=po.getTenPrice();
                        }
                        else{
                            result+=po.getFiftyPrice()*(40*1000/unit);
                            if(40*1000%unit!=0)
                                result+=po.getTenPrice();
                        }
                    }
                    //50-100
                    if(totalWeight>50*1000) {
                        System.out.println(result);
                        if(totalWeight<100*1000){
                            result+=po.getHundredPrice()*((totalWeight-50*1000)/unit);
                            if((totalWeight-50*1000)%unit!=0)
                                result+=po.getTenPrice();
                        }
                        else{
                            result+=po.getHundredPrice()*(50*1000/unit);
                            if(50*1000%unit!=0)
                                result+=po.getTenPrice();
                        }
                        System.out.println(result);
                    }
                    //100-299
                    if(totalWeight>100*1000) {
                        if(totalWeight<300*1000){
                            result+=po.getTrihunPrice()*((totalWeight-100)/unit);
                            if((totalWeight-100)%unit!=0)
                                result+=po.getTenPrice();
                        }
                        else{
                            result+=po.getTrihunPrice()*(200*1000/unit);
                            if(200*1000%unit!=0)
                                result+=po.getTenPrice();
                        }
                        System.out.println(result);
                    }
                    //300-
                    if(totalWeight>300*1000) {
                        result+=po.getAbovePrice()*((totalWeight-300*1000)/unit);
                        if((totalWeight-300*1000)%unit!=0)
                            result+=po.getTenPrice();
                        System.out.println(result);
                    }
                    System.out.println(result);
                    if(maxresult<result)
                        maxresult=result;
                }
                else {
                    //运费模板明细
                    //地区码
                    Long result;
                    System.out.println("计件");
                    PieceFreightModelPoExample pieceFreightModelPoExample = new PieceFreightModelPoExample();
                    PieceFreightModelPoExample.Criteria criteria1 = pieceFreightModelPoExample.createCriteria();
                    criteria1.andFreightModelIdEqualTo(freightModelPo.getId());
                    criteria1.andRegionIdEqualTo(rid);
                    System.out.println(rid);
                    List<PieceFreightModelPo> pieceFreightModelPoList = pieceFreightModelPoMapper.selectByExample(pieceFreightModelPoExample);

                    if(pieceFreightModelPoList.size()==0)
                    {
                        return new ReturnObject(ResponseCode.REGION_NOT_REACH);
                    }
                    PieceFreightModelPo pieceFreightModelPo =pieceFreightModelPoList.get(0);
                    System.out.println(totalPieces);
                    System.out.println(pieceFreightModelPo.getFirstItems());
                    if (totalPieces < pieceFreightModelPo.getFirstItems()) {
                        System.out.println("计算");
                        result = pieceFreightModelPo.getFirstItemsPrice();
                    }
                    else {
                        System.out.println("计算");
                        result=pieceFreightModelPo.getFirstItemsPrice();
                        result=result+ (totalPieces-pieceFreightModelPo.getFirstItems())/
                                pieceFreightModelPo.getAdditionalItems()*
                                pieceFreightModelPo.getAdditionalItemsPrice();
                        if((totalPieces-pieceFreightModelPo.getFirstItems())
                                %pieceFreightModelPo.getAdditionalItems()!=0)
                            result+=pieceFreightModelPo.getAdditionalItemsPrice();
                    }
                    if(result>maxresult)
                        maxresult=result;
                }
            }

        }
        return new ReturnObject(maxresult);
    }
}