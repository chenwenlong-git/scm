package com.hete.supply.scm.server.scm.production.entity.dto;

import com.hete.supply.scm.common.util.ParamValidUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/25.
 */
@Data
public class UpdateAttributeRiskDto {
    @ApiModelProperty(value = "属性风险信息列表")
    @NotEmpty(message = "属性风险信息列表不能为空")
    @Valid
    private List<UpdateAttributeRiskInfoDto> updateAttributeRiskInfoDtoList;


    //属性风险配置全局锁
    private String attrRiskLockKey = "attr_risk_lock_key";

    public void validate() {
        //校验属性id是否重复
        ParamValidUtils.requireEquals(updateAttributeRiskInfoDtoList.stream().map(UpdateAttributeRiskInfoDto::getAttrId).distinct().count(), (long) updateAttributeRiskInfoDtoList.size(),
                "属性风险信息列表中属性id重复，请校验后提交");

        //校验属性值id是否重复
        for (UpdateAttributeRiskInfoDto updateAttributeRiskInfoDto : updateAttributeRiskInfoDtoList) {
            List<Long> attrOptionIds = updateAttributeRiskInfoDto.getAttributeRiskOptInfoList().stream()
                    .map(AttributeRiskOptInfoDto::getAttrOptionId)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
            ParamValidUtils.requireEquals(attrOptionIds.size(), updateAttributeRiskInfoDto.getAttributeRiskOptInfoList().size(),
                    "属性风险信息列表中属性值id重复，请校验后提交");
        }
    }
}
