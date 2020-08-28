package test.jwt.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author Java猿
 */
@Component
public class RedisLockService {
    private static Long SUCCESS_FLG = 1L;
    private static String OK = "OK";
    private final int DEFAULT_EXP = 600;
    private final int TIME_OUT = 6000;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获得锁
     *
     * @param key
     * @param value
     * @param exp   过期时间（单位：秒）
     * @return
     */
    public boolean lock(String key, String value, int exp) {
        Boolean lockResult = Boolean.FALSE;
        try {
            long start = System.currentTimeMillis();
            while (true) {
                lockResult = (Boolean) redisTemplate.execute((RedisCallback) connection -> {
                    Jedis jedis = (Jedis) connection.getNativeConnection();
                    SetParams params = SetParams.setParams().nx().ex(exp);
                    String result = jedis.set(key, value, params);
                    if (OK.equalsIgnoreCase(result)) {
                        return Boolean.TRUE;
                    }
                    return Boolean.FALSE;
                });
                if (lockResult.booleanValue()) {
                    return lockResult.booleanValue();
                }
                //休眠100毫秒
                TimeUnit.MILLISECONDS.sleep(100);
                long currentTime = System.currentTimeMillis();
                if (start + TIME_OUT < currentTime) {
                    break;
                }
            }
        } catch (Exception e) {
            //todo log
        }

        return lockResult.booleanValue();
    }

    /**
     * 获得锁
     *
     * @param key
     * @param key
     * @return
     */
    public boolean lock(String key, String value) {
        return lock(key, key, DEFAULT_EXP);
    }

    /**
     * 释放锁
     *
     * @param key
     * @param value
     */
    public boolean releaseLock(String key, String value) {
        Boolean lockResult = (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            Jedis jedis = (Jedis) connection.getNativeConnection();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
            if (SUCCESS_FLG.equals(result)) {
                return true;
            }
            return false;
        });
        return lockResult.booleanValue();
    }

}
