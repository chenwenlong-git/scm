package com.hete.supply.scm.server.scm.process.builder;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.api.scm.entity.enums.ProcessProgressStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessScanOperateStatus;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.vo.H5WorkbenchVo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author yanjiawei
 * Created on 2024/12/5.
 */
public class ProcessOrderScanBuilder {


    public static H5WorkbenchVo.H5ScanWorkbenchVo buildH5ScanWorkbenchVo(ProcessOrderScanPo processOrderScanPo,
                                                                         ProcessPo processPo) {
        H5WorkbenchVo.H5ScanWorkbenchVo h5ScanWorkbenchVo = new H5WorkbenchVo.H5ScanWorkbenchVo();
        if (Objects.isNull(processOrderScanPo)) {
            return h5ScanWorkbenchVo;
        }

        h5ScanWorkbenchVo.setProcessOrderScanId(processOrderScanPo.getProcessOrderScanId());
        h5ScanWorkbenchVo.setProcessOrderProcedureId(processOrderScanPo.getProcessOrderProcedureId());
        if (StrUtil.isNotBlank(processOrderScanPo.getReceiptUser()) && StrUtil.isBlank(processOrderScanPo.getCompleteUser())) {
            h5ScanWorkbenchVo.setProcessProgressStatus(ProcessProgressStatus.PROCESSING);
        } else if (StrUtil.isNotBlank(processOrderScanPo.getCompleteUser())) {
            h5ScanWorkbenchVo.setProcessProgressStatus(ProcessProgressStatus.COMPLETED);
        }

        if (StrUtil.isNotBlank(processOrderScanPo.getReceiptUser()) && StrUtil.isBlank(processOrderScanPo.getProcessingUser())) {
            h5ScanWorkbenchVo.setScanOperateStatus(ProcessScanOperateStatus.BEGIN_PROCESSING);
        }
        if (StrUtil.isNotBlank(processOrderScanPo.getProcessingUser()) && StrUtil.isBlank(processOrderScanPo.getCompleteUser())) {
            h5ScanWorkbenchVo.setScanOperateStatus(ProcessScanOperateStatus.COMPLETE_PROCESSING);
        }


        if (Objects.nonNull(processPo)) {
            h5ScanWorkbenchVo.setProcessId(processPo.getProcessId());
            h5ScanWorkbenchVo.setProcessCode(processPo.getProcessCode());
            h5ScanWorkbenchVo.setProcessLabel(processPo.getProcessLabel());
            h5ScanWorkbenchVo.setProcessName(processPo.getProcessName());
            h5ScanWorkbenchVo.setProcessSecondName(processPo.getProcessSecondName());
        }

        BigDecimal processCommission = processOrderScanPo.getProcessCommission();
        h5ScanWorkbenchVo.setProcessCommission(processCommission);

        BigDecimal extraCommission = processOrderScanPo.getExtraCommission();
        h5ScanWorkbenchVo.setExtraCommission(extraCommission);

        BigDecimal expectedTotalCommission = processCommission.add(extraCommission);
        h5ScanWorkbenchVo.setExpectedTotalCommission(expectedTotalCommission);

        h5ScanWorkbenchVo.setPlatform(processOrderScanPo.getPlatform());

        h5ScanWorkbenchVo.setReceiptUser(processOrderScanPo.getReceiptUser());
        h5ScanWorkbenchVo.setReceiptUsername(processOrderScanPo.getReceiptUsername());
        h5ScanWorkbenchVo.setReceiptTime(processOrderScanPo.getReceiptTime());
        h5ScanWorkbenchVo.setReceiptNum(processOrderScanPo.getReceiptNum());

        h5ScanWorkbenchVo.setProcessingUser(processOrderScanPo.getProcessingUser());
        h5ScanWorkbenchVo.setProcessingTime(processOrderScanPo.getProcessingTime());
        h5ScanWorkbenchVo.setProcessingUsername(processOrderScanPo.getProcessingUsername());

        h5ScanWorkbenchVo.setCompleteUser(processOrderScanPo.getCompleteUser());
        h5ScanWorkbenchVo.setCompleteUsername(processOrderScanPo.getCompleteUsername());
        h5ScanWorkbenchVo.setCompleteTime(processOrderScanPo.getCompleteTime());

        h5ScanWorkbenchVo.setQualityGoodsCnt(processOrderScanPo.getQualityGoodsCnt());
        h5ScanWorkbenchVo.setDefectiveGoodsCnt(processOrderScanPo.getDefectiveGoodsCnt());
        return h5ScanWorkbenchVo;
    }

    public static H5WorkbenchVo.H5ProcessOrderWorkbenchVo buildH5ProcessOrderWorkbenchVo(ProcessOrderPo matchProcessOrderPo, ProcessOrderItemPo processOrderItemPo) {
        H5WorkbenchVo.H5ProcessOrderWorkbenchVo h5ProcessOrderWorkbenchVo = new H5WorkbenchVo.H5ProcessOrderWorkbenchVo();
        if (Objects.isNull(matchProcessOrderPo)) {
            return h5ProcessOrderWorkbenchVo;
        }

        h5ProcessOrderWorkbenchVo.setProcessOrderNo(matchProcessOrderPo.getProcessOrderNo());
        h5ProcessOrderWorkbenchVo.setProcessOrderStatus(matchProcessOrderPo.getProcessOrderStatus());
        h5ProcessOrderWorkbenchVo.setSpu(matchProcessOrderPo.getSpu());
        h5ProcessOrderWorkbenchVo.setContainerCode(matchProcessOrderPo.getContainerCode());

        if (Objects.nonNull(processOrderItemPo)) {
            h5ProcessOrderWorkbenchVo.setSku(processOrderItemPo.getSku());
            h5ProcessOrderWorkbenchVo.setProcessNum(processOrderItemPo.getProcessNum());
        }
        return h5ProcessOrderWorkbenchVo;
    }
}
