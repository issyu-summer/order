package cn.edu.xmu.outer.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSkuInfo implements Serializable {

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
