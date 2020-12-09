package cn.edu.xmu.freight.model.bo;

import cn.edu.xmu.freight.model.po.WeightFreightModelPo;
import lombok.Data;

import java.time.LocalDateTime;

/*
* @author 史韬韬
* @date 2020/12/9
* 按重计费模板的bo
 */
@Data
public class WeightFreightModelBo {
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

    public WeightFreightModelBo(WeightFreightModelPo weightFreightModelPo){
        id=weightFreightModelPo.getId();
        freightModelId=weightFreightModelPo.getFreightModelId();
        firstWeight=weightFreightModelPo.getFirstWeight();
        firstWeightFreight=weightFreightModelPo.getFirstWeightFreight();
        tenPrice=weightFreightModelPo.getTenPrice();
        fiftyPrice=weightFreightModelPo.getFiftyPrice();
        hundredPrice=weightFreightModelPo.getHundredPrice();
        trihunPrice=weightFreightModelPo.getTrihunPrice();
        abovePrice=weightFreightModelPo.getAbovePrice();
        regionId=weightFreightModelPo.getRegionId();
        gmtCreate=weightFreightModelPo.getGmtCreate();
        gmtModified=weightFreightModelPo.getGmtModified();
    }

    public WeightFreightModelPo createClonePo(Long shopId){
        WeightFreightModelPo weightFreightModelPo=new WeightFreightModelPo();
        weightFreightModelPo.setAbovePrice(abovePrice);
        weightFreightModelPo.setFiftyPrice(fiftyPrice);
        weightFreightModelPo.setFirstWeight(firstWeight);
        weightFreightModelPo.setFirstWeightFreight(firstWeightFreight);
        weightFreightModelPo.setGmtCreate(gmtCreate);
        weightFreightModelPo.setGmtModified(gmtModified);
        weightFreightModelPo.setRegionId(regionId);
        weightFreightModelPo.setTenPrice(tenPrice);
        weightFreightModelPo.setHundredPrice(hundredPrice);
        weightFreightModelPo.setTrihunPrice(trihunPrice);
        return weightFreightModelPo;
    }
}
