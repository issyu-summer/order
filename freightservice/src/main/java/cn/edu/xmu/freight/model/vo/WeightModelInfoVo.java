package cn.edu.xmu.freight.model.vo;

import cn.edu.xmu.freight.model.bo.WeightModelInfoBo;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 重量运费模板传入值对象
 * @author 王薪蕾
 * @date 2020/12/9
 */
@Data
public class WeightModelInfoVo {
    private Long firstWeight;
    private Long firstWeightFreight;
    private Long tenPrice;
    private Long fiftyPrice;
    private Long hundredPrice;
    private Long trihunPrice;
    private Long abovePrice;
    private Long regionId;
    public WeightModelInfoVo(){

    }
    public WeightModelInfoVo(WeightModelInfoBo weightModelInfoBo){
        this.setFirstWeight(weightModelInfoBo.getFirstWeight());
        this.setFirstWeightFreight(weightModelInfoBo.getFirstWeightFreight());
        this.setTenPrice(weightModelInfoBo.getTenPrice());
        this.setFiftyPrice(weightModelInfoBo.getFiftyPrice());
        this.setTrihunPrice(weightModelInfoBo.getTrihunPrice());
        this.setAbovePrice(weightModelInfoBo.getAbovePrice());
        this.setRegionId(weightModelInfoBo.getRegionId());
    }

    public WeightModelInfoBo createWeightModelInfoBo(Long id){
        WeightModelInfoBo weightModelInfoBo = new WeightModelInfoBo();
        weightModelInfoBo.setFreightModelId(id);
        weightModelInfoBo.setGmtCreate(LocalDateTime.now());
        weightModelInfoBo.setGmtModified(LocalDateTime.now());
        weightModelInfoBo.setFirstWeight(this.getFirstWeight());
        weightModelInfoBo.setFirstWeightFreight(this.getFirstWeightFreight());
        weightModelInfoBo.setTenPrice(this.getTenPrice());
        weightModelInfoBo.setHundredPrice(this.hundredPrice);
        weightModelInfoBo.setFiftyPrice(this.getFiftyPrice());
        weightModelInfoBo.setTrihunPrice(this.getTrihunPrice());
        weightModelInfoBo.setAbovePrice(this.getAbovePrice());
        weightModelInfoBo.setRegionId(this.getRegionId());
        return weightModelInfoBo;
    }
    public WeightModelInfoBo createWeightModelInfoBo(){
        WeightModelInfoBo weightModelInfoBo = new WeightModelInfoBo();
        weightModelInfoBo.setFirstWeight(this.getFirstWeight());
        weightModelInfoBo.setFirstWeightFreight(this.getFirstWeightFreight());
        weightModelInfoBo.setTenPrice(this.getTenPrice());
        weightModelInfoBo.setHundredPrice(this.hundredPrice);
        weightModelInfoBo.setFiftyPrice(this.getFiftyPrice());
        weightModelInfoBo.setTrihunPrice(this.getTrihunPrice());
        weightModelInfoBo.setAbovePrice(this.getAbovePrice());
        weightModelInfoBo.setRegionId(this.getRegionId());
        return weightModelInfoBo;
    }
}
