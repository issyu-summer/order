package cn.edu.xmu.ooad.util;

/**
 * @author issyu 30320182200070
 * @date 2020/12/3 11:10
 */

public enum OrderStateCode {
    ORDER_STATE_CREATE(6, "创建订单"),
    ORDER_STATE_CANCEL(0, "订单取消"),
    ORDER_STATE_DEPOSIT_UNPAID(1, "待支付定金"),
    ORDER_STATE_UNPAID(2, "待支付"),
    ORDER_STATE_GROUP_WAITING(3,"待参团"),
    ORDER_STATE_DEPOSIT_PAID(4,"已支付定金"),
    ORDER_STATE_REST_UNPAID(5,"待支付尾款"),
    ORDER_STATE_PRESELL_SUSPEND(7,"预售中止"),
    ORDER_STATE_GROUP(8,"已参团"),
    ORDER_STATE_GROUP_NOT_UP_TO_STANDARD(9,"团购未达门槛"),
    ORDER_STATE_GROUPED(10,"已成团"),
    ORDER_STATE_PAID(11,"已支付"),
    ORDER_STATE_REST_PAID(12,"已支付尾款"),
    ORDER_STATE_REFUNDED(13,"已退款"),
    ORDER_STATE_DISCONTINUE(14,"订单中止"),
    ORDER_STATE_AFTERSALE_DROP_SHIPPING(15,"售后单代发货"),
    ORDER_STATE_DESPATCHING(16,"发货中"),
    ORDER_STATE_AOG(17,"到货"),
    ORDER_STATE_SIGNED(18,"已签收");

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
