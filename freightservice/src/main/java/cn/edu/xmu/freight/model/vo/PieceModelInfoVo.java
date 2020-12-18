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
    private Integer firstItem;
    private Long firstItemPrice;
    private Integer additionalItems;
    private Long additionalItemsPrice;
    public PieceModelInfoVo(){

    }
    public PieceModelInfoVo(PieceModelInfoBo pieceModelInfoBo){
        this.setRegionId(pieceModelInfoBo.getRegionId());
        this.setFirstItem(pieceModelInfoBo.getFirstItems());
        this.setFirstItemPrice(pieceModelInfoBo.getFirstItemsPrice());
        this.setAdditionalItems(pieceModelInfoBo.getAdditionalItems());
        this.setAdditionalItemsPrice(pieceModelInfoBo.getAdditionalItemsPrice());
    }
    public PieceModelInfoBo createPieceModelInfoBo(){
        PieceModelInfoBo pieceModelInfoBo = new PieceModelInfoBo();
        pieceModelInfoBo.setRegionId(this.getRegionId());
        pieceModelInfoBo.setFirstItems(this.getFirstItem());
        pieceModelInfoBo.setFirstItemsPrice(this.getFirstItemPrice());
        pieceModelInfoBo.setAdditionalItems(this.getAdditionalItems());
        pieceModelInfoBo.setAdditionalItemsPrice(this.getAdditionalItemsPrice());
        return pieceModelInfoBo;
    }
}
