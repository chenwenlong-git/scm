package com.hete.supply.scm.server.scm.entity.vo;

import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/9/27.
 */
@Data
public class PlmCategoryInfoVo {
    /**
     * id
     */
    private Long categoryId;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 类目路径
     */
    private String path;
}
