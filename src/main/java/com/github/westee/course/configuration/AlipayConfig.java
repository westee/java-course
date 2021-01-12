package com.github.westee.course.configuration;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000116694932";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCnIXhpL0zaD/3lAKfvyZgEnEnUaI+gxztqsHztXQVSQ/DxySZ5Do3gnie24/8J4N9I1wKppHs1FuwK45WP9WrdP1hM8SxP2ljO/8hNrZA3z6RBFfMpGdPd2/k1UmcWC9zpgCHwWfwIZejJ+IWKGxpELtP40tCKHIRxREwAyOTuLThz9p959kN/RZwt0fBKehzp8yb1n6jQW5rqaortm+wd91mTUTb0zOePqbs2h6E+Dbu+IgIkpkNeKwVRUnjmYAumZ3GpChd6hpy3BwS1IPOdpgzGz+Cc9WRH065FLMxTlRIbef0z+R5bzV9MHzZzo821WtQr1hIwFVgyK0HkGiH5AgMBAAECggEAIvsRbcqVpkqUlJrXi4vBQq3Hkiq0bS1mFUWNUE0Y8QgBZUn5wHq4XVwc6d28or+rXF6MUx93HjyG+Rx7UF9Il7hLbgdIhAoqbmEA9qkuN1BBi9HhTCf4vUXuWie+JdIj0cec0uayprtVxvg6UxarH7xBZ6IQPegMHHzz/3IM4IDEWWeqO3NCQ+Yg3l/uBwMZyJ84TSlNhi/XQsKsqllJR2hlhxlFfCoGsg+/CYwYUi4uhDtbcxGpjAMUMMQmg2/hZ3xYw7E1zFsebISr3zZgJlQnk0iXl69MFo7lsijD2/Hvp+B4ikIenW9HLwNFxj4usjtCOUOOMq4bpJrWA+vIhQKBgQD6HxwFfCq2fVw7sQFLzuFdwlkbY1bhsKBNseX4xvbuDzMwdEK1ccM8Hc2lnZhaUfyD+Ff5v3oDLMJ5DDrXr6htyjvbkaO3ejyBJM8qDp2ZH9j4dJei90ux50vyR5vwJCGX8TyWXsAjEu1NQ2R0Sdm8557oWHlrIr0F5Q0nshSh7wKBgQCrDwkUQBhyk+ebtuXfm7HQnBiQG9Z5Fb16E0lToSr2UXlJNl7Wz+twNPRFDUb1SP/d8KRIln87xqT+V0OATMnHV0tm6SKGpD7Ii975R08ECAg0mZVyLZ/r77Z697c7F4wxxlpu+Zpi0QYOhSDeF5M3X05cbaJZojuEu+RdvcFClwKBgBDsHec5GWKzZJQdS6snWhQP3BuAQxaN7qqTCK4mN/qvSJVUqFbCVaUxlwJHgVFLDryXBsy86bJhSt8Jtsl09v5Gmnomhl8ZhBJwvRtpVbxQbUuSB9hcxuBId9jyg1hh+WvgRGsgCfvVgfyiPJ+ph3PCb/vkUIKxHPzTHGq9H6w9AoGATM1uZSI2F9OijL58zX4JQ0GioaluDWWech4QsTTOplOEYkPAJzCpY/LSreI9Nso034A5MsInlD4nPG8Pxp4zhoYCO813C/7YlZB/Z2sF5ih5Dqzada5YfEVfqCxqn2R+NKb7WZJGKBHzZzeMv0rUdWscMtGisB3wHTODYDdfezcCgYA4m0UjBvxPy7CEZqiyLQrZM5Ql4tDZSE5YTjhtwvhVAdLlYusjKdHA0/q/GEQ/GfXjt2e5b1R2nMwljusFTTJM/UleiqaWbqjP7CP3DtOaLJQKlJxbKJY+SAKPUOJQskRM2SXTUNfwY4ZOSBae7W4D0GqOh0LKd6QaqOvC1L0E8Q==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApyF4aS9M2g/95QCn78mYBJxJ1GiPoMc7arB87V0FUkPw8ckmeQ6N4J4ntuP/CeDfSNcCqaR7NRbsCuOVj/Vq3T9YTPEsT9pYzv/ITa2QN8+kQRXzKRnT3dv5NVJnFgvc6YAh8Fn8CGXoyfiFihsaRC7T+NLQihyEcURMAMjk7i04c/afefZDf0WcLdHwSnoc6fMm9Z+o0Fua6mqK7ZvsHfdZk1E29Mznj6m7NoehPg27viICJKZDXisFUVJ45mALpmdxqQoXeoactwcEtSDznaYMxs/gnPVkR9OuRSzMU5USG3n9M/keW81fTB82c6PNtVrUK9YSMBVYMitB5Boh+QIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8080/";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
//    public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

