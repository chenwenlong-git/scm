package com.hete.supply.scm.server.scm.qc.service.biz;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.google.common.collect.Lists;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingProgramme;
import com.hete.supply.scm.api.scm.entity.enums.QcExportType;
import com.hete.supply.scm.api.scm.entity.enums.QcOrigin;
import com.hete.supply.scm.api.scm.entity.vo.QcExportVo;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.qc.converter.QcOrderConverter;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcReceiveOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcDetailExportBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcReceiveOrderPo;
import com.hete.supply.scm.server.scm.qc.service.base.QcExportFilterStrategy;
import com.hete.supply.scm.server.scm.qc.service.base.QcOrderBaseService;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 质检明细导出策略，导出质检明细数据。
 * <p>
 * 该策略实现了质检明细导出筛选策略接口 {@link QcExportFilterStrategy}，允许根据不良原因的特定条件筛选出符合要求的质检明细数据并进行导出。
 *
 * @author yanjiawei
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class QcExportByQcDetailStrategy implements QcExportFilterStrategy {

    private final QcOrderBizService qcOrderBizService;
    private final QcDetailDao qcDetailDao;
    private final DefectHandlingDao defectHandlingDao;
    private static final CommonResult<Integer> COMMON_RESULT_SUCCESS_ZERO = CommonResult.success(0);
    private static final CommonResult<ExportationListResultBo<QcExportVo>> SUCCESS_RESULT
            = CommonResult.success(new ExportationListResultBo<>());
    private final QcReceiveOrderDao qcReceiveOrderDao;
    private final PlmRemoteService plmRemoteService;
    private final SupplierBaseService supplierBaseService;
    private final QcOrderBaseService qcOrderBaseService;


    /**
     * 根据质检明细筛选条件过滤质检明细数据，生成导出结果。
     *
     * @param qcSearchDto 质检明细导出筛选的输入参数，包含不良原因的筛选条件。
     * @return 一个包含质检明细导出结果的通用结果对象 {@link CommonResult}，其中包含了 {@link ExportationListResultBo} 类型的数据，
     * 该数据表示筛选后的质检明细导出结果。
     * @see QcSearchDto
     * @see CommonResult
     * @see ExportationListResultBo
     * @see QcExportVo
     */
    @Override
    public CommonResult<ExportationListResultBo<QcExportVo>> filterList(QcSearchDto qcSearchDto) {
        ExportationListResultBo<QcExportVo> qcDetailExportVoExportationListResultBo
                = new ExportationListResultBo<>();
        qcSearchDto = qcOrderBizService.processSkuCode(qcSearchDto);
        if (null == qcSearchDto) {
            return SUCCESS_RESULT;
        }

        IPage<QcDetailExportBo> qcDetailExportPageResult
                = qcDetailDao.getExportPage(PageDTO.of(qcSearchDto.getPageNo(), qcSearchDto.getPageSize()), qcSearchDto);
        final List<QcDetailExportBo> qcDetailExportBos = qcDetailExportPageResult.getRecords();
        if (CollectionUtils.isEmpty(qcDetailExportBos)) {
            return SUCCESS_RESULT;
        }

        // 次品处理信息
        final List<Long> qcDetailIds = qcDetailExportBos.stream()
                .map(QcDetailExportBo::getQcDetailId)
                .collect(Collectors.toList());
        List<DefectHandlingPo> defectHandlingPos
                = defectHandlingDao.listByQcDetailIds(qcDetailIds, DefectHandlingProgramme.COMPROMISE);

        // 计算合格率 & 交接数量
        final List<String> qcOrderNos = qcDetailExportBos.stream()
                .map(QcDetailExportBo::getQcOrderNo)
                .distinct()
                .collect(Collectors.toList());
        List<QcDetailPo> allRelateQcDetails = qcDetailDao.getListByQcOrderNoList(qcOrderNos);

        Map<String, String> passRateMap = calculatePassRate(allRelateQcDetails);
        Map<String, Integer> qsbAmount = calculateAmount(allRelateQcDetails);
        Map<String, Integer> qsbHandOverAmount = calculateHandoverAmount(allRelateQcDetails);
        Map<String, Integer> qsbPleAmount = calculateCompleteAmount(allRelateQcDetails);

        // 根据质检单号查询收货单
        final List<QcReceiveOrderPo> qcReceiveOrderPoList = qcReceiveOrderDao.getListByQcOrderNoList(qcOrderNos);
        final Map<String, QcReceiveOrderPo> qcOrderNoReceivePoMap = qcReceiveOrderPoList.stream()
                .collect(Collectors.toMap(QcReceiveOrderPo::getQcOrderNo, Function.identity()));

        // 获取sku产品名称以及商品类目
        final List<String> skuList = qcDetailExportBos.stream()
                .map(QcDetailExportBo::getSkuCode)
                .distinct()
                .collect(Collectors.toList());

        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        final Map<String, String> skuCategoriesMap = plmRemoteService.getSkuSecondCategoriesCnMapBySkuList(skuList);

        // 供应商编码
        List<String> querySupplierCodeList = Lists.newArrayList();

        final List<String> receiveSupplierCodes = qcReceiveOrderPoList.stream()
                .map(QcReceiveOrderPo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(receiveSupplierCodes)) {
            querySupplierCodeList.addAll(receiveSupplierCodes);
        }
        final List<String> qcSupplierCodeList = qcDetailExportBos.stream()
                .map(QcDetailExportBo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(qcSupplierCodeList)) {
            querySupplierCodeList.addAll(qcSupplierCodeList);
        }

        final List<SupplierPo> supplierPoList = supplierBaseService.getBySupplierCodeList(querySupplierCodeList);
        final Map<String, SupplierPo> supplierCodePoMap = supplierPoList.stream()
                .collect(Collectors.toMap(SupplierPo::getSupplierCode, Function.identity()));

        // 驻场质检判断标准
        List<QcOrigin> residentQcOrigins = qcOrderBaseService.getResidentQcOrigins();

        List<QcExportVo> rowDataList = qcDetailExportBos.stream()
                .map(qcDetailExportBo -> {
                    final String qcOrderNo = qcDetailExportBo.getQcOrderNo();
                    final String skuCode = qcDetailExportBo.getSkuCode();
                    final String batchCode = qcDetailExportBo.getBatchCode();
                    final Long qcDetailId = qcDetailExportBo.getQcDetailId();

                    // 匹配次品处理记录
                    DefectHandlingPo matchDefectHandlingPo = defectHandlingPos.stream()
                            .filter(defectHandlingPo -> Objects.equals(qcDetailId, defectHandlingPo.getBizDetailId()))
                            .findFirst()
                            .orElse(null);

                    // 构建导出明细vo
                    QcExportVo qcDetailExportVo
                            = QcOrderConverter.createQcDetailExportVo(qcDetailExportBo, matchDefectHandlingPo);
                    // 设置合格率
                    qcDetailExportVo.setQcPassRate(passRateMap.getOrDefault(qcOrderNo + skuCode + batchCode, ""));

                    // 设置交接数量
                    qcDetailExportVo.setHandoverQuantity(qsbHandOverAmount.get(qcOrderNo + skuCode + batchCode));

                    // 需质检数量
                    qcDetailExportVo.setAmount(qsbAmount.get(qcOrderNo + skuCode + batchCode));

                    // 批次维度已质检数量
                    qcDetailExportVo.setCompleteQcAmount(qsbPleAmount.get(qcOrderNo + skuCode + batchCode));

                    // 质检数量
                    qcDetailExportVo.setQcQuantity(qcDetailExportVo.getHandoverQuantity());
                    // 设置供应商代码
                    final QcReceiveOrderPo qcReceiveOrderPo = qcOrderNoReceivePoMap.get(qcOrderNo);
                    if (null != qcReceiveOrderPo) {
                        qcDetailExportVo.setSupplier(qcReceiveOrderPo.getSupplierCode());
                        qcDetailExportVo.setReceiveType(qcReceiveOrderPo.getReceiveType().getRemark());
                        // 供应商类型 供应商等级
                        final SupplierPo supplierPo = supplierCodePoMap.get(qcReceiveOrderPo.getSupplierCode());
                        if (null != supplierPo) {
                            qcDetailExportVo.setSupplierType(supplierPo.getSupplierType().getRemark());
                            qcDetailExportVo.setSupplierGrade(supplierPo.getSupplierGrade().getRemark());
                        }
                    }

                    SupplierPo supplierPo = supplierCodePoMap.get(Objects.nonNull(qcDetailExportBo.getSupplierCode()) ? qcDetailExportBo.getSupplierCode() : "");
                    if (Objects.isNull(qcDetailExportVo.getSupplier())) {
                        qcDetailExportVo.setSupplier(qcDetailExportBo.getSupplierCode());
                    }
                    if (Objects.isNull(qcDetailExportVo.getSupplierType()) && Objects.nonNull(supplierPo)) {
                        qcDetailExportVo.setSupplierType(supplierPo.getSupplierType().getRemark());
                    }
                    if (Objects.isNull(qcDetailExportVo.getSupplierGrade()) && Objects.nonNull(supplierPo)) {
                        qcDetailExportVo.setSupplierGrade(supplierPo.getSupplierGrade().getRemark());
                    }

                    // 产品名称
                    qcDetailExportVo.setSkuEncode(skuEncodeMap.get(skuCode));
                    qcDetailExportVo.setCategoryName(skuCategoriesMap.get(skuCode));
                    qcDetailExportVo.setIsUrgentOrder(
                            Objects.nonNull(qcDetailExportBo.getIsUrgentOrder()) ?
                                    qcDetailExportBo.getIsUrgentOrder().getValue() : "");
                    qcDetailExportVo.setGoodAndDefectAmount(
                            qcDetailExportVo.getGoodQuantity() +
                                    qcDetailExportVo.getDefectiveQuantity()
                    );
                    return qcDetailExportVo;
                })
                .collect(Collectors.toList());

        qcDetailExportVoExportationListResultBo.setRowDataList(rowDataList);
        return CommonResult.success(qcDetailExportVoExportationListResultBo);
    }

    /**
     * @Description 统计批次维度已质检总数
     * @author yanjiawei
     * @Date 2024/6/27 15:39
     */
    private Map<String, Integer> calculateCompleteAmount(List<QcDetailPo> allRelateQcDetails) {
        Map<String, Integer> batchCompleteMap = new HashMap<>(16);

        Map<String, List<QcDetailPo>> groupByQsb = allRelateQcDetails.stream()
                .collect(Collectors.groupingBy(qcDetailPo -> qcDetailPo.getQcOrderNo() + qcDetailPo.getSkuCode() + qcDetailPo.getBatchCode()));
        for (Map.Entry<String, List<QcDetailPo>> entry : groupByQsb.entrySet()) {
            List<QcDetailPo> group = entry.getValue();
            int passAmount = group.stream().mapToInt(QcDetailPo::getPassAmount).sum();
            int notPassAmount = group.stream().mapToInt(QcDetailPo::getNotPassAmount).sum();

            batchCompleteMap.put(entry.getKey(), passAmount + notPassAmount);
        }
        return batchCompleteMap;
    }

    /**
     * @Description 统计批次维度需质检数量
     * @author yanjiawei
     * @Date 2024/6/27 15:43
     */
    private Map<String, Integer> calculateAmount(List<QcDetailPo> allRelateQcDetails) {
        Map<String, Integer> qsbAmountMap = new HashMap<>(16);

        Map<String, List<QcDetailPo>> groupByQsb = allRelateQcDetails.stream()
                .filter(qcDetailPo -> qcDetailPo.getRelationQcDetailId() == 0L)
                .collect(Collectors.groupingBy(qcDetailPo -> qcDetailPo.getQcOrderNo() + qcDetailPo.getSkuCode() + qcDetailPo.getBatchCode()));

        for (Map.Entry<String, List<QcDetailPo>> entry : groupByQsb.entrySet()) {
            List<QcDetailPo> group = entry.getValue();
            int totalAmount = group.stream().mapToInt(QcDetailPo::getAmount).sum();
            qsbAmountMap.put(entry.getKey(), totalAmount);
        }
        return qsbAmountMap;
    }

    @Override
    public CommonResult<Integer> filterCount(QcSearchDto qcSearchDto) {
        qcSearchDto = qcOrderBizService.processSkuCode(qcSearchDto);
        if (null == qcSearchDto) {
            return COMMON_RESULT_SUCCESS_ZERO;
        }

        return CommonResult.success(qcDetailDao.getExportCount(qcSearchDto));
    }

    @Override
    public QcExportType getQcDetailExportStrategy() {
        return QcExportType.BY_QC_ORDER_DETAIL;
    }

    /**
     * 计算根据 SKU 和批次码分组后的合格率。
     *
     * @param allRelateQcDetails 关联质检明细
     * @return 以 SKU + 批次码为键，合格率为值的映射
     */
    private Map<String, String> calculatePassRate(List<QcDetailPo> allRelateQcDetails) {
        Map<String, String> passRateMap = new HashMap<>(16);
        // 使用流式操作将 qcDetailPageResults 按照 SKU + 批次码分组
        Map<String, List<QcDetailPo>> groupedByQcOrderNoAndSkuAndBatchCode = allRelateQcDetails.stream()
                .collect(Collectors.groupingBy(qcDetailPo -> qcDetailPo.getQcOrderNo() + qcDetailPo.getSkuCode() + qcDetailPo.getBatchCode()));

        // 遍历分组并计算合格率
        for (Map.Entry<String, List<QcDetailPo>> entry : groupedByQcOrderNoAndSkuAndBatchCode.entrySet()) {
            List<QcDetailPo> group = entry.getValue();
            int totalAmount = group.stream()
                    .mapToInt(QcDetailPo::getAmount)
                    .sum();
            int passAmount = group.stream()
                    .mapToInt(QcDetailPo::getPassAmount)
                    .sum();

            BigDecimal passRate = totalAmount == 0 ? BigDecimal.ZERO :
                    new BigDecimal(passAmount)
                            .divide(new BigDecimal(totalAmount), 4, RoundingMode.DOWN)
                            .multiply(BigDecimal.TEN.multiply(BigDecimal.TEN))
                            .setScale(2, RoundingMode.DOWN);

            passRateMap.put(entry.getKey(), StrUtil.format("{}%", passRate));
        }
        return passRateMap;
    }

    /**
     * @Description 计算批次维度交接数
     * @author yanjiawei
     * @Date 2024/6/27 15:52
     */
    private Map<String, Integer> calculateHandoverAmount(List<QcDetailPo> allRelateQcDetails) {
        Map<String, Integer> qsbHandOverMap = new HashMap<>(16);

        Map<String, List<QcDetailPo>> groupByQsb = allRelateQcDetails.stream()
                .filter(qcDetailPo -> qcDetailPo.getRelationQcDetailId() == 0L)
                .collect(Collectors.groupingBy(qcDetailPo -> qcDetailPo.getQcOrderNo() + qcDetailPo.getSkuCode() + qcDetailPo.getBatchCode()));

        // 遍历分组并计算交接数
        for (Map.Entry<String, List<QcDetailPo>> entry : groupByQsb.entrySet()) {
            List<QcDetailPo> group = entry.getValue();
            int handOverAmount = group.stream().mapToInt(QcDetailPo::getHandOverAmount).sum();
            qsbHandOverMap.put(entry.getKey(), handOverAmount);
        }
        return qsbHandOverMap;
    }

    /**
     * 组合质检单号列表，考虑筛选条件中的查询质检单号、质检明细列表、质检收货单列表，生成一个合并后的质检单号列表。
     *
     * @param qcSearchDto          筛选条件的 DTO，包含查询质检单号的条件。
     * @param qcDetailPoList       质检明细列表，用于提取质检单号。
     * @param qcReceiveOrderPoList 质检收货单列表，用于提取质检单号。
     * @return 合并后的质检单号列表。
     */
    private List<String> getRelateOrderNoList(QcSearchDto qcSearchDto,
                                              List<QcDetailPo> qcDetailPoList,
                                              List<QcReceiveOrderPo> qcReceiveOrderPoList) {
        final List<String> queryQcOrderNos = Optional.ofNullable(qcSearchDto.getQcOrderNoList())
                .orElse(Collections.emptyList());
        final List<String> qcDetailQcOrderNoList = Optional.ofNullable(qcDetailPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(QcDetailPo::getQcOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<String> qcReceiveQcOrderNoList = Optional.ofNullable(qcReceiveOrderPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(QcReceiveOrderPo::getQcOrderNo)
                .distinct()
                .collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(queryQcOrderNos) ? queryQcOrderNos : Stream.of(qcDetailQcOrderNoList, qcReceiveQcOrderNoList)
                .distinct()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}