package com.hete.supply.scm.server.scm.supplier.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SupplierSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.ReconciliationCycle;
import com.hete.supply.scm.api.scm.entity.vo.SupplierExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierSimpleVo;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierRecentReconciliationBo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierTimelinessDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierUrgentDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierTimelinessVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierUrgentVo;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierUrgentStatus;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应商基础类
 *
 * @author ChenWenLong
 * @date 2022/11/15 10:08
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplierBaseService {

    private final SupplierDao supplierDao;
    private final PlmSkuDao plmSkuDao;

    /**
     * 模糊查询供应商代码获取列表
     *
     * @author ChenWenLong
     * @date 2022/11/21 18:10
     */
    public List<SupplierPo> getByLikeSupplierCode(String supplierCode) {
        return supplierDao.getByLikeSupplierCode(supplierCode);
    }

    public SupplierPo getBySupplierName(String supplierName) {
        return supplierDao.getOneBySupplierName(supplierName);
    }

    /**
     * 通过供应商代码获取信息
     *
     * @author ChenWenLong
     * @date 2023/1/13 14:53
     */
    public SupplierPo getSupplierByCode(String supplierCode) {
        return supplierDao.getBySupplierCode(supplierCode);
    }

    /**
     * 通过供应商代码批量查询
     *
     * @author ChenWenLong
     * @date 2022/11/29 10:15
     */
    public List<SupplierPo> getBySupplierCodeList(List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return supplierDao.getBySupplierCodeList(supplierCodeList);
    }

    /**
     * 设置供应商等级
     *
     * @param list
     */
    public <T extends SupplierSimpleVo> void batchSetSupplierGrade(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        final List<String> supplierCodeList = list.stream()
                .map(SupplierSimpleVo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());

        final Map<String, SupplierPo> supplierPoMap = supplierDao.getSupplierMapBySupplierCodeList(supplierCodeList);
        list.forEach(vo -> {
            final SupplierPo supplierPo = supplierPoMap.get(vo.getSupplierCode());
            if (null != supplierPo) {
                vo.setSupplierGrade(supplierPo.getSupplierGrade());
            }
        });
    }

    public void checkSupplierExists(@NotEmpty List<String> supplierCodeList) {
        final List<SupplierPo> supplierPoList = supplierDao.getBySupplierCodeList(supplierCodeList);
        if (supplierPoList.size() != supplierCodeList.size()) {
            throw new ParamIllegalException("供应商不存在，请重新选择供应商");
        }
    }

    /**
     * 统计导出的总数
     *
     * @author ChenWenLong
     * @date 2022/12/16 17:59
     */
    public Integer getExportTotals(SupplierSearchDto dto) {
        return supplierDao.getExportTotals(dto);
    }

    /**
     * 查询导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<SupplierExportVo> getExportList(SupplierSearchDto dto) {
        return supplierDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
    }

    /**
     * 通过供应商和sku获取紧急状态
     *
     * @param dtoList:
     * @return List<SupplierUrgentVo>
     * @author ChenWenLong
     * @date 2023/7/18 15:27
     */
    public List<SupplierUrgentVo> getUrgentVoListBySupplierCode(@Valid @NotEmpty List<SupplierUrgentDto> dtoList) {
        log.info("通过供应商和sku获取紧急状态。dto={}", JacksonUtil.parse2Str(dtoList));
        List<String> supplierCodeList = dtoList.stream().map(SupplierUrgentDto::getSupplierCode).distinct().collect(Collectors.toList());
        List<String> skuList = dtoList.stream().map(SupplierUrgentDto::getSku).distinct().collect(Collectors.toList());
        //查询供应商信息
        List<SupplierPo> supplierPoList = supplierDao.getBySupplierCodeList(supplierCodeList);
        Map<String, Integer> supplierAgingMap = supplierPoList.stream().collect(Collectors.toMap(SupplierPo::getSupplierCode, SupplierPo::getLogisticsAging));

        //查询sku信息
        List<PlmSkuPo> plmSkuPoList = plmSkuDao.getList(skuList);
        Map<String, BigDecimal> skuCycleMap = plmSkuPoList.stream().collect(Collectors.toMap(PlmSkuPo::getSku, PlmSkuPo::getCycle));

        //返回数据
        List<SupplierUrgentVo> list = new ArrayList<>();

        for (SupplierUrgentDto supplierUrgentDto : dtoList) {
            SupplierUrgentVo supplierUrgentVo = new SupplierUrgentVo();

            supplierUrgentVo.setSupplierCode(supplierUrgentDto.getSupplierCode());
            supplierUrgentVo.setSku(supplierUrgentDto.getSku());


            if (!supplierAgingMap.containsKey(supplierUrgentDto.getSupplierCode()) &&
                    !skuCycleMap.containsKey(supplierUrgentDto.getSku())) {
                //同时不存在
                supplierUrgentVo.setSupplierUrgentStatus(SupplierUrgentStatus.LOGISTICS_AGING_CYCLE);
            } else if (null == supplierAgingMap.get(supplierUrgentDto.getSupplierCode()) &&
                    null == skuCycleMap.get(supplierUrgentDto.getSku())) {
                supplierUrgentVo.setSupplierUrgentStatus(SupplierUrgentStatus.LOGISTICS_AGING_CYCLE);
            } else if (!supplierAgingMap.containsKey(supplierUrgentDto.getSupplierCode()) ||
                    null == supplierAgingMap.get(supplierUrgentDto.getSupplierCode()) ||
                    0 == supplierAgingMap.get(supplierUrgentDto.getSupplierCode())) {
                //供应商不存在信息
                supplierUrgentVo.setSupplierUrgentStatus(SupplierUrgentStatus.LOGISTICS_AGING);
            } else if (!skuCycleMap.containsKey(supplierUrgentDto.getSku()) ||
                    null == skuCycleMap.get(supplierUrgentDto.getSku()) ||
                    BigDecimal.ZERO.compareTo(skuCycleMap.get(supplierUrgentDto.getSku())) == 0) {
                //sku不存在信息
                supplierUrgentVo.setSupplierUrgentStatus(SupplierUrgentStatus.CYCLE);
            }

            //如果已经有结果直接跳出
            if (null != supplierUrgentVo.getSupplierUrgentStatus()) {
                list.add(supplierUrgentVo);
                continue;
            }

            //计算是否加急
            //生产周期
            BigDecimal cycle = skuCycleMap.get(supplierUrgentDto.getSku());
            //物流时效
            BigDecimal logisticsAging = new BigDecimal(supplierAgingMap.get(supplierUrgentDto.getSupplierCode()));
            BigDecimal timelinessValue = cycle.add(logisticsAging).add(ScmConstant.SUPPLIER_TIMELINESS_VALUE);
            if (supplierUrgentDto.getTimeValue().compareTo(timelinessValue) < 0) {
                supplierUrgentVo.setSupplierUrgentStatus(SupplierUrgentStatus.URGENT);
            } else {
                supplierUrgentVo.setSupplierUrgentStatus(SupplierUrgentStatus.NOT_URGENT);
            }

            list.add(supplierUrgentVo);
        }
        log.info("通过供应商和sku获取紧急状态。vo={}", JacksonUtil.parse2Str(list));
        return list;

    }

    /**
     * 通过供应商和sku获取时效值
     *
     * @param dtoList:
     * @return List<SupplierTimelinessVo>
     * @author ChenWenLong
     * @date 2023/7/18 16:47
     */
    public List<SupplierTimelinessVo> getTimelinessVoListBySupplierCode(@Valid @NotEmpty List<SupplierTimelinessDto> dtoList) {
        log.info("通过供应商和sku获取时效值。dto={}", JacksonUtil.parse2Str(dtoList));
        List<String> supplierCodeList = dtoList.stream().map(SupplierTimelinessDto::getSupplierCode).distinct().collect(Collectors.toList());
        //查询供应商信息
        List<SupplierPo> supplierPoList = supplierDao.getBySupplierCodeList(supplierCodeList);
        Map<String, Integer> supplierAgingMap = supplierPoList.stream().collect(Collectors.toMap(SupplierPo::getSupplierCode, SupplierPo::getLogisticsAging));

        //返回数据
        List<SupplierTimelinessVo> list = new ArrayList<>();

        for (SupplierTimelinessDto supplierTimelinessDto : dtoList) {
            SupplierTimelinessVo supplierTimelinessVo = new SupplierTimelinessVo();
            supplierTimelinessVo.setSupplierCode(supplierTimelinessDto.getSupplierCode());
            supplierTimelinessVo.setSku(supplierTimelinessDto.getSku());

            //计算时效性
            if (supplierAgingMap.containsKey(supplierTimelinessDto.getSupplierCode()) &&
                    null != supplierAgingMap.get(supplierTimelinessDto.getSupplierCode()) &&
                    0 != supplierAgingMap.get(supplierTimelinessDto.getSupplierCode())) {
                //物流时效
                BigDecimal logisticsAging = new BigDecimal(supplierAgingMap.get(supplierTimelinessDto.getSupplierCode()));
                BigDecimal timelinessValue = logisticsAging.add(ScmConstant.SUPPLIER_TIMELINESS_VALUE);
                supplierTimelinessVo.setTimelinessValue(timelinessValue);

            } else {
                supplierTimelinessVo.setTimelinessValue(BigDecimal.ZERO);
            }

            list.add(supplierTimelinessVo);
        }
        log.info("通过供应商和sku获取时效值。vo={}", JacksonUtil.parse2Str(list));
        return list;

    }

    /**
     * 获取供应商近期对账周期
     *
     * @param supplierCode:
     * @return SupplierRecentReconciliationBo
     * @author ChenWenLong
     * @date 2024/5/22 10:51
     */
    public SupplierRecentReconciliationBo getSupplierRecentReconciliation(String supplierCode) {

        if (StringUtils.isBlank(supplierCode)) {
            return null;
        }
        SupplierPo supplierPo = supplierDao.getBySupplierCode(supplierCode);
        if (null == supplierPo) {
            throw new BizException("供应商数据已被修改或删除，请刷新页面后重试！");
        }
        if (null == supplierPo.getReconciliationCycle()) {
            return null;
        }

        // 获取当前CN的时间
        final LocalDateTime localDateTimeNow = TimeUtil.now(TimeZoneId.CN);

        LocalDateTime reconciliationLastStartTime = ReconciliationCycle.getReconciliationLastStartTime(supplierPo.getReconciliationCycle(), localDateTimeNow);
        LocalDateTime reconciliationLastEndTime = ReconciliationCycle.getReconciliationLastEndTime(supplierPo.getReconciliationCycle(), localDateTimeNow);
        SupplierRecentReconciliationBo supplierRecentReconciliationBo = new SupplierRecentReconciliationBo();
        supplierRecentReconciliationBo.setReconciliationStartTime(reconciliationLastStartTime);
        supplierRecentReconciliationBo.setReconciliationEndTime(reconciliationLastEndTime);
        return supplierRecentReconciliationBo;

    }


    public List<String> getSupplierCodeListByFollower(String followUser) {
        if (StringUtils.isBlank(followUser)) {
            return Collections.emptyList();
        }
        final List<SupplierPo> supplierPoList = supplierDao.getSupplierPoListByFollowUser(followUser);
        if (CollectionUtils.isEmpty(supplierPoList)) {
            return Collections.emptyList();
        }
        return supplierPoList.stream()
                .map(SupplierPo::getSupplierCode)
                .collect(Collectors.toList());
    }

    /**
     * 通过供应商代码获得供应商别称
     *
     * @param supplierCode:
     * @return String
     * @author ChenWenLong
     * @date 2024/6/1 13:58
     */
    public String getSupplierAliasByCode(String supplierCode) {
        if (StringUtils.isBlank(supplierCode)) {
            return supplierCode;
        }
        if (supplierCode.length() == 1 && Character.isDigit(supplierCode.charAt(0))) {
            return "0" + supplierCode;
        }
        return supplierCode;
    }
}
