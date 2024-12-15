package com.hete.supply.scm.server.scm.properties;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.TreeSet;

/**
 * @author yanjiawei
 * @date 2023年08月17日 13:47
 */
@Component
public class ProductionPoolConfigReader {

    @Autowired
    private Environment environment;

    public TreeSet<String> getProductionPoolCodes() {
        TreeSet<String> productionPoolCodes = Sets.newTreeSet();
        int index = 0;
        String poolCode;
        while ((poolCode = environment.getProperty(StrUtil.format("production-pool-config[{}].production-pool-code", index))) != null) {
            productionPoolCodes.add(poolCode);
            index++;
        }
        return productionPoolCodes;
    }

}
