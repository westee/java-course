package com.github.westee.course.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.westee.course.configuration.AlipayConfig;
import com.github.westee.course.configuration.Config;
import com.github.westee.course.dao.CourseDao;
import com.github.westee.course.dao.CourseOrderDao;
import com.github.westee.course.model.*;
import com.github.westee.course.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
public class OrderController {
    @Autowired
    CourseOrderDao courseOrderDao;

    @Autowired
    CourseDao courseDao;

    @Autowired
    AlipayService alipayService;

    @GetMapping("/showPay")
    @ResponseBody
    public String showPay(@RequestParam("courseId") Integer courseId, HttpServletResponse response) throws AlipayApiException, IOException {
        User user = Config.UserContext.getCurrentUserOr401();
        Optional<CourseOrder> order = courseOrderDao.findByCourseIdAndUserId(courseId, user.getId());
        if (order.isEmpty()) {
            Course course = courseDao.findById(courseId).orElseThrow(
                    () -> new HttpException(404, "课程不存在: " + courseId)
            );
            CourseOrder newOrder = new CourseOrder();
            newOrder.setPrice(course.getPrice());
            newOrder.setStatus(OrderStatus.UNPAID);
            newOrder.setCourse(course);
            newOrder.setUser(user);
            courseOrderDao.save(newOrder);
            return alipayService.getPayPageHtml(newOrder);
        } else {
            if (order.get().getStatus() == OrderStatus.PAID) {
                // 已经付过款，无需再次付款
                response.sendRedirect("http://localhost:8080/api/v1/course/" + courseId);
                return "";
            } else {
                return alipayService.getPayPageHtml(order.get());
            }
        }
    }

    @GetMapping("/checkPay")
    @ResponseBody
    public void checkPay(
            @RequestParam("orderId") Integer orderId,
            @RequestParam("trade_no") String alipayTradeNo,
            HttpServletResponse response
    ) throws IOException, AlipayApiException {
        User user = Config.UserContext.getCurrentUserOr401();
        CourseOrder order = courseOrderDao.findById(orderId).orElseThrow(
                () -> new HttpException(404, "Not found")
        );

        if (!user.getId().equals(order.getUser().getId())) {
            throw new HttpException(403, "Forbidden");
        }

        if (order.getStatus() == OrderStatus.UNPAID) {
            // 如果能从支付宝处查到交易记录，则将订单状态设置为PAID
            String status = alipayService.checkOrderStatus(order, alipayTradeNo);
            if ("TRADE_SUCCESS".equals(status)) {
                order.setStatus(OrderStatus.PAID);
                courseOrderDao.save(order);
            }
        }
        response.sendRedirect("http://localhost:8080/api/v1/course/" + order.getCourse().getId());
    }
}
