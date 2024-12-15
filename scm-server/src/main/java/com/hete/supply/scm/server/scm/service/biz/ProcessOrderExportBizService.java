package com.hete.supply.scm.server.scm.service.biz;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.InventoryShortageReportExportDto;
import com.hete.supply.scm.api.scm.entity.vo.InventoryShortageReportExportVo;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderItemDao;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/12/6.
 */
@Service
@RequiredArgsConstructor
public class ProcessOrderExportBizService {

    private final ProcessOrderDao processOrderDao;
    private final ProcessOrderItemDao processOrderItemDao;
    private final PlmRemoteService plmRemoteService;
    private final ConsistencySendMqService consistencySendMqService;
    private final static int LEVEL_ONE = 1;
    private final static int LEVEL_TWO = 2;
    private final static int LEVEL_THREE = 3;
    private final static int LEVEL_FOUR = 4;


    /**
     * 获取缺货报表导出数据
     *
     * @param dto 缺货报表导出参数
     * @return 包含库存不足报表导出数据的 CommonResult
     */
    public CommonResult<ExportationListResultBo<InventoryShortageReportExportVo>> getInventoryShortageReportData(InventoryShortageReportExportDto dto) {
        // 初始化导出结果对象
        ExportationListResultBo<InventoryShortageReportExportVo> result = new ExportationListResultBo<>();

        CommonPageResult.PageInfo<ProcessOrderPo> inventoryShortageReportPage
                = processOrderDao.getInventoryShortageReportPage(dto);

        List<ProcessOrderPo> pageResults = inventoryShortageReportPage.getRecords();
        // 如果查询结果为空，则返回空导出结果
        if (CollectionUtils.isEmpty(pageResults)) {
            return CommonResult.success(result);
        }

        // 提取查询结果中的订单号集合，查询加工单号集合对应的加工单明细数据
        Set<String> processOrderNos = pageResults.stream()
                .map(ProcessOrderPo::getProcessOrderNo)
                .collect(Collectors.toSet());
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNos(processOrderNos);

        // 提取订单明细中的 SKU 集合，查询 SKU 对应的最终分类信息
        List<String> processSkus = processOrderItemPos.stream()
                .map(ProcessOrderItemPo::getSku)
                .collect(Collectors.toList());
        Map<String, List<Map<Integer, String>>> skuCategoryMap = plmRemoteService.getSkuCategories(processSkus);

        // 构建导出数据列表
        List<InventoryShortageReportExportVo> rowDataList = pageResults.stream()
                .map(pageResult -> {
                    final String processOrderNo = pageResult.getProcessOrderNo();

                    // 构建导出数据对象
                    InventoryShortageReportExportVo vo = new InventoryShortageReportExportVo();
                    vo.setProcessOrderNo(processOrderNo);
                    vo.setSpu(pageResult.getSpu());

                    ProcessOrderItemPo matchPoi = processOrderItemPos.stream()
                            .filter(poi -> Objects.equals(processOrderNo, poi.getProcessOrderNo()))
                            .findFirst()
                            .orElse(null);

                    // 设置导出数据对象的相关属性
                    vo.setSku(Objects.nonNull(matchPoi) ? matchPoi.getSku() : "");
                    List<Map<Integer, String>> categoryMaps = skuCategoryMap.get(vo.getSku());
                    if (CollectionUtils.isNotEmpty(categoryMaps)) {
                        for (Map<Integer, String> categoryMap : categoryMaps) {
                            categoryMap.forEach((k, v) -> {
                                if (Objects.equals(LEVEL_ONE, k)) {
                                    vo.setLevelOneCategoryCnName(v);
                                }
                                if (Objects.equals(LEVEL_TWO, k)) {
                                    vo.setLevelTwoCategoryCnName(v);
                                }
                                if (Objects.equals(LEVEL_THREE, k)) {
                                    vo.setLevelThreeCategoryCnName(v);
                                }
                                if (Objects.equals(LEVEL_FOUR, k)) {
                                    vo.setLevelFourCategoryCnName(v);
                                }
                            });
                        }
                    }
                    vo.setProcessOrderType(Objects.nonNull(pageResult.getProcessOrderType()) ? pageResult.getProcessOrderType()
                            .getRemark() : "");
                    vo.setProcessOrderStatus(Objects.nonNull(pageResult.getProcessOrderStatus()) ? pageResult.getProcessOrderStatus()
                            .getRemark() : "");
                    vo.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(pageResult.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    vo.setProcessNum(pageResult.getTotalProcessNum());
                    vo.setDaysToDelivery(ScmTimeUtil.calculateDuration(pageResult.getCreateTime()));
                    return vo;
                })
                .collect(Collectors.toList());
        result.setRowDataList(rowDataList);
        return CommonResult.success(result);
    }


    /**
     * 获取库存缺货报告总数并返回通用结果对象。
     *
     * @param dto 包含查询条件的库存缺货报告导出数据传输对象
     * @return 包含库存缺货报告总数的通用结果对象
     */
    public CommonResult<Integer> getInventoryShortageReportTotals(InventoryShortageReportExportDto dto) {
        return CommonResult.success(processOrderDao.getInventoryShortageReportTotals(dto));
    }

    /**
     * 导出库存缺货报告，并通过 MQ 发送消息通知。
     *
     * @param dto 包含导出条件的库存缺货报告导出数据传输对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportInventoryShortageReport(InventoryShortageReportExportDto dto) {
        consistencySendMqService.execSendMq(
                ScmExportHandler.class,
                new FileOperateMessageDto<>(
                        GlobalContext.getUserKey(),
                        GlobalContext.getUsername(),
                        FileOperateBizType.SCM_PROCESS_ORDER_INVENTORY_SHORTAGE_REPORT_EXPORT.getCode(),
                        dto
                )
        );
    }

}
