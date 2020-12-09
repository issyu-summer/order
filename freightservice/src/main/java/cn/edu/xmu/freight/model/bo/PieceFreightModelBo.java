package cn.edu.xmu.freight.model.bo;


import cn.edu.xmu.freight.model.po.PieceFreightModelPo;
import lombok.Data;

import java.time.LocalDateTime;

/*
 * @author 史韬韬
 * @date 2020/12/9
 * 按件计费模板的bo
 */
@Data
public class PieceFreightModelBo {
    private Long id;
    private Long freightModelId;
    private Integer firstItems;
    private Long firstItemsPrice;
    private Integer additionalItems;
    private Long additionalItemsPrice;
    private Long regionId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    public PieceFreightModelBo(PieceFreightModelPo pieceFreightModelPo){
        id=pieceFreightModelPo.getId();
        firstItems=pieceFreightModelPo.getFirstItems();
        firstItemsPrice=pieceFreightModelPo.getFirstItemsPrice();
        additionalItems=pieceFreightModelPo.getAdditionalItems();
        additionalItemsPrice=pieceFreightModelPo.getAdditionalItemsPrice();
        regionId=pieceFreightModelPo.getRegionId();
        gmtCreate=pieceFreightModelPo.getGmtCreate();
        gmtModified=pieceFreightModelPo.getGmtModified();
    }
    public PieceFreightModelPo createClonePo(){
        PieceFreightModelPo pieceFreightModelPo=new PieceFreightModelPo();
        pieceFreightModelPo.setAdditionalItems(additionalItems);
        pieceFreightModelPo.setAdditionalItemsPrice(additionalItemsPrice);
        pieceFreightModelPo.setFirstItems(firstItems);
        pieceFreightModelPo.setFirstItemsPrice(firstItemsPrice);
        pieceFreightModelPo.setGmtCreate(gmtCreate);
        pieceFreightModelPo.setGmtModified(gmtModified);
        pieceFreightModelPo.setRegionId(regionId);
        return pieceFreightModelPo;
    }
}
