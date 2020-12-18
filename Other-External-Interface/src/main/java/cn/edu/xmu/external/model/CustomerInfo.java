package cn.edu.xmu.external.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfo {

    public Long Id;

    public String userName;

    public String realName;

}
