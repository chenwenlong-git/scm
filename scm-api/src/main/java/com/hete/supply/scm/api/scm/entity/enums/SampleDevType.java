package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weiwenxin
 * @date 2022/11/2 15:23
 */
@Getter
@AllArgsConstructor
public enum SampleDevType implements IRemark {
    // 开发类型
    DISBURSEMENT_NEW("全新开款"),
    OLD_DERIVATIVE("老款衍生"),
    BRAND_OPTIMIZE("本款优化"),
    NEW_SKU("新开SKU"),
    DEFECTIVE_REOPEN("衍生样开款"),
    SUPPLY_RECOMMEND("供应商自荐"),
    LIMITED("limited"),
    SMALL_INNOVATION("微创新"),
    OUTSOURCING_SAMPLE("委外打样"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    private static final Map<String, SampleDevType> SAMPLE_DEV_TYPE_MAP = new HashMap<>();

    static {
        for (SampleDevType value : values()) {
            SAMPLE_DEV_TYPE_MAP.put(value.getRemark(), value);
        }
    }

    public static SampleDevType getByRemark(String remark) {
        return SAMPLE_DEV_TYPE_MAP.get(remark);
    }

    //确认提交验证是否limited、微创新、委外打样
    public static Boolean confirmSubmitAudited(SampleDevType sampleDevType) {
        return LIMITED.equals(sampleDevType) || SMALL_INNOVATION.equals(sampleDevType) || OUTSOURCING_SAMPLE.equals(sampleDevType);
    }

    //打样是否微创新、委外打样
    public static Boolean proofingAudited(SampleDevType sampleDevType) {
        return SMALL_INNOVATION.equals(sampleDevType) || OUTSOURCING_SAMPLE.equals(sampleDevType);
    }

    //验收是否limited
    public static Boolean limitedAudited(SampleDevType sampleDevType) {
        return LIMITED.equals(sampleDevType);
    }

    //创建不打样时检验生产信息
    public static Boolean createSettleSampleAudited(SampleDevType sampleDevType) {
        return DISBURSEMENT_NEW.equals(sampleDevType) || OLD_DERIVATIVE.equals(sampleDevType)
                || BRAND_OPTIMIZE.equals(sampleDevType) || NEW_SKU.equals(sampleDevType)
                || LIMITED.equals(sampleDevType) || SMALL_INNOVATION.equals(sampleDevType)
                || OUTSOURCING_SAMPLE.equals(sampleDevType);
    }
}
