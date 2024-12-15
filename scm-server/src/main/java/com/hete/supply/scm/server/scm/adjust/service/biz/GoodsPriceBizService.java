package com.hete.supply.scm.server.scm.adjust.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsPlatVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmNormalSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.GoodsPriceDto;
import com.hete.supply.scm.api.scm.entity.enums.GoodsPriceMaintain;
import com.hete.supply.scm.api.scm.entity.vo.GoodsPriceExportVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.GoodsPriceImportationDto;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.server.scm.adjust.converter.GoodsPriceConverter;
import com.hete.supply.scm.server.scm.adjust.dao.ChannelDao;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceApproveDao;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceDao;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceItemDao;
import com.hete.supply.scm.server.scm.adjust.entity.bo.AdjustApproveBo;
import com.hete.supply.scm.server.scm.adjust.entity.bo.GoodsAdjustDetailItemBo;
import com.hete.supply.scm.server.scm.adjust.entity.bo.GoodsPriceImportBo;
import com.hete.supply.scm.server.scm.adjust.entity.dto.*;
import com.hete.supply.scm.server.scm.adjust.entity.po.*;
import com.hete.supply.scm.server.scm.adjust.entity.vo.*;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveType;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceEffectiveStatus;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceItemStatus;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceUniversal;
import com.hete.supply.scm.server.scm.adjust.service.base.AdjustApproveBaseService;
import com.hete.supply.scm.server.scm.adjust.service.base.GoodsPriceBaseService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.dto.SkuDto;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.enums.DefaultDatabaseTime;
import com.hete.supply.scm.server.scm.feishu.config.FeiShuConfig;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierProductCompareRefService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ChenWenLong
 * @date 2024/6/19 10:01
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsPriceBizService {

    private final GoodsPriceDao goodsPriceDao;
    private final PlmSkuDao plmSkuDao;
    private final PlmRemoteService plmRemoteService;
    private final ConsistencySendMqService consistencySendMqService;
    private final GoodsPriceItemDao goodsPriceItemDao;
    private final ChannelDao channelDao;
    private final GoodsPriceApproveDao goodsPriceApproveDao;
    private final AdjustApproveBaseService adjustApproveBaseService;
    private final IdGenerateService idGenerateService;
    private final SupplierDao supplierDao;
    private final SkuInfoDao skuInfoDao;
    private final GoodsPriceBaseService goodsPriceBaseService;
    private final UdbRemoteService udbRemoteService;
    private final FeiShuConfig feiShuConfig;
    private final SupplierProductCompareRefService supplierProductCompareRefService;

    /**
     * 列表查询
     *
     * @param dto:
     * @return PageInfo<GoodsPriceSearchVo>
     * @author ChenWenLong
     * @date 2024/6/19 10:47
     */
    public CommonPageResult.PageInfo<GoodsPriceSearchVo> searchGoodsPrice(GoodsPriceDto dto) {
        //条件过滤
        if (null == this.getSearchGoodsPriceWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        CommonPageResult.PageInfo<GoodsPriceSearchVo> pageResult = plmSkuDao.searchGoodsPricePage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<GoodsPriceSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }
        List<String> skuList = records.stream().map(GoodsPriceSearchVo::getSku).collect(Collectors.toList());

        //查询产品名称
        final List<PlmNormalSkuVo> plmNormalSkuVoList = plmRemoteService.getSkuInfoBySkuList(skuList, null);

        //查询渠道价格
        List<GoodsPricePo> goodsPricePoList = goodsPriceDao.getListBySkuList(skuList);
        Map<String, List<GoodsPricePo>> goodsPricePoMap = goodsPricePoList.stream()
                .collect(Collectors.groupingBy(GoodsPricePo::getSku));

        // 查询是否存在审批中
        List<Long> goodsPriceIdList = goodsPricePoList.stream().map(GoodsPricePo::getGoodsPriceId).collect(Collectors.toList());
        List<GoodsPriceItemPo> goodsPriceItemPoList = goodsPriceItemDao.getListByGoodsPriceIdListOrStatus(goodsPriceIdList, List.of(GoodsPriceItemStatus.TO_BE_APPROVE));

        // 查询维护状态
        List<SkuInfoPo> skuInfoPoList = skuInfoDao.getListBySkuList(skuList);

        records.forEach(record -> {
            plmNormalSkuVoList.stream()
                    .filter(skuInfoBySku -> skuInfoBySku.getSkuCode().equals(record.getSku()))
                    .findFirst()
                    .ifPresent(skuInfoBySku -> {
                        record.setSkuEncode(skuInfoBySku.getSkuEncode());
                        record.setSkuDevType(skuInfoBySku.getSkuDevType());
                        List<String> platNameList = Optional.ofNullable(skuInfoBySku.getGoodsPlatVoList())
                                .orElse(new ArrayList<>())
                                .stream()
                                .map(PlmGoodsPlatVo::getPlatName)
                                .distinct()
                                .collect(Collectors.toList());
                        record.setPlatNameList(platNameList);

                    });

            List<GoodsPricePo> goodsPricePos = Optional.ofNullable(goodsPricePoMap.get(record.getSku())).orElse(new ArrayList<>());
            // 是否维护
            List<GoodsPriceInfoVo> goodsPriceInfoList = GoodsPriceConverter.listPoToListVo(goodsPricePos.stream().filter(goodsPricePo -> null != goodsPricePo.getEffectiveTime()).collect(Collectors.toList()));
            record.setGoodsPriceInfoList(goodsPriceInfoList);
            record.setGoodsPriceMaintain(GoodsPriceMaintain.WAITING_MAINTAIN);
            SkuInfoPo skuInfoPo = skuInfoPoList.stream()
                    .filter(po -> po.getSku().equals(record.getSku()))
                    .findFirst()
                    .orElse(null);
            if (null != skuInfoPo && GoodsPriceMaintain.MAINTAIN.equals(skuInfoPo.getGoodsPriceMaintain())) {
                record.setGoodsPriceMaintain(skuInfoPo.getGoodsPriceMaintain());
            }

            // 判断是否存在审批中的审批单
            record.setIsApproval(BooleanType.FALSE);
            for (GoodsPricePo goodsPricePo : goodsPricePos) {
                goodsPriceItemPoList.stream()
                        .filter(goodsPriceItemPo -> goodsPricePo.getGoodsPriceId().equals(goodsPriceItemPo.getGoodsPriceId()))
                        .findFirst()
                        .ifPresent(goodsPriceItemPo -> record.setIsApproval(BooleanType.TRUE));
            }
        });

        return pageResult;
    }

    /**
     * 列表查询条件
     *
     * @param dto:
     * @return GoodsPriceDto
     * @author ChenWenLong
     * @date 2024/6/19 10:47
     */
    private GoodsPriceDto getSearchGoodsPriceWhere(GoodsPriceDto dto) {
        if (null != dto.getCategoryId()) {
            List<String> skuCategoryList = plmRemoteService.getSkuByCategoryId(dto.getCategoryId());
            if (CollectionUtils.isEmpty(skuCategoryList)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuCategoryList);
            } else {
                dto.getSkuList().retainAll(skuCategoryList);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }

        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            final List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
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

        if (dto.getGoodsPriceMaintainList() != null && dto.getGoodsPriceMaintainList().size() == 1 && dto.getGoodsPriceMaintainList().get(0).equals(GoodsPriceMaintain.MAINTAIN)) {
            List<SkuInfoPo> skuInfoPoList = skuInfoDao.getListByGoodsPriceMaintain(GoodsPriceMaintain.MAINTAIN);
            if (CollectionUtil.isEmpty(skuInfoPoList)) {
                return null;
            }
            List<String> skuInfoList = skuInfoPoList.stream().map(SkuInfoPo::getSku).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuInfoList);
            } else {
                dto.getSkuList().retainAll(skuInfoList);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }

        if (dto.getGoodsPriceMaintainList() != null && dto.getGoodsPriceMaintainList().size() == 1 && dto.getGoodsPriceMaintainList().get(0).equals(GoodsPriceMaintain.WAITING_MAINTAIN)) {
            List<SkuInfoPo> skuInfoPoList = skuInfoDao.getListByGoodsPriceMaintain(GoodsPriceMaintain.MAINTAIN);
            if (CollectionUtil.isNotEmpty(skuInfoPoList)) {
                List<String> skuInfoList = skuInfoPoList.stream().map(SkuInfoPo::getSku).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(dto.getNotSkuList())) {
                    dto.setNotSkuList(skuInfoList);
                } else {
                    dto.getNotSkuList().retainAll(skuInfoList);
                }
                if (CollectionUtils.isEmpty(dto.getNotSkuList())) {
                    return null;
                }
            }
        }

        return dto;
    }

    /**
     * 商品价格管理列表导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/19 14:15
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportGoodsPrice(GoodsPriceDto dto) {
        //条件过滤
        if (null == this.getSearchGoodsPriceWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        // 这个条件是过滤生效时间为空的数据
        dto.setEffectiveTimeGt(LocalDateTime.of(1970, 1, 1, 10, 0, 0));
        Integer exportTotals = plmSkuDao.getGoodsPriceExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                GlobalContext.getUsername(), FileOperateBizType.SCM_GOODS_PRICE_EXPORT.getCode(), dto));
    }

    /**
     * 商品价格管理列表统计
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/6/19 14:15
     */
    public Integer getExportTotals(GoodsPriceDto dto) {
        //条件过滤
        if (null == this.getSearchGoodsPriceWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = plmSkuDao.getGoodsPriceExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        return exportTotals;
    }

    /**
     * 商品价格管理列表导出数据
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < GoodsPriceExportVo>>
     * @author ChenWenLong
     * @date 2024/6/19 14:16
     */
    public CommonResult<ExportationListResultBo<GoodsPriceExportVo>> getExportList(GoodsPriceDto dto) {
        ExportationListResultBo<GoodsPriceExportVo> resultBo = new ExportationListResultBo<>();

        CommonPageResult.PageInfo<GoodsPriceExportVo> exportList = plmSkuDao.getGoodsPriceExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<GoodsPriceExportVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<String> skuList = records.stream().map(GoodsPriceExportVo::getSku).collect(Collectors.toList());

        //查询产品名称
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        for (GoodsPriceExportVo record : records) {
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            // 时间转换
            record.setEffectiveTimeStr(ScmTimeUtil.localDateTimeToStr(record.getEffectiveTime(), TimeZoneId.CN, DatePattern.NORM_DATE_PATTERN));
        }

        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    /**
     * 商品价格日志列表
     *
     * @param dto:
     * @return List<GoodsPriceItemVo>
     * @author ChenWenLong
     * @date 2024/6/19 14:58
     */
    public List<GoodsPriceItemVo> searchGoodsPriceItem(SkuDto dto) {
        //查询渠道价格
        List<GoodsPricePo> goodsPricePoList = goodsPriceDao.getListBySku(dto.getSku());
        List<Long> goodsPriceIdList = goodsPricePoList.stream().map(GoodsPricePo::getGoodsPriceId).collect(Collectors.toList());

        // 查询商品价格审批单
        List<GoodsPriceItemPo> goodsPriceItemPoList = goodsPriceItemDao.getListByGoodsPriceIdListOrStatus(goodsPriceIdList, List.of(GoodsPriceItemStatus.APPROVE_PASSED));

        return goodsPricePoList.stream().map(goodsPricePo -> {
            GoodsPriceItemVo goodsPriceItemVo = new GoodsPriceItemVo();
            goodsPriceItemVo.setSupplierCode(goodsPricePo.getSupplierCode());
            goodsPriceItemVo.setChannelId(goodsPricePo.getChannelId());
            goodsPriceItemVo.setChannelName(goodsPricePo.getChannelName());
            goodsPriceItemVo.setGoodsPriceUniversal(goodsPricePo.getGoodsPriceUniversal());
            List<GoodsPriceApproveVo> goodsPriceApproveList = goodsPriceItemPoList.stream()
                    .filter(goodsPriceItemPo -> goodsPriceItemPo.getGoodsPriceId().equals(goodsPricePo.getGoodsPriceId()))
                    .map(goodsPriceItemPo -> {
                        GoodsPriceApproveVo goodsPriceApproveVo = new GoodsPriceApproveVo();
                        goodsPriceApproveVo.setCreateUser(goodsPriceItemPo.getCreateUser());
                        goodsPriceApproveVo.setCreateUsername(goodsPriceItemPo.getCreateUsername());
                        goodsPriceApproveVo.setChannelPrice(goodsPriceItemPo.getChannelPrice());
                        goodsPriceApproveVo.setEffectiveTime(goodsPriceItemPo.getEffectiveTime());
                        goodsPriceApproveVo.setEffectiveRemark(goodsPriceItemPo.getEffectiveRemark());
                        goodsPriceApproveVo.setGoodsPriceEffectiveStatus(goodsPriceItemPo.getGoodsPriceEffectiveStatus());
                        return goodsPriceApproveVo;
                    }).collect(Collectors.toList());
            goodsPriceItemVo.setGoodsPriceApproveList(goodsPriceApproveList);
            return goodsPriceItemVo;
        }).collect(Collectors.toList());

    }

    /**
     * 批量调价
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/20 11:26
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchGoodsPrice(GoodsPriceBatchDto dto) {
        List<GoodsPriceBatchDto.GoodsPriceItemBatchDto> goodsPriceItemBatchList = Optional.ofNullable(dto.getGoodsPriceItemBatchList())
                .orElse(new ArrayList<>());

        List<String> skuList = goodsPriceItemBatchList
                .stream()
                .map(GoodsPriceBatchDto.GoodsPriceItemBatchDto::getSku)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Long> goodsPriceItemIdList = goodsPriceItemBatchList
                .stream()
                .map(GoodsPriceBatchDto.GoodsPriceItemBatchDto::getGoodsPriceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Long> channelIdList = goodsPriceItemBatchList
                .stream()
                .map(GoodsPriceBatchDto.GoodsPriceItemBatchDto::getChannelId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<ChannelPo> channelPoList = channelDao.getByIdList(channelIdList);
        for (ChannelPo channelPo : channelPoList) {
            Assert.isTrue(BooleanType.TRUE.equals(channelPo.getChannelStatus()), () -> new ParamIllegalException("渠道：{}已关闭，请先到渠道设置中开启后重试！", channelPo.getChannelName()));
        }
        Map<Long, ChannelPo> channelPoMap = channelPoList.stream()
                .collect(Collectors.toMap(ChannelPo::getChannelId, channelPo -> channelPo));

        // 验证入参goods_price_id数据是否正确,防止数据库已存在但前端不传过来
        List<GoodsPricePo> goodsPricePoVerifyList = goodsPriceDao.getListBySkuListAndSupplierCode(skuList, dto.getSupplierCode());
        for (GoodsPricePo goodsPricePo : goodsPricePoVerifyList) {
            GoodsPriceBatchDto.GoodsPriceItemBatchDto goodsPriceItemBatchDto = goodsPriceItemBatchList.stream()
                    .filter(item -> item.getSku().equals(goodsPricePo.getSku()) && item.getChannelId().equals(goodsPricePo.getChannelId()))
                    .findFirst()
                    .orElse(null);
            if (null != goodsPriceItemBatchDto && !goodsPricePo.getGoodsPriceId().equals(goodsPriceItemBatchDto.getGoodsPriceId())) {
                throw new BizException("数据已被修改或删除，请刷新页面后重试！");
            }
        }


        //查询渠道价格
        List<GoodsPricePo> goodsPricePoList = goodsPriceDao.getListByIdList(goodsPriceItemIdList);
        Map<Long, GoodsPricePo> goodsPricePoMap = goodsPricePoList.stream()
                .collect(Collectors.toMap(GoodsPricePo::getGoodsPriceId, goodsPricePo -> goodsPricePo));


        // 查询是否存在审批中
        List<Long> goodsPriceIdList = goodsPricePoList.stream().map(GoodsPricePo::getGoodsPriceId).collect(Collectors.toList());
        List<GoodsPriceItemPo> goodsPriceItemPoList = goodsPriceItemDao.getListByGoodsPriceIdListOrStatus(goodsPriceIdList, List.of(GoodsPriceItemStatus.TO_BE_APPROVE));
        if (CollectionUtils.isNotEmpty(goodsPriceItemPoList)) {
            throw new ParamIllegalException("sku：{}有关联的审批单在进行审批，无法进行调价，请前去完成审批后再处理！", goodsPriceItemPoList.stream().map(GoodsPriceItemPo::getSku).collect(Collectors.joining(",")));
        }

        // 获取产品名称
        Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        // 查询供应商信息
        SupplierPo supplierPo = supplierDao.getBySupplierCode(dto.getSupplierCode());
        if (supplierPo == null) {
            throw new BizException("供应商查询不到信息");
        }

        if (StringUtils.isBlank(supplierPo.getFollowUser())) {
            throw new ParamIllegalException("供应商代码：{}的跟单采购员为空，请先设置供应商的跟单采购员！", supplierPo.getSupplierCode());
        }

        if (StringUtils.isBlank(supplierPo.getFollowUsername())) {
            throw new BizException("供应商代码：{}的跟单采购员为空，请先设置供应商的跟单采购员！", supplierPo.getSupplierCode());
        }

        UserVo userVo = udbRemoteService.getByUserCode(supplierPo.getFollowUser());

        if (null == userVo) {
            throw new BizException("供应商代码：{}的跟单采购员：{}查询不到对应用户信息，请先联系管理员！",
                    supplierPo.getSupplierCode(),
                    supplierPo.getFollowUsername());
        }

        if (StringUtils.isBlank(userVo.getFeiShuOpenId())) {
            throw new BizException("供应商代码：{}的跟单采购员：{}查询不到对应用户的飞书OpenId信息，请先联系管理员！",
                    supplierPo.getSupplierCode(),
                    supplierPo.getFollowUsername());
        }


        //创建操作详情记录PO
        List<GoodsPriceItemPo> goodsPriceItemPoInsertList = new ArrayList<>();
        List<GoodsPricePo> goodsPricePoInsertList = new ArrayList<>();
        for (GoodsPriceBatchDto.GoodsPriceItemBatchDto goodsPriceItemBatchDto : goodsPriceItemBatchList) {
            ChannelPo channelPo = channelPoMap.get(goodsPriceItemBatchDto.getChannelId());
            if (null == channelPo) {
                throw new BizException("sku：{}的入参错误，查询不到对应渠道信息，请联系管理员！", goodsPriceItemBatchDto.getSku());
            }

            GoodsPricePo goodsPricePo = goodsPricePoMap.get(goodsPriceItemBatchDto.getGoodsPriceId());
            if (null == goodsPricePo) {
                goodsPricePo = new GoodsPricePo();
                // 雪花id
                long goodsPriceId = idGenerateService.getSnowflakeId();
                goodsPricePo.setGoodsPriceId(goodsPriceId);
                goodsPricePo.setSku(goodsPriceItemBatchDto.getSku());
                goodsPricePo.setSupplierCode(dto.getSupplierCode());
                goodsPricePo.setChannelId(channelPo.getChannelId());
                goodsPricePo.setChannelName(channelPo.getChannelName());
                goodsPricePo.setChannelPrice(BigDecimal.ZERO);
                goodsPricePoInsertList.add(goodsPricePo);
            }

            GoodsPriceItemPo goodsPriceItemPo = new GoodsPriceItemPo();
            long goodsPriceItemId = idGenerateService.getSnowflakeId();
            goodsPriceItemPo.setGoodsPriceItemId(goodsPriceItemId);
            goodsPriceItemPo.setGoodsPriceId(goodsPricePo.getGoodsPriceId());
            goodsPriceItemPo.setSku(goodsPriceItemBatchDto.getSku());
            goodsPriceItemPo.setOriginalPrice(goodsPricePo.getChannelPrice());
            goodsPriceItemPo.setChannelPrice(goodsPriceItemBatchDto.getChannelPrice());
            goodsPriceItemPo.setEffectiveTime(goodsPriceItemBatchDto.getEffectiveTime());
            goodsPriceItemPo.setEffectiveRemark(goodsPriceItemBatchDto.getEffectiveRemark());
            goodsPriceItemPo.setGoodsPriceUniversal(goodsPriceItemBatchDto.getGoodsPriceUniversal());

            goodsPriceItemPo.setGoodsPriceItemStatus(GoodsPriceItemStatus.TO_BE_APPROVE);
            goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.WAIT_EXAMINE);
            goodsPriceItemPoInsertList.add(goodsPriceItemPo);
        }

        // 创建审批流
        AdjustApproveBo adjustApproveBo = new AdjustApproveBo();
        adjustApproveBo.setApproveUser(supplierPo.getFollowUser());
        adjustApproveBo.setApproveUsername(supplierPo.getFollowUsername());
        adjustApproveBo.setSupplierCode(dto.getSupplierCode());
        adjustApproveBo.setApproveType(ApproveType.GOODS_ADJUST);
        List<GoodsAdjustDetailItemBo> goodsAdjustList = new ArrayList<>();
        for (GoodsPriceItemPo goodsPriceItemPo : goodsPriceItemPoInsertList) {
            GoodsAdjustDetailItemBo goodsAdjustDetailItemBo = new GoodsAdjustDetailItemBo();
            GoodsPricePo goodsPricePo = goodsPricePoMap.get(goodsPriceItemPo.getGoodsPriceId());
            if (null == goodsPricePo) {
                goodsPricePo = goodsPricePoInsertList.stream()
                        .filter(po -> po.getGoodsPriceId().equals(goodsPriceItemPo.getGoodsPriceId()))
                        .findFirst()
                        .orElse(null);
                if (null == goodsPricePo) {
                    throw new BizException("查询不到对应创建商品调价信息，请联系管理员！");
                }
            }
            ChannelPo channelPo = channelPoMap.get(goodsPricePo.getChannelId());
            if (null == channelPo) {
                throw new BizException("sku：{}的入参错误，查询不到对应渠道信息，请联系管理员！", goodsPriceItemPo.getSku());
            }
            goodsAdjustDetailItemBo.setGoodsPriceItemId(goodsPriceItemPo.getGoodsPriceItemId());
            goodsAdjustDetailItemBo.setSku(goodsPriceItemPo.getSku());
            goodsAdjustDetailItemBo.setSkuEncode(skuEncodeMap.get(goodsPriceItemPo.getSku()));
            goodsAdjustDetailItemBo.setSupplierCode(goodsPricePo.getSupplierCode());
            goodsAdjustDetailItemBo.setChannel(channelPo.getChannelName());
            goodsAdjustDetailItemBo.setOriginalPrice(goodsPricePo.getChannelPrice());
            goodsAdjustDetailItemBo.setOriginalPriceStr(ScmFormatUtil.convertToThousandFormat(goodsPricePo.getChannelPrice()));

            goodsAdjustDetailItemBo.setAdjustPrice(goodsPriceItemPo.getChannelPrice());
            goodsAdjustDetailItemBo.setAdjustPriceStr(ScmFormatUtil.convertToThousandFormat(goodsPriceItemPo.getChannelPrice()));
            goodsAdjustDetailItemBo.setEffectiveTime(goodsPriceItemPo.getEffectiveTime());
            if (null != goodsPriceItemPo.getEffectiveTime()) {
                LocalDateTime effectiveTimeStr = TimeUtil.convertZone(goodsPriceItemPo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN);
                goodsAdjustDetailItemBo.setEffectiveTimeStr(effectiveTimeStr.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            goodsAdjustDetailItemBo.setEffectiveRemark(goodsPriceItemPo.getEffectiveRemark());
            goodsAdjustDetailItemBo.setUniversal(goodsPriceItemPo.getGoodsPriceUniversal().getRemark());
            goodsAdjustList.add(goodsAdjustDetailItemBo);
        }
        // 设置审批人
        adjustApproveBo.setNodeApproverUserCodeList(List.of(supplierPo.getFollowUser()));
        adjustApproveBo.setGoodsAdjustList(goodsAdjustList);
        log.info("创建审批流bo={}", JacksonUtil.parse2Str(adjustApproveBo));
        AdjustPriceApprovePo adjustPriceApprovePo = adjustApproveBaseService.submitForApproval(adjustApproveBo);

        //创建审批单记录PO
        GoodsPriceApprovePo goodsPriceApprovePo = new GoodsPriceApprovePo();
        goodsPriceApprovePo.setSupplierCode(dto.getSupplierCode());
        goodsPriceApprovePo.setAdjustPriceApproveNo(adjustPriceApprovePo.getAdjustPriceApproveNo());
        goodsPriceApproveDao.insert(goodsPriceApprovePo);


        for (GoodsPriceItemPo goodsPriceItemPo : goodsPriceItemPoInsertList) {
            goodsPriceItemPo.setAdjustPriceApproveNo(adjustPriceApprovePo.getAdjustPriceApproveNo());
        }
        goodsPriceItemDao.insertBatch(goodsPriceItemPoInsertList);
        goodsPriceDao.insertBatch(goodsPricePoInsertList);


    }

    /**
     * 自动更新商品调价的生效时间任务
     *
     * @param :
     * @return void
     * @author ChenWenLong
     * @date 2024/6/20 16:10
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsPriceEffectiveTask(String param) {

        // 获取当前CN的时间
        LocalDateTime localDateTimeNow = TimeUtil.now(TimeZoneId.CN);
        // 获取今天的日期
        LocalDate today = localDateTimeNow.toLocalDate();
        // 如果存在入参就以入参时间作为标准
        if (StringUtils.isNotBlank(param)) {
            LocalDateTime localDateTimeParam = ScmTimeUtil.strToLocalDateTime(param);
            if (null == localDateTimeParam) {
                throw new BizException("自动更新商品调价的生效时间任务的入参错误，请联系系统管理员！");
            }
            today = localDateTimeParam.toLocalDate();
        }

        // 查询条件时间
        LocalDateTime effectiveWhereTimeStart = LocalDateTime.of(today, LocalTime.MIDNIGHT);
        LocalDateTime effectiveWhereTimeEnd = LocalDateTime.of(today, LocalTime.MAX);

        List<GoodsPriceItemPo> goodsPriceItemPos = goodsPriceItemDao.getListByEffectiveTimeAndStatusList(
                TimeUtil.convertZone(effectiveWhereTimeStart, TimeZoneId.CN, TimeZoneId.UTC),
                TimeUtil.convertZone(effectiveWhereTimeEnd, TimeZoneId.CN, TimeZoneId.UTC),
                List.of(GoodsPriceItemStatus.APPROVE_PASSED),
                List.of(GoodsPriceEffectiveStatus.WAIT_EFFECTIVE));
        if (CollectionUtils.isEmpty(goodsPriceItemPos)) {
            log.info("时间：查询不到对应更新商品调价的生效时间");
            return;
        }

        LocalDate finalToday = today;
        List<GoodsPriceItemPo> goodsPriceItemPoList = goodsPriceItemPos.stream()
                .filter(po -> po.getEffectiveTime() != null && TimeUtil.convertZone(po.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate().isEqual(finalToday))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(goodsPriceItemPoList)) {
            log.info("时间：查询不到对应更新商品调价的生效时间");
            return;
        }

        log.info("更新商品调价的生效时间的itemID={}",
                JacksonUtil.parse2Str(goodsPriceItemPoList.stream()
                        .map(GoodsPriceItemPo::getGoodsPriceItemId)
                        .collect(Collectors.toList())));

        List<Long> goodsPriceIdList = goodsPriceItemPoList.stream().map(GoodsPriceItemPo::getGoodsPriceId)
                .distinct()
                .collect(Collectors.toList());
        List<GoodsPricePo> goodsPricePoList = goodsPriceDao.getListByIdList(goodsPriceIdList);
        Map<Long, GoodsPricePo> goodsPricePoMap = goodsPricePoList.stream()
                .collect(Collectors.toMap(GoodsPricePo::getGoodsPriceId, goodsPricePo -> goodsPricePo));

        List<GoodsPricePo> updateGoodsPricePoList = new ArrayList<>();
        List<GoodsPriceItemPo> updateGoodsPriceItemPoList = new ArrayList<>();
        for (GoodsPriceItemPo goodsPriceItemPo : goodsPriceItemPoList) {
            GoodsPricePo goodsPricePo = goodsPricePoMap.get(goodsPriceItemPo.getGoodsPriceId());
            if (null != goodsPricePo) {
                GoodsPricePo updateGoodsPricePo = new GoodsPricePo();
                updateGoodsPricePo.setGoodsPriceId(goodsPricePo.getGoodsPriceId());
                updateGoodsPricePo.setSku(goodsPricePo.getSku());
                updateGoodsPricePo.setSupplierCode(goodsPricePo.getSupplierCode());
                updateGoodsPricePo.setChannelPrice(goodsPriceItemPo.getChannelPrice());
                updateGoodsPricePo.setEffectiveTime(goodsPriceItemPo.getEffectiveTime());
                updateGoodsPricePo.setEffectiveRemark(goodsPriceItemPo.getEffectiveRemark());
                updateGoodsPricePo.setGoodsPriceUniversal(goodsPriceItemPo.getGoodsPriceUniversal());
                updateGoodsPricePoList.add(updateGoodsPricePo);
                // 打上生效标签
                GoodsPriceItemPo updateGoodsPriceItemPo = new GoodsPriceItemPo();
                updateGoodsPriceItemPo.setGoodsPriceItemId(goodsPriceItemPo.getGoodsPriceItemId());
                updateGoodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.EFFECTIVE);
                updateGoodsPriceItemPoList.add(updateGoodsPriceItemPo);
            }
        }
        if (CollectionUtils.isNotEmpty(updateGoodsPriceItemPoList)) {
            goodsPriceItemDao.updateBatchById(updateGoodsPriceItemPoList);
            // 打上失效标签
            goodsPriceBaseService.updateBatchGoodsPriceItemInvalid(updateGoodsPriceItemPoList);
        }
        if (CollectionUtils.isNotEmpty(updateGoodsPricePoList)) {
            goodsPriceBaseService.updateGoodsPriceUniversal(updateGoodsPricePoList);
            // 更新sku维护关系
            goodsPriceBaseService.updateGoodsPriceMaintain(updateGoodsPricePoList);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void importGoodsPrice(GoodsPriceImportationDto dto) {
        log.info("批量导入商品价格入参dto={}", dto);
        // 去掉两边空格
        String sku = StringUtils.isNotBlank(dto.getSku()) ? dto.getSku().trim() : dto.getSku();
        String supplierCode = StringUtils.isNotBlank(dto.getSupplierCode()) ? dto.getSupplierCode().trim() : dto.getSupplierCode();
        String universalChannelName = StringUtils.isNotBlank(dto.getUniversalChannelName()) ? dto.getUniversalChannelName().trim() : dto.getUniversalChannelName();
        List<GoodsPriceImportBo> goodsPriceImportBoList = Stream.of(
                        new GoodsPriceImportBo(dto.getChannelName1(), dto.getChannelPrice1()), new GoodsPriceImportBo(dto.getChannelName2(), dto.getChannelPrice2()),
                        new GoodsPriceImportBo(dto.getChannelName3(), dto.getChannelPrice3()), new GoodsPriceImportBo(dto.getChannelName4(), dto.getChannelPrice4()),
                        new GoodsPriceImportBo(dto.getChannelName5(), dto.getChannelPrice5()), new GoodsPriceImportBo(dto.getChannelName6(), dto.getChannelPrice6()),
                        new GoodsPriceImportBo(dto.getChannelName7(), dto.getChannelPrice7()), new GoodsPriceImportBo(dto.getChannelName8(), dto.getChannelPrice8()),
                        new GoodsPriceImportBo(dto.getChannelName9(), dto.getChannelPrice9()), new GoodsPriceImportBo(dto.getChannelName10(), dto.getChannelPrice10()))
                .filter(bo -> StringUtils.isNotBlank(bo.getChannelName()) && StringUtils.isNotBlank(bo.getChannelPrice()))
                .collect(Collectors.toList());

        // 校验入参
        if (StringUtils.isBlank(sku)) {
            throw new ParamIllegalException("SKU不能为空");
        }
        if (StringUtils.isBlank(supplierCode)) {
            throw new ParamIllegalException("供应商代码不能为空");
        }

        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (null == plmSkuPo) {
            throw new ParamIllegalException("查询不到对应的sku");
        }

        SupplierPo supplierPo = supplierDao.getBySupplierCode(supplierCode);
        if (null == supplierPo) {
            throw new ParamIllegalException("查询不到对应的供应商信息");
        }

        ChannelPo universalChannelPo;
        if (StringUtils.isNotBlank(universalChannelName)) {
            universalChannelPo = channelDao.getByChannelName(universalChannelName);
            if (null == universalChannelPo) {
                throw new ParamIllegalException("通用渠道的渠道名称查询不到对应的渠道信息");
            }
            Assert.isTrue(BooleanType.TRUE.equals(universalChannelPo.getChannelStatus()), () -> new ParamIllegalException("渠道：{}已关闭，请先到渠道设置中开启后再导入", universalChannelPo.getChannelName()));
        } else {
            universalChannelPo = null;
        }

        List<String> channelNameList = goodsPriceImportBoList.stream()
                .map(GoodsPriceImportBo::getChannelName)
                .distinct()
                .collect(Collectors.toList());
        // 判断是否存在重复名称
        Set<String> channelNameSet = new HashSet<>();
        for (GoodsPriceImportBo goodsPriceImportBo : goodsPriceImportBoList) {
            String channelName = goodsPriceImportBo.getChannelName();
            if (!channelNameSet.add(channelName)) {
                throw new ParamIllegalException("渠道名称:{}存在重复，请修改渠道名称后再导入", channelName);
            }
        }

        List<ChannelPo> channelPoList = channelDao.getByNameList(channelNameList);
        for (ChannelPo channelPo : channelPoList) {
            Assert.isTrue(BooleanType.TRUE.equals(channelPo.getChannelStatus()), () -> new ParamIllegalException("渠道：{}已关闭，请先到渠道设置中开启后再导入", channelPo.getChannelName()));
        }

        // 新增信息
        List<GoodsPricePo> insertGoodsPricePoList = new ArrayList<>();
        List<GoodsPriceItemPo> insertGoodsPriceItemPoList = new ArrayList<>();

        // 更新信息
        List<GoodsPricePo> updateGoodsPricePoList = new ArrayList<>();

        // 查询商品价格信息
        List<GoodsPricePo> goodsPricePoList = goodsPriceDao.getListBySku(sku);

        List<GoodsPricePo> goodsPricePoSupplierList = goodsPricePoList.stream()
                .filter(po -> po.getSupplierCode().equals(supplierCode))
                .collect(Collectors.toList());

        // 判断sku+供应商维度是否存在通用渠道
        List<GoodsPricePo> goodsPricePoSupplierUniversalList = goodsPricePoList.stream()
                .filter(po -> po.getSupplierCode().equals(supplierCode))
                .filter(po -> GoodsPriceUniversal.UNIVERSAL.equals(po.getGoodsPriceUniversal()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(goodsPricePoSupplierUniversalList) && null != universalChannelPo) {
            throw new ParamIllegalException("SKU+供应商维度已经设置一个通用价格，禁止再次设置通用价格，请调整后再导入");
        }

        // 默认生效时间
        LocalDateTime localDateTimeNow = TimeUtil.now(TimeZoneId.CN);
        LocalDate today = localDateTimeNow.toLocalDate();
        LocalTime startOfDay = LocalTime.MIDNIGHT;
        LocalDateTime effectiveTime = LocalDateTime.of(today, startOfDay);
        effectiveTime = TimeUtil.convertZone(effectiveTime, TimeZoneId.CN, TimeZoneId.UTC);

        for (GoodsPriceImportBo goodsPriceImportBo : goodsPriceImportBoList) {

            ChannelPo channelPo = channelPoList.stream()
                    .filter(po -> po.getChannelName().equals(goodsPriceImportBo.getChannelName()))
                    .findFirst()
                    .orElse(null);

            if (null == channelPo) {
                throw new ParamIllegalException("渠道名称:{}查询不到对应的信息", goodsPriceImportBo.getChannelName());
            }

            String channelPriceStr = goodsPriceImportBo.getChannelPrice();

            // 价格
            BigDecimal channelPrice;
            if (StringUtils.isNotBlank(channelPriceStr)) {
                channelPrice = ScmFormatUtil.bigDecimalFormat(channelPriceStr);
                if (null == channelPrice) {
                    throw new ParamIllegalException("渠道价格：{}错误，渠道价格必须填写正确数值，请调整后再导入", channelPriceStr);
                }
                if (channelPrice.scale() > 2) {
                    throw new ParamIllegalException("渠道价格：{}错误，渠道价格必须保留两位小数，请调整后再导入", channelPriceStr);
                }
                // 如果等于0就不处理写入
                if (channelPrice.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

            } else {
                channelPrice = BigDecimal.ZERO;
            }

            GoodsPricePo goodsPricePo = goodsPricePoSupplierList.stream()
                    .filter(po -> po.getChannelId().equals(channelPo.getChannelId()))
                    .findFirst()
                    .orElse(null);

            // 设置通用逻辑
            GoodsPriceUniversal goodsPriceUniversal = GoodsPriceUniversal.NO_UNIVERSAL;
            if (null != universalChannelPo && universalChannelPo.getChannelId().equals(channelPo.getChannelId())) {
                goodsPriceUniversal = GoodsPriceUniversal.UNIVERSAL;
            }

            BigDecimal originalPrice = BigDecimal.ZERO;

            if (null != goodsPricePo) {
                originalPrice = goodsPricePo.getChannelPrice();
                goodsPricePo.setChannelPrice(channelPrice);
                goodsPricePo.setEffectiveTime(effectiveTime);
                goodsPricePo.setGoodsPriceUniversal(goodsPriceUniversal);
                updateGoodsPricePoList.add(goodsPricePo);
            } else {
                goodsPricePo = new GoodsPricePo();
                // 雪花id
                long goodsPriceId = idGenerateService.getSnowflakeId();
                goodsPricePo.setGoodsPriceId(goodsPriceId);
                goodsPricePo.setSku(sku);
                goodsPricePo.setSupplierCode(supplierCode);
                goodsPricePo.setChannelId(channelPo.getChannelId());
                goodsPricePo.setChannelName(channelPo.getChannelName());
                goodsPricePo.setChannelPrice(channelPrice);
                goodsPricePo.setEffectiveTime(effectiveTime);
                goodsPricePo.setGoodsPriceUniversal(goodsPriceUniversal);
                insertGoodsPricePoList.add(goodsPricePo);
            }

            GoodsPriceItemPo insertGoodsPriceItemPo = new GoodsPriceItemPo();

            insertGoodsPriceItemPo.setGoodsPriceId(goodsPricePo.getGoodsPriceId());
            insertGoodsPriceItemPo.setSku(sku);
            insertGoodsPriceItemPo.setOriginalPrice(originalPrice);
            insertGoodsPriceItemPo.setChannelPrice(channelPrice);
            insertGoodsPriceItemPo.setEffectiveTime(effectiveTime);
            insertGoodsPriceItemPo.setGoodsPriceUniversal(goodsPriceUniversal);
            insertGoodsPriceItemPo.setGoodsPriceItemStatus(GoodsPriceItemStatus.APPROVE_PASSED);
            insertGoodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.EFFECTIVE);
            insertGoodsPriceItemPoList.add(insertGoodsPriceItemPo);

        }

        goodsPriceDao.insertBatch(insertGoodsPricePoList);
        goodsPriceItemDao.insertBatch(insertGoodsPriceItemPoList);
        // 打上失效标签
        goodsPriceBaseService.updateBatchGoodsPriceItemInvalid(insertGoodsPriceItemPoList);


        // 以最后更新为准
        if (CollectionUtils.isNotEmpty(updateGoodsPricePoList)) {
            goodsPriceDao.updateBatchById(updateGoodsPricePoList);
        }


        // 更新sku维护关系
        goodsPriceBaseService.updateGoodsPriceMaintain(insertGoodsPricePoList);
        goodsPriceBaseService.updateGoodsPriceMaintain(updateGoodsPricePoList);

        // 同步绑定供应商对照关系
        updateGoodsPricePoList.addAll(insertGoodsPricePoList);
        List<String> uniqueSupplierCodes = updateGoodsPricePoList.stream()
                .map(GoodsPricePo::getSupplierCode)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(uniqueSupplierCodes)) {
            supplierProductCompareRefService.insertSupplierProductCompareBySku(sku, uniqueSupplierCodes);
        }

    }

    /**
     * 获取商品价格信息
     *
     * @param dto:
     * @return List<GoodsPriceGetListVo>
     * @author ChenWenLong
     * @date 2024/6/21 15:55
     */
    public List<GoodsPriceGetListVo> getGoodsPriceList(GoodsPriceGetListDto dto) {
        return goodsPriceDao.getGoodsPriceList(dto);
    }

    /**
     * 采购获取商品价格信息
     *
     * @param dto:
     * @return List<GoodsPriceGetPurchaseListVo>
     * @author ChenWenLong
     * @date 2024/6/24 15:53
     */
    public List<GoodsPriceGetPurchaseListVo> getGoodsPricePurchaseList(GoodsPriceGetPurchaseListDto dto) {
        List<GoodsPriceGetPurchaseListVo> goodsPricePurchases = goodsPriceDao.getGoodsPricePurchaseList(dto);
        List<GoodsPriceGetPurchaseListVo> goodsPricePurchaseList = goodsPricePurchases.stream()
                .filter(goodsPricePurchase -> null != goodsPricePurchase.getEffectiveTime())
                .collect(Collectors.toList());
        List<Long> channelIdList = goodsPricePurchaseList.stream()
                .map(GoodsPriceGetPurchaseListVo::getChannelId)
                .distinct()
                .collect(Collectors.toList());
        List<ChannelPo> channelPoList = channelDao.getByIdList(channelIdList);
        Map<Long, ChannelPo> channelPoMap = channelPoList.stream()
                .collect(Collectors.toMap(ChannelPo::getChannelId, channelPo -> channelPo));

        // 如果渠道关闭就不显示
        goodsPricePurchaseList.removeIf(goodsPriceGetPurchaseListVo -> {
            ChannelPo channelPo = channelPoMap.get(goodsPriceGetPurchaseListVo.getChannelId());
            return channelPo == null || BooleanType.FALSE.equals(channelPo.getChannelStatus());
        });

        return goodsPricePurchaseList;
    }

    /**
     * 商品价格日志管理列表
     *
     * @param dto:
     * @return List<GoodsPriceItemSearchListVo>
     * @author ChenWenLong
     * @date 2024/7/23 16:04
     */
    public List<GoodsPriceItemSearchListVo> getGoodsPriceItemList(GoodsPriceItemSearchListDto dto) {
        dto.setGoodsPriceEffectiveStatusList(GoodsPriceEffectiveStatus.getCloseEffectiveStatusList());
        dto.setGoodsPriceItemStatusList(List.of(GoodsPriceItemStatus.APPROVE_PASSED));
        List<GoodsPriceItemSearchListVo> voList = goodsPriceItemDao.getGoodsPriceItemListByDto(dto);
        List<String> skuList = voList.stream()
                .map(GoodsPriceItemSearchListVo::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        //查询产品名称
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        for (GoodsPriceItemSearchListVo goodsPriceItemSearchListVo : voList) {
            goodsPriceItemSearchListVo.setSkuEncode(skuEncodeMap.get(goodsPriceItemSearchListVo.getSku()));
        }

        return voList;
    }

    /**
     * 批量禁止商品价格
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/7/23 16:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchGoodsPriceItemClose(GoodsPriceItemBatchIdDto dto) {
        List<GoodsPriceItemBatchIdDto.GoodsPriceItemBatchIdInfoDto> goodsPriceItemBatchIdInfoList = Optional.ofNullable(dto.getGoodsPriceItemBatchIdInfoList())
                .orElse(new ArrayList<>());
        List<Long> goodsPriceItemIdList = goodsPriceItemBatchIdInfoList.stream()
                .map(GoodsPriceItemBatchIdDto.GoodsPriceItemBatchIdInfoDto::getGoodsPriceItemId)
                .collect(Collectors.toList());
        final List<GoodsPriceItemPo> goodsPriceItemPoList = goodsPriceItemDao.getListByIdList(goodsPriceItemIdList);
        if (goodsPriceItemPoList.size() != goodsPriceItemIdList.size()) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        // 验证版本号
        for (GoodsPriceItemBatchIdDto.GoodsPriceItemBatchIdInfoDto goodsPriceItemBatchIdInfoDto : goodsPriceItemBatchIdInfoList) {
            goodsPriceItemPoList.stream()
                    .filter(po -> po.getGoodsPriceItemId().equals(goodsPriceItemBatchIdInfoDto.getGoodsPriceItemId()))
                    .findFirst()
                    .ifPresent(po -> {
                        Assert.isTrue(po.getVersion().equals(goodsPriceItemBatchIdInfoDto.getVersion()),
                                () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
                        // 生效状态
                        if (!GoodsPriceEffectiveStatus.getCloseEffectiveStatusList().contains(po.getGoodsPriceEffectiveStatus())) {
                            throw new ParamIllegalException("当前sku:{}的状态:{}无法进行禁止操作，请刷新后重试！", po.getSku(), po.getGoodsPriceEffectiveStatus().getRemark());
                        }
                    });
        }

        for (GoodsPriceItemPo goodsPriceItemPo : goodsPriceItemPoList) {
            goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.INVALID);
        }

        List<Long> goodsPriceIdList = goodsPriceItemPoList.stream()
                .map(GoodsPriceItemPo::getGoodsPriceId)
                .distinct()
                .collect(Collectors.toList());

        List<GoodsPricePo> goodsPricePoList = goodsPriceDao.getListByIdList(goodsPriceIdList);
        for (GoodsPricePo goodsPricePo : goodsPricePoList) {
            goodsPricePo.setChannelPrice(BigDecimal.ZERO);
            goodsPricePo.setEffectiveTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());
            goodsPricePo.setEffectiveRemark("");
            goodsPricePo.setGoodsPriceUniversal(GoodsPriceUniversal.NO_UNIVERSAL);
        }

        goodsPriceItemDao.updateBatchByIdVersion(goodsPriceItemPoList);
        goodsPriceDao.updateBatchById(goodsPricePoList);
        // 更新sku维护关系
        goodsPriceBaseService.updateGoodsPriceMaintain(goodsPricePoList);

    }

}
