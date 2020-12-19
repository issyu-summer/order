package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.user.model.bo.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CustomerPasswordVo {
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "真实姓名")
    private String captcha;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码")
    private String newPassword;

    public CustomerPasswordVo(String captcha, String newPassword) {
        this.captcha = captcha;
        this.newPassword = newPassword;
    }

    public Customer createCustomer(){
        Customer customer=new Customer();
        customer.setPassword(this.newPassword);
        customer.setGmtModified(LocalDateTime.now());
        return customer;
    }
}
