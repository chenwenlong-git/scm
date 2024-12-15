package com.hete.supply.scm.server.scm.supplier.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.SkuCycleImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierProductImportationDto;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.dto.SkuDto;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.enums.BindingSupplierProduct;
import com.hete.supply.scm.server.scm.service.base.SkuBaseService;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierProductCompareConverter;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierBindingListQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryBatchUpdateBo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareDetailDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareEditDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareItemDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductBindingBySkuVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductCompareDetailVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductComparePageVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductCompareVo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierInventoryBaseService;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierProductCompareBaseService;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierProductCompareRefService;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/3/28 15:20
 */
@Service
@RequiredArgsConstructor
public class SupplierProductCompareBizService {

    private final PlmSkuDao plmSkuDao;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final SupplierDao supplierDao;
    private final PlmRemoteService plmRemoteService;
    private final SupplierInventoryBaseService supplierInventoryBaseService;
    private final SupplierProductCompareBaseService supplierProductCompareBaseService;
    private final SkuInfoDao skuInfoDao;
    private final SkuBaseService skuBaseService;
    private final SupplierProductCompareRefService supplierProductCompareRefService;


    @Deprecated
    public CommonPageResult.PageInfo<SupplierProductComparePageVo> searchSupplierProductCompare(SupplierProductCompareDto dto) {
        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return new CommonPageResult.PageInfo<>();
            }

