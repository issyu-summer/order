package cn.edu.xmu.user.model.vo;

import cn.edu.xmu.user.model.bo.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRetVo {
    private Long id;

    private String userName;

    private String name;

    private Byte gender;

    private LocalDate birthday;

    private String email;

    private String mobile;

    private Integer state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public CustomerRetVo(Customer customer){
        this.id= customer.getId();
        this.userName= customer.getUserName();
        this.name = customer.getRealName();
        this.gender= customer.getGender();
        this.birthday= customer.getBirthday();
        this.email = customer.getEmail();
        this.mobile = customer.getMobile();
        this.state = customer.getState().getCode();
        this.gmtCreate= customer.getGmtCreate();
        this.gmtModified=customer.getGmtModified();
    }
}
