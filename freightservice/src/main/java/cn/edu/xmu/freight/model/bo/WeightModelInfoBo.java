package cn.edu.xmu.freight.model.bo;

import cn.edu.xmu.freight.model.po.WeightFreightModelPo;
import cn.edu.xmu.freight.model.vo.WeightModelInfoRetVo;
import cn.edu.xmu.freight.model.vo.WeightModelInfoVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class WeightModelInfoBo implements VoObject, Serializable {
    private Long id;
    private Long freightModelId;
    private Long firstWeight;
    private Long firstWeightFreight;
    private Long tenPrice;
    private Long fiftyPrice;
    private Long hundredPrice;
    private Long trihunPrice;
    private Long abovePrice;
    private Long regionId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public WeightFreightModelPo getweightFreightModelPo() {
        WeightFreightModelPo weightFreightModelPo=new WeightFreightModelPo();
        weightFreightModelPo.setFreightModelId(this.freightModelId);
        weightFreightModelPo.setFirstWeight(this.firstWeight);
        weightFreightModelPo.setFirstWeightFreight(this.firstWeightFreight);
        weightFreightModelPo.setTenPrice(this.tenPrice);
        weightFreightModelPo.setFiftyPrice(this.fiftyPrice);
        weightFreightModelPo.setHundredPrice(this.hundredPrice);
        weightFreightModelPo.setTrihunPrice(this.trihunPrice);
        weightFreightModelPo.setAbovePrice(this.abovePrice);
        weightFreightModelPo.setRegionId(this.regionId);
        weightFreightModelPo.setGmtCreate(this.gmtCreate);
        weightFreightModelPo.setGmtModified(this.gmtModified);
        return weightFreightModelPo;
    }

    public WeightModelInfoRetVo getRetVo() {
        return new WeightModelInfoRetVo(this);
    }
}
