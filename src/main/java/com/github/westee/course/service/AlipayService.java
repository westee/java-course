package com.github.westee.course.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.westee.course.configuration.AlipayConfig;
import com.github.westee.course.model.CourseOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

public class AlipayService {
    AlipayClient alipayClient;


    @PostConstruct
    public void setup() {
        alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key,
                "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);
    }

    public String getPayPageHtml(CourseOrder order) {
        try {
            BizContent content = new BizContent();
            content.setOut_trade_no(order.getId().toString());

            BigDecimal price = new BigDecimal(order.getCourse().getPrice());
            content.setTotal_amount(price.toString());
            content.setSubject(order.getCourse().toString());
        } catch (Exception e) {

        }
        return  null;
    }

    private static class BizContent {
        private String out_trade_no;
        private String total_amount;
        private String subject;
        private String body;
        private String product_code;

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getProduct_code() {
            return product_code;
        }

        public void setProduct_code(String product_code) {
            this.product_code = product_code;
        }
    }

    @GetMapping("/showPay")
    @ResponseBody
    public String showPay(@RequestParam("courseId") Integer courseId) throws AlipayApiException {
        //获得初始化的AlipayClient


        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
//        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = "test" + courseId;
        System.out.println(courseId);
        //付款金额，必填
        String total_amount = "1";
        //订单名称，必填
        String subject = "旭东的第一个订单";
        //商品描述，可空
        String body = "";

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //输出
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "<title>付款</title>\n" +
                "</head>" + result +
                "<body>\n" +
                "</body>\n" +
                "</html>";
    }
}
