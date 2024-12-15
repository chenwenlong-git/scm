package com.hete.supply.scm.server.supplier.develop.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleOrderSearchDto;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderNoListDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleOrderSearchVo;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopSampleOrderBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/3 14:17
 */
@Service
@RequiredArgsConstructor
@Validated
public class SupplierDevelopSampleOrderBizService {

    private final DevelopSampleOrderBaseService developSampleOrderBaseService;
    private final AuthBaseService authBaseService;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final ConsistencySendMqService consistencySendMqService;

    /**
     * 列表
     *
     * @param dto:
     * @return PageInfo<DevelopSampleOrderSearchVo>
     * @author ChenWenLong
     * @date 2023/8/10 14:06
     */
    public CommonPageResult.PageInfo<DevelopSampleOrderSearchVo> search(DevelopSampleOrderSearchDto dto) {
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCodeList(supplierCodeList);
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult.PageInfo<>();
        }
        return developSampleOrderBaseService.search(dto);
    }

    /**
     * 导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/10 14:06
     */
    @Transactional(rollbackFor = Exception.class)
    public void export(DevelopSampleOrderSearchDto dto) {
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCodeList(supplierCodeList);
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new ParamIllegalException("导出数据为空");
        }
        if (null == developSampleOrderBaseService.getSearchDevelopSampleWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = developSampleOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SPM_DEVELOP_SAMPLE_ORDER_EXPORT.getCode(), dto));
    }

    /**
     * 退样签收
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/10 14:07
     */
    @Transactional(rollbackFor = Exception.class)
    public void sign(DevelopSampleOrderNoListDto dto) {
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(dto.getDevelopSampleOrderNoList());
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            throw new BizException("数据不能为空，请刷新页面后重试！");
        }

        //验证
        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            developSampleOrderPo.setDevelopSampleStatus(developSampleOrderPo.getDevelopSampleStatus().toReturnSamplesReceipt());
            developSampleOrderPo.setReturnUser(GlobalContext.getUserKey());
            developSampleOrderPo.setReturnUsername(GlobalContext.getUsername());
            developSampleOrderPo.setReturnTime(LocalDateTime.now());
        }

        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);
    }

}
