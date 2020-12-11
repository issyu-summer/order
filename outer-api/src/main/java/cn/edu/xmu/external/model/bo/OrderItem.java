package cn.edu.xmu.external.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private Long id;

    private int quantity;

    private int price;

    private Long beShareId;

    private Long skuId;

    private Long orderId;
    
}
