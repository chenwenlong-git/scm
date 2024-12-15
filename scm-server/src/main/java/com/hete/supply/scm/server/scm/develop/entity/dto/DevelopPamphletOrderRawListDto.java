package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.server.scm.entity.dto.RawLocationItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/17 15:00
 */
@Data
@NoArgsConstructor
public class DevelopPamphletOrderRawListDto {

    @NotBlank(message = "原料sku不能为空")
    @ApiModelProperty(value = "原料sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @NotNull(message = "出库数不能为空")
    @ApiModelProperty(value = "出库数")
    @Min(value = 1, message = "出库数不能小于0")
    private Integer skuCnt;


    @ApiModelProperty(value = "指定库位")
    @Valid
    private List<RawLocationItemDto> rawLocationItemList;
}
