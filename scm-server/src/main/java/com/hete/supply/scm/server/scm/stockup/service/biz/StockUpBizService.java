package com.hete.supply.scm.server.scm.stockup.service.biz;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuImage;
import com.hete.supply.scm.api.scm.entity.dto.StockUpSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.SupplierPdcNameItemDto;
import com.hete.supply.scm.api.scm.entity.enums.StockUpOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryRecordStatus;
import com.hete.supply.scm.api.scm.entity.vo.StockUpExportVo;
import com.hete.supply.scm.api.scm.entity.vo.StockUpItemExportVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.StockUpImportDto;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.stockup.converter.StockUpConverter;
import com.hete.supply.scm.server.scm.stockup.dao.StockUpOrderDao;
import com.hete.supply.scm.server.scm.stockup.dao.StockUpOrderItemDao;
import com.hete.supply.scm.server.scm.stockup.entity.bo.StockUpCntBo;
import com.hete.supply.scm.server.scm.stockup.entity.dto.*;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderItemPo;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderPo;
import com.hete.supply.scm.server.scm.stockup.entity.vo.StockUpSearchItemVo;
import com.hete.supply.scm.server.scm.stockup.entity.vo.StockUpSearchVo;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierInventoryConverter;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryRecordDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCodeAndSkuBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.stockup.entity.dto.StockUpAcceptDto;
import com.hete.supply.scm.server.supplier.stockup.entity.dto.StockUpReturnGoodsDto;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/1/11 17:29
 */
@Service
@RequiredArgsConstructor
@Validated
public class StockUpBizService {
    private final StockUpOrderDao stockUpOrderDao;
    private final StockUpOrderItemDao stockUpOrderItemDao;
    private final SupplierInventoryDao supplierInventoryDao;
    private final SupplierInventoryRecordDao supplierInventoryRecordDao;
    private final PlmRemoteService plmRemoteService;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final SupplierBaseService supplierBaseService;
    private final SupplierDao supplierDao;
    private final static String SKU_AND_SUPPLIER_SIGN = "---";

