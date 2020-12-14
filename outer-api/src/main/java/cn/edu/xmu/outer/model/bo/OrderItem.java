package cn.edu.xmu.outer.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements Serializable {

    private Long id;

    private int quantity;

    private Long price;

    private Long beShareId;

    private Long skuId;

    private Long orderId;
    
}
