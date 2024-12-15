package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.api.scm.entity.dto.PurchasePreOrderDto;
import com.hete.supply.scm.api.scm.entity.enums.IsCapacitySatisfy;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * @author yanjiawei
 * Created on 2024/8/13.
 */
@Data
public class CalPreShelfTimeBo {

    //SKU尚未维护单件产能失败原因常量
    public static final String SKU_NOT_MAINTAIN_CAPACITY_FAIL_REASON = "SKU尚未维护单件产能。";
    //SKU信息缺失失败原因常量
    public static final String SKU_INFO_MISSING_FAIL_REASON = "SKU缺失，请前往SCM-生产资料列表同步。";
    public static final long ONE_DAY = 1L;
    //仓内作业时间常量
    public static final long WARE_HOUSE_DAY = 2L;
    //产能周期天数常量
    public static final long CAPACITY_CYCLE_DAY = 7L;
    //产能补偿天数常量
    public static final long CAPACITY_COMPENSATION_DAY = 3L;

    private List<PurchasePreOrderDto.PreOrderInfoDto> preOrderInfoDtoList;
    private Queue<ClaBo> calQueue = new PriorityQueue<>(new CapacityComparator());
    private Queue<ResBo> resQueue = new LinkedList<>();
    private Map<String, BigDecimal> supplierCapacityMap = new TreeMap<>();

    @Data
    @AllArgsConstructor
    public static class ClaBo {
        @ApiModelProperty(value = "业务ID")
        private Long businessId;

        @ApiModelProperty(value = "SKU")
        private String sku;

        @ApiModelProperty(value = "供应商代码")
        private String supplierCode;

        @ApiModelProperty(value = "生产周期天数")
        private long skuProduceCycleDays;

        @ApiModelProperty(value = "所需总产能")
        private BigDecimal needTotalCapacity;

        @ApiModelProperty(value = "供应商物流时效")
        private long logisticsDays;

        @ApiModelProperty(value = "最晚生产时间")
        private LocalDate maxProduceDate;

        public ClaBo(Long businessId, String sku, Long skuProduceCycleDays, String supplierCode, BigDecimal needTotalCapacity, long logisticsDays, LocalDate maxProduceDate) {
            this.businessId = businessId;
            this.sku = sku;
            this.skuProduceCycleDays = skuProduceCycleDays;
            this.supplierCode = supplierCode;
            this.needTotalCapacity = needTotalCapacity;
            this.logisticsDays = logisticsDays;
            this.maxProduceDate = maxProduceDate;
        }
    }

    @Data
    public static class ResBo {
        @ApiModelProperty(value = "业务ID")
        private Long businessId;

        @ApiModelProperty(value = "预计上架日期")
        private LocalDate preShelfTime;

        @ApiModelProperty(value = "产能是否满足", notes = "不为空")
        private IsCapacitySatisfy isCapacitySatisfy;

        @ApiModelProperty(value = "失败原因")
        private String failReason;

        public ResBo(Long businessId, IsCapacitySatisfy isCapacitySatisfy) {
            this.businessId = businessId;
            this.isCapacitySatisfy = isCapacitySatisfy;
        }

        public ResBo(Long businessId, String failReason) {
            this.businessId = businessId;
            this.failReason = failReason;
        }

        public ResBo(Long businessId, IsCapacitySatisfy isCapacitySatisfy, LocalDate preShelfTime) {
            this.businessId = businessId;
            this.isCapacitySatisfy = isCapacitySatisfy;
            this.preShelfTime = preShelfTime;
        }
    }


    public static class CapacityComparator implements Comparator<ClaBo> {
        @Override
        public int compare(ClaBo o1, ClaBo o2) {
            return o2.getNeedTotalCapacity().compareTo(o1.getNeedTotalCapacity());
        }
    }

    public boolean isCompletedCal() {
        return calQueue.isEmpty();
    }
}
