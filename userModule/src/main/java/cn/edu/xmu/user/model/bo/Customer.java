package cn.edu.xmu.user.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.user.model.po.CustomerPo;
import cn.edu.xmu.user.model.vo.CustomerListRetVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements VoObject {

    /**
     * 用户状态
     */
    public enum State {
        BACKSTAGE_USER(0, "后台用户"),
        NORM(4, "正常"),
        FORBID(6, "封禁");

        private static final Map<Integer, Customer.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Customer.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Customer.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    private Long id;

    private String userName;

    private String password;

    private String realName;

    private Byte gender;

    private LocalDate birthday;

    private Integer point;

    private State state = State.NORM;

    private String email;

    private String mobile;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


    public Customer(CustomerPo customerPo){
        this.id=customerPo.getId();
        this.userName = customerPo.getUserName();
        this.password = customerPo.getPassword();
        this.realName = customerPo.getRealName();
        this.gender = customerPo.getGender();
        this.birthday = customerPo.getBirthday();
        this.point = customerPo.getPoint();
        this.state = State.getTypeByCode(Integer.valueOf(customerPo.getState()));
        this.email = customerPo.getEmail();
        this.mobile = customerPo.getMobile();
        this.gmtCreate=customerPo.getGmtCreate();
        this.gmtModified=customerPo.getGmtModified();
    }

    public CustomerPo createPo(){
        CustomerPo customerPo=new CustomerPo();
        customerPo.setId(this.id);
        customerPo.setUserName(this.userName);
        customerPo.setPassword(this.password);
        customerPo.setRealName(this.realName);
        customerPo.setGender(this.gender);
        customerPo.setBirthday(this.birthday);
        customerPo.setPoint(this.point);
        customerPo.setState((byte)this.state.code);
        customerPo.setEmail(this.email);
        customerPo.setMobile(this.mobile);
        customerPo.setGmtCreate(this.gmtCreate);
        customerPo.setGmtModified(this.gmtModified);
        customerPo.setBeDeleted((byte)0);
        return customerPo;
    }

    @Override
    public Object createVo() {
        return new CustomerListRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
