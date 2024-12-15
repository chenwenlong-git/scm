package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author yanjiawei
 * @Description TODO
 * @Date 2024/5/24 13:53
 */
@Data
@NoArgsConstructor
public class FinanceSettleOrderTransferDto {

    @JsonIgnore
    private String curUserKey;

    @Valid
    @NotEmpty(message = "转交结算单信息不能为空")
    @Size(max = 20, message = "同时转交数量不能超过20个")
    @ApiModelProperty(value = "转交结算单信息")
    private List<FianceSettleOrderTransferItemDto> settleOrderTransferItemDtoList;

    @NotBlank(message = "转交人不能为空")
    @ApiModelProperty(value = "转交人")
    private String transferUser;

    @Data
    public static class FianceSettleOrderTransferItemDto {
        @ApiModelProperty(value = "结算单号")
        private String settleOrderNo;

        @ApiModelProperty("任务id")
        private String taskId;

        @ApiModelProperty("意见")
        private String comment;
    }

}
