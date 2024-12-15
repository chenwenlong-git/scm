package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.api.scm.entity.enums.QcType;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 入库收货单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ApiModel(value = "创建质检单 mq dto", description = "创建质检单 mq dto")
public class QcOrderCreateMqDto extends BaseMqMessageDto {

    @NotEmpty
    private List<@Valid QcOrderCreateDto> qcOrderCreateDtoList;

    @Data
    public static class QcOrderCreateDto {

        @NotBlank(message = "仓库编码")
        private String warehouseCode;

        /**
         * 加工单号
         */
        @NotBlank(message = "加工单号不能为空")
        private String processOrderNo;

        private ProcessOrderType processOrderType;

        /**
         * 质检类型：全检、抽检、免检
         */
        @NotNull(message = "质检类型不能为空")
        private QcType qcType;

        /**
         * 商品明细
         */
        @NotEmpty(message = "商品明细不能为空")
        private List<@Valid GoodDetail> goodDetailList;

        @ApiModelProperty(value = "操作人")
        private String operator;

        @ApiModelProperty(value = "操作人")
        private String operatorName;

        @NotBlank(message = "平台不能为空")
        @ApiModelProperty(value = "平台")
        private String platform;

    }

    @Data
    public static class GoodDetail {

        @NotBlank(message = "容器编码不能为空")
        private String containerCode;

        /**
         * 批次码
         */
        @NotBlank(message = "批次码不能为空")
        private String batchCode;

        /**
         * sku
         */
        @NotBlank(message = "sku不能为空")
        private String skuCode;


        /**
         * 应质检数量
         */
        @NotNull(message = "应质检数量不能为空")
        private Integer amount;
    }

}
