package test.jwt.demo.util;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import test.jwt.demo.common.JWTDeserializer;
import test.jwt.demo.common.JWTSerializer;
import test.jwt.demo.common.UserSession;

import java.util.*;

@Component("jwtUtil")
public class JWTUtil {
    public static final String AUTH_HEADER_KEY = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * 密钥
     */
    @Value("${jwt.key}")
    private String keyStr;


    /**
     * 过期时间（设置2小时后过期）
     */
    @Value("${jwt.expire}")
    private long expire;

    /**
     * 签发者
     */
    @Value("${jwt.issuer")
    private String issuer;

    /**
     * 生成JWT Token
     */
    public String signJWT(Map<String, Object> claimsMap) {
        String token = Jwts.builder()
                .setClaims(claimsMap)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .setId(UUID.randomUUID().toString())
                .setIssuer(issuer).signWith(Keys.hmacShaKeyFor(keyStr.getBytes()))
                .serializeToJsonWith(new JWTSerializer()).compact();
        return token;
    }



    private Claims build(Claims claims) {
        return claims.setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .setId(UUID.randomUUID().toString())
                .setIssuer(issuer);
    }


    public UserSession getSessionInfo(String token) {
        Claims claims = verifyToken(token);
        UserSession userSession = JSON.parseObject(claims.get("userSession").toString(), UserSession.class);
        return userSession;
    }

    public Claims getClaims(String token) {
        Claims claims = verifyToken(token);
        return claims;
    }

    /**
     * 解析token
     *
     * @param token
     */
    private Claims verifyToken(String token) {
        Jwt<Header, Claims> jwt = null;
        try {
            jwt = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(keyStr.getBytes()))
                    .deserializeJsonWith(new JWTDeserializer())
                    .build().parse(token);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token过期");
        } catch (Exception e) {
            throw new RuntimeException("Token校验失败");
        }
        return jwt.getBody();
    }
}
