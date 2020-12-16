package cn.edu.xmu.ooad.util;

/**
 * @author issyu 30320182200070
 * @date 2020/12/3 11:10
 */

public enum OrderStateCode {
    ORDER_STATE_CANCEL(11, "新订单"),
    ORDER_STATE_DEPOSIT_UNPAID(1, "待付款"),
    ORDER_STATE_UNPAID(2, "待收货"),
    ORDER_STATE_GROUP_WAITING(3,"已完成"),
    ORDER_STATE_DEPOSIT_PAID(4,"已取消"),
    ORDER_STATE_REST_UNPAID(12,"待支付尾款"),
    ORDER_STATE_PRESELL_SUSPEND(21,"付款完成"),
    ORDER_STATE_GROUP(22,"待成团"),
    ORDER_STATE_GROUP_NOT_UP_TO_STANDARD(24,"已发货"),
    ORDER_STATE_GROUPED(23,"未成团");

    private int code;
    private String message;

    OrderStateCode(int code,String message){
        this.code=code;
        this.message=message;
    }
    public int getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

    public  static String getOrderStateMessageByCode(Byte code) {
        for (OrderStateCode orderStateCode: OrderStateCode.values()) {
            if (orderStateCode.code==code.intValue()) {
                return orderStateCode.getMessage();
            }
        }
        return null;
    }

    public  static OrderStateCode getOrderStateByCode(Byte code) {
        for (OrderStateCode orderStateCode: OrderStateCode.values()) {
            if (orderStateCode.code==code.intValue()) {
                return orderStateCode;
            }
        }
        return null;
    }
}
