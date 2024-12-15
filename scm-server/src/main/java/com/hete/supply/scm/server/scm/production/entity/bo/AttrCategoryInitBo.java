package com.hete.supply.scm.server.scm.production.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/27.
 */
@Data
public class AttrCategoryInitBo {
    private String categoryName;
    private List<SubCategory> subCategoryNames;

    @Data
    public static class SubCategory {
        private String categoryName;
    }
}
