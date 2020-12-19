package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.user.model.bo.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListRetVo {
    private Long id;

    private String userName;

    private String name;

    public CustomerListRetVo(Customer customer){
        this.id=customer.getId();
        this.userName= customer.getUserName();
        this.name = customer.getRealName();
    }
}
