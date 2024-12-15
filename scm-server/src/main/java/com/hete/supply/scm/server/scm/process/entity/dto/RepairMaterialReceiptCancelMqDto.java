package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author yanjiawei
 * @Description TODO
 * @Date 2024/1/5 16:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RepairMaterialReceiptCancelMqDto extends BaseMqMessageDto {

    /**
     * 出库单
     */
    @NotBlank(message = "取消出库单号不能为空")
    private List<String> deliveryOrderNoList;

}
