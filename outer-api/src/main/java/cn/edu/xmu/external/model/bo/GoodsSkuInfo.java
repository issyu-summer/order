package cn.edu.xmu.external.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSkuInfo {

  private Long id;

  private String skuName;

  private String spuName;

  private String skuSn;

  private String imgUrl;

  private int inventory;

  private int originalPrice;

  private int price;

  private boolean disable;
    
}
