package cn.edu.xmu.freight.model.vo;

import cn.edu.xmu.freight.model.bo.PieceModelInfoBo;
import cn.edu.xmu.freight.model.po.PieceFreightModelPo;
import cn.edu.xmu.ooad.util.TimeFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 件数运费模板明细返回值对象
 * @author 陈星如
 * @date 12/9/20 11:15 AM
 */
@Data
public class PieceModelInfoRetVo {
    private Long id;
    private Long regionId;
    private Integer firstItem;
    private Long firstItemPrice;
    private Integer additionalItems;
    private Long additionalItemsPrice;
    private String gmtCreate;
    private String gmtModified;

    public PieceModelInfoRetVo(PieceModelInfoBo pieceModelInfoBo){
        this.id=pieceModelInfoBo.getId();
        this.regionId=pieceModelInfoBo.getRegionId();
        this.firstItem =pieceModelInfoBo.getFirstItems();
        this.firstItemPrice =pieceModelInfoBo.getFirstItemsPrice();
        this.additionalItems=pieceModelInfoBo.getAdditionalItems();
        this.additionalItemsPrice=pieceModelInfoBo.getAdditionalItemsPrice();
        this.gmtCreate= TimeFormat.localDateTimeToString(pieceModelInfoBo.getGmtCreate());
        this.gmtModified=TimeFormat.localDateTimeToString(pieceModelInfoBo.getGmtModified());
    }

    public PieceModelInfoRetVo(PieceFreightModelPo pieceFreightModelPo){
        this.id=pieceFreightModelPo.getId();
        this.regionId=pieceFreightModelPo.getRegionId();
        this.firstItem =pieceFreightModelPo.getFirstItems();
        this.firstItemPrice =pieceFreightModelPo.getFirstItemsPrice();
        this.additionalItems=pieceFreightModelPo.getAdditionalItems();
        this.additionalItemsPrice=pieceFreightModelPo.getAdditionalItemsPrice();
        this.gmtCreate=TimeFormat.localDateTimeToString(pieceFreightModelPo.getGmtCreate());
        this.gmtModified=TimeFormat.localDateTimeToString(pieceFreightModelPo.getGmtModified());
    }
}
