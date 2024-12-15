package com.hete.supply.scm.server.scm.develop.service.strategy;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderSubmitHandleDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleDirection;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 退样样品单处理策略
 *
 * @author ChenWenLong
 * @date 2024/3/23 22:24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DevelopSampleOrderReturnStrategy implements DevelopSampleOrderHandleStrategy {

    private final DevelopSampleOrderDao developSampleOrderDao;

    @Override
    public void submitHandleVerify(List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> dtoList,
                                   List<DevelopSampleOrderPo> developSampleOrderPoList) {
        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto itemDto : dtoList) {
            Assert.isTrue(DevelopSampleDirection.RETURN_SAMPLES.equals(itemDto.getDevelopSampleDirection()),
                    () -> new ParamIllegalException("退样样品单的货物走向只能选:{}，请修改后重试！", DevelopSampleDirection.RETURN_SAMPLES.getRemark()));
        }
    }

    @Override
    public void developSampleOrderSubmitHandle(List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> dtoList,
                                               List<DevelopSampleOrderPo> developSampleOrderPoList) {
        log.info("样品单确认处理-退样样品的DTO={},PO={}", JacksonUtil.parse2Str(dtoList), JacksonUtil.parse2Str(developSampleOrderPoList));
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            throw new ParamIllegalException("样品单数据不能为空，请刷新页面后重试！");
        }
        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto itemDto : dtoList) {
            String developSampleOrderNo = itemDto.getDevelopSampleOrderNo();
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.stream()
                    .filter(po -> po.getDevelopSampleOrderNo().equals(developSampleOrderNo))
                    .filter(po -> po.getVersion().equals(itemDto.getVersion()))
                    .findFirst()
                    .orElse(null);
            if (null == developSampleOrderPo) {
                throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
            }

            // 判断状态是否正确
            developSampleOrderPo.getDevelopSampleStatus().submitHandleVerify();

            developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.RETURN_SAMPLES);
            developSampleOrderPo.setReturnTrackingNo(itemDto.getReturnTrackingNo());
            developSampleOrderPo.setDevelopSampleDirection(itemDto.getDevelopSampleDirection());
            developSampleOrderPo.setHandleUser(GlobalContext.getUserKey());
            developSampleOrderPo.setHandleUsername(GlobalContext.getUsername());
            developSampleOrderPo.setHandleTime(LocalDateTime.now());

        }

        // 批量更新样品单
        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);

    }

    @Override
    public DevelopSampleMethod getHandlerType() {
        return DevelopSampleMethod.SAMPLE_RETURN;
    }
}
