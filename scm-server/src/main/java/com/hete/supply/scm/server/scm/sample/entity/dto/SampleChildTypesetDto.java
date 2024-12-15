package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 22:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleChildTypesetDto extends SampleChildIdAndVersionDto {

    @ApiModelProperty(value = "原料bom表")
    @Valid
    private List<SampleRawDto> sampleRawList;

    @ApiModelProperty(value = "原料收货仓库编码")
    @Length(max = 32, message = "原料收货仓库编码长度不能超过32个字符")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料收货仓库名称")
    @Length(max = 32, message = "原料收货仓库名称长度不能超过32个字符")
    private String rawWarehouseName;

}
