package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import lombok.Data;

import java.util.List;

/**
 * 商品工序导入绑定关系
 *
 * @Author: RockyHuas
 * @date: 2022/12/13 17:09
 */
@Data
public class GoodsProcessImportationDto {
    /**
     * 导入数据
     */
    private List<ImportationDetail> detailList;

    @Data
    public static class ImportationDetail extends BaseImportationRowDto {


        /**
         * sku
         */
        private String sku;

        /**
         * 1 号工序
         */
        private String firstProcessName;

        /**
         * 2 号工序
         */
        private String secondProcessName;

        /**
         * 3 号工序
         */
        private String thirdProcessName;

        /**
         * 4 号工序
         */
        private String fourthProcessName;

        /**
         * 5 号工序
         */
        private String fifthProcessName;

        /**
         * 6 号工序
         */
        private String sixthProcessName;


    }
}
