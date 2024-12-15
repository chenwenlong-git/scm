package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/5 17:39
 */
@Data
public class ProcessMaterialImportationDto {
    /**
     * 导入数据
     */
    private List<ImportationDetail> detailList;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ImportationDetail extends BaseImportationRowDto {
        @ApiModelProperty(value = "关联的加工单")
        private String processOrderNo;


        @ApiModelProperty(value = "sku")
        private String sku;


        @ApiModelProperty(value = "出库数量")
        private Integer deliveryNum;

    }
}
