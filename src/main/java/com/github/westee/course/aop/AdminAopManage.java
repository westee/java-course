package com.github.westee.course.aop;

import com.github.westee.course.configuration.Config;
import com.github.westee.course.model.HttpException;
import com.github.westee.course.model.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;


@Aspect
@Configuration
public class AdminAopManage {
    @Around("@annotation(com.github.westee.course.annotation.Admin)")
    public Object checkAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        User currentUser = Config.UserContext.getCurrentUser();

        if (currentUser.getRoles().stream().anyMatch(role -> "管理员".equals(role.getName())) ) {
            return joinPoint.proceed();
        } else {
            throw new HttpException(403, "没有权限");
        }

    }

    @Around("@annotation(com.github.westee.course.annotation.PermissionRequired)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        User currentUser = Config.UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new HttpException(401, "没有登录！");
        } else if (currentUser.getRoles().stream().anyMatch(role -> "管理员".equals(role.getName()))) {
            // 检查当前用户是否有当前方法要求的角色
            return joinPoint.proceed();
        } else {
            throw new HttpException(403, "没有权限！");
        }
    }
}
