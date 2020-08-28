package test.jwt.demo.controller;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import test.jwt.demo.common.Result;
import test.jwt.demo.common.UserSession;
import test.jwt.demo.util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.UUID;

import static test.jwt.demo.util.JWTUtil.AUTH_HEADER_KEY;
import static test.jwt.demo.util.JWTUtil.TOKEN_PREFIX;

/**
 * Java猿
 */
@RestController
public class UserController {

    private JWTUtil jwtUtil;

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Result adminLogin(HttpServletResponse response) {
        String userId = UUID.randomUUID().toString();
        String role = "admin";

        // 创建token
        HashMap claims = new HashMap();
        UserSession userSession = new UserSession();
        userSession.setRole(role);
        userSession.setUserId(userId);
        claims.put("userSession",userSession);
        String token = jwtUtil.signJWT(claims);
        response.setHeader(AUTH_HEADER_KEY, TOKEN_PREFIX + token);
        return Result.SUCCESS(token);
    }
    @GetMapping("/user/1")
    public Result getUser(HttpServletRequest request){


        return Result.SUCCESS("");
    }

    @Autowired
    public void setJwtUtill(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
}
