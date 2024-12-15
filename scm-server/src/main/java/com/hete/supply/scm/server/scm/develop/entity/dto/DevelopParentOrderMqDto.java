package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/12 09:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopParentOrderMqDto extends BaseMqMessageDto {

    /**
     * 开发需求母单号
     */
    private String developParentOrderNo;

    /**
     * 开发需求子单号
     */
    private List<String> developChildOrderNoList;

}
