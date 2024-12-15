package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author rockyHuas
 * @date 2023/03/07 14:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QcOrderCreateResultMqDto extends BaseMqMessageDto {

    private List<@Valid QcOrderCreateResult> qcOrderCreateResultList;


    @Data
    public static class QcOrderCreateResult {

        @NotBlank(message = "仓库编码")
        private String warehouseCode;

        /**
         * 加工单号
         */
        @NotBlank(message = "加工单号不能为空")
        private String processOrderNo;

        /**
         * 质检类型：全检、抽检、免检
         */
        @NotNull(message = "质检单号")
        private String qcOrderNo;

        @ApiModelProperty(value = "操作人")
        private String operator;

        @ApiModelProperty(value = "操作人")
        private String operatorName;


    }
}
