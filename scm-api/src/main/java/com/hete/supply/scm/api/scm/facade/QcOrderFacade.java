package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.QcDefectHandlingQueryDto;
import com.hete.supply.scm.api.scm.entity.dto.QcDto;
import com.hete.supply.scm.api.scm.entity.dto.QcOrderQueryDto;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.QcDefectHandlingVo;
import com.hete.supply.scm.api.scm.entity.vo.QcDetailSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.QcExportVo;
import com.hete.supply.scm.api.scm.entity.vo.QcVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;

/**
 * 质检单相关接口
 *
 * @author yanjiawei
 * Created on 2023/10/12.
 */
public interface QcOrderFacade {

    /**
     * 根据查询条件查询质检单及其相关质检明细信息。
     * 参数说明：质检单号与收货单号最大支持100条
     * 接口说明：当质检单号与收货单号同时不为空时，结果集根据质检单号去重。
     *
     * @param qcOrderQueryDto 查询条件包含质检单号列表和收货单号列表
     * @return 包含质检单和质检明细信息的结果对象
     */
    CommonResult<QcVo> findQcOrderAndDetails(QcOrderQueryDto qcOrderQueryDto);

    /**
     * 获取质检明细导出的总数。
     *
     * @param dto 质检搜索参数对象 {@link QcSearchDto}
     * @return 质检明细导出的总数
     */
    CommonResult<Integer> getQcDetailExportTotals(QcSearchDto dto);

    /**
     * 获取质检明细导出列表。
     *
     * @param dto 质检搜索参数对象 {@link QcSearchDto}
     * @return 包含质检明细导出列表的通用结果对象 {@link CommonResult}，其中包含 {@link ExportationListResultBo}，其泛型为 {@link QcExportVo}
     */
    CommonResult<ExportationListResultBo<QcExportVo>> getQcDetailExportList(QcSearchDto dto);

    /**
     * 根据查询条件查询质检次品处理信息。
     * 参数说明：质检单号
     *
     * @param qcDefectHandlingQueryDto 查询条件包含质检单号列表
     * @return 包含质检次品处理信息的结果对象
     */
    CommonResult<QcDefectHandlingVo> findQcDefectHandlingByQcOrderNos(QcDefectHandlingQueryDto qcDefectHandlingQueryDto);

    /**
     * 查询sku待质检数量
     *
     * @return
     */
    CommonResult<ResultList<QcDetailSkuVo>> listBySkuAndQcState(QcDto dto);

}
