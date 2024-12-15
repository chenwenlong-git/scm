package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.CostCoefficientUpdateRequestDto;
import com.hete.supply.scm.api.scm.entity.dto.CostCoefficientsRequestDto;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.server.scm.process.builder.CostCoefficientsBuilder;
import com.hete.supply.scm.server.scm.process.dao.CostCoefficientsDao;
import com.hete.supply.scm.server.scm.process.entity.po.CostCoefficientsPo;
import com.hete.supply.scm.server.scm.process.entity.vo.CostCoefficientsVo;
import com.hete.supply.scm.server.scm.process.entity.vo.LatestCostCoefficientsVo;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/2/20.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CostCoefficientsBizService {

    private final CostCoefficientsDao costCoefficientsDao;
    private static final int ALLOWED_MONTH_INTERVAL = 1;
    private static final String UPDATE_COEFFICIENT_GLOBAL_KEY = "updateCoefficientGlobalKey";

    public CommonPageResult.PageInfo<CostCoefficientsVo> getByPage(CostCoefficientsRequestDto dto) {
        IPage<CostCoefficientsVo> pageResult = costCoefficientsDao.getByPage(dto, PageDTO.of(dto.getPageNo(),
                dto.getPageSize()));
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 更新成本系数。
     * 如果用户未填写系数或填写的系数不合法，抛出参数异常。
     * 如果成功更新了当前月和下个月的固定系数，则更新数据库中的系数。
     *
     * @param dto 成本系数更新请求的数据传输对象
     * @throws ParamIllegalException 当用户未填写系数或填写的系数不合法时抛出
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.COST_COEFFICIENT_UPDATE, key = "#dto.key", waitTime = 1, leaseTime = -1)
    public void updateCoefficient(CostCoefficientUpdateRequestDto dto) {
        List<CostCoefficientUpdateRequestDto.CostCoefficientUpdateInfoDto> updateCostCoefficientDtoList
                = dto.getCostCoefficientUpdateInfoDtoList();

        // 获取当前时间和年月
        LocalDateTime curUtcTime = LocalDateTime.now();
        LocalDateTime curCnTime = TimeUtil.convertZone(curUtcTime, TimeZoneId.UTC, TimeZoneId.CN);
        YearMonth curYearMonth = YearMonth.from(curCnTime);

        // 检查当前月份系数不为空且大于0
        boolean invalidCoefficientExistsForCurMon = updateCostCoefficientDtoList.stream()
                .anyMatch(updateCostCoefficient -> Objects.equals(curYearMonth, YearMonth.from(
                        TimeUtil.convertZone(updateCostCoefficient.getEffectiveTime(), TimeZoneId.UTC,
                                TimeZoneId.CN))) && (Objects.isNull(
                        updateCostCoefficient.getCoefficient())));
        if (invalidCoefficientExistsForCurMon) {
            throw new ParamIllegalException("请填写当前月份有效的系数值");
        }

        // 计算下个月的时间
        LocalDateTime nextMonthUtcTime = curUtcTime.plusMonths(ALLOWED_MONTH_INTERVAL);
        LocalDateTime nextMonthCnTime = TimeUtil.convertZone(nextMonthUtcTime, TimeZoneId.UTC, TimeZoneId.CN);
        YearMonth nextYearMonth = YearMonth.from(nextMonthCnTime);
        List<YearMonth> canUpdateEffectiveTimes = Arrays.asList(curYearMonth, nextYearMonth);

        // 过滤不更新固定成本系数的数据
        updateCostCoefficientDtoList.removeIf(updateCostCoefficient -> !canUpdateEffectiveTimes.contains(YearMonth.from(
                TimeUtil.convertZone(updateCostCoefficient.getEffectiveTime(), TimeZoneId.UTC,
                        TimeZoneId.CN))) || Objects.isNull(updateCostCoefficient.getCoefficient()));
        if (CollectionUtils.isEmpty(updateCostCoefficientDtoList)) {
            return;
        }

        // 获取当前月和下个月的固定系数
        List<LocalDateTime> updateEffectiveTimes = updateCostCoefficientDtoList.stream()
                .map(CostCoefficientUpdateRequestDto.CostCoefficientUpdateInfoDto::getEffectiveTime)
                .collect(Collectors.toList());
        List<CostCoefficientsPo> costCoefficientsPos = costCoefficientsDao.listByEffectiveTimes(updateEffectiveTimes);

        // 更新当前月和下个月的固定系数
        for (CostCoefficientUpdateRequestDto.CostCoefficientUpdateInfoDto updateCostCoefficientDto :
                updateCostCoefficientDtoList) {
            LocalDateTime needUpdateEffectiveTime = TimeUtil.convertZone(updateCostCoefficientDto.getEffectiveTime(),
                    TimeZoneId.UTC, TimeZoneId.CN);

            CostCoefficientsPo existCostCoefficientsPo = costCoefficientsPos.stream()
                    .filter(costCoefficientsPo -> Objects.equals(needUpdateEffectiveTime, TimeUtil.convertZone(
                            costCoefficientsPo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN)))
                    .findFirst()
                    .orElse(null);
            if (Objects.isNull(existCostCoefficientsPo)) {
                continue;
            }

            BigDecimal coefficient = updateCostCoefficientDto.getCoefficient();
            existCostCoefficientsPo.setCoefficient(coefficient);
            costCoefficientsDao.updateByIdVersion(existCostCoefficientsPo);
        }
    }

    /**
     * 更新固定成本系数。
     * 如果数据库中已存在当前月份的固定成本系数，则不执行更新。
     * 如果数据库中没有任何固定成本系数数据，则不执行更新。
     * 更新后的固定成本系数数据将被插入到数据库中。
     * 如果成功更新了固定成本系数，则记录日志。
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.COST_COEFFICIENT_UPDATE, key = "#key", waitTime = 1, leaseTime = -1)
    public void updateCoefficient(String key, String curCnTimeStrWithTestStr) {
        LocalDateTime curUtcTimeStrWithTest = null;
        if (StrUtil.isNotBlank(curCnTimeStrWithTestStr)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime curCnTimeStrWithTest = LocalDateTime.parse(curCnTimeStrWithTestStr, formatter);
            curUtcTimeStrWithTest
                    = TimeUtil.convertZone(curCnTimeStrWithTest, TimeZoneId.CN, TimeZoneId.UTC);
        }

        // 获取当前数据库中所有的固定成本系数数据
        List<CostCoefficientsPo> allCoefficientsPos = costCoefficientsDao.getAll();
        // 如果数据库中没有任何固定成本系数数据，则不执行更新
        if (CollectionUtils.isEmpty(allCoefficientsPos)) {
            log.error("初始化固定成本系数结束！不存在历史月份系数，请先通过手动初始化。");
            return;
        }

        // 获取当前时间和年月
        LocalDateTime curUtcTime = Objects.nonNull(curUtcTimeStrWithTest) ? curUtcTimeStrWithTest : LocalDateTime.now();
        LocalDateTime curCnTime = TimeUtil.convertZone(curUtcTime, TimeZoneId.UTC, TimeZoneId.CN);
        YearMonth curYearMonth = YearMonth.from(curCnTime);

        // 检查数据库中是否已存在当前月份的固定成本系数
        CostCoefficientsPo curMonthCoefficientPo = allCoefficientsPos.stream()
                .filter(existCostCoefficientsPo -> {
                    LocalDateTime effectiveDateTime
                            = TimeUtil.convertZone(existCostCoefficientsPo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN);
                    YearMonth effectiveYearMonth = YearMonth.from(effectiveDateTime);
                    return Objects.equals(curYearMonth, effectiveYearMonth);
                })
                .findFirst()
                .orElse(null);

        // 获取最新的有效成本系数
        BigDecimal latestCoefficient = getLastCoefficient(allCoefficientsPos);

        // 如果数据库中不存在当前月份的固定成本系数，则插入
        if (Objects.isNull(curMonthCoefficientPo)) {
            // 构建新的固定成本系数数据对象
            CostCoefficientsPo costCoefficientsPo
                    = CostCoefficientsBuilder.buildCostCoefficientsPo(curUtcTime, latestCoefficient);
            // 将新的固定成本系数数据插入到数据库中
            costCoefficientsDao.insert(costCoefficientsPo);
        } else {
            // 如果存在，如果系数==0
            BigDecimal curMonthCoefficient = curMonthCoefficientPo.getCoefficient();
            if (curMonthCoefficient.compareTo(BigDecimal.ZERO) == 0) {
                curMonthCoefficientPo.setCoefficient(latestCoefficient);
                costCoefficientsDao.updateByIdVersion(curMonthCoefficientPo);
            }
        }

        // 计算下个月的时间
        LocalDateTime nextMonthCnTime = curCnTime.plusMonths(ALLOWED_MONTH_INTERVAL);
        LocalDateTime nextMonthUtcTime = TimeUtil.convertZone(nextMonthCnTime, TimeZoneId.CN, TimeZoneId.UTC);
        YearMonth nextYearMonth = YearMonth.from(nextMonthCnTime);

        // 检查数据库中是否已存在下个月份的固定成本系数
        boolean nextMonthExist = allCoefficientsPos.stream()
                .anyMatch(existCostCoefficientsPo -> {
                    LocalDateTime effectiveDateTime =
                            TimeUtil.convertZone(existCostCoefficientsPo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN);
                    YearMonth effectiveYearMonth = YearMonth.from(effectiveDateTime);
                    return Objects.equals(nextYearMonth, effectiveYearMonth);
                });

        // 如果数据库中不存在下前月份的固定成本系数，则插入
        if (!nextMonthExist) {
            // 获取最新的有效成本系数
            BigDecimal costCoefficients = BigDecimal.ZERO;
            // 构建新的固定成本系数数据对象
            CostCoefficientsPo costCoefficientsPo
                    = CostCoefficientsBuilder.buildCostCoefficientsPo(nextMonthUtcTime, costCoefficients);
            // 将新的固定成本系数数据插入到数据库中
            costCoefficientsDao.insert(costCoefficientsPo);
        }

    }

    /**
     * 获取最新的有效成本系数。
     *
     * @return 最新的有效成本系数，如果不存在有效成本系数则返回 null
     */
    public BigDecimal getLatestCoefficient() {
        // 获取当前数据库中所有的固定成本系数数据
        List<CostCoefficientsPo> existCostCoefficientsPos = costCoefficientsDao.getAll();
        if (CollectionUtils.isEmpty(existCostCoefficientsPos)) {
            return null;
        }

        return getLatestCoefficient(existCostCoefficientsPos);
    }

    /**
     * 获取最新的有效成本系数。
     *
     * @param existCostCoefficientsPos 包含成本系数信息的列表
     * @return 最新的有效成本系数，如果不存在有效成本系数则返回 null
     */
    private BigDecimal getLatestCoefficient(List<CostCoefficientsPo> existCostCoefficientsPos) {
        // 获取当前时间的年份和月份
        LocalDateTime curUtcTime = LocalDateTime.now();
        LocalDateTime curCnTime = TimeUtil.convertZone(curUtcTime, TimeZoneId.UTC, TimeZoneId.CN);
        YearMonth currentYearMonth = YearMonth.from(curCnTime);

        // 筛选出生效时间小于等于当前月份的系数
        return existCostCoefficientsPos.stream()
                .filter(costCoefficient -> Objects.nonNull(costCoefficient.getEffectiveTime()) && YearMonth.from(
                                TimeUtil.convertZone(costCoefficient.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN))
                        .isBefore(currentYearMonth) || YearMonth.from(
                                TimeUtil.convertZone(costCoefficient.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN))
                        .equals(currentYearMonth))
                .max(Comparator.comparing(CostCoefficientsPo::getEffectiveTime))
                .map(CostCoefficientsPo::getCoefficient)
                .orElse(null);
    }

    /**
     * 获取上个月的有效成本系数。
     *
     * @param existCostCoefficientsPos 包含成本系数信息的列表
     * @return 最新的有效成本系数，如果不存在有效成本系数则返回 null
     */
    private BigDecimal getLastCoefficient(List<CostCoefficientsPo> existCostCoefficientsPos) {
        // 获取当前时间的年份和月份
        LocalDateTime curUtcTime = LocalDateTime.now();
        LocalDateTime curCnTime = TimeUtil.convertZone(curUtcTime, TimeZoneId.UTC, TimeZoneId.CN);
        YearMonth currentYearMonth = YearMonth.from(curCnTime);

        // 筛选出生效时间小于等于当前月份的系数
        return existCostCoefficientsPos.stream()
                .filter(costCoefficient -> Objects.nonNull(costCoefficient.getEffectiveTime()) && YearMonth.from(
                                TimeUtil.convertZone(costCoefficient.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN))
                        .isBefore(currentYearMonth))
                .max(Comparator.comparing(CostCoefficientsPo::getEffectiveTime))
                .map(CostCoefficientsPo::getCoefficient)
                .orElse(null);
    }

    public String getGlobUpdateCoefficientKey() {
        return UPDATE_COEFFICIENT_GLOBAL_KEY;
    }

    public LatestCostCoefficientsVo LatestCostCoefficientsVo() {
        BigDecimal latestCoefficient = this.getLatestCoefficient();

        LatestCostCoefficientsVo latestCostCoefficientsVo = new LatestCostCoefficientsVo();
        latestCostCoefficientsVo.setCoefficient(latestCoefficient);
        return latestCostCoefficientsVo;
    }
}
