package com.hete.supply.scm.server.scm.production.service.ref;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.production.builder.ProdBuilder;
import com.hete.supply.scm.server.scm.production.dao.AttributeCategoryDao;
import com.hete.supply.scm.server.scm.production.dao.AttributeDao;
import com.hete.supply.scm.server.scm.production.dao.AttributeOptionDao;
import com.hete.supply.scm.server.scm.production.dao.PlmCategoryRelateDao;
import com.hete.supply.scm.server.scm.production.entity.bo.AttrCategoryBo;
import com.hete.supply.scm.server.scm.production.entity.bo.VerifyAttrValueBo;
import com.hete.supply.scm.server.scm.production.entity.po.AttributeCategoryPo;
import com.hete.supply.scm.server.scm.production.entity.po.AttributeOptionPo;
import com.hete.supply.scm.server.scm.production.entity.po.AttributePo;
import com.hete.supply.scm.server.scm.production.entity.po.PlmCategoryRelatePo;
import com.hete.supply.scm.server.scm.production.entity.vo.AttributeInfoVo;
import com.hete.supply.scm.server.scm.production.entity.vo.AttributeOptionVo;
import com.hete.supply.scm.server.scm.production.entity.vo.SkuCategoryVo;
import com.hete.supply.scm.server.scm.production.enums.*;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.support.api.enums.BooleanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/9/29.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttributeRefService {
    private final PlmRemoteService plmRemoteService;
    private final PlmCategoryRelateDao plmCategoryRelateDao;
    private final AttributeCategoryDao attributeCategoryDao;
    private final AttributeDao attributeDao;
    private final AttributeOptionDao attributeOptionDao;
    private final SupplierProductCompareDao supplierProductCompareDao;

    /**
     * @Description 获取sku可维护属性id列表
     * @author yanjiawei
     * @Date 2024/9/18 17:50
     */
    public List<Long> getMaintainableAttrIds(String sku, AttributeScope scope) {
        List<Long> plmCategoryIds = plmRemoteService.getCategoriesBySku(sku);
        if (CollectionUtils.isEmpty(plmCategoryIds)) {
            log.info("未找到商品二级分类信息，sku=>{}", sku);
            return Collections.emptyList();
        }

        //根据plm类目id查询关联属性id
        List<PlmCategoryRelatePo> plmCategoryRelatePos = plmCategoryRelateDao.listByCategoryIds(plmCategoryIds, PlmCategoryRelateBizType.ATTRIBUTE);
        if (CollectionUtils.isEmpty(plmCategoryRelatePos)) {
            log.info("未找到商品分类关联属性信息，sku=>{}", sku);
            return Collections.emptyList();
        }
        List<Long> categoryRelateAttrIds = plmCategoryRelatePos.stream().map(PlmCategoryRelatePo::getBizId).collect(Collectors.toList());

        //根据plm类目id查询对应数据维度关联属性id
        List<Long> attrIds = attributeDao.getIdsByAttrIdsAndScope(categoryRelateAttrIds, scope, AttributeStatus.ENABLE);
        if (CollectionUtils.isEmpty(attrIds)) {
            log.info("未找到商品属性信息，sku=>{}", sku);
            return Collections.emptyList();
        }
        return attrIds;
    }

    public List<AttributeInfoVo> listByAttrIds(List<Long> attrIds) {
        if (CollectionUtils.isEmpty(attrIds)) {
            return Collections.emptyList();
        }

        //属性列表
        List<AttributePo> attributePoList = attributeDao.listByIds(attrIds);
        //按属性id排序
        attributePoList.sort(Comparator.comparing(AttributePo::getAttributeId));
        List<AttributeInfoVo> attributeInfoVoList = ProdBuilder.buildAttributeDetailVoList(attributePoList);

        //属性分类信息
        List<PlmCategoryRelatePo> plmCategoryRelatePoList
                = plmCategoryRelateDao.listByBizIdsAndBizType(attrIds, PlmCategoryRelateBizType.ATTRIBUTE);

        //属性类型列表
        List<Long> secondAttrCategoryIds = attributeInfoVoList.stream()
                .map(AttributeInfoVo::getSecondAttrCategoryId)
                .collect(Collectors.toList());
        List<AttrCategoryBo> attrCategoryBoList = getAttrCatBoListBySecAttrCatId(secondAttrCategoryIds);

        List<AttributeOptionVo> attributeOptionList = this.listAttrOptByAttrIds(attrIds);
        attributeInfoVoList.forEach(attrDetailVo -> {
            Long attrId = attrDetailVo.getAttrId();

            //匹配属性可选项列表
            List<AttributeOptionVo> matchAttrOptionList = attributeOptionList.stream()
                    .filter(attrOptionVo -> attrOptionVo.getAttributeId().equals(attrId))
                    .collect(Collectors.toList());
            attrDetailVo.setAttrOptionList(matchAttrOptionList);

            //匹配属性关联plm商品类目列表
            List<PlmCategoryRelatePo> plmCategoryRelatePo = plmCategoryRelatePoList.stream()
                    .filter(relatePo -> relatePo.getBizId().equals(attrId))
                    .collect(Collectors.toList());
            List<SkuCategoryVo> skuCategoryVoList = ProdBuilder.buildSkuCategoryVoList(plmCategoryRelatePo);
            attrDetailVo.setSkuCategoryList(skuCategoryVoList);

            //匹配属性类型
            Long secondAttrCategoryId = attrDetailVo.getSecondAttrCategoryId();
            attrCategoryBoList.stream().filter(attrCategoryBo -> Objects.equals(secondAttrCategoryId, attrCategoryBo.getSecondAttrCategoryId()))
                    .findFirst()
                    .ifPresent(attrCategoryBo -> {
                        attrDetailVo.setFirstAttrCategoryId(attrCategoryBo.getFirstAttrCategoryId());
                        attrDetailVo.setFirstAttrCategoryName(attrCategoryBo.getFirstAttrCategoryName());
                        attrDetailVo.setSecondAttrCategoryId(attrCategoryBo.getSecondAttrCategoryId());
                        attrDetailVo.setSecondAttrCategoryName(attrCategoryBo.getSecondAttrCategoryName());
                    });
        });

        //按属性类型-次级类型-主键id排序
        attributeInfoVoList.sort(Comparator.comparing(AttributeInfoVo::getFirstAttrCategoryId)
                .thenComparing(AttributeInfoVo::getSecondAttrCategoryId)
                .thenComparing(AttributeInfoVo::getAttrId));

        return attributeInfoVoList;
    }

    public List<AttrCategoryBo> getAttrCatBoListBySecAttrCatId(List<Long> secondAttrCategoryIds) {
        if (CollectionUtils.isEmpty(secondAttrCategoryIds)) {
            log.info("获取一二级属性类型信息返回为空！secondAttrCategoryIds为空。");
            return Collections.emptyList();
        }

        List<AttributeCategoryPo> secondAttrCategoryPos = attributeCategoryDao.listByIds(secondAttrCategoryIds);
        if (CollectionUtils.isEmpty(secondAttrCategoryPos)) {
            log.info("获取一二级属性类型信息返回为空！");
            return Collections.emptyList();
        }

        Set<Long> firstAttrCategoryIds = secondAttrCategoryPos.stream().map(AttributeCategoryPo::getParentAttributeCategoryId).collect(Collectors.toSet());
        List<AttributeCategoryPo> firstAttrCategoryPos = attributeCategoryDao.listByIds(firstAttrCategoryIds);

        return secondAttrCategoryPos.stream().map(secondAttrCategoryPo -> {
            AttrCategoryBo attrCategoryBo = new AttrCategoryBo();
            attrCategoryBo.setSecondAttrCategoryId(secondAttrCategoryPo.getAttributeCategoryId());
            attrCategoryBo.setSecondAttrCategoryName(secondAttrCategoryPo.getAttributeCategoryName());

            firstAttrCategoryPos.stream()
                    .filter(firstAttrCategoryPo -> Objects.equals(secondAttrCategoryPo.getParentAttributeCategoryId(), firstAttrCategoryPo.getAttributeCategoryId()))
                    .findFirst()
                    .ifPresent(firstAttrCategoryPo -> {
                        attrCategoryBo.setFirstAttrCategoryId(firstAttrCategoryPo.getAttributeCategoryId());
                        attrCategoryBo.setFirstAttrCategoryName(firstAttrCategoryPo.getAttributeCategoryName());
                    });
            return attrCategoryBo;
        }).collect(Collectors.toList());
    }

    public List<AttributeOptionVo> listAttrOptByAttrIds(List<Long> attrIds) {
        //属性可选项列表
        List<AttributeOptionPo> attributeOptionPos = attributeOptionDao.listByAttrIds(attrIds);
        return ProdBuilder.buildAttributeOptionVoList(attributeOptionPos);
    }

    public boolean isBinding(String sku, String supplierCode) {
        if (StrUtil.isBlank(sku)) {
            log.info("sku为空，无法校验属性绑定关系，供应商与SKU绑定关系=>{}", false);
            return false;
        }
        if (StrUtil.isBlank(supplierCode)) {
            log.info("供应商代码为空，无法校验属性绑定关系，供应商与SKU绑定关系=>{}", false);
            return false;
        }
        SupplierProductComparePo supplierProductComparePo
                = supplierProductCompareDao.getBySupplierCodeAndSku(supplierCode, sku);
        if (Objects.isNull(supplierProductComparePo)) {
            log.info("供应商=>{} sku=>{} 对照关系不存在 供应商与SKU绑定关系=>{}", supplierCode, sku, false);
            return false;
        }

        BooleanType supplierProductCompareStatus = supplierProductComparePo.getSupplierProductCompareStatus();
        if (!Objects.equals(BooleanType.TRUE, supplierProductCompareStatus)) {
            log.info("供应商=>{} sku=>{} 对照关系状态为禁用，供应商与SKU绑定关系=>{}", supplierCode, sku, false);
            return false;
        }
        return true;
    }

    public void verifyAttrValList(AttributePo attributePo, List<Long> maintainableAttrIds, List<VerifyAttrValueBo> verifyAttrValList, AttributeScope scope) {
        ParamValidUtils.requireNotNull(attributePo, StrUtil.format("属性信息已更新或删除！请刷新页面后重试。"));
        String attrName = attributePo.getAttributeName();

        //校验：状态=启用
        ParamValidUtils.requireEquals(AttributeStatus.ENABLE, attributePo.getStatus(),
                StrUtil.format("{} 状态为禁用！请校验后提交。", attrName));

        //校验：数据维度
        ParamValidUtils.requireEquals(scope, attributePo.getScope(),
                StrUtil.format("{} 数据维度发生变更！请校验后提交。", attrName));

        //属性分类校验
        Long attrId = attributePo.getAttributeId();
        ParamValidUtils.requireContains(attrId, maintainableAttrIds, StrUtil.format("{} 属性分类信息已更新或删除！请刷新页面后重试。", attrName));

        //必填校验
        AttributeIsRequired isRequired = attributePo.getIsRequired();
        if (Objects.equals(AttributeIsRequired.YES, isRequired)) {
            //校验：属性值必填
            ParamValidUtils.requireNotEmpty(verifyAttrValList,
                    StrUtil.format("{} 为必填项！请校验后提交。", attrName));
            //校验：空置校验
            ParamValidUtils.requireEquals(true, verifyAttrValList.stream().allMatch(verifyAttrValueBo -> StrUtil.isNotBlank(verifyAttrValueBo.getValue())),
                    StrUtil.format("{} 为必填项！请校验后提交。", attrName));
        }

        //录入类型校验
        AttributeInputType inputType = attributePo.getInputType();
        if (CollectionUtils.isNotEmpty(verifyAttrValList)) {
            List<String> attrValList = verifyAttrValList.stream().map(VerifyAttrValueBo::getValue).collect(Collectors.toList());

            //属性值长度校验：单行文本长度：32；多行文本长度：500
            if (Objects.equals(inputType, AttributeInputType.SINGLE_LINE_TEXT)) {
                ParamValidUtils.requireEquals(true, attrValList.stream().allMatch(attrVal -> StrUtil.length(attrVal) <= 32),
                        StrUtil.format("{} 单行文本长度不能超过32个字符，请校验后提交", attrName));
            }
            if (Objects.equals(inputType, AttributeInputType.MULTIPLE_LINE_TEXT)) {
                ParamValidUtils.requireEquals(true, attrValList.stream().allMatch(attrVal -> StrUtil.length(attrVal) <= 500),
                        StrUtil.format("{} 多行文本长度不能超过500个字符，请校验后提交", attrName));
            }
            //图片数量校验：4张
            if (Objects.equals(inputType, AttributeInputType.IMAGE)) {
                ParamValidUtils.requireEquals(true, attrValList.stream().allMatch(attrVal -> attrValList.size() <= 4),
                        StrUtil.format("{} 图片数量不能超过4张，请校验后提交", attrName));
            }
            //单选下拉/多选下拉校验：属性值选项
            if (Objects.equals(inputType, AttributeInputType.SINGLE_SELECT) || Objects.equals(inputType, AttributeInputType.MULTIPLE_SELECT)) {
                ParamValidUtils.requireEquals(true, verifyAttrValList.stream().allMatch(verifyAttrValueBo -> Objects.nonNull(verifyAttrValueBo.getAttrOptId())),
                        StrUtil.format("{} 属性值选项不存在，请刷新页面后提交", attrName));

                List<Long> attrOptIds = verifyAttrValList.stream().map(VerifyAttrValueBo::getAttrOptId).collect(Collectors.toList());
                List<AttributeOptionPo> attrOptPoList = attributeOptionDao.listByIds(attrOptIds);
                ParamValidUtils.requireEquals(true, attrOptPoList.size() == attrOptIds.size(), StrUtil.format("{} 属性值选项不存在，请刷新页面后提交", attrName));
            }
        }
    }
}













