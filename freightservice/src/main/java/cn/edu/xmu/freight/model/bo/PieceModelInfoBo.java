package cn.edu.xmu.freight.model.bo;

import cn.edu.xmu.freight.model.po.PieceFreightModelPo;
import cn.edu.xmu.freight.model.vo.PieceModelInfoRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 陈星如
 * @date 12/9/20 11:19 AM
 */
@Data
public class PieceModelInfoBo implements VoObject, Serializable {
    private Long id;
    private Long regionId;
    private Integer firstItems;
    private Long firstItemsPrice;
    private Integer additionalItems;
    private Long additionalItemsPrice;
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

    public PieceFreightModelPo getpieceFreightModelPo() {
        PieceFreightModelPo pieceFreightModelPo=new PieceFreightModelPo();
        pieceFreightModelPo.setId(this.id);
        pieceFreightModelPo.setRegionId(this.regionId);
        pieceFreightModelPo.setFirstItems(this.firstItems);
        pieceFreightModelPo.setFirstItemsPrice(this.firstItemsPrice);
        pieceFreightModelPo.setAdditionalItems(this.additionalItems);
        pieceFreightModelPo.setAdditionalItemsPrice(this.additionalItemsPrice);
        pieceFreightModelPo.setGmtCreate(this.gmtCreate);
        pieceFreightModelPo.setGmtModified(this.gmtModified);
        return pieceFreightModelPo;
    }

    public PieceModelInfoRetVo getRetVo() {
        return new PieceModelInfoRetVo(this);
    }
}
