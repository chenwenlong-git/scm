package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemRawPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemSupplierPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author ChenWenLong
 * @date 2024/8/12 15:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduceDataGetRawInventoryBo {

    @ApiModelProperty(value = "生产资料Map")
    private Map<String, ProduceDataPo> produceDataPoMap;

    @ApiModelProperty(value = "生产信息BOM列表")
    private List<ProduceDataItemPo> produceDataItemPoList;

    @ApiModelProperty(value = "生产信息BOM的供应商列表")
    private List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList;

    @ApiModelProperty(value = "生产信息BOM的原料")
    private List<ProduceDataItemRawPo> produceDataItemRawPoList;


}
