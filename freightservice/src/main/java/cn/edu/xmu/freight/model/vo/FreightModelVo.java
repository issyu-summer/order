package cn.edu.xmu.freight.model.vo;

import cn.edu.xmu.freight.model.po.FreightModelPo;
import io.lettuce.core.StrAlgoArgs;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 17:04
 */
@Data
public class FreightModelVo {
    private Long shopId;
    private String name;

    private Integer unit;
    private Byte type;

    /**
     * json
     */
    public FreightModelVo(){

    }
    public FreightModelVo(Long shopId, String name){
        this.setShopId(shopId);
        if(name!=null){
            this.setName(name);
        }
    }

    /**
     * 创建运费模板po
     * @param isDefault
     * @param shopId
     * @return
     */
    public FreightModelPo createFreightModePo(boolean isDefault,Long shopId){
        FreightModelPo freightModelPo = new FreightModelPo();
        freightModelPo.setShopId(shopId);
        if(isDefault==Boolean.TRUE){
            freightModelPo.setDefaultModel((byte) 1);
        }else{
            freightModelPo.setType((byte) 0);
        }
        freightModelPo.setName(this.getName());
        freightModelPo.setType(this.getType());
        freightModelPo.setUnit(this.getUnit());
        freightModelPo.setGmtCreate(LocalDateTime.now());
        freightModelPo.setGmtModified(LocalDateTime.now());
        return freightModelPo;
    }
}
