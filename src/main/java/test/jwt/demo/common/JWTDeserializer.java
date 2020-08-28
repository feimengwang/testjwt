package test.jwt.demo.common;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.io.Deserializer;

/**
 * @author Java猿
 */
public class JWTDeserializer implements Deserializer {
    @Override
    public Object deserialize(byte[] bytes) throws DeserializationException {
        return JSON.parse(bytes);
    }
}
