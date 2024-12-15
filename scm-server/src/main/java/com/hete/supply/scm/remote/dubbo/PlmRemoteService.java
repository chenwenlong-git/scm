package com.hete.supply.scm.remote.dubbo;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.plm.api.basedata.entity.dto.CategoryNameDto;
import com.hete.supply.plm.api.basedata.entity.dto.CategorySearchDto;
import com.hete.supply.plm.api.basedata.entity.dto.PlmAttributeDto;
import com.hete.supply.plm.api.basedata.entity.dto.PlmCategoryGradeDto;
import com.hete.supply.plm.api.basedata.entity.vo.ObtainCategoryVo;
import com.hete.supply.plm.api.basedata.entity.vo.PlmAttributeVo;
import com.hete.supply.plm.api.basedata.entity.vo.PlmCategoryAttributeVo;
import com.hete.supply.plm.api.basedata.facade.BaseDataFacade;
import com.hete.supply.plm.api.basedata.facade.CategoryFacade;
import com.hete.supply.plm.api.developorder.entity.dto.PlmDevelopOrderSearchDto;
import com.hete.supply.plm.api.developorder.entity.vo.PlmDevelopOrderDetailVo;
import com.hete.supply.plm.api.developorder.facade.DevelopOrderFacade;
import com.hete.supply.plm.api.goods.entity.dto.*;
import com.hete.supply.plm.api.goods.entity.vo.*;
import com.hete.supply.plm.api.goods.facade.GoodsManegeFacade;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.core.util.CollSplitUtil;
import com.hete.support.dubbo.util.DubboResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/11/30 10:26
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class PlmRemoteService {
    @DubboReference(check = false)
    private GoodsManegeFacade goodsManegeFacade;
    @DubboReference(check = false)
    private CategoryFacade categoryFacade;
    @DubboReference(check = false)
    private DevelopOrderFacade developOrderFacade;

    @DubboReference(check = false)
    private BaseDataFacade baseDataFacade;

    private static final Integer SECOND_LEVEL = 2;
    private static final Integer FIRST_LEVEL = 1;

    /**
     * 通过分类 id，获取 sku
     *
     * @param categoryId
     * @return
     */
    public List<String> getSkuByCategoryId(Long categoryId) {
        if (categoryId == null) {
            return Collections.emptyList();
        }
        PlmCategoryDto plmCategoryDto = new PlmCategoryDto();
        plmCategoryDto.setCategoryId(categoryId);
        CommonResult<PlmSkuSpuVo> result = goodsManegeFacade.getSkuByCategory(plmCategoryDto);
        PlmSkuSpuVo data = DubboResponseUtil.checkCodeAndGetData(result);
        return data.getSkuCodeList();
    }

    /**
     * 通过品类查询本身及其子类
     *
     * @param categoryId
     * @return
     */
    public List<Long> getChildByCategoryId(Long categoryId) {
        if (categoryId == null) {
            return Collections.emptyList();
        }
        CategorySearchDto categorySearchDto = new CategorySearchDto();
        categorySearchDto.setCategoryId(categoryId);
        CommonResult<ResultList<Long>> result = categoryFacade.getChildrenCategoryId(categorySearchDto);
        ResultList<Long> data = DubboResponseUtil.checkCodeAndGetData(result);
        return new ArrayList<>(Optional.ofNullable(data)
                .map(ResultList::getList)
                .orElse(Collections.emptyList()));

    }

    /**
     * 通过 sku 获取分类 id
     *
     * @param sku
     * @return
     */
    public List<Long> getCategoriesBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        ArrayList<Long> categoryIds = new ArrayList<>();
        ArrayList<String> skus = new ArrayList<>();
        skus.add(sku);
        PlmSkuDto plmSkuDto = new PlmSkuDto();
        plmSkuDto.setSkuCodeList(skus);
        CommonResult<ResultList<PlmGoodsDetailVo>> result = goodsManegeFacade.getGoodsDetail(plmSkuDto);
        ResultList<PlmGoodsDetailVo> data = DubboResponseUtil.checkCodeAndGetData(result);
        Optional.ofNullable(data)
                .map(ResultList::getList)
                .orElse(Collections.emptyList())
                .forEach(item -> {
                    item.getCategoryList()
                            .forEach(item2 -> {
                                categoryIds.add(item2.getCategoryId());
                            });
                });

        return categoryIds;

    }

    /**
     * 通过sku获取spu
     *
     * @param sku
     * @return
     */
    public String getSpuBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return null;
        }
        PlmSkuDto plmSkuDto = new PlmSkuDto();
        plmSkuDto.setSkuCodeList(Collections.singletonList(sku));
        CommonResult<ResultList<PlmGoodsDetailVo>> result = goodsManegeFacade.getGoodsDetail(plmSkuDto);
        ResultList<PlmGoodsDetailVo> data = DubboResponseUtil.checkCodeAndGetData(result);
        final List<PlmGoodsDetailVo> plmGoodsDetailVoList = Optional.ofNullable(data)
                .map(ResultList::getList)
                .orElse(Collections.emptyList());

        if (CollectionUtils.isEmpty(plmGoodsDetailVoList)) {
            return null;
        }

        return plmGoodsDetailVoList.stream()
                .findFirst()
                .get()
                .getSpuCode();
    }

    /**
     * 根据sku获取spu，key：sku，value：spu
     *
     * @param skuList
     * @return
     */
    public Map<String, String> getSpuMapBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return new HashMap<>();
        }
        PlmSkuDto plmSkuDto = new PlmSkuDto();
        final List<String> distinctSkuList = skuList.stream()
                .distinct()
                .collect(Collectors.toList());
        plmSkuDto.setSkuCodeList(distinctSkuList);
        CommonResult<ResultList<PlmGoodsDetailVo>> result = goodsManegeFacade.getGoodsDetail(plmSkuDto);
        ResultList<PlmGoodsDetailVo> data = DubboResponseUtil.checkCodeAndGetData(result);
        final List<PlmGoodsDetailVo> plmGoodsDetailVoList = Optional.ofNullable(data)
                .map(ResultList::getList)
                .orElse(Collections.emptyList());

        if (CollectionUtils.isEmpty(plmGoodsDetailVoList)) {
            return new HashMap<>();
        }
        Map<String, String> map = new HashMap<>();

        plmGoodsDetailVoList.forEach(vo -> {
            List<String> skuCodeList = vo.getSkuCodeList();
            final String spuCode = vo.getSpuCode();
            skuCodeList.forEach(sku -> map.put(sku, spuCode));
        });

        return map;

    }

    /**
     * 通过 spu 获取分类信息
     *
     * @param spuList
     * @return
     */
    public List<PlmGoodsDetailVo> getCategoriesBySpu(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }
        spuList = spuList.stream()
                .distinct()
                .collect(Collectors.toList());
        List<PlmGoodsDetailVo> plmVariantVos = CollSplitUtil.collSplitExec(spuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSpuDto plmSpuDto = new PlmSpuDto();
                    plmSpuDto.setSpuCodeList(list);
                    return goodsManegeFacade.getGoodsDetail(plmSpuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向wms获取分类信息异常,降级为null");

        return Optional.ofNullable(plmVariantVos)
                .orElse(Collections.emptyList());

    }


    /**
     * 通过 sku 获取分类信息
     *
     * @param skuList
     * @return
     */
    public List<PlmGoodsDetailVo> getCategoriesBySku(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        skuList = skuList.stream()
                .distinct()
                .collect(Collectors.toList());
        List<PlmGoodsDetailVo> plmVariantVos = CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuDto plmSkuDto = new PlmSkuDto();
                    plmSkuDto.setSkuCodeList(list);
                    return goodsManegeFacade.getGoodsDetail(plmSkuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向wms获取分类信息异常,降级为null");

        return Optional.ofNullable(plmVariantVos)
                .orElse(Collections.emptyList());

    }

    /**
     * 通过 spu 获取分类信息
     *
     * @param spuList
     * @return
     */
    public Map<String, String> getSpuCategoriesMapBySpuList(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return new HashMap<>();
        }
        List<PlmGoodsDetailVo> plmGoodsDetailVos = CollSplitUtil.collSplitExec(spuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSpuDto plmSpuDto = new PlmSpuDto();
                    plmSpuDto.setSpuCodeList(list);
                    return goodsManegeFacade.getGoodsDetail(plmSpuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向wms获取分类信息异常,降级为null");

        return Optional.ofNullable(plmGoodsDetailVos)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(PlmGoodsDetailVo::getSpuCode, vo -> Optional.ofNullable(vo.getCategoryList())
                        .orElse(Collections.emptyList())
                        .stream()
                        .sorted(Comparator.comparing(PlmCategoryVo::getLevel))
                        .map(PlmCategoryVo::getCategoryName)
                        .collect(Collectors.joining("/"))));

    }

    public Map<String, PlmCategoryVo> getSkuSecondCategoriesVoMapBySkuList(List<String> skuList) {
        Map<String, PlmCategoryVo> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(skuList)) {
            return resultMap;
        }
        List<PlmGoodsDetailVo> plmGoodsDetailVos = CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuDto plmSkuDto = new PlmSkuDto();
                    plmSkuDto.setSkuCodeList(list);
                    return goodsManegeFacade.getGoodsDetail(plmSkuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向wms获取分类信息异常,降级为null");

        for (PlmGoodsDetailVo plmGoodsDetailVo : plmGoodsDetailVos) {
            final List<String> skuCodeList = plmGoodsDetailVo.getSkuCodeList();
            final List<PlmCategoryVo> categoryList = plmGoodsDetailVo.getCategoryList();

            for (String sku : skuCodeList) {
                PlmCategoryVo plmCategoryVo = categoryList.stream()
                        .filter(vo -> SECOND_LEVEL.equals(vo.getLevel()))
                        .findFirst()
                        .orElse(null);

                if (null == plmCategoryVo) {
                    plmCategoryVo = categoryList.stream()
                            .filter(vo -> FIRST_LEVEL.equals(vo.getLevel()))
                            .findFirst()
                            .orElse(new PlmCategoryVo());
                }

                resultMap.put(sku, plmCategoryVo);
            }
        }

        return resultMap;
    }

    /**
     * 通过分类名称查询分类
     *
     * @param categoryName
     * @return
     */
    public ObtainCategoryVo getCategoryByName(String categoryName) {
        if (StringUtils.isBlank(categoryName)) {
            return new ObtainCategoryVo();
        }
        CategoryNameDto categoryNameDto = new CategoryNameDto();
        categoryNameDto.setCategoryName(categoryName);
        CommonResult<ObtainCategoryVo> result = categoryFacade.getCategoryName(categoryNameDto);
        return DubboResponseUtil.checkCodeAndGetData(result);
    }

    public List<PlmGoodsDetailVo> getGoodsDetail(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        skuList = skuList.stream()
                .distinct()
                .collect(Collectors.toList());
        List<PlmGoodsDetailVo> plmGoodsDetailVos = CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuDto plmSkuDto = new PlmSkuDto();
                    plmSkuDto.setSkuCodeList(list);
                    return goodsManegeFacade.getGoodsDetail(plmSkuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向wms获取sku详情异常,降级为null");

        return Optional.ofNullable(plmGoodsDetailVos)
                .orElse(Collections.emptyList());
    }

    public List<String> getSkuList(ComPageDto dto) {
        if (dto.getPageNo() == null || dto.getPageSize() == null) {
            return Collections.emptyList();
        }
        final CommonPageResult<String> result = goodsManegeFacade.getSkuCode(dto);
        final CommonPageResult.PageInfo<String> pageInfo = DubboResponseUtil.checkCodeAndGetData(result);

        return pageInfo.getRecords();
    }

    /**
     * 获取 sku 生产图片
     *
     * @param skuList
     * @return
     */
    public List<PlmSkuImage> getSkuImage(List<String> skuList,
                                         String platform) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        skuList = skuList.stream()
                .distinct()
                .collect(Collectors.toList());
        List<PlmSkuImage> plmSkuImages = CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuPlatDto plmSkuPlatDto = new PlmSkuPlatDto();
                    plmSkuPlatDto.setPlatCode(platform);
                    plmSkuPlatDto.setSkuCodeList(list);
                    return goodsManegeFacade.getSkuImage(plmSkuPlatDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向plm获取sku生产图片异常,降级为null");

        return Optional.ofNullable(plmSkuImages)
                .orElse(Collections.emptyList());
    }

    /**
     * 根据sku获取变体属性
     *
     * @param skuList
     * @return
     */
    public List<PlmVariantVo> getVariantAttr(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        skuList = skuList.stream()
                .distinct()
                .collect(Collectors.toList());
        List<PlmVariantVo> plmVariantVos = CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuDto plmSkuDto = new PlmSkuDto();
                    plmSkuDto.setSkuCodeList(list);
                    return goodsManegeFacade.getVariantAttr(plmSkuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向wms获取变体属性异常,降级为null");

        return Optional.ofNullable(plmVariantVos)
                .orElse(Collections.emptyList());
    }

    /**
     * 根据品类id获取生产属性key值
     *
     * @param categoryId
     * @return
     */
    public List<String> getCategoryAttr(Long categoryId) {
        if (null == categoryId) {
            return Collections.emptyList();
        }
        CategorySearchDto categorySearchDto = new CategorySearchDto();
        categorySearchDto.setCategoryId(categoryId);
        final CommonResult<PlmCategoryAttributeVo> result = categoryFacade.getCategoryAttr(categorySearchDto);
        final PlmCategoryAttributeVo plmCategoryAttributeVo = DubboResponseUtil.checkCodeAndGetData(result);
        if (null == plmCategoryAttributeVo) {
            return Collections.emptyList();
        }
        return plmCategoryAttributeVo.getAttributeName();
    }


    /**
     * 根据sku获取产品名称
     *
     * @param skuList
     * @author ChenWenLong
     * @date 2023/4/20 10:23
     */
    public List<PlmSkuVo> getSkuEncodeBySku(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        final List<String> distinctSkuList = skuList.stream()
                .distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(distinctSkuList)) {
            return Collections.emptyList();
        }

        List<PlmSkuVo> plmSkuVoList = CollSplitUtil.collSplitExec(distinctSkuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuDto plmSkuDto = new PlmSkuDto();
                    plmSkuDto.setSkuCodeList(list);
                    return goodsManegeFacade.getSkuEncodeBySku(plmSkuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向PLM获取产品名称信息异常,降级为null");

        return Optional.ofNullable(plmSkuVoList)
                .orElse(Collections.emptyList());
    }

    /**
     * 根据sku获取产品名称map,key-->sku,value-->skuEncode
     *
     * @param skuList
     * @return
     */
    public Map<String, String> getSkuEncodeMapBySku(List<String> skuList) {
        return this.getSkuEncodeBySku(skuList)
                .stream()
                .collect(Collectors.toMap(PlmSkuVo::getSkuCode, PlmSkuVo::getSkuEncode));
    }

    /**
     * 根据产品名称获取sku
     *
     * @param skuEncodeList
     * @author ChenWenLong
     * @date 2023/4/20 10:23
     */
    public List<PlmSkuVo> getSkuBySkuEncode(List<String> skuEncodeList) {
        if (CollectionUtils.isEmpty(skuEncodeList)) {
            return Collections.emptyList();
        }
        skuEncodeList = skuEncodeList.stream()
                .distinct()
                .collect(Collectors.toList());
        List<PlmSkuVo> plmSkuVos = CollSplitUtil.collSplitExec(skuEncodeList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuEncodeDto plmSkuEncodeDto = new PlmSkuEncodeDto();
                    plmSkuEncodeDto.setSkuEncodeList(list);
                    return goodsManegeFacade.getSkuBySkuEncode(plmSkuEncodeDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向PLM获取sku信息异常,降级为null");

        return Optional.ofNullable(plmSkuVos)
                .orElse(Collections.emptyList());
    }

    /**
     * 根据产品名称获取sku
     *
     * @param skuEncodeList
     * @author ChenWenLong
     * @date 2023/4/20 10:23
     */
    public List<String> getSkuStrListBySkuEncode(List<String> skuEncodeList) {
        return this.getSkuBySkuEncode(skuEncodeList)
                .stream()
                .map(PlmSkuVo::getSkuCode)
                .collect(Collectors.toList());
    }

    /**
     * 根据品类id列表获取对应的sku列表
     *
     * @param categoryIdList
     * @return
     */
    public List<String> getSkuListByCategoryIdList(@Size(max = 10) List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Collections.emptyList();
        }
        final PlmCategoryIdListDto plmCategoryIdListDto = new PlmCategoryIdListDto();
        plmCategoryIdListDto.setCategoryIdList(categoryIdList);
        final CommonResult<PlmSkuSpuVo> result = goodsManegeFacade.getSkuByCategory(plmCategoryIdListDto);


        PlmSkuSpuVo data = DubboResponseUtil.checkCodeAndGetData(result);
        return data.getSkuCodeList();
    }

    /**
     * 根据sku获取sku信息
     *
     * @param skuList
     * @return
     */
    public List<PlmGoodsSkuVo> getSkuDetailListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }

        return CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME, splitList -> {
            PlmSkuDto plmPlatSkuDto = new PlmSkuDto();
            plmPlatSkuDto.setSkuCodeList(splitList);
            return goodsManegeFacade.getSkuInfo(plmPlatSkuDto);
        }, result -> DubboResponseUtil.checkCodeAndGetData(result).getList(), "根据sku获取sku的信息失败！");

    }

    /**
     * 通过开发母单和子单查询对应信息
     *
     * @param dto:
     * @return PlmDevelopOrderDetailVo
     * @author ChenWenLong
     * @date 2023/8/12 10:56
     */
    public PlmDevelopOrderDetailVo getDevelopOrderDetail(PlmDevelopOrderSearchDto dto) {
        if (StringUtils.isBlank(dto.getDevelopParentOrderNo()) || CollectionUtils.isEmpty(dto.getDevelopChildOrderNoList())) {
            return new PlmDevelopOrderDetailVo();
        }
        CommonResult<PlmDevelopOrderDetailVo> result = developOrderFacade.getDevelopOrderDetail(dto);
        return DubboResponseUtil.checkCodeAndGetData(result);

    }

    /**
     * 通过平台和SKU编码获取商品信息。
     *
     * @param platSkuListDto 包含平台和SKU信息的数据传输对象。
     * @return 包含商品信息的 PlmPlatSkuVo 列表。
     */
    public List<PlmPlatSkuVo> getPlatSkuByPlatAndCode(PlmPlatSkuListDto platSkuListDto) {
        if (CollectionUtils.isEmpty(platSkuListDto.getPlatSkuDtoList())) {
            return Collections.emptyList();
        }
        List<@NotNull PlmPlatSkuDto> platSkuDtoList = platSkuListDto.getPlatSkuDtoList();
        return CollSplitUtil.collSplitExec(platSkuDtoList, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME,
                splitList -> {
                    PlmPlatSkuListDto splitParam = new PlmPlatSkuListDto();
                    splitParam.setPlatSkuDtoList(splitList);
                    return goodsManegeFacade.getPlatSkuByPlatAndCode(splitParam);
                }, result -> {
                    if (Objects.isNull(result)) {
                        // 无法获取结果返回空的列表
                        return Collections.emptyList();
                    }
                    return DubboResponseUtil.checkCodeAndGetData(result)
                            .getList();
                }, "根据平台sku获取平台商品信息失败！");
    }

    /**
     * 通过 sku 获取具体分类ID
     *
     * @param skuList
     * @return
     */
    public Map<String, Long> getCategoriesIdBySku(List<String> skuList) {
        Map<String, Long> skuToCategoryIdMap = new HashMap<>();
        if (CollectionUtils.isEmpty(skuList)) {
            return skuToCategoryIdMap;
        }
        skuList = skuList.stream()
                .distinct()
                .collect(Collectors.toList());
        List<PlmGoodsDetailVo> plmVariantVos = CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuDto plmSkuDto = new PlmSkuDto();
                    plmSkuDto.setSkuCodeList(list);
                    return goodsManegeFacade.getGoodsDetail(plmSkuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向wms获取分类信息异常,降级为null");

        List<PlmGoodsDetailVo> plmGoodsDetailVoList = Optional.ofNullable(plmVariantVos)
                .orElse(Collections.emptyList());

        plmGoodsDetailVoList.forEach(skuCategories -> {
            skuCategories.getSkuCodeList()
                    .forEach(sku -> {
                        Optional.ofNullable(skuCategories.getCategoryList())
                                .orElse(Collections.emptyList())
                                .stream()
                                .max(Comparator.comparing(PlmCategoryVo::getLevel))
                                .ifPresent(vo -> skuToCategoryIdMap.put(sku, vo.getCategoryId()));
                    });
        });
        return skuToCategoryIdMap;

    }

    /**
     * 获取SKU所有分类名称的方法
     *
     * @param querySkuList 包含SKU编码的列表
     * @return 以SKU编码为键，末级分类名称为值的Map
     */
    public Map<String, List<Map<Integer, String>>> getSkuCategories(List<String> querySkuList) {
        // 用于存储SKU编码和对应的末级分类名称的映射
        Map<String, List<Map<Integer, String>>> skuCategoryMap = new HashMap<>(querySkuList.size());

        if (CollectionUtils.isEmpty(querySkuList)) {
            return skuCategoryMap;
        }
        // 通过线程池并行获取商品详细信息
        List<PlmGoodsDetailVo> plmGoodsDetailVos = CollSplitUtil.collSplitExec(querySkuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME,
                cuttingSkuList -> {
                    PlmSkuDto plmSkuDto
                            = new PlmSkuDto();
                    plmSkuDto.setSkuCodeList(
                            cuttingSkuList);
                    return goodsManegeFacade.getGoodsDetail(
                            plmSkuDto);
                },
                result -> DubboResponseUtil.checkCodeAndGetData(
                                result)
                        .getList(),
                "获取plm分类信息返回为空！");

        // 如果商品详细信息为空，则直接返回空的映射
        if (CollectionUtils.isEmpty(plmGoodsDetailVos)) {
            return skuCategoryMap;
        }

        // 遍历查询的SKU编码列表
        for (String querySku : querySkuList) {
            // 在商品详细信息中查找匹配的SKU
            PlmGoodsDetailVo matchSku = plmGoodsDetailVos.stream()
                    .filter(plmGoodsDetailVo -> CollectionUtils.isNotEmpty(plmGoodsDetailVo.getSkuCodeList())
                            && plmGoodsDetailVo.getSkuCodeList()
                            .contains(querySku))
                    .findFirst()
                    .orElse(null);

            // 如果找到匹配的SKU
            if (Objects.nonNull(matchSku)) {
                List<PlmCategoryVo> categoryList = matchSku.getCategoryList();

                // 如果商品分类列表不为空
                if (CollectionUtils.isNotEmpty(categoryList)) {
                    List<Map<Integer, String>> categoryMaps = categoryList.stream()
                            .map(category -> {
                                Map<Integer, String> categoryMap = new HashMap<>(categoryList.size());
                                categoryMap.put(category.getLevel(), category.getCategoryNameCn());
                                return categoryMap;
                            })
                            .collect(Collectors.toList());
                    // 将SKU编码和分类信息放入映射中
                    skuCategoryMap.put(querySku, categoryMaps);
                }
            }
        }

        // 返回SKU编码和末级分类名称的映射
        return skuCategoryMap;
    }

    /**
     * 通过 sku 获取二级分类信息，没有二级则返回一级 中文名称
     *
     * @param skuList
     * @return
     */
    public Map<String, String> getSkuSecondCategoriesCnMapBySkuList(List<String> skuList) {
        Map<String, String> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(skuList)) {
            return resultMap;
        }
        List<PlmGoodsDetailVo> plmGoodsDetailVos = CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuDto plmSkuDto = new PlmSkuDto();
                    plmSkuDto.setSkuCodeList(list);
                    return goodsManegeFacade.getGoodsDetail(plmSkuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向wms获取分类信息异常,降级为null");

        for (PlmGoodsDetailVo plmGoodsDetailVo : plmGoodsDetailVos) {
            final List<String> skuCodeList = plmGoodsDetailVo.getSkuCodeList();
            final List<PlmCategoryVo> categoryList = plmGoodsDetailVo.getCategoryList();

            for (String sku : skuCodeList) {
                String categoryName = categoryList.stream()
                        .filter(vo -> SECOND_LEVEL.equals(vo.getLevel()))
                        .map(PlmCategoryVo::getCategoryNameCn)
                        .findFirst()
                        .orElse("");

                if (StringUtils.isBlank(categoryName)) {
                    categoryName = categoryList.stream()
                            .filter(vo -> FIRST_LEVEL.equals(vo.getLevel()))
                            .map(PlmCategoryVo::getCategoryNameCn)
                            .findFirst()
                            .orElse("");
                }

                resultMap.put(sku, categoryName);
            }
        }

        return resultMap;
    }

    /**
     * 根据sku获取二级品类id
     *
     * @param skuList
     * @return
     */
    public Map<String, Long> getSkuSecondCategoriesIdMapBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return new HashMap<>();
        }
        List<PlmGoodsDetailVo> plmGoodsDetailVos = CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuDto plmSkuDto = new PlmSkuDto();
                    plmSkuDto.setSkuCodeList(list);
                    return goodsManegeFacade.getGoodsDetail(plmSkuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向wms获取分类信息异常,降级为null");

        Map<String, Long> resultMap = new HashMap<>();
        for (PlmGoodsDetailVo plmGoodsDetailVo : plmGoodsDetailVos) {
            final List<String> skuCodeList = plmGoodsDetailVo.getSkuCodeList();
            final List<PlmCategoryVo> categoryList = plmGoodsDetailVo.getCategoryList();

            for (String sku : skuCodeList) {
                Long categoryId = categoryList.stream()
                        .filter(vo -> SECOND_LEVEL.equals(vo.getLevel()))
                        .map(PlmCategoryVo::getCategoryId)
                        .findFirst()
                        .orElse(0L);

                resultMap.put(sku, categoryId);
            }
        }

        return resultMap;
    }

    /**
     * 属性名获取属性信息
     *
     * @param attributeNameList:
     * @return List<PlmAttributeVo>
     * @author ChenWenLong
     * @date 2024/3/18 18:54
     */
    public List<PlmAttributeVo> getAttrListByName(List<String> attributeNameList) {
        if (CollectionUtils.isEmpty(attributeNameList)) {
            return Collections.emptyList();
        }
        return CollSplitUtil.collSplitExec(attributeNameList, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME,
                splitList -> {
                    PlmAttributeDto plmAttributeDto = new PlmAttributeDto();
                    plmAttributeDto.setAttributeNameList(splitList);
                    return baseDataFacade.getAttrList(plmAttributeDto);
                }, result -> {
                    if (Objects.isNull(result)) {
                        // 无法获取结果返回空的列表
                        return Collections.emptyList();
                    }
                    return DubboResponseUtil.checkCodeAndGetData(result)
                            .getList();
                }, "根据属性名获取属性信息失败！");
    }

    /**
     * 查询关联分类编号的方法
     *
     * @param skuList SKU列表
     * @return 包含关联分类编号的Map，键是SKU，值是关联的分类编号列表
     */
    public Map<String, List<Long>> queryCategoryIds(List<String> skuList) {
        Map<String, List<Long>> resultMap = new HashMap<>(16);
        if (CollectionUtils.isEmpty(skuList)) {
            return resultMap;
        }

        List<PlmGoodsDetailVo> plmVariantVos
                = CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME,
                cuttingSkuList -> {
                    PlmSkuDto queryPlmGoodsDetail = new PlmSkuDto();
                    queryPlmGoodsDetail.setSkuCodeList(cuttingSkuList);
                    return goodsManegeFacade.getGoodsDetail(queryPlmGoodsDetail);
                },
                result -> DubboResponseUtil.checkCodeAndGetData(
                                result)
                        .getList(),
                "通过sku获取产品信息返回为空！");
        if (CollectionUtils.isEmpty(plmVariantVos)) {
            return resultMap;
        }

        for (String sku : skuList) {
            PlmGoodsDetailVo matchPlmGoodsDetailVo = plmVariantVos.stream()
                    .filter(plmVariantVo -> Optional.ofNullable(plmVariantVo.getSkuCodeList())
                            .map(skuCodeList -> skuCodeList.contains(sku))
                            .orElse(false))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchPlmGoodsDetailVo)) {
                List<PlmCategoryVo> categoryList = matchPlmGoodsDetailVo.getCategoryList();
                if (CollectionUtils.isNotEmpty(categoryList)) {
                    List<Long> skuCategoryIds = categoryList.stream()
                            .map(PlmCategoryVo::getCategoryId)
                            .collect(Collectors.toList());
                    resultMap.put(sku, skuCategoryIds);
                }
            }
        }

        return resultMap;
    }

    /**
     * 获取sku的信息
     *
     * @param skuList:
     * @param platCode:非必填
     * @return List<PlmNormalSkuVo>
     * @author ChenWenLong
     * @date 2024/6/20 11:08
     */
    public List<PlmNormalSkuVo> getSkuInfoBySkuList(List<String> skuList, String platCode) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME, splitList -> {
            PlmSkuPlatDto plmSkuPlatDto = new PlmSkuPlatDto();
            plmSkuPlatDto.setSkuCodeList(splitList);
            if (StringUtils.isNotBlank(platCode)) {
                plmSkuPlatDto.setPlatCode(platCode);
            }
            return goodsManegeFacade.getNormalSkuInfo(plmSkuPlatDto);
        }, result -> DubboResponseUtil.checkCodeAndGetData(result).getList(), "获取sku的信息失败！");
    }

    /**
     * 获取SPU的全部SKU和产品名称列表
     *
     * @param spuList:
     * @return List<PlmSkuVo>
     * @author ChenWenLong
     * @date 2024/9/12 17:13
     */
    public List<PlmSkuVo> getSkuBySpuCode(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }
        spuList = spuList.stream()
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }
        List<PlmSkuVo> plmSkuVos = CollSplitUtil.collSplitExec(spuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSpuDto plmSpuDto = new PlmSpuDto();
                    plmSpuDto.setSpuCodeList(list);
                    return goodsManegeFacade.getSkuBySpuCode(plmSpuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result).getList(), "向PLM获取SKU信息异常");

        return Optional.ofNullable(plmSkuVos).orElse(Collections.emptyList());
    }

    public List<com.hete.supply.plm.api.basedata.entity.vo.PlmCategoryVo> getCategoryTree(PlmCategoryGradeDto categoryGradeDto) {
        CommonResult<ResultList<com.hete.supply.plm.api.basedata.entity.vo.PlmCategoryVo>> result = categoryFacade.getCategoryTree(categoryGradeDto);
        ResultList<com.hete.supply.plm.api.basedata.entity.vo.PlmCategoryVo> data = DubboResponseUtil.checkCodeAndGetData(result);
        return data.getList();
    }

    /**
     * 通过 sku 获取具体分类信息
     *
     * @param skuList:
     * @return Map<String, PlmCategoryVo>
     * @author ChenWenLong
     * @date 2024/10/9 18:09
     */
    public Map<String, PlmCategoryVo> getCategoriesInfoBySku(List<String> skuList) {
        Map<String, PlmCategoryVo> skuToCategoryIdMap = new HashMap<>();
        if (CollectionUtils.isEmpty(skuList)) {
            return skuToCategoryIdMap;
        }
        skuList = skuList.stream()
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(skuList)) {
            return skuToCategoryIdMap;
        }
        List<PlmGoodsDetailVo> plmVariantVos = CollSplitUtil.collSplitExec(skuList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    PlmSkuDto plmSkuDto = new PlmSkuDto();
                    plmSkuDto.setSkuCodeList(list);
                    return goodsManegeFacade.getGoodsDetail(plmSkuDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "向wms获取分类信息异常,降级为null");

        List<PlmGoodsDetailVo> plmGoodsDetailVoList = Optional.ofNullable(plmVariantVos)
                .orElse(Collections.emptyList());

        plmGoodsDetailVoList.forEach(skuCategories -> {
            skuCategories.getSkuCodeList()
                    .forEach(sku -> {
                        Optional.ofNullable(skuCategories.getCategoryList())
                                .orElse(Collections.emptyList())
                                .stream()
                                .max(Comparator.comparing(PlmCategoryVo::getLevel))
                                .ifPresent(vo -> skuToCategoryIdMap.put(sku, vo));
                    });
        });
        return skuToCategoryIdMap;
    }

    public Map<String, String> getCategoryEnNameBySkuList(List<String> skuList, int level) {
        Map<String, String> skuCatEnNameMap = new HashMap<>();
        if (CollectionUtils.isEmpty(skuList)) {
            return skuCatEnNameMap;
        }

        List<PlmGoodsDetailVo> skuCategoryList = getCategoriesBySku(skuList);
        for (PlmGoodsDetailVo plmGoodsDetailVo : skuCategoryList) {
            List<String> skuCodeList = plmGoodsDetailVo.getSkuCodeList();
            List<PlmCategoryVo> categoryList = plmGoodsDetailVo.getCategoryList();

            for (String sku : skuCodeList) {
                String categoryName = categoryList.stream()
                        .filter(vo -> Objects.nonNull(vo) && Objects.equals(level, vo.getLevel()))
                        .map(vo -> vo.getCategoryNameCn() == null ? "" : vo.getCategoryNameCn())
                        .findFirst()
                        .orElse("");
                skuCatEnNameMap.put(sku, categoryName);
            }
        }
        return skuCatEnNameMap;
    }
}