            if (CollectionUtils.isNotEmpty(dto.getSkuList())) {
                skuListByEncode.retainAll(dto.getSkuList());
                if (CollectionUtils.isEmpty(skuListByEncode)) {
                    return new CommonPageResult.PageInfo<>();
                }
            }
            dto.setSkuList(skuListByEncode);
        }

        if (CollectionUtil.isNotEmpty(dto.getSupplierCodeList())) {
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBatchSupplierCode(dto.getSupplierCodeList());
            if (CollectionUtil.isEmpty(supplierProductComparePoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<String> skuList = supplierProductComparePoList.stream()
                    .map(SupplierProductComparePo::getSku)
                    .filter(StringUtils::isNotBlank)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuList);
            } else {
                dto.getSkuList().retainAll(skuList);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return new CommonPageResult.PageInfo<>();
            }
        }

        if (CollectionUtil.isNotEmpty(dto.getSupplierProductNameList())) {
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBatchSupplierProductName(dto.getSupplierProductNameList());
            if (CollectionUtil.isEmpty(supplierProductComparePoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<String> skuList = supplierProductComparePoList.stream()
                    .map(SupplierProductComparePo::getSku)
                    .filter(StringUtils::isNotBlank)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuList);
            } else {
                dto.getSkuList().retainAll(skuList);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return new CommonPageResult.PageInfo<>();
            }
        }

        CommonPageResult.PageInfo<SupplierProductComparePageVo> pageResult = plmSkuDao.selectSupplierProductPage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<SupplierProductComparePageVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }
        //获取sku的信息
        List<String> skuList = records.stream().map(SupplierProductComparePageVo::getSku).collect(Collectors.toList());
        List<PlmGoodsDetailVo> goodsSkuList = plmRemoteService.getGoodsDetail(skuList);
        Map<String, String> skuMap = plmRemoteService.getSkuEncodeBySku(skuList)
                .stream()
                .filter(p -> StringUtils.isNotBlank(p.getSkuCode()))
                .collect(Collectors.toMap(PlmSkuVo::getSkuCode, PlmSkuVo::getSkuEncode));

        // 获取单件产能
        Map<String, BigDecimal> singleCapacityMap = skuInfoDao.getListBySkuList(skuList).stream()
                .collect(Collectors.toMap(SkuInfoPo::getSku, SkuInfoPo::getSingleCapacity));

        Map<String, List<SupplierProductComparePo>> supplierProductCompareMap = supplierProductCompareDao.getBySkuList(skuList);
        List<SupplierProductComparePageVo> newRecords = records.stream().peek(item -> {
            for (PlmGoodsDetailVo plmGoodsDetailVo : goodsSkuList) {
                if (plmGoodsDetailVo.skuCodeInside(item.getSku())) {
                    item.setCategoryList(plmGoodsDetailVo.getCategoryList());
                }
            }
            List<SupplierProductComparePo> supplierProductComparePos = supplierProductCompareMap.get(item.getSku());
            if (CollectionUtil.isNotEmpty(supplierProductComparePos)) {
                item.setUpdateUsername(supplierProductComparePos.get(0).getUpdateUsername());
                item.setUpdateTime(supplierProductComparePos.get(0).getUpdateTime());
                item.setSupplierCodeList(supplierProductComparePos.stream().map(SupplierProductComparePo::getSupplierCode).collect(Collectors.toList()));
            }
            item.setSkuEncode(skuMap.get(item.getSku()));
            item.setSingleCapacity(BigDecimal.ZERO);
            if (singleCapacityMap.containsKey(item.getSku())) {
                item.setSingleCapacity(singleCapacityMap.get(item.getSku()));
            }
        }).collect(Collectors.toList());
        pageResult.setRecords(newRecords);
        return pageResult;
    }

    @Deprecated
    public SupplierProductCompareDetailVo getDetail(SupplierProductCompareDetailDto dto) {
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dto.getSku());
        if (plmSkuPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBySku(dto.getSku());
        SupplierProductCompareDetailVo detailVo = new SupplierProductCompareDetailVo();

        List<String> skuList = new ArrayList<>();
        skuList.add(plmSkuPo.getSku());
        List<PlmGoodsDetailVo> goodsSkuList = plmRemoteService.getGoodsDetail(skuList);
        if (CollectionUtils.isNotEmpty(goodsSkuList)) {
            detailVo.setCategoryList(goodsSkuList.get(0).getCategoryList());
        }

        Map<String, String> skuMap = plmRemoteService.getSkuEncodeBySku(skuList).stream().collect(Collectors.toMap(PlmSkuVo::getSkuCode, PlmSkuVo::getSkuEncode));
        detailVo.setSkuEncode(skuMap.get(detailVo.getSku()));
        detailVo.setPlmSkuId(plmSkuPo.getPlmSkuId());
        detailVo.setSku(plmSkuPo.getSku());
        detailVo.setVersion(plmSkuPo.getVersion());
        detailVo.setCycle(plmSkuPo.getCycle());

        // 获取单件产能
        Map<String, BigDecimal> singleCapacityMap = skuInfoDao.getListBySkuList(skuList).stream()
                .collect(Collectors.toMap(SkuInfoPo::getSku, SkuInfoPo::getSingleCapacity));
        detailVo.setSingleCapacity(BigDecimal.ZERO);
        if (singleCapacityMap.containsKey(detailVo.getSku())) {
            detailVo.setSingleCapacity(singleCapacityMap.get(detailVo.getSku()));
        }

        List<SupplierProductCompareVo> list = new ArrayList<>();
        for (SupplierProductComparePo supplierProductComparePo : supplierProductComparePoList) {
            SupplierProductCompareVo supplierProductCompareVo = new SupplierProductCompareVo();
            supplierProductCompareVo.setSupplierProductCompareId(supplierProductComparePo.getSupplierProductCompareId());
            supplierProductCompareVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
            supplierProductCompareVo.setSupplierCode(supplierProductComparePo.getSupplierCode());
            supplierProductCompareVo.setVersion(supplierProductComparePo.getVersion());
            supplierProductCompareVo.setSku(supplierProductComparePo.getSku());
            supplierProductCompareVo.setSupplierProductCompareStatus(supplierProductComparePo.getSupplierProductCompareStatus());
            list.add(supplierProductCompareVo);
        }
        detailVo.setSupplierProductCompareVoList(list);
        return detailVo;
    }

    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(SupplierProductCompareEditDto dto) {
        PlmSkuPo plmSkuPo = plmSkuDao.getByIdVersion(dto.getPlmSkuId(), dto.getVersion());
        if (plmSkuPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        String user = GlobalContext.getUserKey();
        String userName = GlobalContext.getUsername();

        // 供应商库存信息
        List<SupplierInventoryBatchUpdateBo> supplierInventoryBatchUpdateBoList = new ArrayList<>();

        //当绑定供应商产品对照列表不为空时
        if (CollectionUtils.isNotEmpty(dto.getSupplierProductCompareEditList())) {
            List<String> supplierCodeList = dto.getSupplierProductCompareEditList().stream()
                    .map(SupplierProductCompareItemDto::getSupplierCode).distinct().collect(Collectors.toList());
            if (supplierCodeList.size() != dto.getSupplierProductCompareEditList().size()) {
                throw new ParamIllegalException("禁止重复添加供应商");
            }
            Map<String, SupplierPo> supplierMap = supplierDao.getSupplierMapBySupplierCodeList(supplierCodeList);
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBySku(plmSkuPo.getSku());
            List<SupplierProductComparePo> newSupplierProductComparePoList = SupplierProductCompareConverter.itemDtoToPo(dto.getSupplierProductCompareEditList());

            CompareResult<SupplierProductComparePo> itemResult = DataCompareUtil.compare(newSupplierProductComparePoList, supplierProductComparePoList, SupplierProductComparePo::getSupplierProductCompareId);
            List<SupplierProductComparePo> collect = itemResult.getNewItems().stream().peek(item -> {
                SupplierPo supplierPo = supplierMap.get(item.getSupplierCode());

                // 供应商库存信息组装
                SupplierInventoryBatchUpdateBo supplierInventoryBatchUpdateBo = new SupplierInventoryBatchUpdateBo();
                supplierInventoryBatchUpdateBo.setSku(plmSkuPo.getSku());
                supplierInventoryBatchUpdateBo.setSupplierCode(item.getSupplierCode());

                if (null != supplierPo) {
                    item.setSupplierName(supplierPo.getSupplierName());
                    supplierInventoryBatchUpdateBo.setSupplierName(supplierPo.getSupplierName());
                }
                supplierInventoryBatchUpdateBoList.add(supplierInventoryBatchUpdateBo);

                item.setSku(plmSkuPo.getSku());
                item.setCreateUser(user);
                item.setCreateUsername(userName);
                item.setUpdateUser(user);
                item.setUpdateUsername(userName);
                item.setSupplierProductCompareStatus(BooleanType.TRUE);
            }).collect(Collectors.toList());
            supplierProductCompareDao.insertBatch(collect);
            // 编辑时数据处理
            if (CollectionUtils.isNotEmpty(itemResult.getExistingItems())) {
                for (SupplierProductComparePo existingItem : itemResult.getExistingItems()) {
                    // 供应商库存信息组装
                    SupplierInventoryBatchUpdateBo supplierInventoryBatchUpdateBo = new SupplierInventoryBatchUpdateBo();
                    supplierInventoryBatchUpdateBo.setSku(plmSkuPo.getSku());
                    supplierInventoryBatchUpdateBo.setSupplierCode(existingItem.getSupplierCode());
                    SupplierPo supplierPo = supplierMap.get(existingItem.getSupplierCode());
                    if (null != supplierPo) {
                        existingItem.setSupplierName(supplierPo.getSupplierName());
                        supplierInventoryBatchUpdateBo.setSupplierName(supplierPo.getSupplierName());
                    }
                    supplierInventoryBatchUpdateBoList.add(supplierInventoryBatchUpdateBo);
                    Assert.notNull(existingItem.getSupplierProductCompareStatus(), () -> new BizException("编辑的关联供应商：{}入参需要传状态字段，请联系系统管理员！", existingItem.getSupplierCode()));

                }
            }
            supplierProductCompareDao.updateBatchByIdVersion(itemResult.getExistingItems());
            supplierProductCompareDao.removeBatchByIds(itemResult.getDeletedItems());
            plmSkuPo.setBindingSupplierProduct(BindingSupplierProduct.TRUE);
        }

        plmSkuPo.setCycle(dto.getCycle());
        plmSkuDao.updateByIdVersion(plmSkuPo);

        // 增加时执行初始供应商库存信息
        supplierProductCompareBaseService.supplierInventoryUpdateBatch(supplierInventoryBatchUpdateBoList);

        // 更新单价产能
        skuBaseService.saveSkuInfoSingleCapacity(plmSkuPo.getSku(), dto.getSingleCapacity());

        return true;
    }

    /**
     * 导入供应商产品对照
     *
     * @author ChenWenLong
     * @date 2023/3/29 11:27
     */
    @Transactional(rollbackFor = Exception.class)
    public void importData(SupplierProductImportationDto.ImportationDetail dto) {
        if (StringUtils.isBlank(dto.getSupplierCode())) {
            throw new ParamIllegalException("供应商代码不能为空");
        }
        if (StringUtils.isBlank(dto.getSku())) {
            throw new ParamIllegalException("SKU不能为空");
        }
        if (StringUtils.isBlank(dto.getSupplierProductName())) {
            throw new ParamIllegalException("供应商产品名称不能为空");
        }
        if (dto.getSupplierProductName().length() > 100) {
            throw new ParamIllegalException("供应商产品名称字符长度不能超过 100 位");
        }
        String supplierCode = dto.getSupplierCode().strip();
        String sku = dto.getSku().strip();
        String supplierProductName = dto.getSupplierProductName().strip();

        SupplierPo supplierPo = supplierDao.getBySupplierCode(supplierCode);
        if (supplierPo == null) {
            throw new ParamIllegalException("创建产品对照关系失败，失败原因：供应商代码不存在");
        }

        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (plmSkuPo == null) {
            throw new ParamIllegalException("创建产品对照关系失败，失败原因：sku不存在");
        }

        if (StringUtils.isBlank(supplierProductName)) {
            throw new ParamIllegalException("创建产品对照关系失败，失败原因：供应商产品名称不能为空");
        }

        SupplierProductComparePo supplierProductComparePo = supplierProductCompareDao.getBySupplierCodeAndSku(supplierCode, sku);
        if (null != supplierProductComparePo) {
            //更新产品对照
            supplierProductComparePo.setSupplierProductName(dto.getSupplierProductName());
            supplierProductCompareDao.updateById(supplierProductComparePo);
        } else {
            //创建产品对照
            SupplierProductComparePo insertSupplierProductCompare = new SupplierProductComparePo();
            insertSupplierProductCompare.setSupplierProductName(dto.getSupplierProductName());
            insertSupplierProductCompare.setSku(sku);
            insertSupplierProductCompare.setSupplierCode(supplierPo.getSupplierCode());
            insertSupplierProductCompare.setSupplierName(supplierPo.getSupplierName());
            insertSupplierProductCompare.setSupplierProductCompareStatus(BooleanType.TRUE);
            supplierProductCompareDao.insert(insertSupplierProductCompare);
        }

        // 供应商库存信息
        List<SupplierInventoryBatchUpdateBo> supplierInventoryBatchUpdateBoList = new ArrayList<>();

        //更新SKU绑定状态
        plmSkuPo.setBindingSupplierProduct(BindingSupplierProduct.TRUE);
        plmSkuDao.updateByIdVersion(plmSkuPo);


        // 判断sku是由未绑定变成绑定时执行初始供应商库存信息
        SupplierInventoryBatchUpdateBo supplierInventoryBatchUpdateBo = new SupplierInventoryBatchUpdateBo();
        supplierInventoryBatchUpdateBo.setSku(plmSkuPo.getSku());
        supplierInventoryBatchUpdateBo.setSupplierCode(supplierPo.getSupplierCode());
        supplierInventoryBatchUpdateBo.setSupplierName(supplierPo.getSupplierName());
        supplierInventoryBatchUpdateBoList.add(supplierInventoryBatchUpdateBo);
        supplierProductCompareBaseService.supplierInventoryUpdateBatch(supplierInventoryBatchUpdateBoList);


    }

    /**
     * 导入sku生产周期
     *
     * @author ChenWenLong
     * @date 2023/5/25 17:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void importCycleData(SkuCycleImportationDto dto) {
        if (StringUtils.isBlank(dto.getSku())) {
            throw new ParamIllegalException("导入失败，失败原因：sku不能为空");
        }
        String sku = dto.getSku().strip();
        String cycleStr = dto.getCycle();
        String singleCapacityStr = dto.getSingleCapacity();
        String user = GlobalContext.getUserKey();
        String userName = GlobalContext.getUsername();

        if (StringUtils.isEmpty(cycleStr) && StringUtils.isEmpty(singleCapacityStr)) {
            throw new ParamIllegalException("导入失败，失败原因：生产周期和单件产能不能同时为空，请调整后再导入");
        }

        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (plmSkuPo == null) {
            throw new ParamIllegalException("导入sku生产周期关系失败，失败原因：sku不存在");
        }

        // 生产周期
        if (StringUtils.isNotBlank(cycleStr)) {
            BigDecimal cycle = ScmFormatUtil.bigDecimalFormat(cycleStr);
            if (null == cycle) {
                throw new ParamIllegalException("生产周期必须填写正确数值，请调整后再导入");
            }
            if (cycle.compareTo(BigDecimal.ZERO) < 0) {
                throw new ParamIllegalException("导入sku生产周期关系失败，失败原因：生产周期必须大于等0");
            }
            if (cycle.scale() > 2) {
                throw new ParamIllegalException("导入生产周期必须保留两位小数，请调整后再导入");
            }
            //更新
            plmSkuPo.setCycle(cycle);
            plmSkuPo.setUpdateUser(user);
            plmSkuPo.setUpdateUsername(userName);
            plmSkuDao.updateByIdVersion(plmSkuPo);
        }


        // 单件产能
        if (StringUtils.isNotBlank(singleCapacityStr)) {
            BigDecimal singleCapacity = ScmFormatUtil.bigDecimalFormat(singleCapacityStr);
            if (null == singleCapacity) {
                throw new ParamIllegalException("单件产能必须填写正确数值，请调整后再导入");
            }
            if (singleCapacity.compareTo(BigDecimal.ZERO) < 0) {
                throw new ParamIllegalException("导入单件产能必须大于等0，请调整后再导入");
            }
            if (singleCapacity.scale() > 2) {
                throw new ParamIllegalException("导入单件产能必须保留两位小数，请调整后再导入");
            }
            skuBaseService.saveSkuInfoSingleCapacity(sku, singleCapacity);
        }

    }

    /**
     * 通过sku获取绑定且开启供应商列表
     *
     * @param dto:
     * @return List<SupplierProductBindingBySkuVo>
     * @author ChenWenLong
     * @date 2024/3/7 18:36
     */
    public List<SupplierProductBindingBySkuVo> getBindingSupplierBySkuList(SkuDto dto) {
        // 获取供应商绑定关系
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareBaseService.getBindingSupplierList(SupplierBindingListQueryBo.builder()
                .skuList(List.of(dto.getSku())).build());
        List<SupplierProductBindingBySkuVo> voList = new ArrayList<>();
        supplierProductComparePoList.stream()
                .filter(supplierProductComparePo -> BooleanType.TRUE.equals(supplierProductComparePo.getSupplierProductCompareStatus()))
                .forEach(supplierProductComparePo -> {
                    SupplierProductBindingBySkuVo existVo = voList.stream()
                            .filter(vo -> vo.getSupplierCode().equals(supplierProductComparePo.getSupplierCode()))
                            .findFirst()
                            .orElse(null);
                    if (existVo == null) {
                        SupplierProductBindingBySkuVo supplierProductBindingBySkuVo = new SupplierProductBindingBySkuVo();
                        supplierProductBindingBySkuVo.setSupplierCode(supplierProductComparePo.getSupplierCode());
                        supplierProductBindingBySkuVo.setSupplierName(supplierProductComparePo.getSupplierName());
                        voList.add(supplierProductBindingBySkuVo);
                    }
                });
        return voList;
    }
}
