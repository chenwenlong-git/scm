package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.vo.ProduceDataDetailAttrVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/10/17 11:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduceDataSkuDetailVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "生产图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "bom原料信息")
    private List<ProduceDataDetailRawVo> rawVoList;

    @ApiModelProperty(value = "工序描述信息")
    private List<ProduceDataDetailProcessDescVo> processDescVoList;

    @ApiModelProperty(value = "工序信息")
    private List<ProduceDataDetailProcessVo> processVoList;

    @ApiModelProperty(value = "生产信息列表")
    private List<ProduceDataDetailAttrVo> produceAttrList;
}
