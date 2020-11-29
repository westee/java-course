package com.github.westee.course.controller;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController("/api/v1")
@RequestMapping("/api/v1")
public class VideoController {
    @GetMapping("/course/{id}")
    public String getVideos(@PathVariable("id") Integer courseId) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-qingdao.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = "<yourAccessKeyId>";
        String accessKeySecret = "<yourAccessKeySecret>";
        String bucketName = "<yourBucketName>";
        String objectName = "<yourObjectName>";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 设置URL过期时间为1小时。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);

        return "<video href="+url+"><video>" ;
    }

    @GetMapping("/course/{id}/token")
    public Token getToken(@PathVariable("id") Integer courseId) {
        String accessKeyId = ""; // 请填写您的AccessKeyId。
        String secretAccessKey = ""; // 请填写您的AccessKeySecret。
        String endpoint = "oss-cn-qingdao.aliyuncs.com"; // 请填写您的 endpoint。
        String bucket = "sun-course"; // 请填写您的 bucketname 。
        String host = "http://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
        String callbackUrl = "http://88.88.88.88.:8888";
        String dir = "course-" + courseId + "/"; // 用户上传文件时指定的前缀。

        OSSClient client = new OSSClient(endpoint, (CredentialsProvider) (new DefaultCredentialProvider(accessKeyId, secretAccessKey)), (ClientConfiguration) null);

        try {
            long expireTimeSeconds = 3600 * 1000;
            long expireEndTimeMS = System.currentTimeMillis() + expireTimeSeconds;
            Date expiration = new Date(expireEndTimeMS);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Token token = new Token();
            token.setAccessid(accessKeyId);
            token.setPolicy(encodedPolicy);
            token.setSignature(postSignature);
            token.setDir(dir);
            token.setHost(host);
            token.setExpire(expireEndTimeMS / 1000);
            return token;
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static class Token {
        private String accessid;
        private String host;
        private String policy;
        private String signature;
        private Number expire;
        private String dir;

        public String getAccessid() {
            return accessid;
        }

        public void setAccessid(String accessid) {
            this.accessid = accessid;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPolicy() {
            return policy;
        }

        public void setPolicy(String policy) {
            this.policy = policy;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public Number getExpire() {
            return expire;
        }

        public void setExpire(Number expire) {
            this.expire = expire;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }
    }
}
