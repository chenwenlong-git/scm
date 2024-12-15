package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/28 09:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BizLogCreateMqDto extends BaseMqMessageDto {

    /**
     * 业务日志序列号（雪花算法生成，用于做幂等）
     */
    @NotBlank
    private String bizLogCode;

    /**
     * 日志版本
     * 当报文内容发生不可兼容的升级时，需要改变该版本号，便于刷数或前端兼容
     */
    @NotNull
    private Integer logVersion;

    /**
     * 业务系统编号
     */
    @NotBlank
    private String bizSystemCode;

    /**
     * 业务模块
     */
    @NotBlank
    private String bizModule;

    /**
     * 业务单号，比如运单号、uid等
     */
    @NotBlank
    private String bizCode;

    /**
     * 业务日志操作时间（可用作日志版本号）
     */
    @NotNull
    private Long operateTime;

    /**
     * 操作人
     */
    @NotBlank
    private String operateUser;

    /**
     * 操作人名称
     */
    @NotBlank
    private String operateUsername;

    /**
     * 日志内容（展示于前台的string）
     */
    private String content;

    /**
     * 日志详情，可存储对象，最终以json格式数据存储数据库、展示前端
     */
    @Valid
    private Object detail;

}
