package com.hete.supply.scm.server.scm.process.service.ref;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderDao;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author weiwenxin
 * @date 2023/10/12 15:16
 */
@Service
@RequiredArgsConstructor
@Validated
public class ProcessOrderRefService {
    private final ProcessOrderDao processOrderDao;

    public ProcessOrderPo getProcessOrderPoByNo(String processOrderNo) {
        if (StringUtils.isBlank(processOrderNo)) {
            return null;
        }
        return processOrderDao.getByProcessOrderNo(processOrderNo);
    }
}
