package com.hete.supply.scm.server.supplier.settle.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleSettleSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleSettleStatus;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettleOrderDetailDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettleOrderExamineDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleSearchVo;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopExportBizService;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopSampleSettleBaseService;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.dao.DevelopSampleSettleOrderDao;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/2 10:33
 */
@Service
@RequiredArgsConstructor
public class SupplierDevelopSampleSettleBizService {

    private final DevelopSampleSettleBaseService developSampleSettleBaseService;
    private final AuthBaseService authBaseService;
    private final DevelopSampleSettleOrderDao developSampleSettleOrderDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final DevelopExportBizService developExportBizService;

    /**
     * 分页查询
     *
     * @param dto:
     * @return PageInfo<DevelopSampleSettleSearchVo>
     * @author ChenWenLong
     * @date 2023/8/1 16:37
     */
    public CommonPageResult.PageInfo<DevelopSampleSettleSearchVo> search(DevelopSampleSettleSearchDto dto) {
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        List<DevelopSampleSettleStatus> notDevelopSampleSettleStatus = new ArrayList<>();
        notDevelopSampleSettleStatus.add(DevelopSampleSettleStatus.WAIT_CONFIRM);
        dto.setNotDevelopSampleSettleStatusList(notDevelopSampleSettleStatus);
        dto.setAuthSupplierCodeList(supplierCodeList);
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult.PageInfo<>();
        }
        return developSampleSettleBaseService.search(dto);
    }

    /**
     * 详情
     *
     * @param dto:
     * @return String
     * @author ChenWenLong
     * @date 2023/8/1 16:42
     */
    public DevelopSampleSettleOrderDetailVo detail(DevelopSampleSettleOrderDetailDto dto) {
        DevelopSampleSettleOrderPo developSampleSettleOrderPo = developSampleSettleOrderDao.getByDevelopSampleSettleOrderNo(dto.getDevelopSampleSettleOrderNo());
        this.verifyAuth(developSampleSettleOrderPo);
        return developSampleSettleBaseService.detail(dto);
    }

    /**
     * 审核
     *
     * @param dto:
     * @return Boolean
     * @author ChenWenLong
     * @date 2023/8/1 17:02
     */
    public void examine(DevelopSampleSettleOrderExamineDto dto) {
        DevelopSampleSettleOrderPo developSampleSettleOrderPo = developSampleSettleOrderDao.getByIdVersion(dto.getDevelopSampleSettleOrderId(), dto.getVersion());
        this.verifyAuth(developSampleSettleOrderPo);
        developSampleSettleBaseService.examine(dto);
    }

    /**
     * 列表导出
     *
     * @param dto:
     * @return Boolean
     * @author ChenWenLong
     * @date 2023/8/1 17:13
     */
    @Transactional(rollbackFor = Exception.class)
    public void export(DevelopSampleSettleSearchDto dto) {
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        List<DevelopSampleSettleStatus> notDevelopSampleSettleStatus = new ArrayList<>();
        notDevelopSampleSettleStatus.add(DevelopSampleSettleStatus.WAIT_CONFIRM);
        dto.setNotDevelopSampleSettleStatusList(notDevelopSampleSettleStatus);
        dto.setAuthSupplierCodeList(supplierCodeList);
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new ParamIllegalException("导出数据为空");
        }
        developExportBizService.getExportTotals(dto);
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SPM_SAMPLE_SETTLE_ORDER_EXPORT.getCode(), dto));
    }

    /**
     * 验证供应商
     *
     * @param developSampleSettleOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/5 14:14
     */
    public void verifyAuth(DevelopSampleSettleOrderPo developSampleSettleOrderPo) {
        if (developSampleSettleOrderPo == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new BizException("禁止非法请求");
        }

        if (CollectionUtil.isNotEmpty(supplierCodeList) && !supplierCodeList.contains(developSampleSettleOrderPo.getSupplierCode())) {
            throw new BizException("禁止非法请求");
        }

    }
}
