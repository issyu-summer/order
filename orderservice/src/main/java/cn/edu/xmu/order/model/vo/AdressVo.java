package cn.edu.xmu.order.model.vo;

import lombok.Data;

@Data
public class AdressVo {
    private String consignee;
    private Long regionId;
    private String address;
    private String mobile;
}
