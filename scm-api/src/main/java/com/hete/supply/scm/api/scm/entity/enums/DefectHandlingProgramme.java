package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/30 16:18
 */
@Getter
@AllArgsConstructor
public enum DefectHandlingProgramme implements IRemark {
    // 次品方案:RETURN_SUPPLY(次品退供),SCRAPPED(次品报废),EXCHANGE_GOODS(次品换货),COMPROMISE(次品让步),
    RETURN_SUPPLY("次品退供"),
    SCRAPPED("次品报废"),
    EXCHANGE_GOODS("次品换货"),
    COMPROMISE("次品让步"),
    ;

    private final String remark;
}
