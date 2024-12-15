package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.server.scm.sample.entity.dto.SampleChildItemDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleCreateDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleProcessDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleRawDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/3/7 10:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleSettleCreateDto extends SampleCreateDto {
    @ApiModelProperty(value = "子单信息")
    @NotEmpty(message = "子单信息不能为空")
    @Valid
    private List<SampleChildItemDto> sampleChildItemList;

    @ApiModelProperty(value = "原料bom表")
    @Valid
    private List<SampleRawDto> sampleRawList;

    @ApiModelProperty(value = "原料收货仓库编码")
    @Length(max = 32, message = "原料收货仓库编码长度不能超过32个字符")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料收货仓库名称")
    @Length(max = 32, message = "原料收货仓库名称长度不能超过32个字符")
    private String rawWarehouseName;

    @ApiModelProperty(value = "样品工序列表")
    @Valid
    private List<SampleProcessDto> sampleProcessList;


}
