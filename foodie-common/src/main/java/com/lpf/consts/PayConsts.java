package com.lpf.consts;

/** 分页相关常量
 * @author liupf
 * @date 2021-03-28 15:25
 */
public final class PayConsts {
    // 支付中心的调用地址
    private final static String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";		// produce

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url
    private final static String payReturnUrl = "http://api.z.mukewang.com/foodie-dev-api/orders/notifyMerchantOrderPaid";
}
