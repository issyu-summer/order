package cn.edu.xmu.outer.model.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 23:43
 */
@Data
public class Aftersale implements Serializable {
    Long id;
    Long orderItemId;
    Long customerId;
    Long shopId;
    Byte type;
    String reason;
    String consignee;
    String conflusion;
    Long refund;
    Long quantility;
    Long regionId;
    String detail;
    String mobile;
    String customerLogSn;
    String shopLogSn;
    Byte state;
    Byte beDeleted;
}