    public CommonPageResult.PageInfo<StockUpSearchVo> searchStockUp(StockUpSearchDto dto) {
        if (null == this.getSearchStockUpWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }

        final CommonPageResult.PageInfo<StockUpSearchVo> pageInfo = stockUpOrderDao.searchStockUp(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<StockUpSearchVo> records = pageInfo.getRecords();
        final List<String> skuList = records.stream()
                .map(StockUpSearchVo::getSku)
                .collect(Collectors.toList());
        // 获取sku主图
        List<PlmSkuImage> skuImageList = plmRemoteService.getSkuImage(skuList, "");
        final Map<String, PlmSkuImage> skuImageMap = skuImageList.stream()
                .collect(Collectors.toMap(PlmSkuImage::getSkuCode, Function.identity()));

        final List<String> stockUpOrderNoList = records.stream().map(StockUpSearchVo::getStockUpOrderNo)
                .collect(Collectors.toList());
        final List<StockUpOrderItemPo> stockUpOrderItemPoList = stockUpOrderItemDao.getListByStockUpOrderNoList(stockUpOrderNoList);
        List<StockUpSearchItemVo> stockUpSearchItemVoList = SupplierInventoryConverter.itemPoListToVo(stockUpOrderItemPoList);
        final Map<String, List<StockUpSearchItemVo>> stockUpOrderNoItemPoMap = stockUpSearchItemVoList.stream()
                .collect(Collectors.groupingBy(StockUpSearchItemVo::getStockUpOrderNo));
        // 查询plm的产品名称
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        // 查询供应商产品名称
        final Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);


        records.forEach(record -> {
            final PlmSkuImage plmSkuImage = skuImageMap.get(record.getSku());
            if (null != plmSkuImage) {
                record.setFileCodeList(plmSkuImage.getFileCodeList());
                record.setSkuEncode(skuEncodeMap.get(record.getSku()));
                record.setStockUpSearchItemList(stockUpOrderNoItemPoMap.get(record.getStockUpOrderNo()));
            }
            final SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(record.getSupplierCode() + record.getSku());
            if (null != supplierProductComparePo) {
                record.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
            }
        });
        return pageInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createStockUp(StockUpCreateDto dto) {
        final List<StockUpCreateItemDto> stockUpCreateItemDtoList = dto.getStockUpCreateItemList();

        final List<SupplierCodeAndSkuBo> supplierCodeAndSkuBoList = StockUpConverter.convertCreateDtoToSupplierSkuBo(stockUpCreateItemDtoList);
        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryDao.getInventoryBySkuAndSupplier(supplierCodeAndSkuBoList);
        // 已经存在绑定关系的sku
        final List<String> inventorySkuList = supplierInventoryPoList.stream()
                .map(po -> po.getSku() + SKU_AND_SUPPLIER_SIGN + po.getSupplierCode())
                .distinct()
                .collect(Collectors.toList());
        // 用户入仓的
        final List<String> itemDtoSkuList = supplierCodeAndSkuBoList.stream()
                .map(itemDto -> itemDto.getSku() + SKU_AND_SUPPLIER_SIGN + itemDto.getSupplierCode()).distinct()
                .collect(Collectors.toList());
        List<String> notExistSkuList = itemDtoSkuList.stream()
                .filter(str -> !inventorySkuList.contains(str))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(notExistSkuList)) {
            throw new ParamIllegalException("供应商与sku绑定关系不存在，{}，请校验后再创建备货单！", notExistSkuList);
        }

        final Map<String, SupplierInventoryPo> supplierSkuInventoryPoMap = supplierInventoryPoList.stream()
                .collect(Collectors.toMap(supplierInventoryPo
                        -> supplierInventoryPo.getSupplierCode() + supplierInventoryPo.getSku(), Function.identity()));
        final List<StockUpOrderPo> stockUpOrderPoList = StockUpConverter.convertCreateDtoToStockUpPo(stockUpCreateItemDtoList, supplierSkuInventoryPoMap);

        stockUpOrderPoList.forEach(stockUpOrderPo -> {
            final String stockUpOrderNo = idGenerateService.getConfuseCode(ScmConstant.STOCK_UP_ORDER_NO, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
            stockUpOrderPo.setStockUpOrderNo(stockUpOrderNo);
        });

        stockUpOrderDao.insertBatch(stockUpOrderPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void followConfirm(StockUpFollowConfirmDto dto) {
        final List<StockUpFollowConfirmItemDto> stockUpFollowConfirmItemDtoList = dto.getStockUpFollowConfirmItemList();
        final List<String> stockUpOrderNoList = stockUpFollowConfirmItemDtoList.stream()
                .map(StockUpFollowConfirmItemDto::getStockUpOrderNo)
                .distinct()
                .collect(Collectors.toList());

        final List<StockUpOrderPo> stockUpOrderPoList = stockUpOrderDao.getListByNoList(stockUpOrderNoList);
        if (stockUpOrderNoList.size() != stockUpOrderPoList.size()) {
            throw new ParamIllegalException("数据已被更新,请刷新页面后重试!");
        }
        final Map<String, StockUpOrderPo> stockUpOrderNoPoMap = stockUpOrderPoList.stream()
                .collect(Collectors.toMap(StockUpOrderPo::getStockUpOrderNo, Function.identity()));

        stockUpFollowConfirmItemDtoList.forEach(itemDto -> {
            final StockUpOrderPo stockUpOrderPo = stockUpOrderNoPoMap.get(itemDto.getStockUpOrderNo());
            if (null == stockUpOrderPo) {
                throw new BizException("备货单数据异常，请联系系统管理员!");
            }
            stockUpOrderPo.setStockUpPrice(itemDto.getStockUpPrice());
            stockUpOrderPo.setFollowDate(LocalDateTime.now());
            stockUpOrderPo.setFollowUser(GlobalContext.getUserKey());
            stockUpOrderPo.setFollowUsername(GlobalContext.getUsername());
            stockUpOrderPo.setStockUpOrderStatus(stockUpOrderPo.getStockUpOrderStatus().toBeAccept());
            stockUpOrderPo.setFollowRemark(itemDto.getFollowRemark());
        });

        stockUpOrderDao.updateBatchByIdVersion(stockUpOrderPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void finishStockUp(StockIdAndVersionDto dto) {
        final StockUpOrderPo stockUpOrderPo = stockUpOrderDao.getByIdVersion(dto.getStockUpOrderId(), dto.getVersion());
        if (null == stockUpOrderPo) {
            throw new ParamIllegalException("数据已被更新,请刷新页面后重试!");
        }
        stockUpOrderPo.setFinishDate(LocalDateTime.now());
        stockUpOrderPo.setFinishUser(GlobalContext.getUserKey());
        stockUpOrderPo.setFinishUsername(GlobalContext.getUsername());
        stockUpOrderPo.setStockUpOrderStatus(stockUpOrderPo.getStockUpOrderStatus().toFinish());

        stockUpOrderDao.updateByIdVersion(stockUpOrderPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelStockUp(StockIdAndVersionDto dto) {
        final StockUpOrderPo stockUpOrderPo = stockUpOrderDao.getByIdVersion(dto.getStockUpOrderId(), dto.getVersion());
        if (null == stockUpOrderPo) {
            throw new ParamIllegalException("数据已被更新,请刷新页面后重试!");
        }
        stockUpOrderPo.setFinishDate(LocalDateTime.now());
        stockUpOrderPo.setFinishUser(GlobalContext.getUserKey());
        stockUpOrderPo.setFinishUsername(GlobalContext.getUsername());
        stockUpOrderPo.setStockUpOrderStatus(stockUpOrderPo.getStockUpOrderStatus().toCancel());

        stockUpOrderDao.updateByIdVersion(stockUpOrderPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void acceptStockUp(StockUpAcceptDto dto) {
        final StockUpOrderPo stockUpOrderPo = stockUpOrderDao.getByIdVersion(dto.getStockUpOrderId(), dto.getVersion());
        if (null == stockUpOrderPo) {
            throw new ParamIllegalException("数据已被更新,请刷新页面后重试!");
        }
        if (BooleanType.TRUE.equals(dto.getAcceptType())) {
            stockUpOrderPo.setStockUpOrderStatus(stockUpOrderPo.getStockUpOrderStatus().toInProcess());
        } else {
            stockUpOrderPo.setStockUpOrderStatus(stockUpOrderPo.getStockUpOrderStatus().toBeFollowConfirm());
        }


        stockUpOrderDao.updateByIdVersion(stockUpOrderPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void returnGoodsStockUp(StockUpReturnGoodsDto dto) {
        final StockUpOrderPo stockUpOrderPo = stockUpOrderDao.getByIdVersion(dto.getStockUpOrderId(), dto.getVersion());
        if (null == stockUpOrderPo) {
            throw new ParamIllegalException("数据已被更新,请刷新页面后重试!");
        }
        if (!StockUpOrderStatus.IN_PROGRESS.equals(stockUpOrderPo.getStockUpOrderStatus())) {
            throw new ParamIllegalException("当前备货单状态不处于【】,无法进行回货操作,请刷新页面后重试!",
                    StockUpOrderStatus.IN_PROGRESS.getRemark());
        }

        // 增加一个item数据
        final StockUpOrderItemPo stockUpOrderItemPo = StockUpConverter.dtoToStockUpItemPo(dto, stockUpOrderPo);
        stockUpOrderItemDao.insert(stockUpOrderItemPo);
        // 修改供应商库存数据
        final SupplierInventoryPo supplierInventoryPo = supplierInventoryDao.getInventoryBySkuAndSupplier(stockUpOrderPo.getSupplierCode(),
                stockUpOrderPo.getSku());
        if (null == supplierInventoryPo) {
            throw new BizException("查找不到对应的供应商库存数据，数据异常，请联系系统管理员!");
        }

        final SupplierInventoryRecordPo supplierInventoryRecordPo
                = StockUpConverter.convertSupplierInventoryPoToRecordPo(dto, supplierInventoryPo,
                stockUpOrderPo.getStockUpOrderNo(),
                SupplierInventoryRecordStatus.EFFECTIVE);

        final List<StockUpOrderItemPo> stockUpOrderItemPoList = stockUpOrderItemDao.getListByStockUpOrderNo(stockUpOrderPo.getStockUpOrderNo());
        final int totalReturnGoodsCnt = stockUpOrderItemPoList.stream()
                .mapToInt(StockUpOrderItemPo::getReturnGoodsCnt)
                .sum();

        // 如果回货数大于下单数，则备货单进入完结状态
        if (totalReturnGoodsCnt >= stockUpOrderPo.getPlaceOrderCnt()) {
            stockUpOrderPo.setStockUpOrderStatus(stockUpOrderPo.getStockUpOrderStatus().toFinish());
            stockUpOrderPo.setFinishDate(LocalDateTime.now());
            stockUpOrderPo.setFinishUser(GlobalContext.getUserKey());
            stockUpOrderPo.setFinishUsername(GlobalContext.getUsername());
            stockUpOrderDao.updateByIdVersion(stockUpOrderPo);
        }
        supplierInventoryDao.returnGoodsStockUp(stockUpOrderPo.getSupplierCode(), stockUpOrderPo.getSku(),
                dto.getWarehousingCnt());

        supplierInventoryRecordDao.insert(supplierInventoryRecordPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportStockUp(StockUpSearchDto dto, FileOperateBizType fileOperateBizType) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                fileOperateBizType.getCode(), dto));
    }

    public Integer getExportTotals(StockUpSearchDto dto) {
        if (null == this.getSearchStockUpWhere(dto)) {
            return 0;
        }
        return stockUpOrderDao.getExportTotals(dto);
    }

    public CommonResult<ExportationListResultBo<StockUpExportVo>> getExportList(StockUpSearchDto dto) {
        ExportationListResultBo<StockUpExportVo> resultBo = new ExportationListResultBo<>();
        if (null == this.getSearchStockUpWhere(dto)) {
            return CommonResult.success(resultBo);
        }
        final CommonPageResult.PageInfo<StockUpExportVo> pageInfo = stockUpOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);

        final List<StockUpExportVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        List<String> skuList = records.stream()
                .map(StockUpExportVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final List<String> stockUpOrderNoList = records.stream()
                .map(StockUpExportVo::getStockUpOrderNo)
                .collect(Collectors.toList());
        final List<StockUpCntBo> stockUpCntBoList = stockUpOrderItemDao.getSumCntByNoList(stockUpOrderNoList);
        final Map<String, StockUpCntBo> stockUpOrderNoCntMap = stockUpCntBoList.stream()
                .collect(Collectors.toMap(StockUpCntBo::getStockUpOrderNo, Function.identity()));

        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        // 获取供应商别名
        final List<String> supplierCodeList = records.stream()
                .map(StockUpExportVo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> supplierAliasMap = supplierDao.getMapAliasBySupplierCodeList(supplierCodeList);

        records.forEach(record -> {
            record.setStockUpOrderStatusStr(record.getStockUpOrderStatus().getRemark());
            record.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(record.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setFollowDateStr(ScmTimeUtil.localDateTimeToStr(record.getFollowDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setFinishDateStr(ScmTimeUtil.localDateTimeToStr(record.getFinishDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            final StockUpCntBo stockUpCntBo = stockUpOrderNoCntMap.get(record.getStockUpOrderNo());
            if (null != stockUpCntBo) {
                record.setWarehousingCnt(stockUpCntBo.getWarehousingCnt());
                record.setReturnGoodsCnt(stockUpCntBo.getReturnGoodsCnt());
            }
            record.setSupplierAlias(supplierAliasMap.get(record.getSupplierCode()));
        });

        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportStockUpItem(StockUpSearchDto dto, FileOperateBizType fileOperateBizType) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                fileOperateBizType.getCode(), dto));
    }

    public CommonResult<ExportationListResultBo<StockUpItemExportVo>> getItemExportList(StockUpSearchDto dto) {
        ExportationListResultBo<StockUpItemExportVo> resultBo = new ExportationListResultBo<>();
        if (null == this.getSearchStockUpWhere(dto)) {
            return CommonResult.success(resultBo);
        }
        final CommonPageResult.PageInfo<StockUpSearchVo> pageInfo = stockUpOrderDao.searchStockUp(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);

        final List<StockUpSearchVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        List<String> skuList = records.stream()
                .map(StockUpSearchVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final List<String> stockUpOrderNoList = records.stream()
                .map(StockUpSearchVo::getStockUpOrderNo)
                .collect(Collectors.toList());

        final List<StockUpOrderItemPo> stockUpOrderItemPoList = stockUpOrderItemDao.getListByStockUpOrderNoList(stockUpOrderNoList);
        if (CollectionUtils.isEmpty(stockUpOrderItemPoList)) {
            return CommonResult.success(resultBo);
        }
        final Map<String, StockUpSearchVo> stockUpOrderNoVoMap = records.stream()
                .collect(Collectors.toMap(StockUpSearchVo::getStockUpOrderNo, Function.identity()));
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        // 获取供应商别名
        final List<String> supplierCodeList = records.stream()
                .map(StockUpSearchVo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> supplierAliasMap = supplierDao.getMapAliasBySupplierCodeList(supplierCodeList);

        final List<StockUpItemExportVo> stockUpItemExportVoList = stockUpOrderItemPoList.stream()
                .map(itemPo -> {
                    final StockUpSearchVo stockUpSearchVo = stockUpOrderNoVoMap.get(itemPo.getStockUpOrderNo());

                    final StockUpItemExportVo stockUpItemExportVo = new StockUpItemExportVo();
                    stockUpItemExportVo.setStockUpOrderNo(itemPo.getStockUpOrderNo());
                    stockUpItemExportVo.setSku(itemPo.getSku());
                    stockUpItemExportVo.setSkuEncode(skuEncodeMap.get(itemPo.getSku()));
                    stockUpItemExportVo.setCategoryName(stockUpSearchVo.getCategoryName());
                    stockUpItemExportVo.setStockUpOrderStatusStr(stockUpSearchVo.getStockUpOrderStatus().getRemark());
                    stockUpItemExportVo.setWarehousingCnt(itemPo.getWarehousingCnt());
                    stockUpItemExportVo.setReturnGoodsCnt(itemPo.getReturnGoodsCnt());
                    stockUpItemExportVo.setReturnGoodsDateStr(ScmTimeUtil.localDateTimeToStr(itemPo.getReturnGoodsDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    stockUpItemExportVo.setSupplierAlias(supplierAliasMap.get(stockUpSearchVo.getSupplierCode()));
                    return stockUpItemExportVo;
                }).collect(Collectors.toList());

        resultBo.setRowDataList(stockUpItemExportVoList);
        return CommonResult.success(resultBo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void importCreateStockUp(StockUpImportDto dto) {
        final LocalDateTime localDateTime = ScmTimeUtil.dateStrToLocalDateTime(dto.getRequestReturnGoodsDateStr(), DatePattern.NORM_DATETIME_PATTERN);
        final LocalDateTime requestReturnGoodsDate = ScmTimeUtil.getLastSecondTimeOfDayForTime(localDateTime);
        if (null == requestReturnGoodsDate) {
            throw new ParamIllegalException("填写回货时间错误，请重新填写后再操作！");
        }
        if (requestReturnGoodsDate.isBefore(LocalDateTime.now())) {
            throw new ParamIllegalException("回货时间不能选择当前日期之前，请重新填写后再操作！");
        }
        dto.setRequestReturnGoodsDate(requestReturnGoodsDate);

        final SupplierPo supplierPo = supplierBaseService.getSupplierByCode(dto.getSupplierCode());
        if (null == supplierPo) {
            throw new ParamIllegalException("查找不到对应的供应商:{}", dto.getSupplierCode());
        }

        final SupplierInventoryPo supplierInventoryPo = supplierInventoryDao.getInventoryBySkuAndSupplier(dto.getSupplierCode(), dto.getSku());
        if (null == supplierInventoryPo) {
            throw new ParamIllegalException("供应商:{}与sku:{}绑定关系不存在，请校验后再创建备货单！",
                    dto.getSupplierCode(), dto.getSku());
        }

        if (null == dto.getPlaceOrderCnt() || dto.getPlaceOrderCnt() <= 0) {
            throw new ParamIllegalException("下单数只能填写正整数，请校验后再创建备货单！");
        }
        final StockUpOrderPo stockUpOrderPo = new StockUpOrderPo();
        stockUpOrderPo.setStockUpOrderStatus(StockUpOrderStatus.TO_BE_FOLLOW_CONFIRM);
        stockUpOrderPo.setSku(supplierInventoryPo.getSku());
        stockUpOrderPo.setCategoryName(supplierInventoryPo.getCategoryName());
        stockUpOrderPo.setPlaceOrderCnt(dto.getPlaceOrderCnt());
        stockUpOrderPo.setRequestReturnGoodsDate(dto.getRequestReturnGoodsDate());
        stockUpOrderPo.setSupplierCode(supplierInventoryPo.getSupplierCode());
        stockUpOrderPo.setSupplierName(supplierInventoryPo.getSupplierName());
        final String stockUpOrderNo = idGenerateService.getConfuseCode(ScmConstant.STOCK_UP_ORDER_NO, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
        stockUpOrderPo.setStockUpOrderNo(stockUpOrderNo);

        stockUpOrderDao.insert(stockUpOrderPo);
    }

    /**
     * 查询导出的条件
     *
     * @param dto:
     * @return StockUpSearchDto
     * @author ChenWenLong
     * @date 2024/5/5 13:59
     */
    public StockUpSearchDto getSearchStockUpWhere(StockUpSearchDto dto) {
        //产品名称
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }
        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }
        // 供应商产品名称
        if (StringUtils.isNotBlank(dto.getSupplierProductName())) {
            final List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getListBySupplierProductName(dto.getSupplierProductName());
            if (CollectionUtils.isEmpty(supplierProductComparePoList)) {
                return null;
            }
            final List<SupplierPdcNameItemDto> supplierPdcNameItemList = supplierProductComparePoList.stream().map(po -> {
                final SupplierPdcNameItemDto supplierPdcNameItemDto = new SupplierPdcNameItemDto();
                supplierPdcNameItemDto.setSupplierCode(po.getSupplierCode());
                supplierPdcNameItemDto.setSku(po.getSku());
                return supplierPdcNameItemDto;
            }).collect(Collectors.toList());
            dto.setSupplierPdcNameItemList(supplierPdcNameItemList);
            if (CollectionUtils.isEmpty(dto.getSupplierPdcNameItemList())) {
                return null;
            }
        }
        return dto;
    }
}
