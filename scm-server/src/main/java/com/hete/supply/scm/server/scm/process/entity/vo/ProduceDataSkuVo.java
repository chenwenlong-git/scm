package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/22 14:45
 */
@Data
@NoArgsConstructor
public class ProduceDataSkuVo {
    @ApiModelProperty(value = "id")
    private Long produceDataId;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "状态")
    private BindingProduceData bindingProduceData;

    @ApiModelProperty(value = "更新人")
    private String createUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "生产信息详情原料列表")
    private List<ProduceDataItemRawListVo> produceDataItemRawList;

    @ApiModelProperty(value = "生产信息详情工序列表")
    private List<ProduceDataItemProcessListVo> produceDataItemProcessList;

    @ApiModelProperty(value = "生产信息详情工序描述列表")
    private List<ProduceDataItemProcessDescListVo> produceDataItemProcessDescList;


}
