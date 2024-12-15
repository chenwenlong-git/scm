package com.hete.supply.scm.server.scm.process.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleDetailExportVo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessSettleDetailReportPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.ProcessSettleOrderDetailAndScanSettleVo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/12/20.
 */
public class ProcessSettleConverter {

    public static IPage<ProcessSettleOrderDetailAndScanSettleVo> convertToVoPage(IPage<ProcessSettleDetailReportPo> pageResult) {
        IPage<ProcessSettleOrderDetailAndScanSettleVo> convertedPage = new Page<>();
        List<ProcessSettleOrderDetailAndScanSettleVo> convertedList = pageResult.getRecords()
                .stream()
                .map(po -> {
                    ProcessSettleOrderDetailAndScanSettleVo vo = new ProcessSettleOrderDetailAndScanSettleVo();
                    vo.setProcessSettleOrderId(po.getProcessSettleOrderId());
                    vo.setProcessSettleOrderItemId(po.getProcessSettleOrderItemId());
                    vo.setCompleteUser(po.getCompleteUser());
                    vo.setCompleteUserName(po.getCompleteUsername());
                    vo.setProcessCode(po.getProcessCode());
                    vo.setProcessLabel(po.getProcessLabel());
                    vo.setProcessName(po.getProcessName());
                    vo.setQualityGoodsCnt(po.getQualityGoodsCnt());
                    vo.setFirstLevelQualityGoodsCnt(po.getFirstLevelQualityGoodsCnt());
                    vo.setFirstLevelTotalCommission(po.getFirstLevelTotalCommission());
                    vo.setSecondLevelQualityGoodsCnt(po.getSecondLevelQualityGoodsCnt());
                    vo.setSecondLevelTotalCommission(po.getSecondLevelTotalCommission());
                    return vo;
                })
                .collect(Collectors.toList());
        convertedPage.setCurrent(pageResult.getCurrent());
        convertedPage.setSize(pageResult.getSize());
        convertedPage.setTotal(pageResult.getTotal());
        convertedPage.setPages(pageResult.getPages());
        convertedPage.setRecords(convertedList);

        return convertedPage;
    }


    public static IPage<ProcessSettleDetailExportVo> convertToExportVoPage(IPage<ProcessSettleDetailReportPo> pageResult) {
        IPage<ProcessSettleDetailExportVo> convertedPage = new Page<>();
        List<ProcessSettleDetailExportVo> convertedList = pageResult.getRecords()
                .stream()
                .map(po -> {
                    ProcessSettleDetailExportVo vo = new ProcessSettleDetailExportVo();
                    vo.setProcessSettleOrderItemId(po.getProcessSettleOrderItemId());
                    vo.setCompleteUser(po.getCompleteUser());
                    vo.setCompleteUserName(po.getCompleteUsername());
                    vo.setProcessCode(po.getProcessCode());
                    vo.setProcessLabel(Objects.nonNull(po.getProcessLabel()) ? po.getProcessLabel()
                            .getRemark() : "");
                    vo.setProcessLabelEnum(po.getProcessLabel());
                    vo.setProcessName(po.getProcessName());
                    vo.setProcessNameQualityGoodsCnt(po.getQualityGoodsCnt());
                    vo.setFirstLevelQualityGoodsCnt(po.getFirstLevelQualityGoodsCnt());
                    vo.setFirstLevelTotalCommission(po.getFirstLevelTotalCommission());
                    vo.setSecondLevelQualityGoodsCnt(po.getSecondLevelQualityGoodsCnt());
                    vo.setSecondLevelTotalCommission(po.getSecondLevelTotalCommission());
                    return vo;
                })
                .collect(Collectors.toList());
        convertedPage.setCurrent(pageResult.getCurrent());
        convertedPage.setSize(pageResult.getSize());
        convertedPage.setTotal(pageResult.getTotal());
        convertedPage.setPages(pageResult.getPages());
        convertedPage.setRecords(convertedList);

        return convertedPage;
    }

}
