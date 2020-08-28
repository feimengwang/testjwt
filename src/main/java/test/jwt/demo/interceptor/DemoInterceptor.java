package test.jwt.demo.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import test.jwt.demo.service.RedisLockService;
import test.jwt.demo.util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Enumeration;

import static test.jwt.demo.util.JWTUtil.AUTH_HEADER_KEY;
import static test.jwt.demo.util.JWTUtil.TOKEN_PREFIX;


/**
 * @author Java猿
 */
@Component
public class DemoInterceptor implements HandlerInterceptor {
    private JWTUtil jwtUtil;
    private RedisLockService redisLockService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("authorization");
        if (!StringUtils.isEmpty(token)) {
            token = token.substring(JWTUtil.TOKEN_PREFIX.length());
            String newToken;
            if ((newToken = verifyAndRefreshToken(token)) != null) {
                if(!token.equals(newToken)) {
                    response.setHeader(AUTH_HEADER_KEY, TOKEN_PREFIX + newToken);
                }
                return true;
            }
        }
        return false;
    }


    private String verifyAndRefreshToken(String token) {
        Claims claims = jwtUtil.getClaims(token);
        if (claims == null) {
            return null;
        }
        Date expiration = claims.getExpiration();
        String newToken = token;
        if (checkExpiration(expiration)) {
            String id = claims.getId();
            try {
                if (redisLockService.lock(id, id)) {
                    newToken = jwtUtil.signJWT(claims);
                }
            } finally {
                redisLockService.releaseLock(id, id);
            }
        }
        return newToken;
    }

    private boolean checkExpiration(Date expiration) {
        long currentTimeMillis = System.currentTimeMillis();
        //过期之前10分钟刷新
        if (currentTimeMillis + 1000 * 60 * 10 > expiration.getTime()) {
            return true;
        }
        return false;
    }

    @Autowired
    public void setJwtUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    public void setRedisLockService(RedisLockService redisLockService) {
        this.redisLockService = redisLockService;
    }
}
