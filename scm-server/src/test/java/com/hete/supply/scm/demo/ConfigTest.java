package com.hete.supply.scm.demo;

import com.hete.supply.scm.server.scm.entity.bo.ProductionPoolConfigBo;
import com.hete.supply.scm.server.scm.process.service.base.CapacityPoolBaseService;
import com.hete.supply.scm.server.scm.service.base.ProductionPoolBaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

/**
 * @author yanjiawei
 * @date 2023年08月17日 15:24
 */
@ActiveProfiles("local")
@SpringBootTest
public class ConfigTest {

    @Autowired
    private CapacityPoolBaseService capacityPoolBaseService;
    @Autowired
    private ProductionPoolBaseService productionPoolBaseService;

    @Test
    public void getConfig() {
        Set<String> productionPoolCodes = productionPoolBaseService.getProductionPoolCodes();
        for (String productionPoolCode : productionPoolCodes) {
            ProductionPoolConfigBo productionPoolConfig = capacityPoolBaseService.getProductionPoolConfig(productionPoolCode);
            System.out.println(1);
        }
    }
}
