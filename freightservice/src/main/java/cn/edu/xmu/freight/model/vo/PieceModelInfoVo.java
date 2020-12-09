package cn.edu.xmu.freight.model.vo;

import cn.edu.xmu.freight.model.bo.PieceModelInfoBo;
import lombok.Data;

/**
 * 件数运费模板明细传入值对象
 * @author 陈星如
 * @date 12/9/20 11:14 AM
 */
@Data
public class PieceModelInfoVo {
    private Long regionId;
    private Integer firstItems;
    private Long firstItemsPrice;
    private Integer additionalItems;
    private Long additionalItemsPrice;
    public PieceModelInfoVo(){

    }
    public PieceModelInfoVo(PieceModelInfoBo pieceModelInfoBo){
        this.setRegionId(pieceModelInfoBo.getRegionId());
        this.setFirstItems(pieceModelInfoBo.getFirstItems());
        this.setFirstItemsPrice(pieceModelInfoBo.getFirstItemsPrice());
        this.setAdditionalItems(pieceModelInfoBo.getAdditionalItems());
        this.setAdditionalItemsPrice(pieceModelInfoBo.getAdditionalItemsPrice());
    }
    public PieceModelInfoBo createPieceModelInfoBo(){
        PieceModelInfoBo pieceModelInfoBo = new PieceModelInfoBo();
        pieceModelInfoBo.setRegionId(this.getRegionId());
        pieceModelInfoBo.setFirstItems(this.getFirstItems());
        pieceModelInfoBo.setFirstItemsPrice(this.getFirstItemsPrice());
        pieceModelInfoBo.setAdditionalItems(this.getAdditionalItems());
        pieceModelInfoBo.setAdditionalItemsPrice(this.getAdditionalItemsPrice());
        return pieceModelInfoBo;
    }
}
