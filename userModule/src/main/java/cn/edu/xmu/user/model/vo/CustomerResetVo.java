package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.user.model.bo.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResetVo {
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(value = "邮箱")
    private String email;



    public Customer createCustomer(){
        Customer customer=new Customer();
        customer.setEmail(this.email);
        customer.setUserName(this.userName);
        customer.setPassword("123456");
        customer.setGmtModified(LocalDateTime.now());
        return customer;
    }
}
