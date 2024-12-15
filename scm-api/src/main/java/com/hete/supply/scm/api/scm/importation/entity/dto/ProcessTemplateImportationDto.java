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
public class ProcessTemplateImportationDto {
    /**
     * 导入数据
     */
    private List<ImportationDetail> detailList;

    @Data
    public static class ImportationDetail extends BaseImportationRowDto {


        /**
         * 工序模版名称
         */
        private String name;

        /**
         * 类型
         */
        private String type;

        /**
         * 类型值
         */
        private String typeValue;

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

        /**
         * 7 号工序
         */
        private String seventhProcessName;

        /**
         * 8 号工序
         */
        private String eighthProcessName;

        /**
         * 9 号工序
         */
        private String ninthProcessName;

        /**
         * 10 号工序
         */
        private String tenthProcessName;


    }
}
