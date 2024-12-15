package com.hete.supply.scm.server.scm.qc.service.ref;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingStatus;
import com.hete.supply.scm.api.scm.entity.enums.QcState;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.server.scm.common.service.base.IContainer;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.enums.QcBizOperate;
import com.hete.supply.scm.server.scm.qc.service.base.QcContainer;
import com.hete.supply.scm.server.scm.qc.service.base.QcOrderBaseService;
import com.hete.support.api.exception.BizException;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/18 15:20
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QcDefectRefService {
    private final QcOrderDao qcOrderDao;
    private final DefectHandlingDao defectHandlingDao;
    private final QcDetailDao qcDetailDao;
    private final QcOrderBaseService qcOrderBaseService;

    @RedisLock(prefix = ScmRedisConstant.QC_DEFECT_DEAL_UPDATE, key = "#qcOrderNo", waitTime = 1, leaseTime = -1)
    public void afterDefectDeal(String qcOrderNo, List<Long> qcDetailIdList) {
        final QcOrderPo qcOrderPo = qcOrderDao.getByQcOrderNo(qcOrderNo);
        if (null == qcOrderPo) {
            return;
        }
        final List<DefectHandlingPo> defectHandlingPoList = defectHandlingDao.selectListByQcOrderNo(qcOrderNo);
        if (CollectionUtils.isEmpty(defectHandlingPoList)) {
            throw new BizException("查找不到质检单号:{}对应的次品处理记录，数据错误，请联系系统管理员!", qcOrderNo);
        }
        // 如果质检单对应的次品处理记录还有未处理的，直接结束程序
        for (DefectHandlingPo defectHandlingPo : defectHandlingPoList) {
            if (DefectHandlingStatus.WAIT_CONFIRM.equals(defectHandlingPo.getDefectHandlingStatus())) {
                return;
            }
        }

        // 这里是一个兼容逻辑，加工单创建的质检单，在生成次品处理记录之前，质检单已经进入了已完结状态，无需变更质检单状态
        if (StringUtils.isBlank(qcOrderPo.getProcessOrderNo())) {
            final QcState resultQcState = qcOrderPo.getQcState().defectToFinish();
            qcOrderPo.setQcState(resultQcState);
            qcOrderDao.updateByIdVersion(qcOrderPo);
            // 日志
            qcOrderBaseService.createStatusChangeLog(qcOrderPo);
            // 推送结果给wms
            qcOrderBaseService.handleStatusChange(qcOrderPo.getQcOrderNo(), resultQcState, QcBizOperate.START);
        }

        final List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByIdList(qcDetailIdList);
        // 非加工质检释放容器
        boolean isProcessQc = StrUtil.isNotBlank(qcOrderPo.getProcessOrderNo());
        // 驻场质检
        boolean residentQc = qcOrderBaseService.isResidentQc(qcOrderPo.getQcOrderNo());
        qcDetailPoList.forEach(qcDetailPo -> {
            if (!isProcessQc && !residentQc) {
                IContainer container = new QcContainer(qcDetailPo.getContainerCode(),
                        qcOrderPo.getWarehouseCode());
                container.tryReleaseContainer();
            }
        });
    }

    /**
     * 让步上架在wms处理失败，则需要判断当前质检单是否已经进入了完结状态，若进入了需要回滚至次品处理中
     *
     * @param qcOrderNo
     */
    public void updateQcOrderStatusToFail(String qcOrderNo) {
        final QcOrderPo qcOrderPo = qcOrderDao.getByQcOrderNo(qcOrderNo);
        if (null == qcOrderPo) {
            return;
        }
        if (!QcState.FINISHED.equals(qcOrderPo.getQcState())) {
            return;
        }
        qcOrderPo.setQcState(qcOrderPo.getQcState().toFailCompromise());
        qcOrderDao.updateByIdVersion(qcOrderPo);
        // 日志
        qcOrderBaseService.createStatusChangeLog(qcOrderPo);
    }

    /**
     * 若次品处理让步上架在wms处成功，且当前质检单状态已经为已完结，则推送多一次mq给wms
     *
     * @param qcOrderNo
     */
    public void afterSuccessNotify(String qcOrderNo) {
        final QcOrderPo qcOrderPo = qcOrderDao.getByQcOrderNo(qcOrderNo);
        if (null == qcOrderPo) {
            return;
        }
        if (!QcState.FINISHED.equals(qcOrderPo.getQcState())) {
            return;
        }
        qcOrderBaseService.handleStatusChange(qcOrderNo, qcOrderPo.getQcState(), QcBizOperate.START);

    }
}
