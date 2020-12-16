package cn.edu.xmu.freight.service.impl;

import cn.edu.xmu.freight.mapper.FreightModelPoMapper;
import cn.edu.xmu.freight.model.po.FreightModelPo;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.outer.model.bo.Freight;
import cn.edu.xmu.outer.model.bo.MyReturn;
import cn.edu.xmu.outer.service.IFreightService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@DubboService
public class FreightOuterServerImpl implements IFreightService {

    @Autowired
    private FreightModelPoMapper freightModelPoMapper;
    /**
     *获取运费模板信息
     * @param freightId
     * @return Freight
     * create by 王薪蕾
     * @date 2020/12/11
     */
    @Override
    public Freight getFreightById(Long freightId){
        FreightModelPo po=freightModelPoMapper.selectByPrimaryKey(freightId);
        if(po==null){
            return null;
        }
        Freight freight=new Freight();
        freight.setId(po.getId());
        freight.setGmtCreate(po.getGmtCreate());
        freight.setGmtModified(po.getGmtModified());
        freight.setName(po.getName());
        freight.setIsDefault(po.getDefaultModel());
        freight.setType(po.getType());
        freight.setUnit(po.getUnit());
        return freight;
    }
}
