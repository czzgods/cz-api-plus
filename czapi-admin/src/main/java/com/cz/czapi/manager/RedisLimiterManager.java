package com.cz.czapi.manager;

import com.cz.czapi.exception.BusinessException;
import com.cz.czapicommon.common.ErrorCode;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 专门提供 RedisLimiter 限流基础服务的（提供了通用的能力）
 */
@Service
public class RedisLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流操作
     *
     * @param key 区分不同的限流器，比如不同的用户 id 应该分别统计
     */
    public void doRateLimit(String key) {
        // 创建一个名称为user_limiter的限流器，每秒最多访问 2 次
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        /*RateType.OVERALL：表示速率限制适用于整体速率，而不是每个用户或每个端点。
        2：每秒允许的最大操作数（请求次数）。
        1：指定时间间隔内的时间单位，这里是 1 秒。
        RateIntervalUnit.SECONDS：表示时间单位为秒。 这样设置后，限流器将每秒允许最多 2 次请求。*/
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        // 每当一个操作来了后，请求一个令牌
        boolean canOp = rateLimiter.tryAcquire(1);
        if (!canOp) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }
}
