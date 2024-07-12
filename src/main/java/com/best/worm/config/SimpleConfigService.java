package com.best.worm.config;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class SimpleConfigService implements ConfigService {

    @Override
    public int step() {
        return 10;
    }

    private Map<String, Set<Integer>> remoteConfigData() {
        Map<String, Set<Integer>> ans = new HashMap<>();
        ans.put("2.0.0.1", Set.of(1, 2));
        ans.put("1.2.3.4", Set.of(3, 4));
        return ans;
    }

    @Override
    public Set<Integer> scope() {
        Map<String, Set<Integer>> data = remoteConfigData();
        String localIP = "2.0.0.1";
        return data.get(localIP);
    }


}
