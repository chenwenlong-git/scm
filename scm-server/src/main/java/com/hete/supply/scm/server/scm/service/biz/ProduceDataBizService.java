package com.hete.supply.scm.server.scm.service.biz;

import cn.hutool.core.lang.Assert;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.basedata.entity.vo.PlmAttributeVo;
import com.hete.supply.plm.api.basedata.enums.AttributeEntryType;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataAttrSkuDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSkuListDto;
import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataAttrSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuAttrVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuProcessVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProduceDataAttrImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProduceDataItemSupplierImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProduceDataSpecImportationDto;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.converter.ProduceDataAttrConverter;
import com.hete.supply.scm.server.scm.converter.ProduceDataConverter;
import com.hete.supply.scm.server.scm.converter.ProduceDataItemConverter;
import com.hete.supply.scm.server.scm.converter.ProduceDataSpecConverter;
import com.hete.supply.scm.server.scm.dao.*;
import com.hete.supply.scm.server.scm.entity.bo.*;
import com.hete.supply.scm.server.scm.entity.dto.*;
import com.hete.supply.scm.server.scm.entity.po.*;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.ProduceDataCreateType;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.supply.scm.server.scm.production.service.base.SkuProdBaseService;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseLatestPriceItemBo;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.service.ref.ProduceDataRefService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierProductCompareBaseService;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierProductCompareRefService;
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
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ChenWenLong
 * @date 2023/3/28 15:20
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProduceDataBizService {

    private final PlmSkuDao plmSkuDao;
    private final PlmRemoteService plmRemoteService;
    private final ProduceDataItemRawDao produceDataItemRawDao;
    private final ProduceDataItemProcessDao produceDataItemProcessDao;
    private final ProduceDataItemProcessDescDao produceDataItemProcessDescDao;
    private final ProduceDataDao produceDataDao;
    private final ProduceDataAttrDao produceDataAttrDao;
    private final ProduceDataItemDao produceDataItemDao;
    private final ScmImageBaseService scmImageBaseService;
    private final IdGenerateService idGenerateService;
    private final ProduceDataSpuDao produceDataSpuDao;
    private final ProduceDataBaseService produceDataBaseService;
    private final ProduceDataSpecDao produceDataSpecDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final ProduceDataItemSupplierDao produceDataItemSupplierDao;
    private final SupplierProductCompareBaseService supplierProductCompareBaseService;
    private final ProduceDataSpecSupplierDao produceDataSpecSupplierDao;
    private final SupplierDao supplierDao;
    private final SupplierProductCompareRefService supplierProductCompareRefService;
    private final SkuProdBaseService skuProdBaseService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final ProduceDataRefService produceDataRefService;


    /**
     * 列表
     *
     * @param dto:
     * @return PageInfo<ProduceDataSearchVo>
     * @author ChenWenLong
     * @date 2023/8/22 18:48
     */
    public CommonPageResult.PageInfo<ProduceDataSearchVo> search(ProduceDataSearchDto dto) {
        //条件过滤
        if (null == produceDataBaseService.getSearchProduceDataWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }

        CommonPageResult.PageInfo<ProduceDataSearchVo> pageResult = plmSkuDao.selectProduceDataPage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<ProduceDataSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        List<String> spuList = records.stream().map(ProduceDataSearchVo::getSpu).distinct().collect(Collectors.toList());
        List<PlmSkuPo> plmSkuPoList = plmSkuDao.getListBySpuList(spuList);
        List<String> skuList = plmSkuPoList.stream().map(PlmSkuPo::getSku).distinct().collect(Collectors.toList());
        Map<String, List<PlmSkuPo>> plmSkuPoMap = plmSkuPoList.stream().collect(Collectors.groupingBy(PlmSkuPo::getSpu));

        Map<String, String> spuCategoriesMapBySpuList = plmRemoteService.getSpuCategoriesMapBySpuList(spuList);

        Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeBySku(skuList)
                .stream()
                .filter(p -> StringUtils.isNotBlank(p.getSkuCode())).collect(Collectors.toMap(PlmSkuVo::getSkuCode, PlmSkuVo::getSkuEncode));

        // spu主图
        List<ProduceDataSpuPo> produceDataSpuPoList = produceDataSpuDao.getListBySpuList(spuList);
        List<Long> produceDataSpuIdList = produceDataSpuPoList.stream().map(ProduceDataSpuPo::getProduceDataSpuId).collect(Collectors.toList());
        Map<String, Long> spuMap = produceDataSpuPoList.stream().collect(Collectors.toMap(ProduceDataSpuPo::getSpu, ProduceDataSpuPo::getProduceDataSpuId));
        Map<Long, List<String>> spuFileCodeListMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_SPU, produceDataSpuIdList);

        //生产信息
        List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySpuList(spuList);
        List<ProduceDataItemPo> produceDataItemPoList = new ArrayList<>();
        List<ProduceDataItemRawPo> produceDataItemRawPoList;
        Map<Long, List<ProduceDataItemRawPo>> produceDataItemRawPoMap = new HashMap<>();
        Map<String, String> rawSkuEncodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(produceDataPoList)) {
            produceDataItemPoList = produceDataItemDao.getListBySkuList(produceDataPoList.stream().map(ProduceDataPo::getSku).collect(Collectors.toList()));

            if (CollectionUtils.isNotEmpty(produceDataItemPoList)) {
                //原料
                produceDataItemRawPoList = produceDataItemRawDao.getByProduceDataItemIdList(produceDataItemPoList.stream()
                        .map(ProduceDataItemPo::getProduceDataItemId).collect(Collectors.toList()));
                produceDataItemRawPoMap = produceDataItemRawPoList.stream().collect(Collectors.groupingBy(ProduceDataItemRawPo::getProduceDataItemId));
                rawSkuEncodeMap = plmRemoteService.getSkuEncodeBySku(produceDataItemRawPoList.stream()
                                .map(ProduceDataItemRawPo::getSku).collect(Collectors.toList()))
                        .stream().filter(p -> StringUtils.isNotBlank(p.getSkuCode()))
                        .collect(Collectors.toMap(PlmSkuVo::getSkuCode, PlmSkuVo::getSkuEncode));
            }
        }


        //工序
        List<ProduceDataItemProcessPo> produceDataItemProcessPoList = produceDataItemProcessDao.getListBySkuList(skuList);
        Map<String, List<ProduceDataItemProcessPo>> produceDataItemProcessPoMap = produceDataItemProcessPoList.stream().collect(Collectors.groupingBy(ProduceDataItemProcessPo::getSku));

        //工序描述
        List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoList = produceDataItemProcessDescDao.getBySkuList(skuList);
        Map<String, List<ProduceDataItemProcessDescPo>> produceDataItemProcessDescPoMap = produceDataItemProcessDescPoList.stream().collect(Collectors.groupingBy(ProduceDataItemProcessDescPo::getSku));


        for (ProduceDataSearchVo record : records) {
            record.setCategoryName(spuCategoriesMapBySpuList.get(record.getSpu()));
            Long produceDataSpuId = spuMap.get(record.getSpu());
            if (produceDataSpuId != null) {
                record.setSpuFileCodeList(spuFileCodeListMap.get(produceDataSpuId));
            }
            List<PlmSkuPo> poList = plmSkuPoMap.get(record.getSpu());
            if (CollectionUtils.isEmpty(poList)) {
                continue;
            }
            //sku列表

            List<ProduceDataSkuVo> produceDataSkuList = new ArrayList<>();
            for (PlmSkuPo plmSkuPo : poList) {
                ProduceDataSkuVo produceDataSkuVo = new ProduceDataSkuVo();
                produceDataSkuVo.setSku(plmSkuPo.getSku());
                produceDataSkuVo.setSkuEncode(skuEncodeMap.get(plmSkuPo.getSku()));
                ProduceDataPo produceDataPo = produceDataPoList.stream().filter(w -> w.getSku().equals(plmSkuPo.getSku())).findFirst().orElse(null);
                if (produceDataPo != null) {
                    produceDataSkuVo.setProduceDataId(produceDataPo.getProduceDataId());
                    produceDataSkuVo.setUpdateTime(produceDataPo.getUpdateTime());
                    produceDataSkuVo.setCreateUsername(produceDataPo.getUpdateUsername());
                    produceDataSkuVo.setBindingProduceData(produceDataPo.getBindingProduceData());
                }

                //原料列表
                List<ProduceDataItemPo> itemPoList = produceDataItemPoList.stream().filter(w -> w.getSku().equals(plmSkuPo.getSku())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(itemPoList)) {
                    List<ProduceDataItemRawListVo> produceDataItemRawVoList = new ArrayList<>();
                    for (ProduceDataItemPo produceDataItemPo : itemPoList) {
                        List<ProduceDataItemRawPo> produceDataItemRawPos = produceDataItemRawPoMap.get(produceDataItemPo.getProduceDataItemId());
                        Map<String, String> finalRawSkuEncodeMap = rawSkuEncodeMap;
                        Optional.ofNullable(produceDataItemRawPos)
                                .orElse(new ArrayList<>())
                                .forEach(produceDataItemRawPo -> {
                                    ProduceDataItemRawListVo produceDataItemRawListVo = new ProduceDataItemRawListVo();
                                    produceDataItemRawListVo.setSku(produceDataItemRawPo.getSku());
                                    produceDataItemRawListVo.setSkuCnt(produceDataItemRawPo.getSkuCnt());
                                    produceDataItemRawListVo.setMaterialType(produceDataItemRawPo.getMaterialType());
                                    produceDataItemRawListVo.setSkuEncode(finalRawSkuEncodeMap.get(produceDataItemRawPo.getSku()));
                                    produceDataItemRawVoList.add(produceDataItemRawListVo);
                                });

                    }
                    produceDataSkuVo.setProduceDataItemRawList(produceDataItemRawVoList);
                }

                //工序
                List<ProduceDataItemProcessPo> produceDataItemProcessPos = produceDataItemProcessPoMap.get(plmSkuPo.getSku());
                if (CollectionUtils.isNotEmpty(produceDataItemProcessPos)) {
                    List<ProduceDataItemProcessListVo> produceDataItemProcessVoList = new ArrayList<>();
                    for (ProduceDataItemProcessPo produceDataItemProcessPo : produceDataItemProcessPos) {
                        ProduceDataItemProcessListVo produceDataItemProcessListVo = new ProduceDataItemProcessListVo();
                        produceDataItemProcessListVo.setProcessCode(produceDataItemProcessPo.getProcessCode());
                        produceDataItemProcessListVo.setProcessName(produceDataItemProcessPo.getProcessName());
                        produceDataItemProcessListVo.setProcessSecondCode(produceDataItemProcessPo.getProcessSecondCode());
                        produceDataItemProcessListVo.setProcessSecondName(produceDataItemProcessPo.getProcessSecondName());
                        produceDataItemProcessListVo.setProcessFirst(produceDataItemProcessPo.getProcessFirst());
                        produceDataItemProcessListVo.setProcessLabel(produceDataItemProcessPo.getProcessLabel());
                        produceDataItemProcessVoList.add(produceDataItemProcessListVo);
                    }
                    produceDataSkuVo.setProduceDataItemProcessList(produceDataItemProcessVoList);
                }


                //工序描述
                List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPos = produceDataItemProcessDescPoMap.get(plmSkuPo.getSku());
                if (CollectionUtils.isNotEmpty(produceDataItemProcessDescPos)) {
                    List<ProduceDataItemProcessDescListVo> produceDataItemProcessDescVoList = new ArrayList<>();
                    for (ProduceDataItemProcessDescPo produceDataItemProcessDescPo : produceDataItemProcessDescPos) {
                        ProduceDataItemProcessDescListVo produceDataItemProcessDescListVo = new ProduceDataItemProcessDescListVo();
                        produceDataItemProcessDescListVo.setName(produceDataItemProcessDescPo.getName());
                        produceDataItemProcessDescListVo.setDescValue(produceDataItemProcessDescPo.getDescValue());
                        produceDataItemProcessDescVoList.add(produceDataItemProcessDescListVo);
                    }
                    produceDataSkuVo.setProduceDataItemProcessDescList(produceDataItemProcessDescVoList);
                }

                produceDataSkuList.add(produceDataSkuVo);
            }
            record.setProduceDataSkuList(produceDataSkuList);


        }
        return pageResult;
    }

    /**
     * 详情
     *
     * @param dto:
     * @return ProduceDataDetailVo
     * @author ChenWenLong
     * @date 2023/8/23 11:35
     */
    public ProduceDataDetailVo detail(ProduceDataSkuDto dto) {
        ProduceDataDetailVo detailVo = new ProduceDataDetailVo();
        String sku = dto.getSku();
        //默认数据
        detailVo.setSku(dto.getSku());
        detailVo.setBindingProduceData(BindingProduceData.FALSE);
        detailVo.setRawManage(BooleanType.TRUE);
        Map<String, String> skuEncodeMapBySku = plmRemoteService.getSkuEncodeMapBySku(List.of(sku));
        detailVo.setSkuEncode(skuEncodeMapBySku.get(sku));

        // 查询PLM的商品类目名称
        final Map<String, PlmCategoryVo> categoriesVoMap = plmRemoteService.getCategoriesInfoBySku(List.of(sku));

        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (plmSkuPo != null) {
            detailVo.setSpu(plmSkuPo.getSpu());
            ProduceDataSpuPo produceDataSpuPo = produceDataSpuDao.getBySpu(plmSkuPo.getSpu());
            if (produceDataSpuPo != null) {
                List<String> spuFileCodeList = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.PRODUCE_DATA_SPU,
                        List.of(produceDataSpuPo.getProduceDataSpuId()));
                detailVo.setSpuFileCodeList(spuFileCodeList);
            }
        }

        PlmCategoryVo plmCategoryVo = categoriesVoMap.get(sku);
        if (null != plmCategoryVo) {
            detailVo.setCategoryId(plmCategoryVo.getCategoryId());
            detailVo.setCategoryName(plmCategoryVo.getCategoryName());
        }


        //规格书信息列表
        List<ProduceDataSpecPo> produceDataSpecPoList = produceDataSpecDao.getListBySku(sku);
        if (CollectionUtils.isNotEmpty(produceDataSpecPoList)) {
            List<Long> produceDataSpecIdList = produceDataSpecPoList.stream().map(ProduceDataSpecPo::getProduceDataSpecId).collect(Collectors.toList());
            Map<Long, List<String>> fileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCT_IMAGE, produceDataSpecIdList);
            // 查询关联供应商
            List<ProduceDataSpecSupplierPo> produceDataSpecSupplierPoList = produceDataSpecSupplierDao.getByProduceDataSpecIdList(produceDataSpecIdList);
            detailVo.setProduceDataSpecList(ProduceDataSpecConverter.specPoToVo(produceDataSpecPoList, fileCodeMap, produceDataSpecSupplierPoList));
        }

        //生产信息
        ProduceDataPo produceDataPo = produceDataDao.getBySku(dto.getSku());
        if (produceDataPo == null) {
            return detailVo;
        }


        final List<String> sealImageFileCode = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SEAL_IMAGE,
                Collections.singletonList(produceDataPo.getProduceDataId()));
        detailVo.setVersion(produceDataPo.getVersion());
        detailVo.setWeight(produceDataPo.getWeight());
        detailVo.setBindingProduceData(produceDataPo.getBindingProduceData());
        detailVo.setProduceDataId(produceDataPo.getProduceDataId());
        detailVo.setSealImageFileCodeList(sealImageFileCode);
        detailVo.setGoodsPurchasePrice(produceDataPo.getGoodsPurchasePrice());
        detailVo.setRawManage(produceDataPo.getRawManage());

        //获取生产属性
        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getBySku(sku);
        detailVo.setProduceDataAttrList(ProduceDataAttrConverter.INSTANCE.convert(produceDataAttrPoList));


        //获取生产信息详情列表
        List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySku(sku);
        // 获取ID
        List<Long> produceDataItemIdList = produceDataItemPoList
                .stream()
                .map(ProduceDataItemPo::getProduceDataItemId)
                .collect(Collectors.toList());

        //原料BOM信息
        List<ProduceDataItemRawPo> produceDataItemRawPoList = produceDataItemRawDao.getByProduceDataItemIdList(produceDataItemIdList);

        List<String> skuRawList = produceDataItemRawPoList.stream().map(ProduceDataItemRawPo::getSku).collect(Collectors.toList());
        Map<String, String> skuEncodeMapByRaw = plmRemoteService.getSkuEncodeMapBySku(skuRawList);


        Map<Long, List<ProduceDataItemRawPo>> produceDataItemRawPoMap = produceDataItemRawPoList
                .stream()
                .collect(Collectors.groupingBy(ProduceDataItemRawPo::getProduceDataItemId));

        //获取工序和描述
        List<ProduceDataItemProcessPo> produceDataItemProcessPoList = produceDataItemProcessDao.getListBySku(sku);
        Map<Long, List<ProduceDataItemProcessPo>> produceDataItemProcessPoMap = produceDataItemProcessPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemProcessPo::getProduceDataItemId));
        List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoList = produceDataItemProcessDescDao.getListBySku(sku);
        Map<Long, List<ProduceDataItemProcessDescPo>> produceDataItemProcessDescPoMap = produceDataItemProcessDescPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemProcessDescPo::getProduceDataItemId));

        //获取图片
        Map<Long, List<String>> produceDataItemEffectMap = new HashMap<>();
        Map<Long, List<String>> produceDataItemDetailMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(produceDataItemIdList)) {
            produceDataItemEffectMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_ITEM_EFFECT, produceDataItemIdList);
            produceDataItemDetailMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_ITEM_DETAIL, produceDataItemIdList);
        }

        // 获取关联供应商
        List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList = produceDataItemSupplierDao.getByProduceDataItemIdList(produceDataItemIdList);

        detailVo.setProduceDataItemList(ProduceDataConverter.itemPoToVo(produceDataItemPoList,
                produceDataItemRawPoMap,
                produceDataItemProcessPoMap,
                produceDataItemProcessDescPoMap,
                produceDataItemEffectMap,
                produceDataItemDetailMap,
                skuEncodeMapByRaw,
                produceDataItemSupplierPoList
        ));
        return detailVo;
    }

    /**
     * 保存
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/23 16:18
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(ProduceDataSaveDto dto) {
        String sku = dto.getSku();
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (plmSkuPo == null) {
            throw new BizException("sku:{}还没同步到scm系统", sku);
        }
        if (StringUtils.isBlank(plmSkuPo.getSpu())) {
            throw new BizException("sku:{}对应的spu还没同步到scm系统", plmSkuPo.getSku());
        }

        //操作类型
        ProduceDataCreateType curOperate = dto.getProduceDataCreateType();

        // 验证原料入参是否存在重复
        if (CollectionUtils.isNotEmpty(dto.getProduceDataItemList())) {
            for (ProduceDataItemDto produceDataItemDto : dto.getProduceDataItemList()) {
                ProduceDataCreateType copy = ProduceDataCreateType.COPY;
                if (Objects.equals(curOperate, copy)) {
                    String beforeBusinessNo = produceDataItemDto.getBusinessNo();
                    produceDataItemDto.setBusinessNo(Strings.EMPTY);
                    log.info("{}操作，清空来源单号！sku=>{} 来源单号清空前=>{} 来源单号清空后=>{}",
                            copy.getRemark(), sku, beforeBusinessNo, produceDataItemDto.getBusinessNo());
                }

                Set<String> skuRawSet = new HashSet<>();
                Optional.ofNullable(produceDataItemDto.getProduceDataItemRawList())
                        .orElse(new ArrayList<>())
                        .forEach(produceDataItemRaw -> {
                            if (skuRawSet.contains(produceDataItemRaw.getSku())) {
                                throw new BizException("原料列表禁止添加的重复的原料sku：{}", produceDataItemRaw.getSku());
                            } else {
                                skuRawSet.add(produceDataItemRaw.getSku());
                            }
                        });
            }
        }

        String spu = plmSkuPo.getSpu();
        List<ProduceDataItemDto> produceDataItemDtoList = Optional.ofNullable(dto.getProduceDataItemList())
                .orElse(new ArrayList<>());
        List<Long> produceDataItemDtoIdList = produceDataItemDtoList.stream()
                .map(ProduceDataItemDto::getProduceDataItemId)
                .collect(Collectors.toList());

        // 新增BOM的信息初始化新增ID,新增效果图、细节图使用主键ID
        for (ProduceDataItemDto produceDataItemDto : produceDataItemDtoList) {
            if (produceDataItemDto.getProduceDataItemId() == null) {
                // 新增重新赋值ID
                produceDataItemDto.setProduceDataItemId(idGenerateService.getSnowflakeId());
            }
        }

        // 查询已存在BOM信息
        List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySku(sku);

        // spu表保存
        ProduceDataSpuPo produceDataSpuPo = this.produceDataSpuPoSave(spu);

        // 生产信息保存
        List<ProduceDataItemRawListDto> processRawPoDtoList = new ArrayList<>();
        List<ProduceDataItemProcessListDto> processPoDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(produceDataItemDtoList)) {
            processRawPoDtoList = produceDataItemDtoList.get(0).getProduceDataItemRawList();
            processPoDtoList = produceDataItemDtoList.get(0).getProduceDataItemProcessList();
            // 判断版本号是否一致
            for (ProduceDataItemDto produceDataItemDto : produceDataItemDtoList) {
                produceDataItemPoList.stream()
                        .filter(produceDataItemPo -> produceDataItemPo.getProduceDataItemId().equals(produceDataItemDto.getProduceDataItemId()))
                        .findFirst()
                        .ifPresent(itemPo -> Assert.isTrue(itemPo.getVersion().equals(produceDataItemDto.getVersion()), () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！")));
            }

        }

        // 生产信息的生产属性保存（注意：先保存生产属性，方便后面生产资料主表查询）
        List<ProduceDataAttrDto> produceDataAttrList = Optional.ofNullable(dto.getProduceDataAttrList()).orElse(new ArrayList<>());
        this.produceDataAttrPoSave(produceDataAttrList, spu, sku);

        BindingProduceData bindingProduceData = BindingProduceData.FALSE;
        if (dto.getWeight() != null && CollectionUtils.isNotEmpty(processRawPoDtoList) && CollectionUtils.isNotEmpty(processPoDtoList)) {
            bindingProduceData = BindingProduceData.TRUE;
        }
        ProduceDataPo produceDataPo = this.produceDataPoSave(dto, spu, sku, bindingProduceData, dto.getProduceDataCreateType());

        // 封样图保存
        scmImageBaseService.removeAllImage(ImageBizType.SEAL_IMAGE, produceDataPo.getProduceDataId());
        if (CollectionUtils.isNotEmpty(dto.getSealImageFileCodeList())) {
            scmImageBaseService.insertBatchImage(dto.getSealImageFileCodeList(), ImageBizType.SEAL_IMAGE, produceDataPo.getProduceDataId());
        }

        // 规格书信息保存
        List<ProduceDataSpecDto> produceDataSpecList = Optional.ofNullable(dto.getProduceDataSpecList()).orElse(new ArrayList<>());
        this.produceDataSpecSave(produceDataSpecList, spu, sku);

        // 生产信息详情效果图、细节图的保存
        this.produceDataItemEffectDetailSave(produceDataItemDtoList, produceDataItemDtoIdList);

        // 生产信息SPU图片保存
        this.produceDataSpuImageSave(produceDataPo, produceDataSpuPo, produceDataItemDtoList);

        // BOM信息保存
        this.produceDataItemSave(produceDataItemDtoList, produceDataItemPoList, spu, sku);

    }

    /**
     * 生产信息的规格书信息保存
     *
     * @param produceDataSpecList:
     * @param spu:
     * @param sku:
     * @return void
     * @author ChenWenLong
     * @date 2023/11/1 16:51
     */
    private void produceDataSpecSave(List<ProduceDataSpecDto> produceDataSpecList, String spu, String sku) {
        List<ProduceDataSpecPo> produceDataSpecPoList = produceDataSpecDao.getListBySku(sku);
        List<Long> produceDataSpecIdList = produceDataSpecPoList.stream()
                .map(ProduceDataSpecPo::getProduceDataSpecId)
                .collect(Collectors.toList());

        // 验证数据
        List<String> supplierCodeList = produceDataSpecList.stream()
                .filter(specDto -> CollectionUtils.isNotEmpty(specDto.getProduceDataSpecSupplierList()))
                .flatMap(specDto -> specDto.getProduceDataSpecSupplierList().stream())
                .map(ProduceDataSpecSupplierDto::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());

        Map<String, SupplierPo> supplierPoMap = supplierDao.getSupplierMapBySupplierCodeList(supplierCodeList);
        // 获取sku绑定供应商
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareBaseService.getAllPoListBySkuList(List.of(sku));
        for (ProduceDataSpecDto produceDataSpecDto : produceDataSpecList) {
            List<ProduceDataSpecSupplierDto> produceDataSpecSupplierList = Optional.ofNullable(produceDataSpecDto.getProduceDataSpecSupplierList())
                    .orElse(new ArrayList<>());
            List<String> errorNotSupplierCodeList = new ArrayList<>();
            List<String> errorBindingSupplierCodeList = new ArrayList<>();
            for (ProduceDataSpecSupplierDto produceDataSpecSupplierDto : produceDataSpecSupplierList) {
                String supplierCode = produceDataSpecSupplierDto.getSupplierCode();
                if (StringUtils.isBlank(supplierCode)) {
                    continue;
                }
                SupplierPo supplierPo = supplierPoMap.get(produceDataSpecSupplierDto.getSupplierCode());
                if (null == supplierPo) {
                    errorNotSupplierCodeList.add(supplierCode);
                    continue;
                }
                List<SupplierProductComparePo> supplierProductComparePoFilterList = supplierProductComparePoList.stream()
                        .filter(supplierProductComparePo -> supplierCode.equals(supplierProductComparePo.getSupplierCode()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(supplierProductComparePoFilterList)) {
                    errorBindingSupplierCodeList.add(supplierCode);
                }
            }
            if (CollectionUtils.isNotEmpty(errorNotSupplierCodeList)) {
                String errorNotSupplierCode = String.join(",", errorNotSupplierCodeList);
                throw new ParamIllegalException("供应商代码:{}不存在！", errorNotSupplierCode);
            }
            if (CollectionUtils.isNotEmpty(errorBindingSupplierCodeList)) {
                String errorBindingSupplierCode = String.join(",", errorBindingSupplierCodeList);
                throw new ParamIllegalException("SKU:{}与供应商代码:{}无绑定关系，请在基础设置-商品对照关系中绑定后重试，请调整后再导入",
                        sku, errorBindingSupplierCode);
            }


        }


        List<ProduceDataSpecPo> newProduceDataSpecPoList = ProduceDataSpecConverter.editDtoToPo(produceDataSpecList);

        // 对比数据获取新增、编辑、删除的数据
        CompareResult<ProduceDataSpecPo> poResult = DataCompareUtil.compare(newProduceDataSpecPoList, produceDataSpecPoList, ProduceDataSpecPo::getProduceDataSpecId);

        produceDataSpecDao.removeBatchByIds(poResult.getDeletedItems());

        // 删除关联供应商数据
        List<ProduceDataSpecSupplierPo> produceDataSpecSupplierPoOldList = produceDataSpecSupplierDao.getByProduceDataSpecIdList(produceDataSpecIdList);
        if (CollectionUtils.isNotEmpty(produceDataSpecSupplierPoOldList)) {
            List<Long> removeProduceDataSpecSupplierIds = produceDataSpecSupplierPoOldList.stream()
                    .map(ProduceDataSpecSupplierPo::getProduceDataSpecSupplierId)
                    .collect(Collectors.toList());
            produceDataSpecSupplierDao.removeBatchByIds(removeProduceDataSpecSupplierIds);
        }

        if (CollectionUtils.isEmpty(produceDataSpecList)) {
            return;
        }

        // 新增
        List<ProduceDataSpecDto> insertDtoCollect = produceDataSpecList.stream()
                .filter(produceDataSpecDto -> produceDataSpecDto.getProduceDataSpecId() == null)
                .collect(Collectors.toList());
        List<ProduceDataSpecPo> insertSpecPoList = new ArrayList<>();
        List<ProduceDataSpecSupplierPo> insertSpecSupplierPoList = new ArrayList<>();
        for (ProduceDataSpecDto produceDataSpecDto : insertDtoCollect) {
            ProduceDataSpecPo produceDataSpecPo = new ProduceDataSpecPo();
            Long produceDataSpecId = idGenerateService.getSnowflakeId();
            produceDataSpecPo.setProduceDataSpecId(produceDataSpecId);
            produceDataSpecPo.setSpu(spu);
            produceDataSpecPo.setSku(sku);
            produceDataSpecPo.setProductLink(produceDataSpecDto.getProductLink());
            insertSpecPoList.add(produceDataSpecPo);

            // 图片处理
            if (CollectionUtils.isNotEmpty(produceDataSpecDto.getProductFileCode())) {
                scmImageBaseService.insertBatchImage(produceDataSpecDto.getProductFileCode(),
                        ImageBizType.PRODUCT_IMAGE, produceDataSpecPo.getProduceDataSpecId());
            }

            // 关联供应商处理
            if (CollectionUtils.isNotEmpty(produceDataSpecDto.getProduceDataSpecSupplierList())) {
                insertSpecSupplierPoList.addAll(ProduceDataConverter.specSupplierDtoToPo(produceDataSpecDto.getProduceDataSpecSupplierList(), produceDataSpecPo));
            }
        }
        produceDataSpecDao.insertBatch(insertSpecPoList);

        // 编辑
        List<ProduceDataSpecDto> editDtoCollect = produceDataSpecList.stream()
                .filter(produceDataSpecDto -> produceDataSpecDto.getProduceDataSpecId() != null)
                .collect(Collectors.toList());
        List<ProduceDataSpecPo> editSpecPoList = new ArrayList<>();

        // 先清除旧数据
        List<Long> editProduceDataSpecIdList = editDtoCollect.stream().map(ProduceDataSpecDto::getProduceDataSpecId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(editProduceDataSpecIdList)) {
            scmImageBaseService.removeAllImageList(ImageBizType.PRODUCT_IMAGE, editProduceDataSpecIdList);
        }

        for (ProduceDataSpecDto produceDataSpecDto : editDtoCollect) {
            ProduceDataSpecPo oldProduceDataSpecPo = produceDataSpecPoList.stream().filter(po -> po.getProduceDataSpecId().equals(produceDataSpecDto.getProduceDataSpecId())
                    && po.getVersion().equals(produceDataSpecDto.getVersion())).findFirst().orElse(null);
            if (oldProduceDataSpecPo == null) {
                continue;
            }
            oldProduceDataSpecPo.setSpu(spu);
            oldProduceDataSpecPo.setSku(sku);
            oldProduceDataSpecPo.setProductLink(produceDataSpecDto.getProductLink());
            editSpecPoList.add(oldProduceDataSpecPo);

            // 图片处理
            if (CollectionUtils.isNotEmpty(produceDataSpecDto.getProductFileCode())) {
                scmImageBaseService.insertBatchImage(produceDataSpecDto.getProductFileCode(),
                        ImageBizType.PRODUCT_IMAGE, oldProduceDataSpecPo.getProduceDataSpecId());
            }
            insertSpecSupplierPoList.addAll(ProduceDataConverter.specSupplierDtoToPo(produceDataSpecDto.getProduceDataSpecSupplierList(), oldProduceDataSpecPo));

        }
        produceDataSpecDao.updateBatchByIdVersion(editSpecPoList);
        produceDataSpecSupplierDao.insertBatch(insertSpecSupplierPoList);

    }

    /**
     * SPU的主图编辑
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/9/26 17:41
     */
    @Transactional(rollbackFor = Exception.class)
    public void editSpuPicture(ProduceDataSpuDto dto) {
        ProduceDataSpuPo produceDataSpuPo = produceDataSpuDao.getBySpu(dto.getSpu());
        Long produceDataSpuId;
        if (produceDataSpuPo != null) {
            produceDataSpuId = produceDataSpuPo.getProduceDataSpuId();
        } else {
            produceDataSpuId = idGenerateService.getSnowflakeId();
            ProduceDataSpuPo insertSpuPo = new ProduceDataSpuPo();
            insertSpuPo.setProduceDataSpuId(produceDataSpuId);
            insertSpuPo.setSpu(dto.getSpu());
            produceDataSpuDao.insert(insertSpuPo);
        }

        //处理图片
        scmImageBaseService.removeAllImage(ImageBizType.PRODUCE_DATA_SPU, produceDataSpuId);
        if (CollectionUtils.isNotEmpty(dto.getSpuFileCodeList())) {
            scmImageBaseService.insertBatchImage(dto.getSpuFileCodeList(), ImageBizType.PRODUCE_DATA_SPU, produceDataSpuId);
        }
    }

    /**
     * 上传规格书信息
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/11/1 11:11
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLoadProductFile(ProduceUploadFileDto dto) {
        String sku = dto.getSku();
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (plmSkuPo == null) {
            throw new BizException("sku:{}还没同步到scm系统", sku);
        }
        if (StringUtils.isBlank(plmSkuPo.getSpu())) {
            throw new BizException("sku:{}对应的spu还没同步到scm系统", plmSkuPo.getSku());
        }
        //规格书信息入参检验
        this.produceDataSpecVerify(List.of(dto), sku);

        String spu = plmSkuPo.getSpu();

        //生产信息
        Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(List.of(sku));
        ProduceDataPo produceDataPo = produceDataDao.getBySku(sku);
        if (produceDataPo == null) {
            ProduceDataPo insertProduceDataPo = new ProduceDataPo();
            insertProduceDataPo.setSpu(spu);
            insertProduceDataPo.setSku(sku);
            insertProduceDataPo.setCategoryId(categoriesIdMap.get(sku));
            insertProduceDataPo.setBindingProduceData(BindingProduceData.FALSE);
            insertProduceDataPo.setRawManage(BooleanType.TRUE);
            produceDataDao.insert(insertProduceDataPo);
        }

        ProduceDataSpecPo produceDataSpecPo = new ProduceDataSpecPo();
        produceDataSpecPo.setSpu(spu);
        produceDataSpecPo.setSku(sku);
        produceDataSpecPo.setProductLink(dto.getProductLink());
        produceDataSpecDao.insert(produceDataSpecPo);

        // 增加关联供应商
        List<ProduceDataSpecSupplierDto> produceDataSpecSupplierDtoList = dto.getProduceDataSpecSupplierList();
        if (CollectionUtils.isNotEmpty(produceDataSpecSupplierDtoList)) {
            produceDataSpecSupplierDao.insertBatch(ProduceDataConverter.specSupplierDtoToPo(produceDataSpecSupplierDtoList, produceDataSpecPo));
        }

        scmImageBaseService.insertBatchImage(dto.getProductFileCode(), ImageBizType.PRODUCT_IMAGE,
                produceDataSpecPo.getProduceDataSpecId());
    }

    /**
     * 查询上传规格书信息
     *
     * @param dto:
     * @return List<ProduceDataSpecVo>
     * @author ChenWenLong
     * @date 2023/11/1 11:12
     */
    public List<ProduceDataSpecVo> getLoadProductFile(SkuDto dto) {
        final List<ProduceDataSpecPo> produceDataSpecPoList = produceDataSpecDao.getListBySku(dto.getSku());
        if (CollectionUtils.isEmpty(produceDataSpecPoList)) {
            return Collections.emptyList();
        }
        List<Long> produceDataSpecIdList = produceDataSpecPoList.stream().map(ProduceDataSpecPo::getProduceDataSpecId).collect(Collectors.toList());

        // 查询关联供应商
        List<ProduceDataSpecSupplierPo> produceDataSpecSupplierPoList = produceDataSpecSupplierDao.getByProduceDataSpecIdList(produceDataSpecIdList);

        Map<Long, List<String>> fileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCT_IMAGE, produceDataSpecIdList);
        return ProduceDataSpecConverter.specPoToVo(produceDataSpecPoList, fileCodeMap, produceDataSpecSupplierPoList);

    }

    /**
     * 采购单根据sku获取bom信息
     *
     * @param dto:
     * @return List<ProduceDataSkuDetailVo>
     * @author ChenWenLong
     * @date 2023/10/17 13:44
     */
    public List<ProduceDataSkuDetailVo> getDetailBySkuList(SkuListDto dto) {
        List<String> skuList = dto.getSkuList();
        List<ProduceDataBySkuListDto> produceDataBySkuListDtoList = new ArrayList<>();
        for (String sku : skuList) {
            ProduceDataBySkuListDto produceDataBySkuListDto = new ProduceDataBySkuListDto();
            produceDataBySkuListDto.setSku(sku);
            produceDataBySkuListDtoList.add(produceDataBySkuListDto);
        }
        List<String> rawSkuList = new ArrayList<>();
        List<ProduceDataDetailBo> produceDataDetailBoList = produceDataBaseService.getProduceDataBySkuList(produceDataBySkuListDtoList);
        for (ProduceDataDetailBo produceDataDetailBo : produceDataDetailBoList) {
            List<ProduceDataItemBo> produceDataItemBoList = produceDataDetailBo.getProduceDataItemBoList();
            if (CollectionUtils.isNotEmpty(produceDataItemBoList)) {
                List<ProduceDataItemRawListBo> produceDataItemRawBoList = Optional.ofNullable(produceDataItemBoList.get(0).getProduceDataItemRawBoList())
                        .orElse(new ArrayList<>());
                rawSkuList.addAll(produceDataItemRawBoList.stream().map(ProduceDataItemRawListBo::getSku).collect(Collectors.toList()));
            }
        }
        //查询plm的产品名称
        Map<String, String> skuEncodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(rawSkuList)) {
            skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(rawSkuList);
        }
        return ProduceDataConverter.detailBoToDetailVo(produceDataDetailBoList, skuEncodeMap);
    }

    /**
     * 通过SKU批量查询产品规格书信息
     *
     * @param dto:
     * @return List<ProduceDataSpecBatchVo>
     * @author ChenWenLong
     * @date 2023/11/2 10:15
     */
    public List<ProduceDataSpecBatchVo> getBatchLoadProductFile(SkuListDto dto) {
        return produceDataBaseService.getBatchLoadProductFile(dto.getSkuList());
    }

    /**
     * 检验规格书信息
     *
     * @param produceDataSpecList:
     * @return void
     * @author ChenWenLong
     * @date 2023/11/9 10:15
     */
    private void produceDataSpecVerify(List<ProduceDataSpecDto> produceDataSpecList, String sku) {
        if (CollectionUtils.isEmpty(produceDataSpecList)) {
            return;
        }
        List<String> supplierCodeList = produceDataSpecList.stream()
                .filter(specDto -> CollectionUtils.isNotEmpty(specDto.getProduceDataSpecSupplierList()))
                .flatMap(specDto -> specDto.getProduceDataSpecSupplierList().stream())
                .map(ProduceDataSpecSupplierDto::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());

        Map<String, SupplierPo> supplierPoMap = supplierDao.getSupplierMapBySupplierCodeList(supplierCodeList);
        List<String> errorNotSupplierCodeList = new ArrayList<>();
        List<String> errorDisabledSupplierCodeList = new ArrayList<>();
        List<String> errorBindingSupplierCodeList = new ArrayList<>();
        // 获取sku绑定供应商
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareBaseService.getPoListBySkuList(List.of(sku));
        for (ProduceDataSpecDto produceDataSpecDto : produceDataSpecList) {
            if (StringUtils.isBlank(produceDataSpecDto.getProductLink())
                    && CollectionUtils.isEmpty(produceDataSpecDto.getProductFileCode())) {
                throw new ParamIllegalException("链接或规格书图片必填其中一项，不能都为空");
            }
            if (StringUtils.isNotBlank(produceDataSpecDto.getProductLink())
                    && produceDataSpecDto.getProductLink().length() > 100) {
                throw new ParamIllegalException("规格书链接字符长度不能超过 100 位");
            }
            List<ProduceDataSpecSupplierDto> produceDataSpecSupplierList = produceDataSpecDto.getProduceDataSpecSupplierList();
            if (CollectionUtils.isNotEmpty(produceDataSpecSupplierList)) {
                for (ProduceDataSpecSupplierDto produceDataSpecSupplierDto : produceDataSpecSupplierList) {
                    String supplierCode = produceDataSpecSupplierDto.getSupplierCode();
                    SupplierPo supplierPo = supplierPoMap.get(produceDataSpecSupplierDto.getSupplierCode());
                    if (null == supplierPo) {
                        errorNotSupplierCodeList.add(supplierCode);
                        continue;
                    }
                    if (SupplierStatus.DISABLED.equals(supplierPo.getSupplierStatus())) {
                        errorDisabledSupplierCodeList.add(supplierCode);
                        continue;
                    }
                    List<SupplierProductComparePo> supplierProductComparePoFilterList = supplierProductComparePoList.stream()
                            .filter(supplierProductComparePo -> supplierCode.equals(supplierProductComparePo.getSupplierCode()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(supplierProductComparePoFilterList)) {
                        errorBindingSupplierCodeList.add(supplierCode);
                    }
                }
                if (CollectionUtils.isNotEmpty(errorNotSupplierCodeList)) {
                    String errorNotSupplierCode = String.join(",", errorNotSupplierCodeList);
                    throw new ParamIllegalException("供应商代码:{}不存在！", errorNotSupplierCode);
                }
                if (CollectionUtils.isNotEmpty(errorDisabledSupplierCodeList)) {
                    String errorDisabledSupplierCode = String.join(",", errorDisabledSupplierCodeList);
                    throw new ParamIllegalException("供应商代码:{}已被禁用，不可进行此操作！", errorDisabledSupplierCode);
                }
                if (CollectionUtils.isNotEmpty(errorBindingSupplierCodeList)) {
                    String errorBindingSupplierCode = String.join(",", errorBindingSupplierCodeList);
                    throw new ParamIllegalException("SKU:{}与供应商代码:{}无绑定关系，请在基础设置-商品对照关系中绑定后重试，请调整后再导入",
                            sku, errorBindingSupplierCode);
                }
            }
        }
    }

    /**
     * 原料工序导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/1/2 10:48
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportSkuProcess(ProduceDataSearchDto dto) {
        // 如果存在勾选，优先勾选
        if (CollectionUtils.isNotEmpty(dto.getSpuList())) {
            ProduceDataSearchDto checkDto = new ProduceDataSearchDto();
            checkDto.setSpuList(dto.getSpuList());
            dto = checkDto;
        }
        //条件过滤
        if (null == produceDataBaseService.getSearchProduceDataWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = this.getSkuProcessExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));

        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_PRODUCE_DATA_PROCESS_EXPORT.getCode(), dto));
    }

    /**
     * 原料工序导出总数
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/1/2 11:52
     */
    public Integer getSkuProcessExportTotals(ProduceDataSearchDto dto) {
        return produceDataRefService.getSkuProcessExportTotals(dto);
    }

    /**
     * 原料工序导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < ProduceDataExportSkuProcessVo>>
     * @author ChenWenLong
     * @date 2024/1/2 13:57
     */
    public CommonResult<ExportationListResultBo<ProduceDataExportSkuProcessVo>> getSkuProcessExportList(ProduceDataSearchDto dto) {
        ExportationListResultBo<ProduceDataExportSkuProcessVo> resultBo = new ExportationListResultBo<>();
        //条件过滤
        if (null == produceDataBaseService.getSearchProduceDataWhere(dto)) {
            return CommonResult.success(resultBo);
        }

        CommonPageResult.PageInfo<ProduceDataExportSkuProcessVo> pageResult = plmSkuDao.getSkuProcessExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<ProduceDataExportSkuProcessVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<Long> produceDataItemIdList = records.stream().map(ProduceDataExportSkuProcessVo::getProduceDataItemId).collect(Collectors.toList());
        List<String> skuList = records.stream().map(ProduceDataExportSkuProcessVo::getSku).distinct().collect(Collectors.toList());
        List<String> rawSkuList = records.stream().map(ProduceDataExportSkuProcessVo::getRawSku).distinct().collect(Collectors.toList());

        //sku类目列表
        Map<String, String> skuCategoryNameMap = plmRemoteService.getCategoryEnNameBySkuList(skuList, 2);

        //生产资料
        List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(skuList);

        //最新采购价
        List<PurchaseLatestPriceItemBo> purchasePriceList = purchaseChildOrderDao.getPurchasePriceBySkuList(skuList);

        //工序
        List<ProduceDataItemProcessPo> produceDataItemProcessPoList = produceDataItemProcessDao.getByProduceDataItemIdList(produceDataItemIdList);
        Map<Long, List<ProduceDataItemProcessPo>> produceDataItemProcessPoMap = produceDataItemProcessPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemProcessPo::getProduceDataItemId));

        //获取BOM优先级排序
        List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySkuList(skuList);
        Map<String, List<ProduceDataItemPo>> produceDataItemPoMap = produceDataItemPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemPo::getSku));

        // 使用合并并去重
        List<String> combinedSkuList = Stream.concat(skuList.stream(), rawSkuList.stream())
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(combinedSkuList);

        // 获取关联供应商
        final List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList = produceDataItemSupplierDao.getByProduceDataItemIdList(produceDataItemIdList);
        final Map<Long, List<ProduceDataItemSupplierPo>> produceDataItemSupplierPoMap = produceDataItemSupplierPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemSupplierPo::getProduceDataItemId));

        for (ProduceDataExportSkuProcessVo record : records) {
            //工序
            List<ProduceDataItemProcessPo> produceDataItemProcessPos = produceDataItemProcessPoMap.get(record.getProduceDataItemId());
            if (CollectionUtils.isNotEmpty(produceDataItemProcessPos)) {
                record.setProcess1(produceDataItemProcessPos.get(0).getProcessSecondName());
                record.setProcess2(produceDataItemProcessPos.size() > 1 ? produceDataItemProcessPos.get(1).getProcessSecondName() : "");
                record.setProcess3(produceDataItemProcessPos.size() > 2 ? produceDataItemProcessPos.get(2).getProcessSecondName() : "");
                record.setProcess4(produceDataItemProcessPos.size() > 3 ? produceDataItemProcessPos.get(3).getProcessSecondName() : "");
                record.setProcess5(produceDataItemProcessPos.size() > 4 ? produceDataItemProcessPos.get(4).getProcessSecondName() : "");
                record.setProcess6(produceDataItemProcessPos.size() > 5 ? produceDataItemProcessPos.get(5).getProcessSecondName() : "");
                record.setProcess7(produceDataItemProcessPos.size() > 6 ? produceDataItemProcessPos.get(6).getProcessSecondName() : "");
            }

            // 获取关联供应商信息
            List<ProduceDataItemSupplierPo> prodDataItemSupplierPos = produceDataItemSupplierPoMap.get(record.getProduceDataItemId());
            if (CollectionUtils.isNotEmpty(prodDataItemSupplierPos)) {
                List<String> supplierCodeList = prodDataItemSupplierPos.stream()
                        .map(ProduceDataItemSupplierPo::getSupplierCode)
                        .sorted()
                        .collect(Collectors.toList());
                String supplierCodeJoining = String.join(",", supplierCodeList);
                record.setSupplierCodeJoining(supplierCodeJoining);

                List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBatchSupplierCode(supplierCodeList);
                String supplierProductNameJoining = supplierCodeList.stream()
                        .map(supplierCode -> supplierProductComparePoList.stream()
                                .filter(po -> po.getSupplierCode().equals(supplierCode))
                                .map(SupplierProductComparePo::getSupplierProductName)
                                .findFirst()
                                .orElse(""))
                        .collect(Collectors.joining(","));
                record.setSupplierProductNameJoining(supplierProductNameJoining);

                String latestGoodsPurchasePriceJoining = supplierCodeList.stream()
                        .map(supplierCode -> purchasePriceList.stream()
                                .filter(purchasePrice ->
                                        Objects.equals(purchasePrice.getSupplierCode(), supplierCode) &&
                                                Objects.equals(purchasePrice.getSku(), record.getSku()) &&
                                                Objects.nonNull(purchasePrice.getConfirmTime()))
                                .max(Comparator.comparing(PurchaseLatestPriceItemBo::getConfirmTime))
                                .map(PurchaseLatestPriceItemBo::getPurchasePrice)
                                .orElse(BigDecimal.ZERO).toString())
                        .collect(Collectors.joining(","));
                record.setLatestGoodsPurchasePriceJoining(latestGoodsPurchasePriceJoining);
            }

            // 获取产品名称
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            record.setRawSkuEncode(skuEncodeMap.get(record.getRawSku()));

            //二级类目名称
            record.setCategoryName(skuCategoryNameMap.get(record.getSku()));

            //商品采购价
            BigDecimal goodsPurchasePrice = produceDataPoList.stream()
                    .filter(produceDataPo -> Objects.equals(produceDataPo.getSku(), record.getSku()))
                    .map(ProduceDataPo::getGoodsPurchasePrice)
                    .findFirst().orElse(BigDecimal.ZERO);
            record.setGoodsPurchasePrice(goodsPurchasePrice.toString());

            // 获取优先级
            List<ProduceDataItemPo> produceDataItemPos = Optional.ofNullable(produceDataItemPoMap.get(record.getSku()))
                    .orElse(new ArrayList<>());
            int sort = 1;
            for (int i = 0; i < produceDataItemPos.size(); i++) {
                if (record.getProduceDataItemId().equals(produceDataItemPos.get(i).getProduceDataItemId())) {
                    sort = i + 1;
                    break;
                }
            }
            record.setSort(sort);
        }
        resultBo.setRowDataList(records);
        return CommonResult.success(resultBo);
    }

    /**
     * 导入生产信息产品规格书
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/8 14:43
     */
    @Transactional(rollbackFor = Exception.class)
    public void importProduceDataSpec(ProduceDataSpecImportationDto dto) {
        log.info("批量导入生产信息产品规格书入参dto={}", dto);
        String sku = dto.getSku();
        String productLink = dto.getProductLink();

        // 校验入参
        if (StringUtils.isBlank(sku)) {
            throw new ParamIllegalException("SKU不能为空");
        }
        if (sku.length() > 100) {
            throw new ParamIllegalException("SKU字符长度不能超过 100 位");
        }
        if (StringUtils.isBlank(productLink)) {
            throw new ParamIllegalException("规格书链接不能为空");
        }
        if (productLink.length() > 100) {
            throw new ParamIllegalException("规格书链接字符长度不能超过 100 位");
        }

        // 新增关联供应商信息
        List<ProduceDataSpecSupplierPo> insertProduceDataSpecSupplierPoList = new ArrayList<>();

        // 关联供应商验证是否存在
        if (StringUtils.isNotBlank(dto.getSupplierCodeList())) {
            // 按/拆分成数组
            String[] supplierCodeArray = dto.getSupplierCodeList().split("/");
            // 将数组转换为List，并使用Stream去重
            List<String> supplierCodeList = Arrays.stream(supplierCodeArray)
                    .distinct()
                    .collect(Collectors.toList());
            Map<String, SupplierPo> supplierMap = supplierDao.getSupplierMapBySupplierCodeList(supplierCodeList);
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareBaseService.getPoListBySkuList(List.of(sku));
            List<String> errorNotSupplierCodeList = new ArrayList<>();
            List<String> errorDisabledSupplierCodeList = new ArrayList<>();
            List<String> errorBindingSupplierCodeList = new ArrayList<>();
            for (String supplierCode : supplierCodeList) {
                SupplierPo supplierPo = supplierMap.get(supplierCode);
                // 验证是否存在
                if (null == supplierPo) {
                    errorNotSupplierCodeList.add(supplierCode);
                    continue;
                }
                if (SupplierStatus.DISABLED.equals(supplierPo.getSupplierStatus())) {
                    errorDisabledSupplierCodeList.add(supplierCode);
                    continue;
                }

                // 验证供应商和SKU是否绑定
                List<SupplierProductComparePo> supplierProductComparePoFilterList = supplierProductComparePoList.stream()
                        .filter(supplierProductComparePo -> supplierCode.equals(supplierProductComparePo.getSupplierCode()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(supplierProductComparePoFilterList)) {
                    errorBindingSupplierCodeList.add(supplierCode);
                    continue;
                }

                ProduceDataSpecSupplierPo produceDataSpecSupplierPo = new ProduceDataSpecSupplierPo();
                produceDataSpecSupplierPo.setSupplierCode(supplierCode);
                produceDataSpecSupplierPo.setSupplierName(supplierPo.getSupplierName());
                insertProduceDataSpecSupplierPoList.add(produceDataSpecSupplierPo);
            }
            if (CollectionUtils.isNotEmpty(errorNotSupplierCodeList)) {
                String errorNotSupplierCode = String.join(",", errorNotSupplierCodeList);
                throw new ParamIllegalException("供应商代码:{}不存在，请调整后再导入", errorNotSupplierCode);
            }
            if (CollectionUtils.isNotEmpty(errorDisabledSupplierCodeList)) {
                String errorDisabledSupplierCode = String.join(",", errorDisabledSupplierCodeList);
                throw new ParamIllegalException("供应商代码:{}已被禁用，不可进行此操作，请调整后再导入", errorDisabledSupplierCode);
            }
            if (CollectionUtils.isNotEmpty(errorBindingSupplierCodeList)) {
                String errorBindingSupplierCode = String.join(",", errorBindingSupplierCodeList);
                throw new ParamIllegalException("SKU:{}与供应商代码:{}无绑定关系，请在基础设置-商品对照关系中绑定后重试，请调整后再导入",
                        sku, errorBindingSupplierCode);
            }
        }

        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (plmSkuPo == null) {
            throw new BizException("SKU:{}还没同步到scm系统", sku);
        }
        if (StringUtils.isBlank(plmSkuPo.getSpu())) {
            throw new BizException("SKU:{}对应的SPU还没同步到scm系统", plmSkuPo.getSku());
        }
        String spu = plmSkuPo.getSpu();

        //生产信息
        Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(List.of(sku));
        ProduceDataPo produceDataPo = produceDataDao.getBySku(sku);
        if (produceDataPo == null) {
            ProduceDataPo insertProduceDataPo = new ProduceDataPo();
            insertProduceDataPo.setSpu(spu);
            insertProduceDataPo.setSku(sku);
            insertProduceDataPo.setCategoryId(categoriesIdMap.get(sku));
            insertProduceDataPo.setBindingProduceData(BindingProduceData.FALSE);
            insertProduceDataPo.setRawManage(BooleanType.TRUE);
            produceDataDao.insert(insertProduceDataPo);
        }

        ProduceDataSpecPo produceDataSpecPo = new ProduceDataSpecPo();
        produceDataSpecPo.setSpu(spu);
        produceDataSpecPo.setSku(sku);
        produceDataSpecPo.setProductLink(productLink);
        produceDataSpecDao.insert(produceDataSpecPo);

        // 增加关联供应商
        for (ProduceDataSpecSupplierPo produceDataSpecSupplierPo : insertProduceDataSpecSupplierPoList) {
            produceDataSpecSupplierPo.setProduceDataSpecId(produceDataSpecPo.getProduceDataSpecId());
            produceDataSpecSupplierPo.setSpu(spu);
            produceDataSpecSupplierPo.setSku(sku);
        }
        produceDataSpecSupplierDao.insertBatch(insertProduceDataSpecSupplierPoList);


    }

    /**
     * 生产信息详情效果图、细节图的保存
     *
     * @param produceDataItemDtoList:
     * @param produceDataItemDtoIdList:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/11 17:14
     */
    private void produceDataItemEffectDetailSave(List<ProduceDataItemDto> produceDataItemDtoList,
                                                 List<Long> produceDataItemDtoIdList) {
        //生产信息详情效果图
        Map<Long, List<String>> produceDataItemEffectMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_ITEM_EFFECT, produceDataItemDtoIdList);
        //生产信息详情细节图
        Map<Long, List<String>> produceDataItemDetailMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_ITEM_DETAIL, produceDataItemDtoIdList);

        // 效果图和细节图保存
        for (ProduceDataItemDto produceDataItemDto : produceDataItemDtoList) {
            List<String> fileEffectList = produceDataItemEffectMap.get(produceDataItemDto.getProduceDataItemId());
            if (CollectionUtils.isNotEmpty(fileEffectList)) {
                scmImageBaseService.removeAllImageList(ImageBizType.PRODUCE_DATA_ITEM_EFFECT, List.of(produceDataItemDto.getProduceDataItemId()));
            }
            scmImageBaseService.insertBatchImage(produceDataItemDto.getEffectFileCodeList(), ImageBizType.PRODUCE_DATA_ITEM_EFFECT, produceDataItemDto.getProduceDataItemId());

            List<String> fileDetailList = produceDataItemDetailMap.get(produceDataItemDto.getProduceDataItemId());
            if (CollectionUtils.isNotEmpty(fileDetailList)) {
                scmImageBaseService.removeAllImageList(ImageBizType.PRODUCE_DATA_ITEM_DETAIL, List.of(produceDataItemDto.getProduceDataItemId()));
            }
            scmImageBaseService.insertBatchImage(produceDataItemDto.getDetailFileCodeList(), ImageBizType.PRODUCE_DATA_ITEM_DETAIL, produceDataItemDto.getProduceDataItemId());

        }
    }

    /**
     * 生产信息SPU图片保存
     *
     * @param produceDataPo:
     * @param produceDataSpuPo:
     * @param produceDataItemDtoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/11 17:01
     */
    private void produceDataSpuImageSave(ProduceDataPo produceDataPo,
                                         ProduceDataSpuPo produceDataSpuPo,
                                         List<ProduceDataItemDto> produceDataItemDtoList) {
        // spu主图获取
        Map<Long, List<String>> spuFileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_SPU, List.of(produceDataSpuPo.getProduceDataSpuId()));

        List<String> spuFileCodeList = spuFileCodeMap.get(produceDataSpuPo.getProduceDataSpuId());
        if (BindingProduceData.TRUE.equals(produceDataPo.getBindingProduceData()) &&
                CollectionUtils.isNotEmpty(produceDataItemDtoList) &&
                CollectionUtils.isNotEmpty(produceDataItemDtoList.get(0).getEffectFileCodeList())) {
            if (CollectionUtils.isNotEmpty(spuFileCodeList)) {
                scmImageBaseService.removeAllImageList(ImageBizType.PRODUCE_DATA_SPU, List.of(produceDataSpuPo.getProduceDataSpuId()));
            }
            scmImageBaseService.insertBatchImage(produceDataItemDtoList.get(0).getEffectFileCodeList(), ImageBizType.PRODUCE_DATA_SPU, produceDataSpuPo.getProduceDataSpuId());
        }
    }

    /**
     * BOM信息保存
     *
     * @param produceDataItemDtoList:
     * @param produceDataItemPoList:
     * @param spu:
     * @param sku:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/11 16:55
     */
    private void produceDataItemSave(List<ProduceDataItemDto> produceDataItemDtoList,
                                     List<ProduceDataItemPo> produceDataItemPoList,
                                     String spu,
                                     String sku) {
        List<Long> produceDataItemDtoIdList = produceDataItemDtoList.stream().map(ProduceDataItemDto::getProduceDataItemId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(produceDataItemDtoList)) {
            produceDataItemDao.removeBatchByIds(produceDataItemPoList.stream()
                    .map(ProduceDataItemPo::getProduceDataItemId).collect(Collectors.toList()));
        }
        //生产信息详情
        if (CollectionUtils.isNotEmpty(produceDataItemDtoList)) {
            List<ProduceDataItemPo> insertProduceDataItemPoList = ProduceDataItemConverter.INSTANCE.convert(produceDataItemDtoList);
            //设置优先级
            int sort = insertProduceDataItemPoList.size();
            for (ProduceDataItemPo produceDataItemPo : insertProduceDataItemPoList) {
                produceDataItemPo.setSort(sort);
                sort--;
            }
            //编辑生产信息详情
            CompareResult<ProduceDataItemPo> itemResult = DataCompareUtil.compare(insertProduceDataItemPoList, produceDataItemPoList, ProduceDataItemPo::getProduceDataItemId);
            List<ProduceDataItemPo> collect = itemResult.getNewItems().stream().peek(item -> {
                item.setSpu(spu);
                item.setSku(sku);
            }).collect(Collectors.toList());
            produceDataItemDao.insertBatch(collect);
            produceDataItemDao.updateBatchByIdVersion(itemResult.getExistingItems());
            produceDataItemDao.removeBatchByIds(itemResult.getDeletedItems());

            //生产信息详情原料BOM要根据item的ID关联
            List<ProduceDataItemRawPo> produceDataItemRawPoList = produceDataItemRawDao.getByProduceDataItemIdList(produceDataItemDtoIdList);
            if (CollectionUtils.isNotEmpty(produceDataItemRawPoList)) {
                produceDataItemRawDao.deleteByProduceDataItemIdList(produceDataItemDtoIdList);
            }
            produceDataItemRawDao.insertBatch(ProduceDataConverter.itemDtoToRawPo(produceDataItemDtoList, spu));

            //工序
            List<ProduceDataItemProcessPo> produceDataItemProcessPoList = produceDataItemProcessDao.getListBySku(sku);
            if (CollectionUtils.isNotEmpty(produceDataItemProcessPoList)) {
                produceDataItemProcessDao.deleteByProduceDataItemIdList(produceDataItemProcessPoList.stream()
                        .map(ProduceDataItemProcessPo::getProduceDataItemId).collect(Collectors.toList()));
            }
            produceDataItemProcessDao.insertBatch(ProduceDataConverter.itemDtoToProcessPo(produceDataItemDtoList, spu, sku));

            //工序描述
            List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoList = produceDataItemProcessDescDao.getListBySku(sku);
            if (CollectionUtils.isNotEmpty(produceDataItemProcessDescPoList)) {
                produceDataItemProcessDescDao.deleteByProduceDataItemIdList(produceDataItemProcessDescPoList.stream()
                        .map(ProduceDataItemProcessDescPo::getProduceDataItemId).collect(Collectors.toList()));
            }
            produceDataItemProcessDescDao.insertBatch(ProduceDataConverter.itemDtoToProcessDescPo(produceDataItemDtoList, spu, sku));

            //生产信息详情关联供应商
            List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList = produceDataItemSupplierDao.getByProduceDataItemIdList(produceDataItemDtoIdList);
            if (CollectionUtils.isNotEmpty(produceDataItemSupplierPoList)) {
                produceDataItemSupplierDao.deleteByProduceDataItemIdList(produceDataItemDtoIdList);
            }
            List<ProduceDataItemSupplierPo> insertSupplierList = ProduceDataConverter.itemDtoToSupplierPo(produceDataItemDtoList, spu, sku);
            produceDataItemSupplierDao.insertBatch(insertSupplierList);

            //同步绑定供应商对照关系
            List<String> uniqueSupplierCodes = insertSupplierList.stream()
                    .map(ProduceDataItemSupplierPo::getSupplierCode)
                    .filter(StringUtils::isNotBlank)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(uniqueSupplierCodes)) {
                supplierProductCompareRefService.insertSupplierProductCompareBySku(sku, uniqueSupplierCodes);
            }
        }
    }

    /**
     * 生产信息的生产属性保存
     *
     * @param produceDataAttrList:
     * @param spu:
     * @param sku:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/11 16:32
     */
    @RedisLock(key = "#sku", prefix = ScmRedisConstant.SCM_PRODUCE_DATA_ATTR_UPDATE, waitTime = 1, leaseTime = -1, exceptionDesc = "商品属性风险信息处理中，请稍后再试。")
    private void produceDataAttrPoSave(List<ProduceDataAttrDto> produceDataAttrList, String spu, String sku) {
        List<ProduceDataAttrPo> insertAttrPoList = ProduceDataAttrConverter.INSTANCE.insertConvert(produceDataAttrList);
        if (CollectionUtils.isNotEmpty(insertAttrPoList)) {
            for (ProduceDataAttrPo produceDataAttrPo : insertAttrPoList) {
                produceDataAttrPo.setSku(sku);
                produceDataAttrPo.setSpu(spu);
            }
        }
        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getListBySkuList(List.of(sku));
        if (CollectionUtils.isNotEmpty(produceDataAttrPoList)) {
            produceDataAttrDao.deleteBySkuList(List.of(sku));
        }
        if (CollectionUtils.isNotEmpty(insertAttrPoList)) {
            produceDataAttrDao.insertBatch(insertAttrPoList);
        }
    }

    /**
     * 生产信息详情信息保存
     *
     * @param dto:
     * @param spu:
     * @param sku:
     * @param bindingProduceData:
     * @param produceDataCreateType:
     * @return ProduceDataPo
     * @author ChenWenLong
     * @date 2024/9/13 10:53
     */
    private ProduceDataPo produceDataPoSave(ProduceDataSaveTopDto dto,
                                            String spu,
                                            String sku,
                                            BindingProduceData bindingProduceData,
                                            ProduceDataCreateType produceDataCreateType) {

        //生产信息
        ProduceDataPo produceDataPo = produceDataDao.getBySku(sku);
        if (produceDataPo == null) {
            produceDataPo = new ProduceDataPo();
            produceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
        }
        BigDecimal goodsPurchasePrice = produceDataPo.getGoodsPurchasePrice();
        produceDataPo.setSpu(spu);
        produceDataPo.setSku(sku);
        produceDataPo.setCategoryId(dto.getCategoryId());
        produceDataPo.setBindingProduceData(bindingProduceData);
        produceDataPo.setWeight(dto.getWeight());
        produceDataPo.setGoodsPurchasePrice(dto.getGoodsPurchasePrice());
        produceDataPo.setRawManage(dto.getRawManage());
        if (null == goodsPurchasePrice || goodsPurchasePrice.compareTo(dto.getGoodsPurchasePrice()) != 0) {
            produceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
        }

        // 采购商品价格为空或者等于0或者是复制的情况,调用SKU定价表获取价格
        if (ProduceDataCreateType.COPY.equals(produceDataCreateType) || produceDataPo.getGoodsPurchasePrice() == null || produceDataPo.getGoodsPurchasePrice().compareTo(BigDecimal.ZERO) == 0) {
            List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getBySku(produceDataPo.getSku());
            // 重置采购价格，重新调用sku定价获取
            produceDataPo.setGoodsPurchasePrice(BigDecimal.ZERO);
            produceDataBaseService.saveProduceDataUpdatePrice(List.of(produceDataPo), produceDataAttrPoList);
        }

        produceDataDao.insertOrUpdate(produceDataPo);
        return produceDataPo;
    }

    /**
     * spu主图保存
     *
     * @param spu:
     * @return ProduceDataSpuPo
     * @author ChenWenLong
     * @date 2024/3/11 16:16
     */
    private ProduceDataSpuPo produceDataSpuPoSave(String spu) {
        ProduceDataSpuPo produceDataSpuPo = produceDataSpuDao.getBySpu(spu);
        if (produceDataSpuPo == null) {
            produceDataSpuPo = new ProduceDataSpuPo();
            Long produceDataSpuId = idGenerateService.getSnowflakeId();
            produceDataSpuPo.setProduceDataSpuId(produceDataSpuId);
            produceDataSpuPo.setSpu(spu);
            produceDataSpuDao.insert(produceDataSpuPo);
        }
        return produceDataSpuPo;
    }

    /**
     * 导入生产信息的BOM供应商
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/18 10:11
     */
    @Transactional(rollbackFor = Exception.class)
    public void importProduceDataItemSupplier(ProduceDataItemSupplierImportationDto dto) {
        log.info("批量导入生产信息的BOM供应商入参dto={}", dto);

        String produceDataItemIdStr = dto.getProduceDataItemId();
        String sku = StringUtils.isNotBlank(dto.getSku()) ? dto.getSku().trim() : dto.getSku();
        String bomName = StringUtils.isNotBlank(dto.getBomName()) ? dto.getBomName().trim() : dto.getBomName();
        String handleWay = StringUtils.isNotBlank(dto.getHandleWay()) ? dto.getHandleWay().trim() : dto.getHandleWay();
        // 如果空不处理
        if (StringUtils.isBlank(handleWay)) {
            return;
        }

        // 校验入参
        if (StringUtils.isBlank(produceDataItemIdStr)) {
            throw new ParamIllegalException("BOM ID不能为空");
        }
        Long produceDataItemId = Long.parseLong(produceDataItemIdStr);

        // 获取BOM信息
        ProduceDataItemPo produceDataItemPo = produceDataItemDao.getById(produceDataItemId);
        if (null == produceDataItemPo) {
            throw new ParamIllegalException("BOM ID:{}查询不到对应信息，请调整后再导入", produceDataItemId);
        }

        // 删除BOM信息
        if (!ScmConstant.PRODUCE_DATA_ITEM_BOM_DELETE.equals(handleWay)) {
            produceDataItemDao.removeById(produceDataItemId);
            return;
        }

        if (StringUtils.isBlank(sku)) {
            throw new ParamIllegalException("SKU不能为空");
        }
        if (sku.length() > 100) {
            throw new ParamIllegalException("SKU字符长度不能超过 100 位");
        }
        if (StringUtils.isBlank(bomName)) {
            throw new ParamIllegalException("生产信息的BOM名称不能为空");
        }
        if (bomName.length() > 15) {
            throw new ParamIllegalException("BOM名称长度不能超过15个字符");
        }


        // 新增关联供应商信息
        List<ProduceDataItemSupplierPo> insertProduceDataItemSupplierPoList = new ArrayList<>();

        // 关联供应商验证是否存在
        if (StringUtils.isNotBlank(dto.getSupplierCodeList())) {
            // 按/拆分成数组
            String[] supplierCodeArray = dto.getSupplierCodeList().split("/");
            // 将数组转换为List，并使用Stream去重
            List<String> supplierCodeList = Arrays.stream(supplierCodeArray)
                    .distinct()
                    .collect(Collectors.toList());
            Map<String, SupplierPo> supplierMap = supplierDao.getSupplierMapBySupplierCodeList(supplierCodeList);
            List<String> errorNotSupplierCodeList = new ArrayList<>();
            List<String> errorDisabledSupplierCodeList = new ArrayList<>();
            for (String supplierCode : supplierCodeList) {
                SupplierPo supplierPo = supplierMap.get(supplierCode);
                // 验证是否存在
                if (null == supplierPo) {
                    errorNotSupplierCodeList.add(supplierCode);
                    continue;
                }
                if (SupplierStatus.DISABLED.equals(supplierPo.getSupplierStatus())) {
                    errorDisabledSupplierCodeList.add(supplierCode);
                    continue;
                }

                ProduceDataItemSupplierPo produceDataItemSupplierPo = new ProduceDataItemSupplierPo();
                produceDataItemSupplierPo.setSupplierCode(supplierCode);
                produceDataItemSupplierPo.setSupplierName(supplierPo.getSupplierName());
                insertProduceDataItemSupplierPoList.add(produceDataItemSupplierPo);
            }
            if (CollectionUtils.isNotEmpty(errorNotSupplierCodeList)) {
                String errorNotSupplierCode = String.join(",", errorNotSupplierCodeList);
                throw new ParamIllegalException("供应商代码:{}不存在，请调整后再导入", errorNotSupplierCode);
            }
            if (CollectionUtils.isNotEmpty(errorDisabledSupplierCodeList)) {
                String errorDisabledSupplierCode = String.join(",", errorDisabledSupplierCodeList);
                throw new ParamIllegalException("供应商代码:{}已被禁用，不可进行此操作，请调整后再导入", errorDisabledSupplierCode);
            }

        }


        if (!sku.equals(produceDataItemPo.getSku())) {
            throw new ParamIllegalException("BOM ID:{}和SKU:{}关联不上，请调整后再导入", produceDataItemId, sku);
        }


        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (plmSkuPo == null) {
            throw new BizException("SKU:{}还没同步到scm系统", sku);
        }
        if (StringUtils.isBlank(plmSkuPo.getSpu())) {
            throw new BizException("SKU:{}对应的SPU还没同步到scm系统", plmSkuPo.getSku());
        }
        String spu = plmSkuPo.getSpu();

        //生产信息
        Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(List.of(sku));
        ProduceDataPo produceDataPo = produceDataDao.getBySku(sku);
        if (produceDataPo == null) {
            ProduceDataPo insertProduceDataPo = new ProduceDataPo();
            insertProduceDataPo.setSpu(spu);
            insertProduceDataPo.setSku(sku);
            insertProduceDataPo.setCategoryId(categoriesIdMap.get(sku));
            insertProduceDataPo.setBindingProduceData(BindingProduceData.FALSE);
            insertProduceDataPo.setRawManage(BooleanType.TRUE);
            produceDataDao.insert(insertProduceDataPo);
        }

        produceDataItemPo.setBomName(bomName);
        produceDataItemDao.updateById(produceDataItemPo);

        // 增加关联供应商
        for (ProduceDataItemSupplierPo produceDataItemSupplierPo : insertProduceDataItemSupplierPoList) {
            produceDataItemSupplierPo.setProduceDataItemId(produceDataItemPo.getProduceDataItemId());
            produceDataItemSupplierPo.setSpu(spu);
            produceDataItemSupplierPo.setSku(sku);
        }

        if (CollectionUtils.isNotEmpty(insertProduceDataItemSupplierPoList)) {
            List<ProduceDataItemSupplierPo> produceDataItemSupplierPoOldList = produceDataItemSupplierDao.getByProduceDataItemIdList(List.of(produceDataItemPo.getProduceDataItemId()));
            if (CollectionUtils.isNotEmpty(produceDataItemSupplierPoOldList)) {
                List<Long> produceDataItemSupplierIds = produceDataItemSupplierPoOldList.stream().map(ProduceDataItemSupplierPo::getProduceDataItemSupplierId).collect(Collectors.toList());
                produceDataItemSupplierDao.removeBatchByIds(produceDataItemSupplierIds);
            }
        }

        produceDataItemSupplierDao.insertBatch(insertProduceDataItemSupplierPoList);

        // 同步绑定供应商对照关系
        if (CollectionUtils.isNotEmpty(insertProduceDataItemSupplierPoList)) {
            List<String> supplierCodes = insertProduceDataItemSupplierPoList.stream()
                    .map(ProduceDataItemSupplierPo::getSupplierCode)
                    .distinct()
                    .collect(Collectors.toList());
            supplierProductCompareRefService.insertSupplierProductCompareBySku(sku, supplierCodes);
        }


    }

    /**
     * PDC获取生产资料的信息
     *
     * @param dto:
     * @return List<ProduceDataVo>
     * @author ChenWenLong
     * @date 2024/2/29 15:32
     */
    public List<ProduceDataVo> getProduceDataListBySkuList(ProduceDataSkuListDto dto) {
        List<String> skuList = new ArrayList<>(dto.getSkuList());
        final List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(skuList);
        //获取生产属性
        final List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getListBySkuList(skuList);
        final Map<String, List<ProduceDataAttrPo>> produceDataAttrPoMap = produceDataAttrPoList.stream()
                .filter(produceDataAttrPo -> StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
                .collect(Collectors.groupingBy(ProduceDataAttrPo::getSku));
        // 获取Bom表Po
        final List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySkuList(skuList);
        final Map<String, List<ProduceDataItemPo>> produceDataItemPoMap = produceDataItemPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemPo::getSku));

        return ProduceDataConverter.poListToProduceDataVoList(produceDataPoList, produceDataAttrPoMap, produceDataItemPoMap);
    }

    /**
     * 批量导入生产属性
     *
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void importProduceDataAttr(ProduceDataAttrImportationDto dto) {
        log.info("批量导入生产信息的生产属性入参dto={}", dto);
        String sku = dto.getSku();
        String weightStr = dto.getWeight();
        String goodsPurchasePriceStr = dto.getGoodsPurchasePrice();
        List<ProduceDataAttrImportBo> attrValueList = Stream.of(
                        new ProduceDataAttrImportBo(dto.getAttrName1(), dto.getAttrValue1()), new ProduceDataAttrImportBo(dto.getAttrName2(), dto.getAttrValue2()),
                        new ProduceDataAttrImportBo(dto.getAttrName3(), dto.getAttrValue3()), new ProduceDataAttrImportBo(dto.getAttrName4(), dto.getAttrValue4()),
                        new ProduceDataAttrImportBo(dto.getAttrName5(), dto.getAttrValue5()), new ProduceDataAttrImportBo(dto.getAttrName6(), dto.getAttrValue6()),
                        new ProduceDataAttrImportBo(dto.getAttrName7(), dto.getAttrValue7()), new ProduceDataAttrImportBo(dto.getAttrName8(), dto.getAttrValue8()),
                        new ProduceDataAttrImportBo(dto.getAttrName9(), dto.getAttrValue9()), new ProduceDataAttrImportBo(dto.getAttrName10(), dto.getAttrValue10()),
                        new ProduceDataAttrImportBo(dto.getAttrName11(), dto.getAttrValue11()), new ProduceDataAttrImportBo(dto.getAttrName12(), dto.getAttrValue12()),
                        new ProduceDataAttrImportBo(dto.getAttrName13(), dto.getAttrValue13()), new ProduceDataAttrImportBo(dto.getAttrName14(), dto.getAttrValue14()),
                        new ProduceDataAttrImportBo(dto.getAttrName15(), dto.getAttrValue15()), new ProduceDataAttrImportBo(dto.getAttrName16(), dto.getAttrValue16()),
                        new ProduceDataAttrImportBo(dto.getAttrName17(), dto.getAttrValue17()), new ProduceDataAttrImportBo(dto.getAttrName18(), dto.getAttrValue18()),
                        new ProduceDataAttrImportBo(dto.getAttrName19(), dto.getAttrValue19()), new ProduceDataAttrImportBo(dto.getAttrName20(), dto.getAttrValue20()))
                .filter(attrImportBo -> StringUtils.isNotBlank(attrImportBo.getAttrName()) && StringUtils.isNotBlank(attrImportBo.getAttrValue()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(attrValueList)) {
            throw new ParamIllegalException("属性值不能为空");
        }
        for (ProduceDataAttrImportBo produceDataAttrImportBo : attrValueList) {
            // 校验长度
            if (StringUtils.isNotBlank(produceDataAttrImportBo.getAttrName()) && produceDataAttrImportBo.getAttrName().length() > 100) {
                throw new ParamIllegalException("属性名称长度不能超过 100 位");
            }
            if (StringUtils.isNotBlank(produceDataAttrImportBo.getAttrValue())) {
                String[] attrValueVerify = produceDataAttrImportBo.getAttrValue().split("/");
                for (String attrValue : attrValueVerify) {
                    if (StringUtils.isNotBlank(attrValue) && attrValue.length() > 200) {
                        throw new ParamIllegalException("属性值长度不能超过 200 位");
                    }
                }
            }
        }

        // 校验入参
        if (StringUtils.isBlank(sku)) {
            throw new ParamIllegalException("SKU不能为空");
        }
        if (sku.length() > 100) {
            throw new ParamIllegalException("SKU字符长度不能超过 100 位");
        }

        // 重量
        BigDecimal weight = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(weightStr)) {
            weight = ScmFormatUtil.bigDecimalFormat(weightStr);
            if (null == weight) {
                throw new ParamIllegalException("重量必须填写正确数值，请调整后再导入");
            }
            if (weight.scale() > 2) {
                throw new ParamIllegalException("重量必须保留两位小数，请调整后再导入");
            }

        }

        // 商品采购价格
        BigDecimal goodsPurchasePrice = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(goodsPurchasePriceStr)) {
            goodsPurchasePrice = ScmFormatUtil.bigDecimalFormat(goodsPurchasePriceStr);
            if (null == goodsPurchasePrice) {
                throw new ParamIllegalException("商品采购价格必须填写正确数值，请调整后再导入");
            }
            if (goodsPurchasePrice.scale() > 2) {
                throw new ParamIllegalException("商品采购价格必须保留两位小数，请调整后再导入");
            }
        }

        // 新增属性信息
        List<ProduceDataAttrPo> insertProduceDataAttrPoList = new ArrayList<>();

        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (plmSkuPo == null) {
            throw new BizException("SKU:{}还没同步到scm系统", sku);
        }
        if (StringUtils.isBlank(plmSkuPo.getSpu())) {
            throw new BizException("SKU:{}对应的SPU还没同步到scm系统", plmSkuPo.getSku());
        }

        String spu = plmSkuPo.getSpu();

        // 查sku类目
        Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(List.of(sku));
        if (!categoriesIdMap.containsKey(sku)) {
            throw new BizException("查询不到SKU:对应的分类信息", sku);
        }
        Long categoriesId = categoriesIdMap.get(sku);

        // 获取假发类下属性
        List<String> categoryAttrWigList = plmRemoteService.getCategoryAttr(produceDataBaseService.getWigAttributeNameId());

        // 获取sku下属性
        List<String> categoryAttrList = plmRemoteService.getCategoryAttr(categoriesId);

        List<String> attributeNameList = attrValueList.stream()
                .map(ProduceDataAttrImportBo::getAttrName)
                .distinct()
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(attributeNameList)) {
            throw new BizException("获取不到对应的导入表头的配置信息，请联系管理员！", sku);
        }

        // 查询旧生产属性
        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getBySku(sku);

        // 要删除的旧生产属性ID
        Set<Long> delProduceDataAttrPoIds = new HashSet<>();

        // 获取属性信息
        List<PlmAttributeVo> plmAttributeList = plmRemoteService.getAttrListByName(attributeNameList);

        // 不限制输入属性值的属性类型
        List<AttributeEntryType> attributeEntryTypeNotLimitList = List.of(AttributeEntryType.TEXT_INPUT,
                AttributeEntryType.PULL_INPUT_SINGLE,
                AttributeEntryType.PULL_INPUT);

        for (ProduceDataAttrImportBo produceDataAttrImportBo : attrValueList) {
            String attributeName = produceDataAttrImportBo.getAttrName();
            String attrValueStr = produceDataAttrImportBo.getAttrValue();

            if (!categoryAttrWigList.contains(attributeName)) {
                throw new ParamIllegalException("属性名称：{}在PDC的{}类目中不存在，请确认后再填写！", attributeName, produceDataBaseService.getWigAttributeName());
            }

            PlmAttributeVo plmAttributeVo = plmAttributeList.stream()
                    .filter(plmAttribute -> plmAttribute.getAttributeName().equals(attributeName))
                    .findFirst()
                    .orElse(null);
            if (plmAttributeVo == null) {
                throw new ParamIllegalException("属性名称：{}在PDC的{}类目中不存在，请确认后再填写！", attributeName, produceDataBaseService.getWigAttributeName());
            }

            if (BooleanType.FALSE.equals(plmAttributeVo.getState())) {
                throw new ParamIllegalException("属性名称：{}在PDC系统已关闭，请在PDC系统开启后再填写！", attributeName, produceDataBaseService.getWigAttributeName());
            }

            if (CollectionUtils.isEmpty(categoryAttrList) || !categoryAttrList.contains(plmAttributeVo.getAttributeName())) {
                continue;
            }

            String[] attrValueSplit = attrValueStr.split("/");
            if (AttributeEntryType.TEXT_INPUT.equals(plmAttributeVo.getEntryType())) {
                if (attrValueSplit.length > 1) {
                    throw new ParamIllegalException("属性名称：{}的录入类型是：{} 禁止输入多个值，请确认后再填写！", attributeName, AttributeEntryType.TEXT_INPUT.getRemark());
                }
            }
            if (AttributeEntryType.PULL_INPUT_SINGLE.equals(plmAttributeVo.getEntryType())) {
                if (attrValueSplit.length > 1) {
                    throw new ParamIllegalException("属性名称：{}的录入类型是：{} 禁止输入多个值，请确认后再填写！", attributeName, AttributeEntryType.PULL_INPUT_SINGLE.getRemark());
                }
            }
            if (AttributeEntryType.PULL_DOWN_SINGLE.equals(plmAttributeVo.getEntryType())) {
                if (attrValueSplit.length > 1) {
                    throw new ParamIllegalException("属性名称：{}的录入类型是：{} 禁止输入多个值，请确认后再填写！", attributeName, AttributeEntryType.PULL_DOWN_SINGLE.getRemark());
                }
            }

            for (String attrValue : attrValueSplit) {
                if (StringUtils.isNotBlank(attrValue)) {
                    if (!attributeEntryTypeNotLimitList.contains(plmAttributeVo.getEntryType())
                            && CollectionUtils.isNotEmpty(plmAttributeVo.getAttributeValueList())
                            && !plmAttributeVo.getAttributeValueList().contains(attrValue)) {
                        throw new ParamIllegalException("属性值：{}属性值在属性名称：{}中不存在，请确认后再填写！", attrValue, plmAttributeVo.getAttributeName());
                    }
                    // 删除旧文本记录
                    produceDataAttrPoList.stream()
                            .filter(produceDataAttrPo -> produceDataAttrPo.getAttributeNameId().equals(plmAttributeVo.getAttributeNameId()))
                            .forEach(produceDataAttrPo -> delProduceDataAttrPoIds.add(produceDataAttrPo.getProduceDataAttrId()));

                    insertProduceDataAttrPoList.add(ProduceDataConverter.attrImportationDtoToPo(spu, sku, plmAttributeVo.getAttributeNameId(), plmAttributeVo.getAttributeName(), attrValue));
                }
            }

        }

        //生产信息
        ProduceDataPo produceDataPo = produceDataDao.getBySku(sku);
        if (produceDataPo == null) {
            produceDataPo = new ProduceDataPo();
            produceDataPo.setSpu(spu);
            produceDataPo.setSku(sku);
            produceDataPo.setCategoryId(categoriesId);
            produceDataPo.setBindingProduceData(BindingProduceData.FALSE);
            produceDataPo.setRawManage(BooleanType.TRUE);
        }
        if (BigDecimal.ZERO.compareTo(weight) != 0) {
            produceDataPo.setWeight(weight);
        }
        if (BigDecimal.ZERO.compareTo(goodsPurchasePrice) != 0) {
            produceDataPo.setGoodsPurchasePrice(goodsPurchasePrice);
            produceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
        }

        // 采购商品价格为空或者等于0的情况,调用SKU定价表获取价格
        if (produceDataPo.getGoodsPurchasePrice() == null || produceDataPo.getGoodsPurchasePrice().compareTo(BigDecimal.ZERO) == 0) {
            produceDataBaseService.saveProduceDataUpdatePrice(List.of(produceDataPo), produceDataAttrPoList);
        }

        produceDataDao.insertOrUpdate(produceDataPo);

        if (CollectionUtils.isNotEmpty(delProduceDataAttrPoIds)) {
            produceDataAttrDao.removeBatchByIds(delProduceDataAttrPoIds);
        }

        produceDataAttrDao.insertBatch(insertProduceDataAttrPoList);

    }


    /**
     * 采购通过sku更新生产资料的商品采购价格（含事务提供Handler调用）
     *
     * @param boList:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/26 14:25
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsPurchasePriceBySkuHandler(List<ProduceDataUpdatePurchasePriceBo> boList) {
        produceDataBaseService.updateGoodsPurchasePriceBySkuBatch(boList);
    }

    /**
     * 生产属性导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/27 10:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportSkuAttr(ProduceDataSearchDto dto) {
        // 如果存在勾选，优先勾选
        if (CollectionUtils.isNotEmpty(dto.getSpuList())) {
            ProduceDataSearchDto checkDto = new ProduceDataSearchDto();
            checkDto.setSpuList(dto.getSpuList());
            dto = checkDto;
        }
        //条件过滤
        if (null == produceDataBaseService.getSearchProduceDataWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = this.getSkuAttrExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));

        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_PRODUCE_DATA_ATTR_EXPORT.getCode(), dto));
    }

    /**
     * 生产属性导出
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/6/27 10:17
     */
    public Integer getSkuAttrExportTotals(ProduceDataSearchDto dto) {
        //条件过滤
        if (null == produceDataBaseService.getSearchProduceDataWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        return plmSkuDao.getExportSkuAttrTotals(dto);
    }

    /**
     * 生产属性导出
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < ProduceDataExportSkuAttrVo>>
     * @author ChenWenLong
     * @date 2024/6/27 10:17
     */
    public CommonResult<ExportationListResultBo<ProduceDataExportSkuAttrVo>> getSkuAttrExportList(ProduceDataSearchDto dto) {
        ExportationListResultBo<ProduceDataExportSkuAttrVo> resultBo = new ExportationListResultBo<>();
        //条件过滤
        if (null == produceDataBaseService.getSearchProduceDataWhere(dto)) {
            return CommonResult.success(resultBo);
        }

        CommonPageResult.PageInfo<ProduceDataExportSkuAttrVo> pageResult = plmSkuDao.getSkuAttrExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<ProduceDataExportSkuAttrVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<String> skuList = records.stream()
                .map(ProduceDataExportSkuAttrVo::getSku)
                .distinct()
                .collect(Collectors.toList());

        //生产资料-销售属性
        List<ProduceDataExportSkuAttrVo> prodDataAttrExportVoList = skuProdBaseService.getProduceDataExportSkuAttrVoList(skuList);

        for (ProduceDataExportSkuAttrVo record : records) {
            prodDataAttrExportVoList.stream()
                    .filter(prodDataExportSkuAttrVo -> Objects.equals(prodDataExportSkuAttrVo.getSku(), record.getSku()))
                    .findFirst().ifPresent(prodDataExportSkuAttrVo -> {
                        record.setSkuEncode(prodDataExportSkuAttrVo.getSkuEncode());

                        record.setWeight(prodDataExportSkuAttrVo.getWeight());
                        record.setTolerance(prodDataExportSkuAttrVo.getTolerance());

                        record.setColor(prodDataExportSkuAttrVo.getColor());
                        record.setLaceArea(prodDataExportSkuAttrVo.getLaceArea());
                        record.setFileLengthSize(prodDataExportSkuAttrVo.getFileLengthSize());
                        record.setCompleteLongSize(prodDataExportSkuAttrVo.getCompleteLongSize());
                        record.setNetCapSize(prodDataExportSkuAttrVo.getNetCapSize());
                        record.setPartedBangs(prodDataExportSkuAttrVo.getPartedBangs());
                        record.setParting(prodDataExportSkuAttrVo.getParting());
                        record.setMaterial(prodDataExportSkuAttrVo.getMaterial());
                        record.setContour(prodDataExportSkuAttrVo.getContour());
                        record.setColorSystem(prodDataExportSkuAttrVo.getColorSystem());
                        record.setColorMixPartition(prodDataExportSkuAttrVo.getColorMixPartition());
                        record.setLeftSideLength(prodDataExportSkuAttrVo.getLeftSideLength());
                        record.setLeftFinish(prodDataExportSkuAttrVo.getLeftFinish());
                        record.setRightSideLength(prodDataExportSkuAttrVo.getRightSideLength());
                        record.setRightFinish(prodDataExportSkuAttrVo.getRightFinish());
                        record.setSymmetry(prodDataExportSkuAttrVo.getSymmetry());
                        record.setPreselectionLace(prodDataExportSkuAttrVo.getPreselectionLace());
                    });
        }
        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    /**
     * 获取类中带有 @ApiModelProperty 注解的字段名和描述信息，并存入 Map 中返回
     *
     * @param clazz 要获取字段信息的类的 Class 对象
     * @return 包含字段名和描述信息的 Map
     */
    public static Map<String, String> getFieldDescriptions(Class<?> clazz) {
        Map<String, String> fieldDescriptionMap = new HashMap<>();

        // 获取类中定义的所有字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 判断字段是否有 ApiModelProperty 注解
            if (field.isAnnotationPresent(ApiModelProperty.class)) {
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                String fieldName = field.getName();
                String description = apiModelProperty.value();
                // 将字段名和描述信息存入 Map
                fieldDescriptionMap.put(fieldName, description);
            }
        }

        return fieldDescriptionMap;
    }

    /**
     * 根据不同属性匹配赋值不同字段
     *
     * @param record:
     * @param fieldName:
     * @param value:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/26 17:49
     */
    private void setFieldValue(ProduceDataExportSkuAttrVo record, String fieldName, String value) {
        switch (fieldName) {
            case "color":
                record.setColor(value);
                break;
            case "laceArea":
                record.setLaceArea(value);
                break;
            case "fileLengthSize":
                record.setFileLengthSize(value);
                break;
            case "completeLongSize":
                record.setCompleteLongSize(value);
                break;
            case "netCapSize":
                record.setNetCapSize(value);
                break;
            case "partedBangs":
                record.setPartedBangs(value);
                break;
            case "parting":
                record.setParting(value);
                break;
            case "material":
                record.setMaterial(value);
                break;
            case "contour":
                record.setContour(value);
                break;
            case "colorSystem":
                record.setColorSystem(value);
                break;
            case "colorMixPartition":
                record.setColorMixPartition(value);
                break;
            case "leftSideLength":
                record.setLeftSideLength(value);
                break;
            case "leftFinish":
                record.setLeftFinish(value);
                break;
            case "rightSideLength":
                record.setRightSideLength(value);
                break;
            case "rightFinish":
                record.setRightFinish(value);
                break;
            case "symmetry":
                record.setSymmetry(value);
                break;
            case "preselectionLace":
                record.setPreselectionLace(value);
                break;
            default:
                throw new BizException("导出VO字段{}数据错误，系统匹配不到对应表头字段" + fieldName);
        }
    }

    /**
     * 生产信息头部信息保存
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/7/2 15:18
     */
    @Transactional(rollbackFor = Exception.class)
    public void topSave(ProduceDataSaveTopDto dto) {
        String sku = dto.getSku();
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (plmSkuPo == null) {
            throw new BizException("sku:{}还没同步到scm系统", sku);
        }
        if (StringUtils.isBlank(plmSkuPo.getSpu())) {
            throw new BizException("sku:{}对应的spu还没同步到scm系统", plmSkuPo.getSku());
        }

        String spu = plmSkuPo.getSpu();

        // spu表保存
        this.produceDataSpuPoSave(spu);

        // 生产信息保存
        List<ProduceDataItemRawPo> processRawPoList = new ArrayList<>();
        List<ProduceDataItemProcessPo> processPoList = new ArrayList<>();

        ProduceDataItemPo produceDataItemPo = produceDataItemDao.getOneBySku(sku);
        if (null != produceDataItemPo) {
            processRawPoList = produceDataItemRawDao.getByProduceDataItemId(produceDataItemPo.getProduceDataItemId());
            processPoList = produceDataItemProcessDao.getByProduceDataItemId(produceDataItemPo.getProduceDataItemId());
        }

        BindingProduceData bindingProduceData = BindingProduceData.FALSE;
        if (dto.getWeight() != null
                && CollectionUtils.isNotEmpty(processRawPoList)
                && CollectionUtils.isNotEmpty(processPoList)) {
            bindingProduceData = BindingProduceData.TRUE;
        }
        this.produceDataPoSave(dto, spu, sku, bindingProduceData, ProduceDataCreateType.EDIT);
    }

    /**
     * 生产信息主体信息保存
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/7/3 09:42
     */
    @Transactional(rollbackFor = Exception.class)
    public void bodySave(ProduceDataSaveBodyDto dto) {
        String sku = dto.getSku();
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        if (plmSkuPo == null) {
            throw new BizException("sku:{}还没同步到scm系统", sku);
        }
        if (StringUtils.isBlank(plmSkuPo.getSpu())) {
            throw new BizException("sku:{}对应的spu还没同步到scm系统", plmSkuPo.getSku());
        }

        Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(List.of(sku));
        if (!categoriesIdMap.containsKey(sku)) {
            throw new BizException("sku:{}查询不到对应的商品类目信息，请联系管理员！", sku);
        }

        // 验证原料入参是否存在重复
        if (CollectionUtils.isNotEmpty(dto.getProduceDataItemList())) {
            for (ProduceDataItemDto produceDataItemDto : dto.getProduceDataItemList()) {
                Set<String> skuRawSet = new HashSet<>();
                Optional.ofNullable(produceDataItemDto.getProduceDataItemRawList())
                        .orElse(new ArrayList<>())
                        .forEach(produceDataItemRaw -> {
                            if (skuRawSet.contains(produceDataItemRaw.getSku())) {
                                throw new BizException("原料列表禁止添加的重复的原料sku：{}", produceDataItemRaw.getSku());
                            } else {
                                skuRawSet.add(produceDataItemRaw.getSku());
                            }
                        });
            }
        }

        String spu = plmSkuPo.getSpu();
        List<ProduceDataItemDto> produceDataItemDtoList = Optional.ofNullable(dto.getProduceDataItemList())
                .orElse(new ArrayList<>());
        List<Long> produceDataItemDtoIdList = produceDataItemDtoList.stream()
                .map(ProduceDataItemDto::getProduceDataItemId)
                .collect(Collectors.toList());

        // 新增BOM的信息初始化新增ID,新增效果图、细节图使用主键ID
        for (ProduceDataItemDto produceDataItemDto : produceDataItemDtoList) {
            if (produceDataItemDto.getProduceDataItemId() == null) {
                // 新增重新赋值ID
                produceDataItemDto.setProduceDataItemId(idGenerateService.getSnowflakeId());
            }
        }

        // 查询已存在BOM信息
        List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySku(sku);

        // spu表保存
        ProduceDataSpuPo produceDataSpuPo = this.produceDataSpuPoSave(spu);

        // 检验版本号和是否绑定
        List<ProduceDataItemRawListDto> processRawPoDtoList = new ArrayList<>();
        List<ProduceDataItemProcessListDto> processPoDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(produceDataItemDtoList)) {
            processRawPoDtoList = produceDataItemDtoList.get(0).getProduceDataItemRawList();
            processPoDtoList = produceDataItemDtoList.get(0).getProduceDataItemProcessList();
            // 判断版本号是否一致
            for (ProduceDataItemDto produceDataItemDto : produceDataItemDtoList) {
                produceDataItemPoList.stream()
                        .filter(produceDataItemPo -> produceDataItemPo.getProduceDataItemId().equals(produceDataItemDto.getProduceDataItemId()))
                        .findFirst()
                        .ifPresent(itemPo -> Assert.isTrue(itemPo.getVersion().equals(produceDataItemDto.getVersion()), () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！")));
            }

        }

        // 生产信息的生产属性保存
        List<ProduceDataAttrDto> produceDataAttrList = Optional.ofNullable(dto.getProduceDataAttrList()).orElse(new ArrayList<>());
        this.produceDataAttrPoSave(produceDataAttrList, spu, sku);

        //生产信息保存
        ProduceDataPo produceDataPo = produceDataDao.getBySku(sku);
        if (produceDataPo == null) {
            produceDataPo = new ProduceDataPo();
            produceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
            produceDataPo.setSpu(spu);
            produceDataPo.setSku(sku);
            produceDataPo.setCategoryId(categoriesIdMap.get(sku));
            produceDataPo.setBindingProduceData(BindingProduceData.FALSE);
            produceDataPo.setRawManage(BooleanType.TRUE);

            List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getBySku(produceDataPo.getSku());
            produceDataBaseService.saveProduceDataUpdatePrice(List.of(produceDataPo), produceDataAttrPoList);
            produceDataDao.insert(produceDataPo);
        } else {
            if (produceDataPo.getWeight() != null
                    && CollectionUtils.isNotEmpty(processRawPoDtoList)
                    && CollectionUtils.isNotEmpty(processPoDtoList)) {
                produceDataPo.setBindingProduceData(BindingProduceData.TRUE);
            }
            Assert.isTrue(produceDataPo.getVersion().equals(dto.getVersion()),
                    () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));

            // 采购商品价格为空或者等于0的情况,调用SKU定价表获取价格
            if (produceDataPo.getGoodsPurchasePrice() == null || produceDataPo.getGoodsPurchasePrice().compareTo(BigDecimal.ZERO) == 0) {
                List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getBySku(produceDataPo.getSku());
                produceDataBaseService.saveProduceDataUpdatePrice(List.of(produceDataPo), produceDataAttrPoList);
            }
            produceDataDao.updateByIdVersion(produceDataPo);
        }

        // 封样图保存
        scmImageBaseService.removeAllImage(ImageBizType.SEAL_IMAGE, produceDataPo.getProduceDataId());
        if (CollectionUtils.isNotEmpty(dto.getSealImageFileCodeList())) {
            scmImageBaseService.insertBatchImage(dto.getSealImageFileCodeList(), ImageBizType.SEAL_IMAGE, produceDataPo.getProduceDataId());
        }

        // 规格书信息保存
        List<ProduceDataSpecDto> produceDataSpecList = Optional.ofNullable(dto.getProduceDataSpecList()).orElse(new ArrayList<>());
        this.produceDataSpecSave(produceDataSpecList, spu, sku);

        // 生产信息详情效果图、细节图的保存
        this.produceDataItemEffectDetailSave(produceDataItemDtoList, produceDataItemDtoIdList);

        // 生产信息SPU图片保存
        this.produceDataSpuImageSave(produceDataPo, produceDataSpuPo, produceDataItemDtoList);

        // BOM信息保存
        this.produceDataItemSave(produceDataItemDtoList, produceDataItemPoList, spu, sku);

    }

    public CommonPageResult<ProduceDataAttrSkuVo> getSkuAttrBySkuAndId(ProduceDataAttrSkuDto dto) {

        return new CommonPageResult<>(produceDataAttrDao.getPageBySkuList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto));
    }
}
