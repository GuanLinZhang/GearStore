package com.interconn.demo.Exception;

@SuppressWarnings("serial")
public class OrderException extends RuntimeException {
    public final static int SUCCESS = 0;
    public final static int USER_IS_NOT_EXIST = 4001;
    public final static int SERVER_NOT_RESPONSE = 4002;
    public final static int INTEGRAL_IS_NOT_ENOUGH = 4003;
    public final static int ORDER_NOT_FOUND = 4004;
    public static final int ORDER_IS_PAID = 4005;
    private int code;


    private static String getMessage(int code) {
        switch (code) {
            case USER_IS_NOT_EXIST:
                return "用户不存在";
            case SERVER_NOT_RESPONSE:
                return "服务器响应异常";
            case ORDER_NOT_FOUND:
                return "订单不存在";
            case INTEGRAL_IS_NOT_ENOUGH:
                return "用户积分不足";
            case ORDER_IS_PAID:
                return "订单已被支付";
            default:
                return null;
        }
    }

    public int getCode() {
        return code;
    }

    public OrderException(int code) {
        super(getMessage(code));
        this.code = code;
    }
}
