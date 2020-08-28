package test.jwt.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    RedisTemplate redisTemplate;

    public boolean checkUser(String userid, String token) {
        Object redisValue = redisTemplate.opsForValue().get(userid);
        if (redisValue != null && redisValue.equals(token)) {
            return true;
        }
        return false;
    }

    public void addOrUpdateUser(String userid, String token) {
        redisTemplate.opsForValue().set(userid, token);
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
