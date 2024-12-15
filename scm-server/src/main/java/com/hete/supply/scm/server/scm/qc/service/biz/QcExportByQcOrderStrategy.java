package com.hete.supply.scm.server.scm.qc.service.biz;

import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.QcExportType;
import com.hete.supply.scm.api.scm.entity.vo.QcExportVo;
import com.hete.supply.scm.server.scm.qc.converter.QcOrderConverter;
import com.hete.supply.scm.server.scm.qc.entity.vo.QcSearchVo;
import com.hete.supply.scm.server.scm.qc.service.base.QcExportFilterStrategy;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 质检明细导出策略，按照质检订单筛选导出质检明细数据。
 * <p>
 * 该策略实现了质检明细导出筛选策略接口 {@link QcExportFilterStrategy}，允许根据质检订单的特定条件筛选出符合要求的质检明细数据并进行导出。
 *
 * @author yanjiawei
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class QcExportByQcOrderStrategy implements QcExportFilterStrategy {

    private final QcOrderBizService qcOrderBizService;

    /**
     * 根据质检订单的筛选条件过滤质检明细数据，生成导出结果。
     *
     * @param qcSearchDto 质检明细导出筛选的输入参数，包含质检订单的筛选条件。
     * @return 一个包含质检明细导出结果的通用结果对象 {@link CommonResult}，其中包含了 {@link ExportationListResultBo} 类型的数据，
     * 该数据表示筛选后的质检明细导出结果。
     * @see QcSearchDto
     * @see CommonResult
     * @see ExportationListResultBo
     * @see QcExportVo
     */
    @Override
    public CommonResult<ExportationListResultBo<QcExportVo>> filterList(QcSearchDto qcSearchDto) {
        CommonPageResult.PageInfo<QcSearchVo> qcSearchVoPageInfo = qcOrderBizService.searchQc(qcSearchDto);
        List<QcSearchVo> qcSearchVos = qcSearchVoPageInfo.getRecords();

        List<QcExportVo> rowDataList = qcSearchVos.stream()
                .map(QcOrderConverter::createQcExportVo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        final ExportationListResultBo<QcExportVo> qcDetailExportVoExportationListResultBo
                = new ExportationListResultBo<>();
        qcDetailExportVoExportationListResultBo.setRowDataList(rowDataList);
        return CommonResult.success(qcDetailExportVoExportationListResultBo);
    }

    /**
     * 根据质检明细导出筛选条件，获取符合条件的质检记录总数。
     *
     * @param qcSearchDto 质检明细导出筛选条件的 DTO
     * @return 符合条件的质检记录总数
     */
    @Override
    public CommonResult<Integer> filterCount(QcSearchDto qcSearchDto) {
        CommonPageResult.PageInfo<QcSearchVo> qcSearchVoPageInfo = qcOrderBizService.searchQc(qcSearchDto);

        int totalCount = (int) qcSearchVoPageInfo.getTotalCount();
        return CommonResult.success(totalCount);
    }

    /**
     * 获取质检导出策略。
     *
     * @return 质检明细导出策略，通常返回一个具体的枚举值，表示所选择的策略。
     */
    @Override
    public QcExportType getQcDetailExportStrategy() {
        return QcExportType.BY_QC_ORDER;
    }
}
