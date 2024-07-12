package com.best.worm.config;

import java.util.Set;

public interface ConfigService {
    /**
     * @return max_sequence增长的步长
     */
    int step();
    /**
     * 
     * @return 当前节点服务哪些号段
     */
    Set<Integer> scope();

}
