package com.hete.supply.scm.server.scm.process.entity.dto;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.plm.api.repair.entity.enums.ProcessType;
import com.hete.supply.scm.server.scm.enums.wms.ProductQuality;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.exception.BizException;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author yanjiawei
 * Created on 2023/12/28.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "创建返修单信息")
public class CreateRepairOrderMqDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "计划单号", example = "P2023001")
    private String planNo;

    @ApiModelProperty(value = "标题", example = "维修计划")
    private String title;

    @ApiModelProperty(value = "生成人用户编号", example = "user123")
    private String generateUserCode;

    @ApiModelProperty(value = "生成人用户名", example = "John Doe")
    private String generateUsername;

    @ApiModelProperty(value = "生成时间", example = "2023-01-01T10:00:00")
    private LocalDateTime generateTime;

    @ApiModelProperty(value = "收货仓库编码", example = "WH001")
    private String receiveWarehouseCode;

    @ApiModelProperty(value = "收货仓库名称", example = "Main Warehouse")
    private String receiveWarehouseName;

    @ApiModelProperty(value = "期望完成时间", example = "2023-01-10T12:00:00")
    private LocalDateTime expectCompleteTime;

    @ApiModelProperty(value = "平台编码", example = "PLT001")
    private String platCode;

    @ApiModelProperty(value = "销售单价", example = "25.99")
    private BigDecimal salePrice;

    @ApiModelProperty(value = "加工类型", example = "SINGLE", allowableValues = "SINGLE, MULTIPLE")
    private ProcessType processType;

    @ApiModelProperty(value = "备注", example = "需要特殊处理的维修计划")
    private String remark;

    @ApiModelProperty(value = "返修SKU列表")
    private List<RepairChildItem> repairChildList;

    @ApiModelProperty(value = "返修原料列表")
    private List<RepairMaterialItem> repairMaterialList;


    @ApiModel(description = "维修SKU信息")
    @Data
    public static class RepairChildItem implements Comparable<RepairChildItem> {
        @ApiModelProperty(value = "SPU编码", example = "SPU001")
        private String spuCode;

        @ApiModelProperty(value = "SKU编码", example = "SKU001", required = true)
        private String skuCode;

        @ApiModelProperty(value = "数量", example = "1", required = true)
        private int amount;

        @Override
        public int compareTo(@NotNull CreateRepairOrderMqDto.RepairChildItem other) {
            return Integer.compare(other.getAmount(), this.getAmount());
        }
    }

    @ApiModel(description = "返修原料信息")
    @Data
    public static class RepairMaterialItem {

        @JsonIgnore
        @ApiModelProperty(value = "唯一编码", example = "MAT001")
        private String uniqueCode;

        @ApiModelProperty(value = "SKU编码", example = "MAT001", required = true)
        private String skuCode;

        @ApiModelProperty(value = "仓库编码", example = "WH002", required = true)
        private String warehouseCode;

        @ApiModelProperty(value = "仓库名称", example = "Spare Parts Warehouse")
        private String warehouseName;

        @ApiModelProperty(value = "仓库位置编码", example = "A001")
        private String warehouseLocationCode;

        @ApiModelProperty(value = "数量", example = "10", required = true)
        private int amount;

        @ApiModelProperty(value = "商品品质", example = "DEFECTIVE", allowableValues = "GOOD, DEFECTIVE")
        private WmsEnum.ProductQuality productQuality;
    }

    /**
     * 校验计划单对象的各个字段，确保它们符合要求。
     *
     * @throws BizException 如果计划单对象的任何字段不符合要求
     */
    public void validate() {
        validateStringField(planNo, "计划单号");
        validateStringField(title, "标题");
        validateStringField(generateUserCode, "下单人用户编号");
        validateStringField(generateUsername, "下单人名称");
        validateLocalDateTimeField(generateTime, "下单时间");
        validateStringField(receiveWarehouseCode, "收货仓库编码");
        validateLocalDateTimeField(expectCompleteTime, "期望完成时间");
        validateStringField(platCode, "需求平台");
        validateBigDecimalField(salePrice, "单价");
        validateEnumField(processType, "计划单类型");
        validateStringField(remark, "备注", 500);
        validateRepairChildList(repairChildList);
        validateRepairMaterialList(repairMaterialList);
    }

    /**
     * 校验字符串字段，确保值不为 null 且去除前后空白后不为空字符串。
     *
     * @param value     要校验的字符串值
     * @param fieldName 字段名称，用于构造异常消息
     * @throws BizException 如果字符串值为 null 或去除前后空白后为空字符串
     */
    private void validateStringField(String value,
                                     String fieldName) {
        if (value == null || value.trim()
                .isEmpty()) {
            throw new BizException(StrUtil.format("创建返修单失败！原因：{}不能为空，请PLM/PDC相关同事注意！", fieldName));
        }
    }


    /**
     * 校验字符串字段是否为空，并检查其长度是否超过指定最大长度。
     *
     * @param value     待校验的字符串
     * @param fieldName 字段名称，用于错误提示
     * @param maxLength 允许的最大长度
     * @throws BizException 如果字符串为空或长度超过最大限制，抛出业务异常
     */
    private void validateStringField(String value,
                                     String fieldName,
                                     int maxLength) {
        if (value != null && value.trim().isEmpty()) {
            if (value.length() > maxLength) {
                throw new BizException(StrUtil.format("创建返修单失败！原因：{}长度不能超过{}，请PLM/PDC相关同事注意！", fieldName, maxLength));
            }
        }
    }


    /**
     * 验证提供的 LocalDateTime 值不为 null。
     *
     * @param value     要验证的 LocalDateTime 值。
     * @param fieldName 正在验证的字段的名称。
     * @throws BizException 如果值为 null。
     */
    private void validateLocalDateTimeField(LocalDateTime value,
                                            String fieldName) {
        if (null == value) {
            throw new BizException(StrUtil.format("创建返修单失败！原因：{}不能为空，请PLM/PDC相关同事注意！", fieldName));
        }
    }

    /**
     * 验证提供的 BigDecimal 值不为 null。
     *
     * @param value     要验证的 BigDecimal 值。
     * @param fieldName 正在验证的字段的名称。
     * @throws BizException 如果值为 null。
     */
    private void validateBigDecimalField(BigDecimal value,
                                         String fieldName) {
        if (null == value) {
            throw new BizException(StrUtil.format("创建返修单失败！原因：{}不能为空，请PLM/PDC相关同事注意！", fieldName));
        }
    }

    /**
     * 校验正整数字段是否大于0。
     *
     * @param value     要校验的整数值
     * @param fieldName 字段名称，用于构造异常消息
     * @throws BizException 如果整数值不大于0
     */
    private void validatePositiveIntegerField(int value,
                                              String fieldName) {
        if (value <= 0) {
            throw new BizException(StrUtil.format("创建返修单失败！原因：{}必须大于0，请PLM/PDC相关同事注意！", fieldName));
        }
    }

    /**
     * 验证提供的 Enum 值不为 null。
     *
     * @param value     要验证的 Enum 值。
     * @param fieldName 正在验证的字段的名称。
     * @param <T>       Enum 的类型。
     * @throws BizException 如果值为 null。
     */
    private <T extends Enum<T>> void validateEnumField(Enum<T> value,
                                                       String fieldName) {
        if (null == value) {
            throw new BizException(StrUtil.format("创建返修单失败！原因：{}不能为空，请PLM/PDC相关同事注意！", fieldName));
        }
    }

    /**
     * 校验返修SKU列表，确保列表不为空，并且每个返修子项的SPU编码、SKU编码和加工数量符合要求。
     *
     * @param repairChildList 返修SKU列表
     * @throws BizException 如果返修SKU列表为空或存在不符合要求的返修子项
     */
    private void validateRepairChildList(List<CreateRepairOrderMqDto.RepairChildItem> repairChildList) {
        if (CollectionUtils.isEmpty(repairChildList)) {
            throw new BizException(StrUtil.format("创建返修单失败！原因：{}不能为空，请PLM/PDC相关同事注意！", "返修SKU列表不能为空"));
        }

        for (CreateRepairOrderMqDto.RepairChildItem repairChild : repairChildList) {
            // 校验返修SKU信息字段
            validateStringField(repairChild.getSpuCode(), "返修商品SPU编码");
            validateStringField(repairChild.getSkuCode(), "返修商品SKU编码");
            validatePositiveIntegerField(repairChild.getAmount(), "返修商品加工数量");
        }
    }

    /**
     * 校验返修材料列表，确保列表不为空，并且每个返修材料的SKU编码、仓库编码和数量符合要求。
     *
     * @param repairMaterialList 返修材料列表
     * @throws BizException 如果返修材料列表为空或存在不符合要求的返修材料
     */
    private void validateRepairMaterialList(List<CreateRepairOrderMqDto.RepairMaterialItem> repairMaterialList) {
        if (CollectionUtils.isEmpty(repairMaterialList)) {
            throw new BizException(StrUtil.format("创建返修单失败！原因：{}不能为空，请PLM/PDC相关同事注意！", "返修原料列表不能为空"));
        }

        for (CreateRepairOrderMqDto.RepairMaterialItem repairMaterial : repairMaterialList) {
            // 校验返修原料字段
            validateStringField(repairMaterial.getSkuCode(), "返修所需原料SKU编码");
            validateStringField(repairMaterial.getWarehouseCode(), "返修所需原料仓库编码");
            validatePositiveIntegerField(repairMaterial.getAmount(), "返修所需原料数量");
            validateEnumField(repairMaterial.getProductQuality(), "返修所需原料商品品质");
        }

        boolean allSameProductQuality = repairMaterialList.stream()
                .map(CreateRepairOrderMqDto.RepairMaterialItem::getProductQuality)
                .distinct()
                .count() == 1;
        if (!allSameProductQuality) {
            throw new BizException(StrUtil.format("创建返修单失败！原因：{}，请PLM/PDC相关同事注意！", "原料信息商品品质必须同一商品品质"));
        }

        boolean allSameWarehouseCode = repairMaterialList.stream()
                .map(CreateRepairOrderMqDto.RepairMaterialItem::getWarehouseCode)
                .distinct()
                .count() == 1;
        if (!allSameWarehouseCode) {
            throw new BizException(StrUtil.format("创建返修单失败！原因：{}，请PLM/PDC相关同事注意！", "原料信息仓库编码必须同一仓库编码"));
        }
    }
}
