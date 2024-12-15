package com.hete.supply.scm.server.supplier.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/28 21:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OnShelvesOrderCreateResultMqDto extends BaseMqMessageDto {
    /**
     * 结果，非空
     */
    private WmsEnum.OnShelvesOrderCreateResult result;

    /**
     * 处理失败原因，成功的时候为空
     */
    private String failReason;

    /**
     * 上架单号，非空
     */
    private String onShelvesOrderNo;

    /**
     * 次品处理单号列表，生成让步上架单回传的时候不为空
     */
    private List<String> defectHandlingNoList;

    /**
     * 质检单号，非空
     */
    private String qcOrderNo;

    /**
     * 计划上架数量，非空
     */
    private Integer planAmount;

    /**
     * 上架单类型：让步上架为CONCESSION,其余为空
     */
    private WmsEnum.OnShelvesOrderCreateType onShelvesOrderCreateType;

}
