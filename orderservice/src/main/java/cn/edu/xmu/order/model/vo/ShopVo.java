package cn.edu.xmu.order.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
* @author 史韬韬
* @Date 2020/12/6
* 改动请通知我
 */
@Data
public class ShopVo {
    private Long id;
    private String name;
    private LocalDateTime gmtCreateTime;
    private LocalDateTime gmtModiTime;
}
