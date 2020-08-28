package test.jwt.demo.common;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.io.SerializationException;
import io.jsonwebtoken.io.Serializer;

public class JWTSerializer implements Serializer {
    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return JSON.toJSONString(o).getBytes();
    }
}
