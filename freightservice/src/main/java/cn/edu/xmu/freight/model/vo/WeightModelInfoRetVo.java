package cn.edu.xmu.freight.model.vo;

import cn.edu.xmu.freight.model.bo.FreightModelBo;
import cn.edu.xmu.freight.model.bo.WeightModelInfoBo;
import cn.edu.xmu.freight.model.po.WeightFreightModelPo;
import cn.edu.xmu.ooad.util.TimeFormat;
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
    private String gmtCreate;
    private String gmtModified;

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
        this.gmtCreate=TimeFormat.localDateTimeToString(weightModelInfoBo.getGmtCreate());
        this.gmtModified=TimeFormat.localDateTimeToString(weightModelInfoBo.getGmtModified());
    }

    public WeightModelInfoRetVo(WeightFreightModelPo weightFreightModelPo){
        this.id=weightFreightModelPo.getId();
        this.firstWeight=weightFreightModelPo.getFirstWeight();
        this.firstWeightFreight=weightFreightModelPo.getFirstWeightFreight();
        this.tenPrice=weightFreightModelPo.getTenPrice();
        this.fiftyPrice=weightFreightModelPo.getFiftyPrice();
        this.hundredPrice=weightFreightModelPo.getHundredPrice();
        this.trihunPrice=weightFreightModelPo.getTrihunPrice();
        this.abovePrice=weightFreightModelPo.getAbovePrice();
        this.regionId=weightFreightModelPo.getRegionId();
        this.gmtCreate=TimeFormat.localDateTimeToString(weightFreightModelPo.getGmtCreate());
        this.gmtModified=TimeFormat.localDateTimeToString(weightFreightModelPo.getGmtModified());
    }
}
