package cn.edu.xmu.ooad.util;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 11:38
 */
public enum PaymentPatternCode {

    SIMULATION_PATTERN_CODE("002","模拟支付"),
    SHARED_PATTERN_CODE("001","返点支付");



    private String payPattern;
    private String name;
    PaymentPatternCode(String payPattern,String name){
        this.name=name;
        this.payPattern=payPattern;
    }

    public String getName(){return this.name;}
    public String getPayPattern(){return this.payPattern;}

    public  static String getPatternNameByPayment(String code) {
        for (PaymentPatternCode paymentPatternCode: PaymentPatternCode.values()) {
            if (paymentPatternCode.payPattern.equals(code)) {
                return paymentPatternCode.getName();
            }
        }
        return null;
    }
}
