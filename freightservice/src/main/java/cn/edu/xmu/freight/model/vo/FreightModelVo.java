package cn.edu.xmu.freight.model.vo;

import lombok.Data;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 17:04
 */
@Data
public class FreightModelVo {
    private Long shopId;
    private String name;

    public FreightModelVo(Long shopId, String name){
        this.setShopId(shopId);
        if(name!=null){
            this.setName(name);
        }
    }
}
