package com.hete.supply.scm.server.scm.qc.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.vo.QcOrderDefectConfigExportVo;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.server.scm.qc.converter.QcOrderConverter;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDefectConfigDao;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcDefectConfigCreateDto;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcDefectConfigStatusDto;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcDefectConfigUpdateDto;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderDefectConfigPo;
import com.hete.supply.scm.server.scm.qc.entity.vo.QcDefectConfigVo;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/10/13 10:21
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class QcOrderConfigBizService {
    private final QcOrderDefectConfigDao qcOrderDefectConfigDao;
    private final ConsistencySendMqService consistencySendMqService;

    public List<QcDefectConfigVo> qcDefectConfigList() {
        final List<QcOrderDefectConfigPo> qcOrderDefectConfigPoList = qcOrderDefectConfigDao.getAll();
        if (CollectionUtils.isEmpty(qcOrderDefectConfigPoList)) {
            return Collections.emptyList();
        }

        final List<QcDefectConfigVo> qcDefectConfigVoList = QcOrderConverter.qcOrderDefectConfigPoToVo(qcOrderDefectConfigPoList);

        final List<QcDefectConfigVo> parentDefectConfigVoList = qcDefectConfigVoList.stream()
                .filter(qcDefectConfigVo -> qcDefectConfigVo.getParentDefectConfigId() == 0)
                .collect(Collectors.toList());
        final Map<Long, List<QcDefectConfigVo>> parentIdDefectConfigVoMap = qcDefectConfigVoList.stream()
                .filter(qcDefectConfigVo -> qcDefectConfigVo.getParentDefectConfigId() != 0)
                .collect(Collectors.groupingBy(QcDefectConfigVo::getParentDefectConfigId));

        // 组合
        parentDefectConfigVoList.forEach(vo -> vo.setChildDefectConfigList(parentIdDefectConfigVoMap.get(vo.getQcOrderDefectConfigId())));
        return parentDefectConfigVoList;
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.QC_CONFIG_DEFECT_UPDATE, key = "", waitTime = 1, leaseTime = -1)
    public void createDefectConfig(QcDefectConfigCreateDto dto) {
        // 校验代码唯一性
        final QcOrderDefectConfigPo checkQcOrderDefectConfigPo = qcOrderDefectConfigDao.getOneByCode(dto.getDefectCode());
        if (null != checkQcOrderDefectConfigPo) {
            throw new ParamIllegalException("次品代码：{}重复，请重新填写次品代码", dto.getDefectCode());
        }

        // 判断是否符合两级的结构
        if (null != dto.getParentDefectConfigId()) {
            final QcOrderDefectConfigPo qcOrderDefectConfigPo = qcOrderDefectConfigDao.getById(dto.getParentDefectConfigId());
            if (0 != qcOrderDefectConfigPo.getParentDefectConfigId()) {
                throw new BizException("次品原因配置:{}的层级关系错误，请联系系统管理员！", qcOrderDefectConfigPo.getDefectCategory());
            }
        }


        final QcOrderDefectConfigPo qcOrderDefectConfigPo = QcOrderConverter.qcDefectConfigDtoToPo(dto);
        qcOrderDefectConfigDao.insert(qcOrderDefectConfigPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDefectConfigStatus(QcDefectConfigStatusDto dto) {
        final QcOrderDefectConfigPo qcOrderDefectConfigPo = qcOrderDefectConfigDao.getByIdVersion(dto.getQcOrderDefectConfigId(), dto.getVersion());
        if (null == qcOrderDefectConfigPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        qcOrderDefectConfigPo.setDefectStatus(dto.getDefectStatus());

        qcOrderDefectConfigDao.updateByIdVersion(qcOrderDefectConfigPo);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.QC_CONFIG_DEFECT_UPDATE, key = "", waitTime = 1, leaseTime = -1)
    public void updateDefectConfig(QcDefectConfigUpdateDto dto) {
        final QcOrderDefectConfigPo qcOrderDefectConfigPo = qcOrderDefectConfigDao.getByIdVersion(dto.getQcOrderDefectConfigId(), dto.getVersion());
        if (null == qcOrderDefectConfigPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        // 校验代码唯一性
        final QcOrderDefectConfigPo checkQcOrderDefectConfigPo = qcOrderDefectConfigDao.getOneByCode(dto.getDefectCode());
        if (null != checkQcOrderDefectConfigPo
                && !checkQcOrderDefectConfigPo.getQcOrderDefectConfigId().equals(qcOrderDefectConfigPo.getQcOrderDefectConfigId())) {
            throw new ParamIllegalException("次品代码：{}重复，请重新填写次品代码", dto.getDefectCode());
        }
        qcOrderDefectConfigPo.setDefectCode(dto.getDefectCode());
        qcOrderDefectConfigPo.setDefectCategory(dto.getDefectCategory());

        qcOrderDefectConfigDao.updateByIdVersion(qcOrderDefectConfigPo);
    }

    /**
     * 次品原因导出
     *
     * @return void
     * @author ChenWenLong
     * @date 2024/1/2 15:16
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportQcDefectConfig() {
        ComPageDto dto = new ComPageDto();
        Integer exportTotals = this.getQcDefectConfigExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_QC_ORDER_CONFIG_EXPORT.getCode(), dto));
    }

    /**
     * 次品原因导出总数
     *
     * @return Integer
     * @author ChenWenLong
     * @date 2024/1/2 15:41
     */
    public Integer getQcDefectConfigExportTotals(ComPageDto dto) {
        // 获取列表信息
        return qcOrderDefectConfigDao.getExportTotals(dto);
    }

    /**
     * 次品原因导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < QcOrderDefectConfigExportVo>>
     * @author ChenWenLong
     * @date 2024/1/2 16:11
     */
    public CommonResult<ExportationListResultBo<QcOrderDefectConfigExportVo>> getQcDefectConfigExportList(ComPageDto dto) {
        ExportationListResultBo<QcOrderDefectConfigExportVo> resultBo = new ExportationListResultBo<>();

        // 获取列表信息
        CommonPageResult.PageInfo<QcOrderDefectConfigExportVo> pageResult = qcOrderDefectConfigDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<QcOrderDefectConfigExportVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        for (QcOrderDefectConfigExportVo record : records) {
            record.setDefectStatusName(record.getDefectStatus() != null ? record.getDefectStatus().getRemark() : "");
        }

        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

}