package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.user.model.bo.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStateRetVo {
    private Long code;

    private String name;

    public CustomerStateRetVo(Customer.State state){
        code=Long.valueOf(state.getCode());
        name=state.getDescription();
    }
}
