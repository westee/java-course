package com.github.westee.course.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.westee.course.configuration.AlipayConfig;
import com.github.westee.course.model.AlipayQueryResult;
import com.github.westee.course.model.CourseOrder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class AlipayService {
    AlipayClient alipayClient;
    ObjectMapper objectMapper;


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
            content.setSubject("课程："+order.getCourse().getName());
            content.setBody(order.getCourse().getDescription());

            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(AlipayConfig.return_url + "?orderId=" + order.getId());

            alipayRequest.setBizContent(objectMapper.writeValueAsString(content));

            return "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                    "<title>付款</title>\n" +
                    "</head>\n" +
                    alipayClient.pageExecute(alipayRequest).getBody() +
                    "<body>\n" +
                    "</body>\n" +
                    "</html>";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String checkOrderStatus(CourseOrder order, String alipayTradeNo) throws AlipayApiException, JsonProcessingException {
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" +  order.getId()
                + "\"," + "\"trade_no\":\"" + alipayTradeNo + "\"}");
        String result = alipayClient.execute(alipayRequest).getBody();
        AlipayQueryResult response = objectMapper.readValue(result, AlipayQueryResult.class);
        return response.getAlipay_trade_query_response().getTrade_status();

    }

    private static class BizContent {
        private String out_trade_no;
        private String total_amount;
        private String subject;
        private String body;

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
            return "FAST_INSTANT_TRADE_PAY";
        }

    }
}
