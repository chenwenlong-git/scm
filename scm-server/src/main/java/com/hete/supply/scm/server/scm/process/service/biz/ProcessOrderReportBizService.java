package com.hete.supply.scm.server.scm.process.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.GradeType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderScanDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderScanRelateDao;
import com.hete.supply.scm.server.scm.process.entity.bo.GradeTypeCountBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcLaborEfficiencyReportBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcProcedureEfficiencyReportBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderScanRelateCountBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import com.hete.supply.scm.server.supplier.dao.EmployeeGradeRelationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/3/13.
 */
@Service
@RequiredArgsConstructor
public class ProcessOrderReportBizService {

    private final ProcessOrderDao processOrderDao;
    private final ProcessOrderScanDao processOrderScanDao;
    private final EmployeeGradeRelationDao employeeGradeRelationDao;
    private final ProcessOrderScanRelateDao processOrderScanRelateDao;

    public void doCalProcProcedureEfficiencyReport(LocalDateTime startUtc,
                                                   LocalDateTime endUtc,
                                                   ProcProcedureEfficiencyReportBo reportResult) {
        calProcProcedureManpower(reportResult);
        calProcProcedureCnt(startUtc, endUtc, reportResult);
    }

    public void calProcProcedureManpower(ProcProcedureEfficiencyReportBo reportResult) {
        // 更新工序人力数
        List<GradeTypeCountBo> gradeTypeCountBos = employeeGradeRelationDao.countDistinctEmployeesByGradeType(
                Arrays.asList(GradeType.STYLIST, GradeType.COLORIST, GradeType.HEADGEAR));
        for (GradeTypeCountBo gradeTypeCountBo : gradeTypeCountBos) {
            GradeType gradeType = gradeTypeCountBo.getGradeType();
            int employeeCount = gradeTypeCountBo.getEmployeeCount();

            switch (gradeType) {
                case STYLIST:
                    reportResult.setStylingTeamStylingManpower(employeeCount);
                    break;
                case COLORIST:
                    reportResult.setColoringTeamStylingManpower(employeeCount);
                    reportResult.setColoringManpower(employeeCount);
                    break;
                case HEADGEAR:
                    reportResult.setSewingHeadbandsManpower(employeeCount);
                    break;
                default:
                    // Do nothing
                    break;
            }
        }
    }

    public void calProcProcedureCnt(LocalDateTime startUtc,
                                    LocalDateTime endUtc,
                                    ProcProcedureEfficiencyReportBo reportResult) {
        List<ProcessLabel> calProcessLabels = Arrays.asList(ProcessLabel.BLOCK_LACE, ProcessLabel.MODELING,
                ProcessLabel.DYEING, ProcessLabel.CLIPS,
                ProcessLabel.SEWING_HEAD);
        List<ProcessOrderScanPo> processOrderScanPos = processOrderScanDao.listByLabelsAndTime(startUtc, endUtc,
                calProcessLabels);
        if (CollectionUtils.isEmpty(processOrderScanPos)) {
            return;
        }
        Map<ProcessLabel, Set<Long>> groupedByProcessLabel = processOrderScanPos.stream()
                .collect(Collectors.groupingBy(ProcessOrderScanPo::getProcessLabel,
                        Collectors.mapping(ProcessOrderScanPo::getProcessOrderScanId,
                                Collectors.toSet())));
        groupedByProcessLabel.forEach((processLabel, processOrderScanIds) -> {
            int totalProcessLabelCount = 0;
            List<ProcessOrderScanRelateCountBo> processOrderScanRelateCountBos
                    = processOrderScanRelateDao.countProcessOrderScanByIds(processOrderScanIds);
            for (Long processOrderScanId : processOrderScanIds) {
                Integer qualityGoodsCnt = processOrderScanPos.stream()
                        .filter(processOrderScanPo -> Objects.equals(processOrderScanId,
                                processOrderScanPo.getProcessOrderScanId()))
                        .map(ProcessOrderScanPo::getQualityGoodsCnt)
                        .findFirst()
                        .orElse(0);
                Integer relateCount = processOrderScanRelateCountBos.stream()
                        .filter(processOrderScanRelateCountBo -> Objects.equals(processOrderScanId,
                                processOrderScanRelateCountBo.getProcessOrderScanId()))
                        .map(ProcessOrderScanRelateCountBo::getCount)
                        .findFirst()
                        .orElse(1);
                totalProcessLabelCount += (qualityGoodsCnt * relateCount);
            }

            switch (processLabel) {
                // 修剪发块蕾丝
                case BLOCK_LACE:
                    // 更新修剪蕾丝数量
                    reportResult.setTrimmingLaceCount(totalProcessLabelCount);
                    break;
                // 造型
                case MODELING:
                    // 更新造型组造型数量和染色组造型数量
                    reportResult.setStylingTeamStylingCount(totalProcessLabelCount);
                    reportResult.setColoringTeamStylingCount(totalProcessLabelCount);
                    break;
                // 染色
                case DYEING:
                    // 更新染色数量
                    reportResult.setColoringCount(totalProcessLabelCount);
                    break;
                // 卡子/松紧带
                case CLIPS:
                    // 更新卡子+松紧带数量
                    reportResult.setClipsElasticBandsCount(totalProcessLabelCount);
                    break;
                //  缝头套
                case SEWING_HEAD:
                    // 更新缝头套数量
                    reportResult.setSewingHeadbandsCount(totalProcessLabelCount);
                    break;
                default:
                    // 如果不是以上任何一种标签，则不进行任何更新
                    break;
            }
        });

    }


    public void doCalProcLaborEfficiencyReport(LocalDateTime startUtc,
                                               LocalDateTime endUtc,
                                               ProcLaborEfficiencyReportBo reportResultBo) {
        if (Objects.isNull(reportResultBo) || Objects.isNull(startUtc) || Objects.isNull(endUtc)) {
            return;
        }
        int shippedQuantity = processOrderDao.countStoredNumBetweenTimes(startUtc, endUtc);
        reportResultBo.setShippedQuantity(shippedQuantity);

        int laborCnt = processOrderScanDao.countDistinctCompleteUser(startUtc, endUtc);
        reportResultBo.setLaborCnt(laborCnt);
    }
}
