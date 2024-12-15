package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author rockyHUas
 * @date 2023/4/3 20:40
 */
@Data
public class ProcessTemplateMaterialImportationDto {
    /**
     * 导入数据
     */
    private List<ImportationDetail> detailList;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ImportationDetail extends BaseImportationRowDto {

        @ApiModelProperty(value = "模版名称")
        private String name;

        @ApiModelProperty(value = "原料 sku")
        private String materialSku;

        @ApiModelProperty(value = "原料名称")
        private String materialName;

        @ApiModelProperty(value = "原料数量")
        private Integer num;

    }
}
