package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.user.model.bo.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
public class CustomerCreateVo {

    @Pattern(regexp = "^1\\d{10}$",
            message = "电话号码格式不正确")
    @NotBlank(message = "电话号码不能为空")
    @ApiModelProperty(value = "电话号码")
    private String mobile;

    @Pattern(regexp = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.]){1,2}[A-Za-z\\d]{2,5}$",
            message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank(message = "真实姓名不能为空")
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(value = "性别")
    private Byte gender;

    @Pattern(regexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)",
            message = "日期格式不正确")
    @NotBlank(message = "生日不能为空")
    @ApiModelProperty(value = "生日")
    private String birthday;

    public CustomerCreateVo(String mobile, String email, String userName,String password, String realName, Byte gender, String birthday) {
        this.mobile = mobile;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.realName = realName;
        this.gender = gender;
        this.birthday = birthday;
    }

    public CustomerCreateVo(){

    }

    public Customer createCustomer(){
        Customer customer=new Customer();
        customer.setMobile(this.mobile);
        customer.setEmail(this.email);
        customer.setUserName(this.userName);
        customer.setPassword(this.password);
        customer.setRealName(this.realName);
        customer.setGender(this.gender);
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        customer.setBirthday(LocalDate.parse(this.birthday));
        customer.setGmtCreate(LocalDateTime.now());
        customer.setGmtCreate(LocalDateTime.now());
        return customer;
    }
}
