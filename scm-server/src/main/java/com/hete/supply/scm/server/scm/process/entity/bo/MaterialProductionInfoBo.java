package com.hete.supply.scm.server.scm.process.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/3/19.
 */
@Data
public class MaterialProductionInfoBo {
    private String sku;
    private Long secondCategoryId;
    private List<String> laceAreas;
    private List<String> lengths;
}
