package com.hete.supply.scm.server.scm.develop.entity.bo;

import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewSampleOrderInfoPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewSampleOrderPo;
import com.hete.supply.scm.server.scm.entity.bo.ScmImageBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/25 09:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevelopReviewAndSampleBo {
    @ApiModelProperty(value = "审版样品")
    private List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList;

    @ApiModelProperty(value = "审版")
    private DevelopReviewOrderPo developReviewOrderPo;

    @ApiModelProperty(value = "审版样品属性")
    private List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList;

    @ApiModelProperty(value = "效果图")
    private List<ScmImageBo> effectScmImageBoList;

    @ApiModelProperty(value = "细节图")
    private List<ScmImageBo> detailScmImageBoList;

}
