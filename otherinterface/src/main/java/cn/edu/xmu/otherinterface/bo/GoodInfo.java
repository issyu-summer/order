package cn.edu.xmu.otherinterface.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 10:56
 */
@Data
public class GoodInfo implements Serializable {
    private Long freightModelId;
    private Long weight;

    public GoodInfo(Long freightModelId, Long weight) {
        this.freightModelId = freightModelId;
        this.weight = weight;
    }
}
