package cn.edu.xmu.freight.model.vo;

import cn.edu.xmu.freight.model.bo.FreightModelBo;
import cn.edu.xmu.freight.model.bo.WeightModelInfoBo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 重量运费模板返回值对象
 * @author 王薪蕾
 * @date 2020/12/9
 */
@Data
public class WeightModelInfoRetVo {
    private Long id;
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
    public WeightModelInfoRetVo(WeightModelInfoBo weightModelInfoBo){
        this.id=weightModelInfoBo.getId();
        this.firstWeight=weightModelInfoBo.getFirstWeight();
        this.firstWeightFreight=weightModelInfoBo.getFirstWeightFreight();
        this.tenPrice=weightModelInfoBo.getTenPrice();
        this.fiftyPrice=weightModelInfoBo.getFiftyPrice();
        this.hundredPrice=weightModelInfoBo.getHundredPrice();
        this.trihunPrice=weightModelInfoBo.getTrihunPrice();
        this.abovePrice=weightModelInfoBo.getAbovePrice();
        this.regionId=weightModelInfoBo.getRegionId();
        this.gmtCreate=weightModelInfoBo.getGmtCreate();
        this.gmtModified=weightModelInfoBo.getGmtModified();
    }

}
