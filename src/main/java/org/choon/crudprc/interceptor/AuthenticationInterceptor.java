package org.choon.crudprc.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.choon.crudprc.annotation.NoAuth;
import org.choon.crudprc.exception.TokenException;
import org.choon.crudprc.util.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    public AuthenticationInterceptor(JwtProvider jwtProvider, ObjectMapper objectMapper) {
        this.jwtProvider = jwtProvider;
        this.objectMapper = objectMapper;
    }

    // preHandle의 return value가 false인 경우엔 controller로 접근하지 못함
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        System.out.println("[CHECK] : 인증용 인터셉터 입장!!!!!");
        boolean check = checkAnnotation(handler, NoAuth.class);
        if (check) return true;

        String token = req.getHeaders(HttpHeaders.AUTHORIZATION).nextElement();
        System.out.println("token = " + token);

        if (token == null || token.isEmpty()) {
            throw new TokenException("토큰 없다!!");
        }

        String resolveToken = jwtProvider.resolveToken(token);
        if (!jwtProvider.validateToken(resolveToken)) {
            throw new TokenException("토큰 만료됨!!");
        }

        return true;
    }

    private boolean checkAnnotation(Object handler, Class cls) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.getMethodAnnotation(cls) != null;
    }
}
