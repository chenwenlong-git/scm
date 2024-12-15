package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
public class SupplierProductImportationDto {
    /**
     * 导入供应商产品对照关系
     */
    private List<ImportationDetail> detailList;

    @Data
    public static class ImportationDetail extends BaseImportationRowDto {

        @NotBlank(message = "供应商代码不能为空")
        @Length(max = 32, message = "供应商代码长度不能超过32个字符")
        private String supplierCode;

        @NotBlank(message = "SKU不能为空")
        @Length(max = 100, message = "SKU长度不能超过100个字符")
        private String sku;

        @NotBlank(message = "供应商产品名称不能为空")
        @Length(max = 32, message = "供应商产品名称长度不能超过32个字符")
        private String supplierProductName;

    }
}
