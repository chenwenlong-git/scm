package com.hete.supply.scm.server.scm.service.biz;

import com.hete.supply.scm.server.scm.entity.vo.TimeCommonVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yanjiawei
 * @date 2023年09月14日 18:23
 */
@Service
@RequiredArgsConstructor
public class TimeCommonBizService {

    /**
     * 获取当前日期和时间的方法。
     *
     * @return 包含当前日期和时间的CommonTimeVo对象。
     */
    public TimeCommonVo getTimeCommon() {
        return new TimeCommonVo();
    }
}
