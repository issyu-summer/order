package cn.edu.xmu.external.model;

import lombok.Data;

@Data
public class Sku {
    Long id;
    String name;
    String skuSn;
    String imgUrl;
    int inventory;
    int originalPrice;
    int price;
    boolean disable;

    public Sku(Long id, String name, String skuSn, String imgUrl, int inventory, int originalPrice, int price, boolean disable) {
        this.id = id;
        this.name = name;
        this.skuSn = skuSn;
        this.imgUrl = imgUrl;
        this.inventory = inventory;
        this.originalPrice = originalPrice;
        this.price = price;
        this.disable = disable;
    }

    public Sku() {
    }
}
