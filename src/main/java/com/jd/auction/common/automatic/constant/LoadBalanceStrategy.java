package com.jd.auction.common.automatic.constant;

import com.jd.auction.common.automatic.balancing.LoadBalance;
import com.jd.auction.common.automatic.balancing.RandomLoadBalance;
import com.jd.auction.common.automatic.balancing.RoundRobinLoadBalance;
import com.jd.auction.common.automatic.balancing.WeightRobinLoadBalance;

/**
 * 负载均衡策略
 * @author yuanchunsen@jd.com
 *         2018/3/15.
 */
public enum LoadBalanceStrategy {
    /**
     * 轮询
     */
    ROUND_ROBIN(new RoundRobinLoadBalance()),
    /**
     * 随机
     */
    RANDOM(new RandomLoadBalance()),
    /**
     * 权重轮询
     */
    WEIGHT_ROBIN(new WeightRobinLoadBalance());

    private final LoadBalance strategy;

    LoadBalanceStrategy(LoadBalance strategy) {
        this.strategy = strategy;
    }

    /**
     * 获取默认的策略
     * @return 权重轮询策略
     */
    public static LoadBalance getDefaultLoadBalance() {
        return ROUND_ROBIN.getLoadBalance();
    }

    public LoadBalance getLoadBalance() {
        return strategy;
    }
}
