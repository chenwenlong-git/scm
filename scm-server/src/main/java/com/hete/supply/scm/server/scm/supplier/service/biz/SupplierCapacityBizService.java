package com.hete.supply.scm.server.scm.supplier.service.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hete.supply.scm.api.scm.entity.dto.ExportSupplierRestDto;
import com.hete.supply.scm.api.scm.entity.dto.SupCapacityPageDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierCapacityExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierCapacityRuleExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierRestExportVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierCapacityImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierCapacityRuleImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierRestImportationDto;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.supplier.builder.SupplierCapacityBuilder;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierCapacityDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierCapacityLogDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierCapacityRuleDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierRestDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupCapDateRangeQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupOpCapacityBatchBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupOpCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityLogPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityRulePo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierRestPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierCapacityPageVo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierCapacityBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.redis.lock.annotation.RedisLock;
import com.hete.trace.util.TraceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/8/5.
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplierCapacityBizService {
    private final SupplierBaseService supplierBaseService;
    private final SupplierCapacityRuleDao supplierCapacityRuleDao;
    private final SupplierCapacityBaseService supplierCapacityBaseService;
    private final SupplierCapacityDao supplierCapacityDao;
    private final SupplierRestDao supplierRestDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final SupplierCapacityLogDao supplierCapacityLogDao;

    private static final int SUPPLIER_CAPACITY_PERIOD = 90;
    // 定义范围的上限和下限
    private static final BigDecimal UPPER_LIMIT = new BigDecimal("99999999.99");
    //定义小数点保留位数
    private static final int SCALE = 2;

    /**
     * 供应商产能规则导入
     *
     * @param importationDto 供应商产能规则导入参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void importCapacityRule(SupplierCapacityRuleImportationDto importationDto) {
        String supplierCode = importationDto.getSupplierCode();

        //供应商编码必填校验&供应商信息
        ParamValidUtils.requireNotBlank(supplierCode, "供应商代码未填写！请确保供应商编码填写后重新导入。");
        ParamValidUtils.requireNotNull(
                supplierBaseService.getSupplierByCode(supplierCode),
                "供应商信息不存在！请确保所有供应商信息正确且已在系统中存在后再试。"
        );

        //产能必填校验&产能合法性校验
        String capacityStr
                = ParamValidUtils.requireNotBlank(importationDto.getCapacityStr(), "产能未填写！请确保产能填写后重新导入。");
        BigDecimal capacity = ScmFormatUtil.bigDecimalFormat(capacityStr);
        ParamValidUtils.requireNotNull(capacity,
                StrUtil.format("产能填写存在非法字符!产能值范围[{}~{}]！请确保产能填写正确后重新导入。", 0.01, UPPER_LIMIT.toString()));
        ParamValidUtils.requireNotNull(capacity, "产能未填写！请确保产能填写后重新导入。");
        ParamValidUtils.requireGreaterThan(capacity, BigDecimal.ZERO, "产能不能小于等于0！请确保产能填写正确后重新导入。");
        ParamValidUtils.requireLessThanOrEqual(capacity, UPPER_LIMIT, "产能不能大于99999999.99！请确保产能填写正确后重新导入。");
        ParamValidUtils.requireLessThanOrEqual(capacity.scale(), SCALE, "产能小数位数不能超过两位！请确保产能填写正确后重新导入。");

        //保存供应商产能规则
        SupplierCapacityRulePo existSupCapRule
                = supplierCapacityRuleDao.getBySupplierCode(supplierCode);
        if (Objects.nonNull(existSupCapRule)) {
            existSupCapRule.setCapacity(capacity);
            supplierCapacityRuleDao.updateByIdVersion(existSupCapRule);
        } else {
            SupplierCapacityRulePo newSupCapRule
                    = SupplierCapacityBuilder.buildSupplierCapacityRulePo(supplierCode, capacity, SUPPLIER_CAPACITY_PERIOD);
            supplierCapacityRuleDao.insert(newSupCapRule);
        }

        //初始化T+1~T+90天供应商日产能信息
        supplierCapacityBaseService.initSupplierCapacity(supplierCode, LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId())).plusDays(1), LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId())).plusDays(SUPPLIER_CAPACITY_PERIOD));
    }

    /**
     * 初始化供应商产能信息
     *
     * @param addDaysStr 递增天数字符串
     */
    @Transactional(rollbackFor = Exception.class)
    public void initSupplierCapacity(String addDaysStr) {
        LocalDate capacityBeginDate;
        LocalDate capacityEndDate;

        int addDays;
        if (StrUtil.isBlank(addDaysStr)) {
            capacityBeginDate = LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId())).plusDays(SUPPLIER_CAPACITY_PERIOD).minusDays(1);
            capacityEndDate = capacityBeginDate;
        } else {
            try {
                addDays = Integer.parseInt(addDaysStr);
            } catch (Exception e) {
                throw new BizException("初始化供应商日产能失败,非法字符:{},请输入数字", addDaysStr);
            }
            capacityBeginDate = LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId()));
            capacityEndDate = capacityBeginDate.plusDays(addDays).minusDays(1);
        }

        Set<String> existRuleSupplierCodes = supplierCapacityRuleDao.getExistRuleSupplierCodes();
        for (String existRuleSupplierCode : existRuleSupplierCodes) {
            supplierCapacityBaseService.initSupplierCapacity(existRuleSupplierCode, capacityBeginDate, capacityEndDate);
        }
    }

    /**
     * 供应商日产能导入
     *
     * @param importationDto 供应商产能导入参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void importCapacity(SupplierCapacityImportationDto importationDto) {
        String supplierCode = importationDto.getSupplierCode();

        //供应商编码必填校验&供应商信息
        ParamValidUtils.requireNotBlank(supplierCode, "供应商代码未填写！请确保供应商编码填写后重新导入。");
        ParamValidUtils.requireNotNull(
                supplierBaseService.getSupplierByCode(supplierCode),
                "供应商信息不存在！请确保所有供应商信息正确且已在系统中存在后再试。"
        );

        //剩余产能校验
        String normalAvailableCapacityStr
                = ParamValidUtils.requireNotBlank(importationDto.getNormalAvailableCapacityStr(), "产能值未填写！请确保产能填写后重新导入。");
        BigDecimal normalAvailableCapacity = ScmFormatUtil.bigDecimalFormat(normalAvailableCapacityStr);
        ParamValidUtils.requireNotNull(normalAvailableCapacity,
                StrUtil.format("剩余产能填写存在非法字符!产能值范围[{}~{}]！请确保产能填写正确后重新导入。", BigDecimal.ZERO.toString(), UPPER_LIMIT.toString()));
        ParamValidUtils.requireGreaterThanOrEqual(normalAvailableCapacity, BigDecimal.ZERO, "剩余产能不能小于0！请确保产能填写正确后重新导入。");
        ParamValidUtils.requireLessThanOrEqual(normalAvailableCapacity, UPPER_LIMIT, "剩余产能不能大于99999999.99！请确保产能填写正确后重新导入。");
        ParamValidUtils.requireLessThanOrEqual(normalAvailableCapacity.scale(), SCALE, "剩余产能小数位数不能超过两位！请确保产能填写正确后重新导入。");

        //产能日期
        LocalDate capacityDate;
        String capacityDateStr = ParamValidUtils.requireNotBlank(importationDto.getCapacityDate(), "日期未填写！请确保日期填写后重新导入。");
        String pattern = DatePattern.NORM_DATETIME_PATTERN;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        try {
            capacityDate = LocalDateTime.parse(capacityDateStr, dateTimeFormatter).toLocalDate();
        } catch (Exception e) {
            log.warn("importCapacity error", e);
            throw new ParamIllegalException("请确保日期格式：{}正确填写后重新导入", "yyyy-MM-dd");
        }

        //校验是否处于停工日期
        List<SupplierRestPo> supplierRestPos = supplierRestDao.listBySupplierCode(supplierCode);
        if (supplierRestPos.stream().anyMatch(supplierRestPo -> capacityDate.isEqual(supplierRestPo.getRestDate()))) {
            throw new ParamIllegalException("该日期为停工日期，请确保日期填写正确后重新导入。");
        }

        SupplierCapacityPo supplierCapacityPo
                = ParamValidUtils.requireNotNull(supplierCapacityDao.getBySupplierCodeAndDate(supplierCode, capacityDate), "供应商未维护日产能，请确保供应商日产能存在后重新导入。");
        supplierCapacityPo.setNormalAvailableCapacity(normalAvailableCapacity);
        supplierCapacityDao.updateById(supplierCapacityPo);
    }

    /**
     * 供应商停工时间导入
     *
     * @param importationDto 供应商停工时间导入参数
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SUPPLIER_REST, key = "#importationDto.supplierCode",
            waitTime = 1, leaseTime = -1, exceptionDesc = "供应商停工时间正在处理中，请稍后再试。")
    public void importRestTime(SupplierRestImportationDto importationDto) {
        //必填校验：供应商编码&供应商信息
        String supplierCode = importationDto.getSupplierCode();
        ParamValidUtils.requireNotBlank(supplierCode, "供应商代码未填写！请确保供应商编码填写后重新导入。");
        ParamValidUtils.requireNotNull(supplierBaseService.getSupplierByCode(supplierCode), "供应商信息不存在！请确保所有供应商信息正确且已在系统中存在后再试。");

        //停工日期校验
        LocalDate restDate;
        String restDateStr = ParamValidUtils.requireNotBlank(importationDto.getRestDate(), "停工日期未填写！请确保停工日期填写后重新导入。");
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);
            restDate = LocalDateTime.parse(restDateStr, dateTimeFormatter).toLocalDate();
        } catch (Exception e) {
            log.warn("importRestTime exception:", e);
            throw new ParamIllegalException("请确保停工日期格式：{}正确填写后重新导入", "yyyy-MM-dd");
        }

        //校验是否停工日期已存在 & 是否在过去时间
        if (restDate.isBefore(LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId())))) {
            throw new ParamIllegalException("{}不能小于当前日期，请确保日期填写正确后重新导入。", restDate.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        }
        List<SupplierRestPo> supplierRestPos = supplierRestDao.listBySupplierCode(supplierCode);
        if (supplierRestPos.stream().anyMatch(supplierRestPo -> restDate.isEqual(supplierRestPo.getRestDate()))) {
            throw new ParamIllegalException("{}已存在停工日期，请确保日期填写正确后重新导入。", restDate.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        }
        SupplierRestPo supplierRestPo = SupplierCapacityBuilder.buildSupplierRestPo(supplierCode, restDate);
        supplierRestDao.insert(supplierRestPo);

        //重置产能
        supplierCapacityBaseService.initSupplierCapacity(supplierCode, restDate, restDate);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSupplierCapacityBatch(SupOpCapacityBatchBo request) {
        log.info("updateSupplierCapacityBatch request:{}", request);

        //校验
        List<SupOpCapacityBo> supOpCapacityBos = request.getSupOpCapacityBos();
        if (CollectionUtils.isEmpty(supOpCapacityBos)) {
            log.error("批量变更供应商产能失败！操作参数为空，请相关同事注意！spanId:{}", TraceUtil.getSpanId());
            return;
        }

        for (SupOpCapacityBo supOpCapacityBo : supOpCapacityBos) {
            String supplierCode = supOpCapacityBo.getSupplierCode();
            if (StrUtil.isBlank(supplierCode)) {
                log.error("批量变更供应商产能失败！供应商编码为空，请相关同事注意！spanId:{}", TraceUtil.getSpanId());
                continue;
            }

            LocalDate operateDate = supOpCapacityBo.getOperateDate();
            if (Objects.isNull(operateDate)) {
                log.error("批量变更供应商产能失败！操作日期不能为空，请相关同事注意！spanId:{}", TraceUtil.getSpanId());
                continue;
            }

            BigDecimal operateValue = supOpCapacityBo.getOperateValue();
            if (Objects.isNull(operateValue)) {
                log.error("批量变更供应商产能失败！操作值为空，请相关同事注意！spanId:{}", TraceUtil.getSpanId());
                continue;
            }
            if (operateValue.compareTo(BigDecimal.ZERO) == 0) {
                log.info("批量变更供应商产能失败！操作值为0，变更供应商产能结束！spanId:{}", TraceUtil.getSpanId());
                continue;
            }

            String bizNo = supOpCapacityBo.getBizNo();

            //查询供应商日产能
            SupplierCapacityPo supplierCapacityPo = supplierCapacityDao.getBySupplierCodeAndDate(supplierCode, operateDate);
            if (Objects.isNull(supplierCapacityPo)) {

                //查询供应商产能规则
                SupplierCapacityRulePo supplierCapacityRulePo = supplierCapacityRuleDao.getBySupplierCode(supplierCode);
                if (Objects.isNull(supplierCapacityRulePo)) {
                    log.info("批量变更供应商产能失败！供应商产能规则不存在，变更供应商产能结束！spanId:{}", TraceUtil.getSpanId());
                    continue;
                }

                //初始化&更新供应商日产能
                List<SupplierCapacityPo> supplierCapacityPos
                        = supplierCapacityBaseService.initSupplierCapacity(supplierCode, operateDate, operateDate);
                supplierCapacityDao.updateSupNorAvailCapacity(supplierCode, operateDate, operateValue);

                //记录日产能日志
                List<SupplierCapacityLogPo> supplierCapacityLogPos
                        = SupplierCapacityBuilder.buildSupplierCapacityLogPos(supplierCapacityPos, bizNo, operateValue);
                supplierCapacityLogDao.insertBatch(supplierCapacityLogPos);

                //刷新缓存
                supplierCapacityBaseService.refreshSupCapCache(supplierCode);
            } else {
                //更新供应商日产能
                supplierCapacityDao.updateSupNorAvailCapacity(supplierCode, operateDate, operateValue);

                //刷新缓存
                supplierCapacityBaseService.refreshSupCapCache(supplierCode);

                //记录日产能日志
                SupplierCapacityLogPo supplierCapacityLogPo
                        = SupplierCapacityBuilder.buildSupplierCapacityLogPo(supplierCapacityPo.getSupplierCapacityId(), bizNo, operateValue);
                supplierCapacityLogDao.insert(supplierCapacityLogPo);

                //刷新缓存
                supplierCapacityBaseService.refreshSupCapCache(supplierCode);
            }
        }
    }

    //获取供应商停工时间导出条数
    public CommonResult<Integer> getSupRestExportTotal(ExportSupplierRestDto dto) {
        return CommonResult.success(supplierRestDao.getExportTotals(dto));
    }

    //供应商停工时间导出数据
    public CommonResult<ExportationListResultBo<SupplierRestExportVo>> getSupRestExportList(ExportSupplierRestDto dto) {
        ExportationListResultBo<SupplierRestExportVo> resultBo = new ExportationListResultBo<>();

        IPage<SupplierRestExportVo> pageResult = supplierRestDao.getByPage(dto);
        List<SupplierRestExportVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        records.forEach(record -> record.setRestDateStr(record.getRestDate().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN))));
        resultBo.setRowDataList(records);
        return CommonResult.success(resultBo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportSupplierRest(ExportSupplierRestDto dto) {
        ParamValidUtils.requireGreaterThan(supplierRestDao.getExportTotals(dto), 0, "供应商未维护停工日期，请确保供应商停工日期存在后重新导出。");
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                        FileOperateBizType.SCM_SUPPLIER_REST_EXPORT.getCode(), dto));
    }

    public CommonPageResult.PageInfo<SupplierCapacityPageVo> page(SupCapacityPageDto dto) {
        //校验分页参数
        dto.pageAndExportValid();

        //校验数据是否为空并填充查询条件
        boolean dataEmpty = pageDataIsEmptyAndFullCondition(dto);
        if (dataEmpty) {
            return new CommonPageResult.PageInfo<>();
        }

        CommonPageResult.PageInfo<SupplierCapacityPageVo> pageResult = supplierCapacityRuleDao.page(dto);
        List<SupplierCapacityPageVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }

        //查询供应商每日剩余产能列表
        LocalDate now = LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId()));
        LocalDateTime filterStartDateTime = dto.getFilterStartDateTime();
        LocalDateTime filterEndDateTime = dto.getFilterEndDateTime();

        LocalDateTime defaultFilterStartDateTime = Objects.isNull(filterStartDateTime) ? LocalDateTime.now() : filterStartDateTime;
        LocalDate filterStartDate = TimeUtil.convertZone(defaultFilterStartDateTime, TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();

        LocalDateTime defaultFilterEndDateTime = Objects.isNull(filterEndDateTime) ? LocalDateTime.now().plusDays(90) : filterEndDateTime;
        LocalDate filterEndDate = TimeUtil.convertZone(defaultFilterEndDateTime, TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();

        //批量查询供应商产能
        Set<String> supplierCodeSet = records.stream().map(SupplierCapacityPageVo::getSupplierCode).collect(Collectors.toSet());

        //查询供应商停工时间
        List<SupplierRestPo> supplierRestPos = supplierRestDao.listBySupplierCodes(supplierCodeSet);

        //默认供应商日产能信息
        List<SupplierCapacityPageVo.SupplierCapacityVo> defaultSupplierCapacityVoList
                = SupplierCapacityBuilder.buildDefaultSupplierCapacityVoList(filterStartDateTime, filterEndDateTime);

        records.forEach(record -> {
            String supplierCode = record.getSupplierCode();
            BigDecimal normalCapacity = record.getNormalCapacity();

            //赋值供应商每日剩余产能信息
            List<SupplierCapacityPageVo.SupplierCapacityVo> supplierCapacityVos = deepCopy(defaultSupplierCapacityVoList);
            List<SupplierCapacityBo> supplierCapacityBoList
                    = supplierCapacityBaseService.listBySupCapWithDateRangeCache(supplierCode, ScmTimeUtil.min(filterStartDate, now), ScmTimeUtil.max(filterEndDate, now.plusDays(91)));
            this.assignSupplierCapacity(supplierCapacityVos, supplierCapacityBoList, supplierRestPos, supplierCode, normalCapacity);

            //统计未来30、60、90天剩余总产能
            BigDecimal restCap30AvailCap = calFutureTotalCapacity(supplierCode, 30, supplierCapacityBoList);
            record.setFuture30TotalCap(restCap30AvailCap);

            BigDecimal restCap60AvailCap = calFutureTotalCapacity(supplierCode, 60, supplierCapacityBoList);
            record.setFuture60TotalCap(restCap60AvailCap);

            BigDecimal restCap90AvailCap = calFutureTotalCapacity(supplierCode, 90, supplierCapacityBoList);
            record.setFuture90TotalCap(restCap90AvailCap);
            record.setSupplierCapacityVoList(supplierCapacityVos);
        });
        return pageResult;
    }

    private boolean pageDataIsEmptyAndFullCondition(SupCapacityPageDto dto) {
        BigDecimal restCap30PerStart = dto.getRestCap30PerStart();
        BigDecimal restCap30PerEnd = dto.getRestCap30PerEnd();
        BigDecimal restCap60PerStart = dto.getRestCap60PerStart();
        BigDecimal restCap60PerEnd = dto.getRestCap60PerEnd();
        BigDecimal restCap90PerStart = dto.getRestCap90PerStart();
        BigDecimal restCap90PerEnd = dto.getRestCap90PerEnd();
        if (Objects.nonNull(restCap30PerStart) || Objects.nonNull(restCap60PerStart) || Objects.nonNull(restCap90PerStart)) {
            List<String> filterPerSupplierCodeList = filterSupplierCodes(restCap30PerStart, restCap30PerEnd, restCap60PerStart, restCap60PerEnd, restCap90PerStart, restCap90PerEnd);
            if (CollectionUtils.isEmpty(filterPerSupplierCodeList)) {
                log.info("30/60/90剩余百分比区间不为空且无法筛选对应供应商数据！");
                return true;
            } else {
                log.info("30/60/90剩余百分比区间不为空且可以筛选对应供应商数据！{}", JSON.toJSONString(filterPerSupplierCodeList));
                dto.setFilterPerSupplierCodeList(filterPerSupplierCodeList);
            }
        }
        return false;
    }

    private List<String> filterSupplierCodes(BigDecimal restCap30PerStart, BigDecimal restCap30PerEnd,
                                             BigDecimal restCap60PerStart, BigDecimal restCap60PerEnd,
                                             BigDecimal restCap90PerStart, BigDecimal restCap90PerEnd) {

        return supplierCapacityDao.filterSupplierCodes(restCap30PerStart, restCap30PerEnd, restCap60PerStart, restCap60PerEnd, restCap90PerStart, restCap90PerEnd);
    }


    public static List<SupplierCapacityPageVo.SupplierCapacityVo> deepCopy(List<SupplierCapacityPageVo.SupplierCapacityVo> availCapList) {
        List<SupplierCapacityPageVo.SupplierCapacityVo> clonedList = new ArrayList<>();
        try {
            for (SupplierCapacityPageVo.SupplierCapacityVo item : availCapList) {
                clonedList.add(item.clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            // Handle the exception according to your needs
        }
        return clonedList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportSupplierCapacity(SupCapacityPageDto dto) {
        // 校验参数
        dto.pageAndExportValid();

        CommonResult<Integer> supCapacityRuleExportTotal = getSupCapacityRuleExportTotal(dto);
        CommonResult<Integer> supCapacityExportTotal = getSupCapacityExportTotal(dto);
        if (Objects.nonNull(supCapacityRuleExportTotal.getData()) && Objects.nonNull(supCapacityExportTotal.getData()) &&
                supCapacityRuleExportTotal.getData() == 0 && supCapacityExportTotal.getData() == 0) {
            throw new ParamIllegalException("供应商产能和供应商每日产能数据都为空，请检查筛选条件");
        }

        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                        FileOperateBizType.SCM_SUPPLIER_CAPACITY_EXPORT.getCode(), dto));
    }

    //获取供应商产能规则导出总条数
    public CommonResult<Integer> getSupCapacityRuleExportTotal(SupCapacityPageDto dto) {
        boolean dataEmpty = pageDataIsEmptyAndFullCondition(dto);
        if (dataEmpty) {
            log.info("供应商产能规则导出数据列表：根据30/60/90天剩余百分比筛选供应商数据为空！");
            return CommonResult.success(0);
        }

        return CommonResult.success(supplierCapacityRuleDao.getSupCapacityRuleExportTotal(dto));
    }

    //供应商产能规则导出数据列表
    public CommonResult<ExportationListResultBo<SupplierCapacityRuleExportVo>> getSupCapacityRuleExportList(SupCapacityPageDto dto) {
        ExportationListResultBo<SupplierCapacityRuleExportVo> resultBo = new ExportationListResultBo<>();

        boolean dataEmpty = pageDataIsEmptyAndFullCondition(dto);
        if (dataEmpty) {
            log.info("供应商产能规则导出数据列表：根据30/60/90天剩余百分比筛选供应商数据为空！");
            return CommonResult.success(resultBo);
        }

        CommonPageResult.PageInfo<SupplierCapacityPageVo> page = supplierCapacityRuleDao.page(dto);
        List<SupplierCapacityPageVo> records = page.getRecords();

        //查询供应商每日剩余产能列表
        LocalDateTime now = LocalDateTime.now();
        LocalDate filterStartDate
                = TimeUtil.convertZone(now, TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();
        LocalDate filterEndDate
                = TimeUtil.convertZone(LocalDateTime.now().plusDays(90), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();

        records.forEach(record -> {
            String supplierCode = record.getSupplierCode();

            List<SupplierCapacityBo> supplierCapacityBoList = supplierCapacityBaseService.listBySupCapWithDateRange(supplierCode, filterStartDate, filterEndDate);
            //统计未来30、60、90天剩余总产能
            BigDecimal restCap30AvailCap = calFutureTotalCapacity(supplierCode, 30, supplierCapacityBoList);
            record.setFuture30TotalCap(restCap30AvailCap);

            BigDecimal restCap60AvailCap = calFutureTotalCapacity(supplierCode, 60, supplierCapacityBoList);
            record.setFuture60TotalCap(restCap60AvailCap);

            BigDecimal restCap90AvailCap = calFutureTotalCapacity(supplierCode, 90, supplierCapacityBoList);
            record.setFuture90TotalCap(restCap90AvailCap);
        });
        resultBo.setRowDataList(SupplierCapacityBuilder.buildSupplierCapacityRuleExportVos(records));
        return CommonResult.success(resultBo);
    }

    //供应商每日剩余产能导出总条数
    public CommonResult<Integer> getSupCapacityExportTotal(SupCapacityPageDto dto) {
        boolean dataEmpty = pageDataIsEmptyAndFullCondition(dto);
        if (dataEmpty) {
            log.info("供应商每日剩余产能规则导出数据列表：根据30/60/90天剩余百分比筛选供应商数据为空！");
            return CommonResult.success(0);
        }

        LocalDateTime filterStartDateTime = dto.getFilterStartDateTime();
        LocalDateTime filterEndDateTime = dto.getFilterEndDateTime();

        if (Objects.isNull(filterStartDateTime) || Objects.isNull(filterEndDateTime)) {
            log.info("供应商每日导出数据列表：筛选时间不能为空！");
            return CommonResult.success(0);
        }
        LocalDate filterStartDate
                = TimeUtil.convertZone(filterStartDateTime, TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();
        dto.setFilterStartDate(filterStartDate);
        LocalDate filterEndDate
                = TimeUtil.convertZone(filterEndDateTime, TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();
        dto.setFilterEndDate(filterEndDate);

        return CommonResult.success(supplierCapacityDao.getSupCapacityExportTotal(dto));
    }

    //供应商每日剩余产能导出数据列表
    public CommonResult<ExportationListResultBo<SupplierCapacityExportVo>> getSupCapacityExportList(SupCapacityPageDto dto) {
        ExportationListResultBo<SupplierCapacityExportVo> resultBo = new ExportationListResultBo<>();

        boolean dataEmpty = pageDataIsEmptyAndFullCondition(dto);
        if (dataEmpty) {
            log.info("供应商每日产能规则导出数据列表：根据30/60/90天剩余百分比筛选供应商数据为空！");
            return CommonResult.success(resultBo);
        }

        LocalDateTime filterStartDateTime = dto.getFilterStartDateTime();
        LocalDateTime filterEndDateTime = dto.getFilterEndDateTime();

        LocalDate filterStartDate
                = TimeUtil.convertZone(filterStartDateTime, TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();
        dto.setFilterStartDate(filterStartDate);
        LocalDate filterEndDate
                = TimeUtil.convertZone(filterEndDateTime, TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();
        dto.setFilterEndDate(filterEndDate);

        IPage<SupplierCapacityExportVo> pageResult = supplierCapacityDao.getSupCapacityExportList(dto);
        List<SupplierCapacityExportVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        records.forEach(record -> record.setCapacityDateStr(record.getCapacityDate().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN))));
        resultBo.setRowDataList(records);
        return CommonResult.success(resultBo);
    }

    private void assignSupplierCapacity(List<SupplierCapacityPageVo.SupplierCapacityVo> supplierCapacityVos,
                                        List<SupplierCapacityBo> supplierCapacityBoList,
                                        List<SupplierRestPo> supplierRestPos,
                                        String supplierCode,
                                        BigDecimal normalCapacity) {
        supplierCapacityVos.forEach(supplierCapacityVo -> {
            //通过供应商编码&产能日期匹配
            supplierCapacityBoList.stream().filter(supplierCapacityBo ->
                            Objects.equals(supplierCode, supplierCapacityBo.getSupplierCode()) &&
                                    Objects.equals(supplierCapacityVo.getCapacityDate(), supplierCapacityBo.getCapacityDate()))
                    .findFirst().ifPresentOrElse(supplierCapacityResBo ->
                                    //匹配成功设置剩余产能，否则默认产能
                                    supplierCapacityVo.setNormalAvailableCapacity(supplierCapacityResBo.getNormalAvailableCapacity()),
                            () -> supplierCapacityVo.setNormalAvailableCapacity(normalCapacity)
                    );

            //匹配停工时间
            supplierRestPos.stream().filter(supplierRestPo ->
                            Objects.equals(supplierRestPo.getSupplierCode(), supplierCode) && supplierRestPo.getRestDate().equals(supplierCapacityVo.getCapacityDate()))
                    .findFirst().ifPresent(supplierRestPo -> supplierCapacityVo.setIsRest(BooleanType.TRUE)
                    );
        });
    }

    // 计算指定天数内的总产能
    private BigDecimal calFutureTotalCapacity(String supplierCode, int futureDays, List<SupplierCapacityBo> supplierCapacityBoList) {
        LocalDate now = LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId()));
        return supplierCapacityBoList.stream()
                .filter(supplierCapacityBo ->
                        Objects.equals(supplierCapacityBo.getSupplierCode(), supplierCode) &&
                                supplierCapacityBo.getCapacityDate().isAfter(now) &&
                                supplierCapacityBo.getCapacityDate().isBefore(now.plusDays(futureDays + 1))
                )
                .map(SupplierCapacityBo::getNormalAvailableCapacity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 格式化 LocalDate 为自定义格式：yy-m-d
     * 月份和年不要补0，年只显示后两位
     *
     * @param date 要格式化的日期
     * @return 格式化后的日期字符串
     */
    private String formatCustom(LocalDate date) {
        // 获取年份的后两位
        String year = String.valueOf(date.getYear()).substring(2);
        // 获取月份和日期，不补0
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        // 构建自定义格式的日期字符串
        return String.format("%s-%d-%d", year, month, day);
    }
}







