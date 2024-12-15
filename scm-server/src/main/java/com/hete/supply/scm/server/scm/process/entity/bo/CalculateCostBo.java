package com.hete.supply.scm.server.scm.process.entity.bo;

import lombok.Data;

import java.util.Set;

/**
 * @author yanjiawei
 * Created on 2024/1/25.
 */
@Data
public class CalculateCostBo {
    private Set<String> processOrderNos;
}
