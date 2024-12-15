package com.hete.supply.scm.server.scm.service.init;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.cost.dao.CostOfGoodsDao;
import com.hete.supply.scm.server.scm.dao.*;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopPricingOrderBaseService;
import com.hete.supply.scm.server.scm.entity.po.*;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/2/26 13:51
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProduceDataInitService {

    private final ProduceDataDao produceDataDao;
    private final ProduceDataItemDao produceDataItemDao;
    private final ProduceDataSpecDao produceDataSpecDao;
    private final PlmRemoteService plmRemoteService;
    private final DevelopPricingOrderBaseService developPricingOrderBaseService;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PlmSkuDao plmSkuDao;
    private final CostOfGoodsDao costOfGoodsDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final ProduceDataAttrDao produceDataAttrDao;
    private final ProduceDataBaseService produceDataBaseService;

    /**
     * 页码
     */
    private final static Integer LOOP_SIZE = 50;

    /**
     * BOM命名前缀
     */
    private final static String BOM_PREFIX = "BOM";


    @Transactional(rollbackFor = Exception.class)
    public void initProduceDataItemBomTask() {
        final Long totalsCnt = produceDataDao.getTotalsCnt();
        for (int i = 1; i <= totalsCnt / LOOP_SIZE + 1; i++) {
            List<ProduceDataItemPo> updateProduceDataItemPoList = new ArrayList<>();
            final IPage<ProduceDataPo> pageResult = produceDataDao.getByPage(PageDTO.of(i, LOOP_SIZE));
            final List<ProduceDataPo> records = pageResult.getRecords();

            final List<String> skuList = records.stream()
                    .map(ProduceDataPo::getSku)
                    .distinct()
                    .collect(Collectors.toList());

            final List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySkuList(skuList);

            Map<String, List<ProduceDataItemPo>> produceDataItemPoMap = produceDataItemPoList.stream()
                    .collect(Collectors.groupingBy(ProduceDataItemPo::getSku));

            produceDataItemPoMap.forEach((String sku, List<ProduceDataItemPo> poList) -> {
                for (int ii = 0; ii < poList.size(); ii++) {
                    ProduceDataItemPo produceDataItemPo = poList.get(ii);
                    ProduceDataItemPo updateProduceDataItemPo = new ProduceDataItemPo();
                    updateProduceDataItemPo.setProduceDataItemId(produceDataItemPo.getProduceDataItemId());
                    updateProduceDataItemPo.setBomName(BOM_PREFIX + (ii + 1));
                    updateProduceDataItemPoList.add(updateProduceDataItemPo);
                }
            });

            produceDataItemDao.updateBatchById(updateProduceDataItemPoList);

        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void initProduceDataGoodsPriceTask() {


        throw new BizException("初始化生产信息商品采购价格初始化失败");

    }

    @Transactional(rollbackFor = Exception.class)
    public void initProduceDataSpecTask() {
        List<ProduceDataSpecPo> produceDataSpecInit = produceDataSpecDao.getProduceDataSpecInit();
        if (CollectionUtils.isEmpty(produceDataSpecInit)) {
            return;
        }
        List<String> skuList = produceDataSpecInit.stream().map(ProduceDataSpecPo::getSku).distinct().collect(Collectors.toList());
        //生产信息
        Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(skuList);

        for (ProduceDataSpecPo produceDataSpecPo : produceDataSpecInit) {
            ProduceDataPo produceDataPo = produceDataDao.getBySku(produceDataSpecPo.getSku());
            if (produceDataPo == null) {
                ProduceDataPo insertProduceDataPo = new ProduceDataPo();
                insertProduceDataPo.setSpu(produceDataSpecPo.getSpu());
                insertProduceDataPo.setSku(produceDataSpecPo.getSku());
                insertProduceDataPo.setCategoryId(categoriesIdMap.get(produceDataSpecPo.getSku()));
                insertProduceDataPo.setBindingProduceData(BindingProduceData.FALSE);
                insertProduceDataPo.setRawManage(BooleanType.TRUE);
                produceDataDao.insert(insertProduceDataPo);
            }
        }


    }

    @Transactional(rollbackFor = Exception.class)
    public void initProduceDataWeightTask() {
        // 获取sku蕾丝面积与长度
        Long laceAreaAttributeNameId = produceDataBaseService.getLaceAreaAttributeNameId();
        Long lengthAttributeNameId = produceDataBaseService.getLengthAttributeNameId();
        List<Long> attributeNameIds = Arrays.asList(laceAreaAttributeNameId, lengthAttributeNameId);
        CommonPageResult.PageInfo<String> pageResult = produceDataAttrDao.selectProduceDataAttrWeightPage(PageDTO.of(1, LOOP_SIZE), attributeNameIds);
        final List<String> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        for (int i = 1; i <= pageResult.getTotalCount() / LOOP_SIZE + 1; i++) {
            List<ProduceDataPo> updateProduceDataPoList = new ArrayList<>();
            final CommonPageResult.PageInfo<String> skuPageInfo = produceDataAttrDao.selectProduceDataAttrWeightPage(PageDTO.of(i, LOOP_SIZE), attributeNameIds);
            final List<String> skuList = skuPageInfo.getRecords();
            final Map<String, ProduceDataPo> produceDataPoMap = produceDataDao.getMapBySkuList(skuList);
            // 获取蕾丝面积和裆长尺寸生产属性对应sku
            Map<String, List<String>> attrMatchSkuMap = produceDataBaseService.getAttrMatchSkuListMap(skuList);
            for (String sku : skuList) {
                ProduceDataPo produceDataPo = produceDataPoMap.get(sku);
                if (produceDataPo == null) {
                    log.warn("初始化sku:{}生产属性在生产信息主表produce_data查询不到！", sku);
                    continue;
                }

                List<String> allSkuList = attrMatchSkuMap.get(sku);
                if (CollectionUtils.isEmpty(allSkuList)) {
                    log.info("初始化sku:{}通过生产属性值匹配不到对应sku", sku);
                    continue;
                }

                List<ProduceDataPo> produceDataPoAllSkuSetList = produceDataDao.getListBySkuList(allSkuList);
                List<ProduceDataPo> produceDataPoAverageWeight = produceDataPoAllSkuSetList.stream()
                        .filter(produceDataPoAllSkuSet -> produceDataPoAllSkuSet.getWeight() != null && BigDecimal.ZERO.compareTo(produceDataPoAllSkuSet.getWeight()) < 0)
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(produceDataPoAverageWeight)) {
                    log.info("初始化sku:{}通过查询参考sku的重量为空，参考sku的po={}", sku, JacksonUtil.parse2Str(produceDataPoAllSkuSetList));
                    continue;
                }
                BigDecimal weight = produceDataPoAverageWeight.stream()
                        .map(ProduceDataPo::getWeight)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(produceDataPoAverageWeight.size()), 2, RoundingMode.DOWN);

                if (BigDecimal.ZERO.compareTo(weight) == 0) {
                    log.info("初始化sku:{}通过查询参考sku的重量为0，参考sku的po={}", sku, JacksonUtil.parse2Str(produceDataPoAverageWeight));
                    continue;
                }

                ProduceDataPo updateProduceDataPo = new ProduceDataPo();
                updateProduceDataPo.setProduceDataId(produceDataPo.getProduceDataId());
                updateProduceDataPo.setWeight(weight);
                updateProduceDataPoList.add(updateProduceDataPo);
                log.info("初始化sku:{}更新成功重量{}", sku, weight);
            }
            produceDataDao.updateBatchById(updateProduceDataPoList);

        }


    }

    /**
     * 初始化生产属性的数据
     *
     * @author ChenWenLong
     * @date 2024/9/13 16:33
     */
    @Transactional(rollbackFor = Exception.class)
    public void initProduceDataAttrTask() {
        final Long totalsCnt = plmSkuDao.getTotalsCnt();
        for (int i = 1; i <= totalsCnt / LOOP_SIZE + 1; i++) {
            final IPage<PlmSkuPo> pageResult = plmSkuDao.getByPage(PageDTO.of(i, LOOP_SIZE));
            final List<PlmSkuPo> records = pageResult.getRecords();

            final List<String> skuList = records.stream()
                    .map(PlmSkuPo::getSku)
                    .distinct()
                    .collect(Collectors.toList());

            Map<String, List<ProduceDataAttrPo>> produceDataAttrPoMap = produceDataAttrDao.getMapBySkuList(skuList);

            List<String> updateSkuList = new ArrayList<>();
            for (PlmSkuPo plmSkuPo : records) {
                List<ProduceDataAttrPo> produceDataAttrPos = produceDataAttrPoMap.get(plmSkuPo.getSku());
                // 存在就不初始化
                // produceDataAttrPos过滤attrValue不为空的
                List<ProduceDataAttrPo> produceDataAttrPoFilterList = Optional.ofNullable(produceDataAttrPos)
                        .orElse(new ArrayList<>())
                        .stream()
                        .filter(produceDataAttrPo -> StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(produceDataAttrPoFilterList)) {
                    log.info("初始化sku:{}生产属性=>{}已存在，不进行初始化", plmSkuPo.getSku(), produceDataAttrPos);
                    continue;
                }
                updateSkuList.add(plmSkuPo.getSku());

            }
            // 先删除旧空值数据
            List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getListBySkuList(updateSkuList);
            if (CollectionUtils.isNotEmpty(produceDataAttrPoList)) {
                List<Long> delIdList = produceDataAttrPoList.stream()
                        .map(ProduceDataAttrPo::getProduceDataAttrId)
                        .collect(Collectors.toList());
                produceDataAttrDao.removeBatchByIds(delIdList);
            }
            // 需要进行处理sku生产属性
            produceDataBaseService.updateSkuProduceDataAttr(updateSkuList);

        }

    }


}
