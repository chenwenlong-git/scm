package com.hete.supply.scm.server.scm.qc.service.biz;

import com.hete.supply.scm.server.scm.defect.service.biz.DefectBizService;
import com.hete.supply.scm.server.scm.qc.handler.AbstractOnShelfOrderHandler;
import com.hete.supply.scm.server.scm.qc.handler.DefaultOnShelfOrderHandler;
import com.hete.supply.scm.server.scm.qc.handler.DefectHandlingOnShelfOrderHandler;
import com.hete.supply.scm.server.scm.qc.service.base.QcOnShelvesOrderBaseService;
import com.hete.supply.scm.server.supplier.entity.dto.OnShelvesOrderCreateResultMqDto;
import com.hete.supply.wms.api.WmsEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.hete.supply.wms.api.WmsEnum.OnShelvesOrderCreateType.CONCESSION;

/**
 * QcOnShelvesOrderBizService 是用于处理上架单业务逻辑的服务类，标记了 Spring Service 注解和 Lombok Log 注解。
 * 这个类包括两个成员变量：DefectBizService 和 QcOnShelvesOrderBaseService。
 *
 * @Description 处理上架单创建/更新消息
 * @Author yanjiawei
 * @Date 2023/10/18 14:28
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QcOnShelvesOrderBizService {
    private final DefectBizService defectBizService;
    private final QcOnShelvesOrderBaseService qcOnShelvesOrderBaseService;

    /**
     * 处理上架单创建/更新消息的方法，根据不同类型的上架单调用不同的处理器。
     *
     * @param message 上架单创建/更新消息
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleMessage(OnShelvesOrderCreateResultMqDto message) {
        final WmsEnum.OnShelvesOrderCreateType onShelvesOrderCreateType = message.getOnShelvesOrderCreateType();

        if (Objects.equals(CONCESSION, onShelvesOrderCreateType)) {
            AbstractOnShelfOrderHandler<OnShelvesOrderCreateResultMqDto> defectHandlingHandler
                    = new DefectHandlingOnShelfOrderHandler(qcOnShelvesOrderBaseService, defectBizService);
            defectHandlingHandler.processShelfOrder(message);
        } else {
            AbstractOnShelfOrderHandler<OnShelvesOrderCreateResultMqDto> defaultHandler
                    = new DefaultOnShelfOrderHandler(qcOnShelvesOrderBaseService);
            defaultHandler.processShelfOrder(message);
        }
    }
}
