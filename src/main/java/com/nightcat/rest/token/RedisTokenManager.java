package com.nightcat.rest.token;

import com.nightcat.common.constant.Constant;
import com.nightcat.entity.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Aollio
 * @date 15/05/2017
 */
@Component
public class RedisTokenManager implements TokenManager {

    private Logger logger = LoggerFactory.getLogger(RedisTokenManager.class);

    private StringRedisTemplate redis;

    @Autowired
    public void setRedis(StringRedisTemplate redis) {
        this.redis = redis;
        redis.setKeySerializer(new JdkSerializationRedisSerializer());
    }

    @Override
    public Token createToken(String uid) {
        String token = UUID.randomUUID().toString().replace("_", "");
        token = uid + "_" + token;
        logger.info("create token. uid:" + uid + "  token:" + token);
        Token tokenModel = new Token(uid, token);
        //set expire time
        redis.boundValueOps(uid).set(token, Constant.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return tokenModel;
    }

    @Override
    public boolean checkToken(Token model) {
        if (model == null) {
            logger.warn("model is null");
            return false;
        }
        String token = redis.boundValueOps(model.getUid()).get();

        if (token == null || !token.equals(model.getId())) {
            return false;
        }

        redis.boundValueOps(model.getUid()).expire(Constant.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        logger.info("check model is right, update the token expires time. uid :" + model.getUid());
        return true;
    }

    @Override
    public Token getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 2) {
            logger.error("authentication is not format[uid_uuid]");
            return null;
        }

        String uid = param[0];
        return new Token(authentication, uid);
    }


    @Override
    public void delToken(String userId) {
        redis.delete(userId);
    }
}