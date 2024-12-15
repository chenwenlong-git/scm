package com.hete.supply.scm.server.scm.adjust.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/6/18 16:41
 */
@Data
@NoArgsConstructor
public class ChannelSaveDto {

    @NotEmpty(message = "渠道列表不能为空")
    @ApiModelProperty(value = "渠道列表")
    @Valid
    private List<ChannelSaveItemDto> channelSaveItemList;

    @Data
    public static class ChannelSaveItemDto {
        @ApiModelProperty(value = "id")
        private Long channelId;

        @ApiModelProperty(value = "version")
        private Integer version;

        @NotBlank(message = "渠道名称不能为空")
        @ApiModelProperty(value = "渠道名称")
        @Length(max = 255, message = "渠道名称字符长度不能超过 255 位")
        private String channelName;

        @NotNull(message = "渠道状态不能为空")
        @ApiModelProperty(value = "状态:启用(TRUE)、禁用(FALSE)")
        private BooleanType channelStatus;
    }

}
