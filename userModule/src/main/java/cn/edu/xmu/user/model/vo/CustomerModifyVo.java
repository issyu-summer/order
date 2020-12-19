package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.user.model.bo.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerModifyVo {
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "性别")
    private Byte gender;

    @ApiModelProperty(value = "生日")
    private String birthday;


    public Customer createCustomer(){
        Customer customer=new Customer();
        customer.setRealName(this.realName);
        customer.setGender(this.gender);
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        customer.setBirthday(LocalDate.parse(this.birthday));
        customer.setGmtModified(LocalDateTime.now());
        return customer;
    }
}
