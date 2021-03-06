package com.framework.aop;

import com.yemao.common.base.BaseObject;
import com.yemao.common.constant.Constant;
import com.yemao.utility.Util;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 记录Web服务日志
 */
@Aspect
@Component
public class WebLogAspect extends BaseObject {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    ThreadLocal<WebLog> webLog = new ThreadLocal<>();

    @Pointcut("execution(public * com.yemao.*.web..*.*(..))")
    public void webLog() {
    }


    private static class WebLog {
        String url;
        String method;
        String ip;
        String host;
        String token;
        Map<String, String[]> params;
        String class_method;
        String class_method_ars;
        long execution_time;
        Object return_value;

        public WebLog(HttpServletRequest request) {
            this.url = request.getRequestURL().toString();
            this.method = request.getMethod();
            this.ip = request.getRemoteAddr();
            this.host = request.getRemoteHost();
            this.token = request.getHeader(Constant.AUTHORIZATION);
            this.params = new HashMap<>(request.getParameterMap());
            if (params.containsKey("file")) {
                params.put("file", new String[]{"文件参数"});
            }
        }

        @Override
        public String toString() {
            return Util.toJson(this);
        }
    }


    @Before("webLog()")
    public void before(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        WebLog log = new WebLog(request);
        log.class_method = joinPoint.getSignature().getDeclaringTypeName() +
                "." + joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        if (args.length() > 1000)
            log.class_method_ars = "参数过长";
        else log.class_method_ars = args;
        logger.info("收到请求(未执行): " + log.toString());
        webLog.set(log);
    }


    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        WebLog log = webLog.get();
        if (ret != null && ret.toString().length() > 10000) {
            log.return_value = "返回值过长";
        } else log.return_value = ret;
        log.execution_time = System.currentTimeMillis() - startTime.get();
        logger.info("返回响应(执行后): " + log.toString());
    }

}
