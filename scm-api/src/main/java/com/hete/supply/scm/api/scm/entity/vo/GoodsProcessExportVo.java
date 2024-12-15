package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import lombok.Data;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 17:07
 */
@Data
public class GoodsProcessExportVo {


    /**
     * sku
     */
    private String sku;

    /**
     * 商品品类
     */
    private String categoryName;

    /**
     * 一级工序
     */
    private ProcessFirst processFirst;

    /**
     * 一级工序
     */
    private String processFirstName;

    /**
     * 二级工序
     */
    private String processSecondName;


}
