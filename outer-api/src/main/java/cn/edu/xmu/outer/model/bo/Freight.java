package cn.edu.xmu.outer.model.bo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author issyu 30320182200070
 * @date 2020/12/12 23:37
 */
@Data
public class Freight implements Serializable {
    private Long id;
    private String name;
    private Byte type;
    private Integer unit;
    private Byte isDefault;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

}
