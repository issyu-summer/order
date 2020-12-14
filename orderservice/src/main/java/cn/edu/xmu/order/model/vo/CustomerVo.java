package cn.edu.xmu.order.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
* @author 史韬韬
* @Date 2020/12/6
* 改动请通知我
 */
@Data
public class CustomerVo {
    @JsonProperty(value = "id")
    private Long customerId;
    private String userName;
    private String realName;
}
