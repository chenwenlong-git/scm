package com.hete.supply.scm.server.scm.qc.service.base;

import com.hete.supply.scm.api.scm.entity.dto.QcDetailSearchExportDto;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.QcExportType;
import com.hete.supply.scm.api.scm.entity.vo.QcExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * 质检明细导出筛选策略接口。
 * <p>
 * 该接口定义了质检明细导出时的筛选策略，允许根据特定条件筛选出符合要求的质检明细数据并进行导出。
 *
 * @author yanjiawei
 * @see QcDetailSearchExportDto 用于传递筛选条件的数据传输对象。
 * @see CommonResult 通用结果对象，包含了导出结果。
 * @see ExportationListResultBo 用于表示筛选后的导出结果的业务对象。
 * @see QcExportVo 用于表示质检明细导出的视图对象。
 */
public interface QcExportFilterStrategy {

    /**
     * 质检明细导出筛选策略接口。
     *
     * @param qcSearchDto 质检明细导出筛选的输入参数，包含筛选条件。
     * @return 一个包含质检明细导出结果的通用结果对象 {@link CommonResult}，其中包含了 {@link QcSearchDto} 类型的数据，
     * 该数据表示筛选后的质检明细导出结果。
     * @see QcSearchDto
     * @see CommonResult
     * @see ExportationListResultBo
     * @see QcExportVo
     */
    CommonResult<ExportationListResultBo<QcExportVo>> filterList(QcSearchDto qcSearchDto);

    /**
     * 计算符合筛选条件的质检明细总数。
     * <p>
     * 该方法用于根据给定的质检明细导出筛选条件计算符合条件的质检明细总数，而不执行具体的导出操作。
     *
     * @param qcSearchDto 质检明细导出筛选的输入参数，包含筛选条件。
     * @return 一个包含质检明细总数的通用结果对象 {@link CommonResult}，其中包含一个整数值，表示符合筛选条件的质检明细总数。
     * @see QcSearchDto
     * @see CommonResult
     */
    CommonResult<Integer> filterCount(QcSearchDto qcSearchDto);

    /**
     * 获取质检明细导出策略实例。
     *
     * @return 质检明细导出策略的实例，用于根据特定策略进行质检数据导出。
     */
    QcExportType getQcDetailExportStrategy();

}
