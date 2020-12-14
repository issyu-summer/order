package cn.edu.xmu.order.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 3:29
 */
@Data
public class ShopRetVo {
    private Long id;
    private String name;
    private LocalDateTime gmtCreateTime;
    private LocalDateTime gmtModiTime;
}
