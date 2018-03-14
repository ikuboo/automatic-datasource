package com.jd.auction.common.core.api;



public enum MasterSlaveLoadBalanceAlgorithmType {
    
    ROUND_ROBIN(new RoundRobinMasterSlaveLoadBalanceAlgorithm()),
    RANDOM(new RandomMasterSlaveLoadBalanceAlgorithm());
    
    private final MasterSlaveLoadBalanceAlgorithm algorithm;


    MasterSlaveLoadBalanceAlgorithmType(MasterSlaveLoadBalanceAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Get default master-slave database load-balance algorithm type.
     * 
     * @return default master-slave database load-balance algorithm type
     */
    public static MasterSlaveLoadBalanceAlgorithmType getDefaultAlgorithmType() {
        return ROUND_ROBIN;
    }

    public MasterSlaveLoadBalanceAlgorithm getAlgorithm() {
        return algorithm;
    }
}
