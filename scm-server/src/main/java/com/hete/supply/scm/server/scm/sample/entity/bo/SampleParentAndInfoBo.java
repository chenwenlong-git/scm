package com.hete.supply.scm.server.scm.sample.entity.bo;

import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderInfoPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderPo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/4/3 00:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleParentAndInfoBo {
    private SampleParentOrderPo sampleParentOrderPo;

    private List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList;
}
