package com.hete.supply.scm.server.scm.settle.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/12/20.
 */
@Data
public class SettleOrderItemCompleteUserVo {


    private List<CompleteUserInfo> completeUserList;

    @Data
    @AllArgsConstructor
    public static class CompleteUserInfo {
        @ApiModelProperty(value = "完成人编号")
        private String completeUser;

        @ApiModelProperty(value = "完成人名称")
        private String completeUsername;
    }

}
