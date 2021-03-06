package com.framework.annotation;

import com.yemao.common.Response;
import com.yemao.common.constant.Constant;
import com.yemao.common.constant.HttpStatus;
import com.yemao.users.models.Token;
import com.yemao.users.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 *
 * 用户认证拦截器. 使用请求头部
 * @author Aollio
 * @date 15/05/2017
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    @Autowired
    private TokenService manager;


    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //if no 'Authorization' annotation, do nothing
        if (method.getAnnotation(Authorization.class) == null) {
            return true;
        }

        // get tokens from header
        String authorization = request.getParameter(Constant.AUTHORIZATION);
        // parse token string from header to `Token`
        Token model = manager.getToken(authorization);
        //check authorization
        boolean result = manager.checkToken(model);
        //success. set uid to the request
        if (result) {
            logger.info("用户认证成功, 用户uid是:  " + model.getUid());
            request.setAttribute(Constant.CURRENT_USER_ID, model.getUid());
            return true;
        }

        if (method.getAnnotation(Authorization.class) != null) {
            logger.info("用户认证失败, 相关信息:  " + model);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().append(Response.error(HttpStatus.UNAUTHORIZATON, "unauentication").toString()).flush();
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
