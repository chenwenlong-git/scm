package com.hete.supply.scm.server.scm.service.base;

import com.hete.supply.scm.server.scm.properties.ProductionPoolConfigReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.TreeSet;

/**
 * @author yanjiawei
 * @date 2023年08月17日 11:15
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProductionPoolBaseService {

    private final ProductionPoolConfigReader productionPoolConfigReader;

    public TreeSet<String> getProductionPoolCodes() {
        return productionPoolConfigReader.getProductionPoolCodes();
    }
}
