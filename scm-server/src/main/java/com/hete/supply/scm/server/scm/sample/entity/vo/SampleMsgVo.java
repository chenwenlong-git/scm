package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseMsgParentVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/13 14:04
 */
@Data
@NoArgsConstructor
public class SampleMsgVo {
    @ApiModelProperty(value = "样品开发需求")
    private List<SampleMsgParentVo> sampleMsgParentList;

    @ApiModelProperty(value = "首单需求")
    private List<PurchaseMsgParentVo> purchaseMsgParentList;
}
