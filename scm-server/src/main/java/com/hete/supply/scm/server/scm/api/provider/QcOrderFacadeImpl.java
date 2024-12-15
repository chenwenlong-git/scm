package com.hete.supply.scm.server.scm.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.QcDefectHandlingQueryDto;
import com.hete.supply.scm.api.scm.entity.dto.QcDto;
import com.hete.supply.scm.api.scm.entity.dto.QcOrderQueryDto;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.QcDefectHandlingVo;
import com.hete.supply.scm.api.scm.entity.vo.QcDetailSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.QcExportVo;
import com.hete.supply.scm.api.scm.entity.vo.QcVo;
import com.hete.supply.scm.api.scm.facade.QcOrderFacade;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.qc.service.biz.QcOrderBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2023/10/19.
 */
@DubboService
@RequiredArgsConstructor
public class QcOrderFacadeImpl implements QcOrderFacade {

    private final QcOrderBizService qcOrderBizService;
    private final DefectHandlingDao defectHandlingDao;

    @Override
    public CommonResult<QcVo> findQcOrderAndDetails(QcOrderQueryDto qcOrderQueryDto) {
        QcVo qcVo = qcOrderBizService.findQcOrderAndDetails(qcOrderQueryDto);
        return CommonResult.success(qcVo);
    }

    /**
     * 获取质检明细导出的总数。
     *
     * @param dto 质检搜索参数对象 {@link QcSearchDto}
     * @return 质检明细导出的总数
     */
    @Override
    public CommonResult<Integer> getQcDetailExportTotals(QcSearchDto dto) {
        return qcOrderBizService.getExportTotals(dto);
    }

    /**
     * 获取质检明细导出列表。
     *
     * @param dto 质检搜索参数对象 {@link QcSearchDto}
     * @return 包含质检明细导出列表的通用结果对象 {@link CommonResult}，其中包含 {@link ExportationListResultBo}，其泛型为 {@link QcExportVo}
     */
    @Override
    public CommonResult<ExportationListResultBo<QcExportVo>> getQcDetailExportList(QcSearchDto dto) {
        return qcOrderBizService.getQcDetailExportList(dto);
    }

    /**
     * 根据质检单号查询质检次品处理信息。
     *
     * @param qcDefectHandlingQueryDto 查询条件包含质检单号
     * @return 包含质检次品处理信息的结果对象
     */
    @Override
    public CommonResult<QcDefectHandlingVo> findQcDefectHandlingByQcOrderNos(@Valid @NotNull QcDefectHandlingQueryDto qcDefectHandlingQueryDto) {
        return qcOrderBizService.findQcDefectHandlingByQcOrderNos(qcDefectHandlingQueryDto);
    }

    @Override
    public CommonResult<ResultList<QcDetailSkuVo>> listBySkuAndQcState(QcDto dto) {
        return CommonResult.successForList(qcOrderBizService.listBySkuAndQcState(dto));
    }
}
