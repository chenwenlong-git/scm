package com.hete.supply.scm.server.scm.defect.entity.bo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author ChenWenLong
 * @date 2024/3/28 15:10
 */
@Data
@NoArgsConstructor
public class DefectHandlingPageBo {

    @ApiModelProperty(value = "次品记录分页列表")
    private IPage<DefectHandlingPo> pageResult;

    @ApiModelProperty(value = "图片")
    private Map<Long, List<String>> idFileCodeMap;

}
