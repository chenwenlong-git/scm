package com.hete.supply.scm.server.scm.supplier.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SpAcPyReqDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierPaymentAccountSearchDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierPaymentAccountSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 供应商收款账号 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-05
 */
@Component
@Validated
public class SupplierPaymentAccountDao extends BaseDao<SupplierPaymentAccountMapper, SupplierPaymentAccountPo> {


    public List<SupplierPaymentAccountPo> getListByAccountList(List<String> accountList) {
        if (CollectionUtils.isEmpty(accountList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierPaymentAccountPo>lambdaQuery()
                .in(SupplierPaymentAccountPo::getAccount, accountList));
    }

    public CommonPageResult.PageInfo<SupplierPaymentAccountSearchVo> selectSupplierPaymentAccountPage(Page<Void> page, SupplierPaymentAccountSearchDto dto) {
        IPage<SupplierPaymentAccountSearchVo> pageResult = baseMapper.selectSupplierPaymentAccountPage(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<SupplierPaymentAccountPo> getListBySupplierCode(String supplierCode) {
        if (StringUtils.isBlank(supplierCode)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierPaymentAccountPo>lambdaQuery()
                .eq(SupplierPaymentAccountPo::getSupplierCode, supplierCode)
                .orderByDesc(SupplierPaymentAccountPo::getSupplierPaymentAccountId));
    }

    public List<SupplierPaymentAccountPo> getListByAccountNeId(String account, Long supplierPaymentAccountId) {
        if (StringUtils.isBlank(account)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplierPaymentAccountPo>lambdaQuery()
                .eq(SupplierPaymentAccountPo::getAccount, account)
                .ne(SupplierPaymentAccountPo::getSupplierPaymentAccountId, supplierPaymentAccountId));
    }

    public SupplierPaymentAccountPo getByFeishuAuditOrderNo(String feishuAuditOrderNo) {
        if (StringUtils.isBlank(feishuAuditOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<SupplierPaymentAccountPo>lambdaQuery()
                .eq(SupplierPaymentAccountPo::getFeishuAuditOrderNo, feishuAuditOrderNo));
    }

    public List<SupplierPaymentAccountPo> getListBySupplierCodeList(List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplierPaymentAccountPo>lambdaQuery()
                .in(SupplierPaymentAccountPo::getSupplierCode, supplierCodeList));
    }

    public List<SupplierPaymentAccountPo> getList(SpAcPyReqDto dto) {
        List<String> supplierCodeList = dto.getSupplierCodeList();
        String searchKey = dto.getSearchKey();

        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<SupplierPaymentAccountPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SupplierPaymentAccountPo::getSupplierCode, supplierCodeList);
        if (StrUtil.isNotBlank(searchKey)) {
            queryWrapper.and(wrapper -> wrapper
                    .like(StrUtil.isNotBlank(searchKey), SupplierPaymentAccountPo::getAccountUsername, searchKey)
                    .or()
                    .like(StrUtil.isNotBlank(searchKey), SupplierPaymentAccountPo::getAccount, searchKey));
        }
        return list(queryWrapper);
    }

    public SupplierPaymentAccountPo getByAccount(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<SupplierPaymentAccountPo>lambdaQuery()
                .eq(SupplierPaymentAccountPo::getAccount, account));
    }
}
