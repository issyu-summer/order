package cn.edu.xmu.freight.model.vo;

import cn.edu.xmu.freight.model.bo.PieceModelInfoBo;
import cn.edu.xmu.freight.model.po.PieceFreightModelPo;
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
    private Integer firstItems;
    private Long firstItemsPrice;
    private Integer additionalItems;
    private Long additionalItemsPrice;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public PieceModelInfoRetVo(PieceModelInfoBo pieceModelInfoBo){
        this.id=pieceModelInfoBo.getId();
        this.regionId=pieceModelInfoBo.getRegionId();
        this.firstItems=pieceModelInfoBo.getFirstItems();
        this.firstItemsPrice=pieceModelInfoBo.getFirstItemsPrice();
        this.additionalItems=pieceModelInfoBo.getAdditionalItems();
        this.additionalItemsPrice=pieceModelInfoBo.getAdditionalItemsPrice();
        this.gmtCreate=pieceModelInfoBo.getGmtCreate();
        this.gmtModified=pieceModelInfoBo.getGmtModified();

    }

    public PieceModelInfoRetVo(PieceFreightModelPo pieceFreightModelPo){
        this.id=pieceFreightModelPo.getId();
        this.regionId=pieceFreightModelPo.getRegionId();
        this.firstItems=pieceFreightModelPo.getFirstItems();
        this.firstItemsPrice=pieceFreightModelPo.getFirstItemsPrice();
        this.additionalItems=pieceFreightModelPo.getAdditionalItems();
        this.additionalItemsPrice=pieceFreightModelPo.getAdditionalItemsPrice();
        this.gmtCreate=pieceFreightModelPo.getGmtCreate();
        this.gmtModified=pieceFreightModelPo.getGmtModified();

    }
}
