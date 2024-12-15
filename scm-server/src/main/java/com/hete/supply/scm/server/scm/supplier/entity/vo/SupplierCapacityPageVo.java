package com.hete.supply.scm.server.scm.supplier.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.server.scm.supplier.serializer.CustomLocalDateSerializer;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 供应商产能分页查询结果类
 *
 * @author yanjiawei
 * Created on 2024/8/7.
 */
@Data
@NoArgsConstructor
@ApiModel(value = "供应商产能分页查询结果实体", description = "供应商产能分页查询结果实体")
public class SupplierCapacityPageVo {
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商别称")
    private String supplierAlias;

    @ApiModelProperty(value = "供应商状态")
    private SupplierStatus supplierStatus;

    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;

    @ApiModelProperty(value = "常规日产能")
    private BigDecimal normalCapacity = BigDecimal.ZERO;

    @ApiModelProperty(value = "30天剩余产能")
    @JsonProperty("restCap30AvailCap")
    private BigDecimal future30TotalCap = BigDecimal.ZERO;

    @ApiModelProperty(value = "60天剩余产能")
    @JsonProperty("restCap60AvailCap")
    private BigDecimal future60TotalCap = BigDecimal.ZERO;

    @ApiModelProperty(value = "90天剩余产能")
    @JsonProperty("restCap90AvailCap")
    private BigDecimal future90TotalCap = BigDecimal.ZERO;

    @ApiModelProperty(value = "每日剩余产能列表")
    @JsonProperty("restCapDayAvailCapVoList")
    private List<SupplierCapacityVo> supplierCapacityVoList;

    @Data
    public static class SupplierCapacityVo implements Cloneable {
        @ApiModelProperty(value = "日期")
        @JsonSerialize(using = CustomLocalDateSerializer.class)
        private LocalDate capacityDate;

        @ApiModelProperty(value = "剩余产能")
        private BigDecimal normalAvailableCapacity;

        @ApiModelProperty(value = "是否休息")
        private BooleanType isRest;

        @Override
        public SupplierCapacityVo clone() throws CloneNotSupportedException {
            return (SupplierCapacityVo) super.clone();
        }
    }
}
