package com.hete.supply.scm.server.scm.supplier.service.base;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.server.scm.supplier.builder.SupplierCapacityBuilder;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierCapacityDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierCapacityLogDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierCapacityRuleDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierRestDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupCapRuleBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityLogPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityRulePo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierRestPo;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/8/5.
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplierCapacityBaseService {
    private final SupplierCapacityDao supplierCapacityDao;
    private final SupplierCapacityRuleDao supplierCapacityRuleDao;
    private final SupplierRestDao supplierRestDao;
    private final SupplierCapacityLogDao supplierCapacityLogDao;
    private final RedissonClient redissonClient;

    @RedisLock(prefix = ScmRedisConstant.SUPPLIER_CAPACITY, key = "#supplierCode",
            waitTime = 1, leaseTime = -1, exceptionDesc = "供应商产能正在处理中，请稍后再试。")
    public List<SupplierCapacityPo> initSupplierCapacity(String supplierCode, LocalDate capacityBeginDate, LocalDate capacityEndDate) {
        log.info("初始化供应商[{}]产能，时间段[{}~{}]", supplierCode, capacityBeginDate, capacityEndDate);

        // 获取供应商产能规则
        SupplierCapacityRulePo supplierCapacityRulePo = supplierCapacityRuleDao.getBySupplierCode(supplierCode);
        if (Objects.isNull(supplierCapacityRulePo)) {
            log.info("供应商[{}]没有产能规则，无需初始化。", supplierCode);
            return Collections.emptyList();
        }

        // 删除该供应商该时间段内的产能
        List<SupplierCapacityPo> existSupplierCapacityPos
                = supplierCapacityDao.listBySupplierCodeAndDateRange(supplierCode, capacityBeginDate, capacityEndDate);
        if (CollectionUtils.isNotEmpty(existSupplierCapacityPos)) {
            List<Long> ids = existSupplierCapacityPos.stream().map(SupplierCapacityPo::getSupplierCapacityId).collect(Collectors.toList());
            supplierCapacityDao.removeBatchByIds(ids);
        }

        // 获取供应商停工时间
        List<SupplierRestPo> supplierRestPos = supplierRestDao.listBySupplierCode(supplierCode);
        List<LocalDate> restDateList = supplierRestPos.stream().map(SupplierRestPo::getRestDate).collect(Collectors.toList());

        //根据产能规则 & 停工时间写入每日产能
        BigDecimal capacity = supplierCapacityRulePo.getCapacity();
        Long supplierCapacityRuleId = supplierCapacityRulePo.getSupplierCapacityRuleId();
        List<SupplierCapacityPo> supplierCapacityPos
                = SupplierCapacityBuilder.buildSupplierCapacityPos(supplierCode, capacityBeginDate, capacityEndDate, capacity, supplierCapacityRuleId, restDateList);
        supplierCapacityDao.insertBatch(supplierCapacityPos);

        //记录产能变更日期
        List<SupplierCapacityLogPo> supplierCapacityLogPos = SupplierCapacityBuilder.buildSupplierCapacityLogPos(supplierCapacityPos);
        supplierCapacityLogDao.insertBatch(supplierCapacityLogPos);

        //刷新缓存
        refreshSupCapCache(supplierCode);
        return supplierCapacityPos;
    }

    public Map<String, BigDecimal> getSupCapRuleMap(Collection<String> supplierCodeList) {
        List<SupCapRuleBo> supCapRuleBos = supplierCapacityRuleDao.listBySupplierCodes(supplierCodeList);
        return supCapRuleBos.stream().collect(Collectors.toMap(SupCapRuleBo::getSupplierCode, SupCapRuleBo::getCapacity,
                (existingValue, newValue) -> existingValue // 如果有重复的键，保留现有的值
        ));
    }

    public List<SupplierCapacityBo> listBySupCapWithDateRange(String supplierCode, LocalDate capacityBeginDate, LocalDate capacityEndDate) {
        return supplierCapacityDao.listBySupCapWithDateRange(supplierCode, capacityBeginDate, capacityEndDate);
    }

    public List<SupplierCapacityBo> listBySupCapWithDateRangeCache(String supplierCode, LocalDate capacityBeginDate, LocalDate capacityEndDate) {
        String cacheKey = genSupCapCacheKey(supplierCode);

        if (redissonClient.getMapCache(cacheKey).isExists() && redissonClient.getMapCache(cacheKey).containsKey(genSupCapMapKey(capacityBeginDate, capacityEndDate))) {
            RMap<String, String> map = redissonClient.getMapCache(cacheKey);

            String objStr = map.get(genSupCapMapKey(capacityBeginDate, capacityEndDate));
            log.info("从缓存中获取供应商=>{},产能数据=>{}", supplierCode, objStr);
            return JSON.parseArray(objStr, SupplierCapacityBo.class);
        } else {
            List<SupplierCapacityBo> supplierCapacityBos
                    = supplierCapacityDao.listBySupCapWithDateRange(supplierCode, capacityBeginDate, capacityEndDate);
            log.info("从数据库中获取供应商=>{},产能数据=>{}", supplierCode, supplierCapacityBos);

            //放入缓存同时设置过期时间1分钟
            RMapCache<String, String> map = redissonClient.getMapCache(cacheKey);
            if (CollectionUtils.isEmpty(supplierCapacityBos)) {
                map.put(genSupCapMapKey(capacityBeginDate, capacityEndDate), JSON.toJSONString(Collections.emptyList()), 60, TimeUnit.SECONDS);
                log.info("供应商=>{},产能数据为空，放入缓存=>{}", supplierCode, cacheKey);
                return Collections.emptyList();
            } else {
                map.put(genSupCapMapKey(capacityBeginDate, capacityEndDate), JSON.toJSONString(supplierCapacityBos), 60, TimeUnit.SECONDS);
                log.info("供应商=>{},产能数据放入缓存=>[{}]", supplierCode, cacheKey);
                return supplierCapacityBos;
            }
        }
    }

    public void refreshSupCapCache(String supplierCode) {
        if (redissonClient.getMapCache(genSupCapCacheKey(supplierCode)).isExists()) {
            log.info("清空供应商=>{}产能缓存", supplierCode);
            redissonClient.getMapCache(genSupCapCacheKey(supplierCode)).clear();
        }
    }

    //供应商日产能每日剩余产能缓存键
    public String genSupCapCacheKey(String supplierCode) {
        return ScmRedisConstant.SUPPLIER_CAPACITY_QUERY + supplierCode;
    }

    public String genSupCapMapKey(LocalDate capacityBeginDate, LocalDate capacityEndDate) {
        return capacityBeginDate + ":" + capacityEndDate;
    }
}









