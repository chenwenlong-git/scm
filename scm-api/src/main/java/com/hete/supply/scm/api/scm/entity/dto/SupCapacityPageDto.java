package com.hete.supply.scm.api.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.exception.ParamIllegalException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author yanjiawei
 * Created on 2024/8/6.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupCapacityPageDto extends ComPageDto {
    @ApiModelProperty(value = "筛选开始日期")
    private LocalDateTime filterStartDateTime;

    @ApiModelProperty(value = "筛选结束日期")
    private LocalDateTime filterEndDateTime;

    @JsonIgnore
    private LocalDate filterStartDate;
    @JsonIgnore
    private LocalDate filterEndDate;

    @ApiModelProperty(value = "供应商代码列表")
    private List<String> supplierCodeList;

    @JsonIgnore
    private List<String> filterPerSupplierCodeList;

    @ApiModelProperty(value = "供应商状态")
    private SupplierStatus supplierStatus;

    @ApiModelProperty(value = "供应商等级")
    private List<SupplierGrade> supplierGradeList;

    @ApiModelProperty(value = "日产能-开始值")
    private BigDecimal normalCapacityStart;

    @ApiModelProperty(value = "日产能-结束值")
    private BigDecimal normalCapacityEnd;

    @ApiModelProperty(value = "30日剩余产能占比-起始值")
    @Digits(integer = 3, fraction = 2, message = "30日剩余产能占比-起始值最多只能有两位小数")
    private BigDecimal restCap30PerStart;

    @ApiModelProperty(value = "30日剩余产能占比-结束值")
    @Digits(integer = 3, fraction = 2, message = "30日剩余产能占比-结束值最多只能有两位小数")
    private BigDecimal restCap30PerEnd;

    @ApiModelProperty(value = "60日剩余产能占比-起始值")
    @Digits(integer = 3, fraction = 2, message = "60日剩余产能占比-起始值最多只能有两位小数")
    private BigDecimal restCap60PerStart;

    @ApiModelProperty(value = "60日剩余产能占比-结束值")
    @Digits(integer = 3, fraction = 2, message = "60日剩余产能占比-结束值最多只能有两位小数")
    private BigDecimal restCap60PerEnd;

    @ApiModelProperty(value = "90日剩余产能占比-起始值")
    @Digits(integer = 3, fraction = 2, message = "90日剩余产能占比-起始值最多只能有两位小数")
    private BigDecimal restCap90PerStart;

    @ApiModelProperty(value = "90日剩余产能占比-结束值")
    @Digits(integer = 3, fraction = 2, message = "90日剩余产能占比-结束值最多只能有两位小数")
    private BigDecimal restCap90PerEnd;

    public void pageAndExportValid() {
        if (Objects.isNull(this.restCap30PerStart) && Objects.nonNull(this.restCap30PerEnd)) {
            throw new ParamIllegalException("30日剩余产能占比-起始值不能为空，请填写30日剩余产能占比-起始值");
        }
        if (Objects.isNull(this.restCap30PerEnd) && Objects.nonNull(this.restCap30PerStart)) {
            throw new ParamIllegalException("30日剩余产能占比-结束值不能为空，请填写30日剩余产能占比-结束值");
        }

        if (Objects.isNull(this.restCap60PerStart) && Objects.nonNull(this.restCap60PerEnd)) {
            throw new ParamIllegalException("60日剩余产能占比-起始值不能为空，请填写60日剩余产能占比-起始值");
        }
        if (Objects.isNull(this.restCap60PerEnd) && Objects.nonNull(this.restCap60PerStart)) {
            throw new ParamIllegalException("60日剩余产能占比-结束值不能为空，请填写60日剩余产能占比-结束值");
        }

        if (Objects.isNull(this.restCap90PerStart) && Objects.nonNull(this.restCap90PerEnd)) {
            throw new ParamIllegalException("90日剩余产能占比-起始值不能为空，请填写90日剩余产能占比-起始值");
        }
        if (Objects.isNull(this.restCap90PerEnd) && Objects.nonNull(this.restCap90PerStart)) {
            throw new ParamIllegalException("90日剩余产能占比-结束值不能为空，请填写90日剩余产能占比-结束值");
        }

        if (Objects.isNull(this.filterStartDateTime) && Objects.nonNull(this.filterEndDateTime)) {
            throw new ParamIllegalException("筛选开始日期不能为空，请填写筛选开始日期");
        }
        if (Objects.isNull(this.filterEndDateTime) && Objects.nonNull(this.filterStartDateTime)) {
            throw new ParamIllegalException("筛选结束日期不能为空，请填写筛选结束日期");
        }
    }
}
