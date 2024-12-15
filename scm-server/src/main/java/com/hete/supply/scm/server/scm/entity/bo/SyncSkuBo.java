package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.server.scm.entity.po.GoodsProcessPo;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanjiawei
 * @date 2023年06月27日 12:37
 */
@Data
@NoArgsConstructor
public class SyncSkuBo {
    @ApiModelProperty(value = "plmSku实体")
    private List<PlmSkuPo> plmSkuPoList;
    @ApiModelProperty(value = "加工工序实体")
    private List<GoodsProcessPo> goodsProcessPoList;
}
