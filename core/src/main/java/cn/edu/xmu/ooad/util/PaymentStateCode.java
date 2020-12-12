package cn.edu.xmu.ooad.util;

/**
 * @author issyu 30320182200070
 * @date 2020/12/8 17:13
 */
public enum PaymentStateCode {

    //0：已支付，1：未支付，2：支付失败
    PAYED_STATE(0,"已支付"),
    NOT_PAYED_STATE(1,"未支付"),
    FAILED_PAY_STATE(3,"支付失败");


    private int code;
    private String message;

    PaymentStateCode(int code, String message){
        this.code=code;
        this.message=message;
    }

    /**
     * 通过状态码获取状态信息
     * @param code
     * @return
     */
    public static String getMessageByCode(Byte code){
        for(PaymentStateCode paymentStateCode : PaymentStateCode.values()){
            if(paymentStateCode.getCode().equals(code)){
                return paymentStateCode.getMessage();
            }
        }
        return null;
    }

    public String getMessage(){return this.message;}
    public Byte getCode(){return (byte)this.code;}
}
