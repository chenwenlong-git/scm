package com.hete.supply.scm.server.scm.production.service.biz;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.google.common.collect.Lists;
import com.hete.supply.plm.api.basedata.entity.dto.PlmCategoryGradeDto;
import com.hete.supply.plm.api.basedata.entity.vo.ObtainCategoryVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import com.hete.supply.scm.api.scm.entity.enums.CrotchLength;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import com.hete.supply.scm.api.scm.importation.entity.dto.AttributeImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.InitAttributeImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupSkuCraftAttrImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupSkuMaterialAttrImportationDto;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.entity.vo.PlmCategoryInfoVo;
import com.hete.supply.scm.server.scm.production.builder.ProdBuilder;
import com.hete.supply.scm.server.scm.production.dao.*;
import com.hete.supply.scm.server.scm.production.entity.bo.AttrCategoryBo;
import com.hete.supply.scm.server.scm.production.entity.bo.AttrCategoryInitBo;
import com.hete.supply.scm.server.scm.production.entity.bo.CalSkuRiskPreBo;
import com.hete.supply.scm.server.scm.production.entity.bo.RiskRangeBo;
import com.hete.supply.scm.server.scm.production.entity.dto.*;
import com.hete.supply.scm.server.scm.production.entity.po.*;
import com.hete.supply.scm.server.scm.production.entity.vo.*;
import com.hete.supply.scm.server.scm.production.enums.*;
import com.hete.supply.scm.server.scm.production.prop.ScmAttrCategoryProp;
import com.hete.supply.scm.server.scm.production.prop.ScmAttrRiskProp;
import com.hete.supply.scm.server.scm.production.service.base.AttributeBaseService;
import com.hete.supply.scm.server.scm.production.service.ref.AttributeRefService;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierBindingListQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierProductCompareRefService;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttributeBizService {

    //顶级属性id常量
    private static final Long ROOT_ATTRIBUTE_CATEGORY_ID = 0L;
    //二级分类层级常量
    private static final Integer SECOND_ATTR_CATEGORY_LEVEL = 2;
    //属性风险最大查询条数
    private static final Integer MAX_ATTR_RISK_TOTAL = 2000;
    //二级类目
    private static final Integer SECOND_CATEGORY_LEVEL = 2;

    private final AttributeCategoryDao attributeCategoryDao;
    private final AttributeDao attributeDao;
    private final AttributeOptionDao attributeOptionDao;
    private final PlmCategoryRelateDao plmCategoryRelateDao;
    private final SkuAttributeDao skuAttributeDao;
    private final SkuAttributeValueDao skuAttributeValueDao;
    private final SupplierSkuAttributeDao supplierSkuAttributeDao;
    private final SupplierSkuAttributeValueDao supplierSkuAttributeValueDao;
    private final PlmRemoteService plmRemoteService;
    private final PlmSkuDao plmSkuDao;
    private final AttributeRiskDao attributeRiskDao;
    private final AttributeOptionRiskDao attributeOptionRiskDao;
    private final ScmAttrCategoryProp scmAttrCategoryProp;
    private final Environment environment;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final AttributeBaseService attributeBaseService;
    private final AttributeRefService attributeRefService;
    private final ScmAttrRiskProp scmAttrRiskProp;
    private final SupplierProductCompareRefService supProdCompareRefService;
    private final SupplierSkuMaterialAttributeDao supSkuMaterialAttrDao;
    private final SupplierSkuCraftAttributeDao supSkuCraftAttrDao;
    private final SupplierDao supplierDao;

    /**
     * @Description : 获取属性分类信息
     * @author yanjiawei
     * @Date 2024/10/18 10:22
     */
    public List<AttributeCategoryVo> listAttrCategory(AttributeCategoryDto dto) {
        Long parentAttrCategoryId
                = Objects.isNull(dto.getParentAttrCategoryId()) ? ROOT_ATTRIBUTE_CATEGORY_ID : dto.getParentAttrCategoryId();
        List<AttributeCategoryPo> attributeCategoryPos
                = attributeCategoryDao.listByParentAttrCategoryId(parentAttrCategoryId);
        return ProdBuilder.buildAttributeCategoryVos(attributeCategoryPos);
    }

    /**
     * @Description : 获取所有次级属性类型
     * @author yanjiawei
     * @Date 2024/10/18 10:21
     */
    public List<AttributeCategoryVo> listChildAttrCategory() {
        List<AttributeCategoryPo> childAttrCategoryPoList = attributeCategoryDao.listChildAttrCategory();
        return ProdBuilder.buildAttributeCategoryVos(childAttrCategoryPoList);
    }

    /**
     * @Description 新增属性
     * @author yanjiawei
     * @Date 2024/10/18 10:23
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.OPERATE_ATTR, key = "#dto.attrName",
            waitTime = 1, leaseTime = -1, exceptionDesc = "供应链属性正在处理中，请稍后再试。")
    public void addAttr(AddAttributeDto dto) {
        //参数校验
        dto.validate();

        //业务校验：属性名称是否重复
        String attrName = dto.getAttrName();
        AttributeScope attributeScope = dto.getAttributeScope();
        boolean attrNameExist = checkIsExist(attrName, 0L, attributeScope);
        ParamValidUtils.requireEquals(attrNameExist, false, StrUtil.format("属性名称：{}已存在！请校验后提交", attrName));

        Long attrCategoryId = dto.getAttrCategoryId();
        AttributeCategoryPo attrCategoryPo = ParamValidUtils.requireNotNull(attributeCategoryDao.getById(attrCategoryId),
                "属性分类不存在！请刷新页面后重试。");

        //新增属性信息
        AttributePo attributePo = ProdBuilder.buildAttributePo(attrCategoryPo, dto);
        attributeDao.insert(attributePo);
        Long attributeId = attributePo.getAttributeId();

        //新增商品分类与属性关联关系
        List<AddAttributeDto.SkuCategoryDto> skuCategoryList = dto.getSkuCategoryList();
        List<PlmCategoryRelatePo> plmCategoryRelatePos = ProdBuilder.buildPlmCategoryRelatePoList(attributeId, skuCategoryList);
        plmCategoryRelateDao.insertBatch(plmCategoryRelatePos);

        //新增属性选项信息
        String attributeName = attributePo.getAttributeName();
        List<AddAttributeDto.AddAttributeOptionDto> attributeOptionList = dto.getAttrOptionList();
        List<AttributeOptionPo> attributeOptionPos = ProdBuilder.buildAddAttributeOptionPos(attributeId, attributeName, attributeOptionList);
        if (CollectionUtils.isNotEmpty(attributeOptionPos)) {
            attributeOptionDao.insertBatch(attributeOptionPos);
        }
    }

    private boolean checkIsExist(String attrName, Long excludeAttrId, AttributeScope attributeScope) {
        if (StrUtil.isBlank(attrName)) {
            throw new ParamIllegalException("校验属性名称是否存在失败！属性名称为空！");
        }
        String trimAttrName = attrName.trim();
        List<AttributePo> attributePos = attributeDao.listByAttrNameAndScope(trimAttrName, attributeScope);
        return CollectionUtils.isNotEmpty(attributePos) && attributePos.stream().noneMatch(attrPo -> Objects.equals(attrPo.getAttributeId(), excludeAttrId));
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.OPERATE_ATTR, key = "#dto.attrName",
            waitTime = 1, leaseTime = -1, exceptionDesc = "供应链属性正在处理中，请稍后再试。")
    public void updateAttr(UpdateAttributeDto dto) {
        //参数校验
        dto.validate();

        //版本号校验
        Integer version = dto.getVersion();
        Long attrId = dto.getAttrId();
        AttributePo attributePo = ParamValidUtils.requireNotNull(attributeDao.getByIdAndVersion(attrId, version),
                "数据已被修改或删除！请刷新页面后重试。！"
        );
        Long attrCategoryId = dto.getAttrCategoryId();
        AttributeCategoryPo attrCategoryPo = ParamValidUtils.requireNotNull(attributeCategoryDao.getById(attrCategoryId),
                "属性分类不存在！请刷新页面后重试。"
        );

        //校验：属性名称是否重复
        String attrName = dto.getAttrName();
        AttributeScope attributeScope = attributePo.getScope();
        ParamValidUtils.requireEquals(false, checkIsExist(attrName, attrId, attributeScope),
                StrUtil.format("属性名称{}已存在！请校验后提交", attrName)
        );
        AttributePo updateAttrPo = ProdBuilder.buildUpdateAttributePo(attrCategoryPo, attributePo, dto);
        attributeDao.updateByIdVersion(updateAttrPo);

        List<AttributeRiskPo> attrRiskPoList = attributeRiskDao.listByAttrId(attrId);
        if (CollectionUtils.isNotEmpty(attrRiskPoList)) {
            attrRiskPoList.forEach(attrRiskPo -> attrRiskPo.setAttributeName(attrName));
            attributeRiskDao.updateBatchByIdVersion(attrRiskPoList);
        }

        //属性值校验：必填
        List<UpdateAttributeDto.UpdateAttributeOptionDto> attrOptionList = dto.getAttrOptionList();
        List<AttributeInputType> validateAttrInputTypes = List.of(AttributeInputType.SINGLE_SELECT, AttributeInputType.MULTIPLE_SELECT);
        AttributeInputType inputType = attributePo.getInputType();
        if (validateAttrInputTypes.contains(inputType)) {
            ParamValidUtils.requireNotEmpty(attrOptionList,
                    StrUtil.format("录入类型为{}，属性选项列表不能为空", validateAttrInputTypes)
            );

            //更新属性选项信息:存在则保留，不存在则新增
            List<AttributeOptionPo> attrOptionPoList = attributeOptionDao.listByAttrId(attrId);
            for (UpdateAttributeDto.UpdateAttributeOptionDto updateAttrOptDto : attrOptionList) {
                String attrVal = updateAttrOptDto.getAttributeValue();
                Optional<AttributeOptionPo> result = attrOptionPoList.stream()
                        .filter(attrOptionPo -> Objects.equals(attrVal, attrOptionPo.getAttributeValue()))
                        .findFirst();
                result.ifPresentOrElse(
                        attrOptionPo -> {
                            log.info("属性名称=>{} 属性值=>{}已存在！本次不新增", attrName, attrVal);
                            attrOptionPo.setAttributeName(attrName);
                            attributeOptionDao.updateByIdVersion(attrOptionPo);
                        }, () -> {
                            AttributeOptionPo attributeOptionPo = ProdBuilder.buildAddAttributeOptionPo(attrId, attrName, attrVal);
                            attributeOptionDao.insert(attributeOptionPo);
                        }
                );
            }
            List<String> optionValList = attrOptionList.stream()
                    .map(UpdateAttributeDto.UpdateAttributeOptionDto::getAttributeValue)
                    .collect(Collectors.toList());
            List<AttributeOptionPo> delAttrOptionPoList = attrOptionPoList.stream()
                    .filter(attrOptionPo -> !optionValList.contains(attrOptionPo.getAttributeValue()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(delAttrOptionPoList)) {
                attributeOptionDao.removeBatchByIds(delAttrOptionPoList);
            }
        }

        //更新商品分类与属性关联关系
        List<PlmCategoryRelatePo> delPlmCategoryRelatePos
                = plmCategoryRelateDao.listByBizIdsAndBizType(Collections.singletonList(attrId), PlmCategoryRelateBizType.ATTRIBUTE);
        if (CollectionUtils.isNotEmpty(delPlmCategoryRelatePos)) {
            plmCategoryRelateDao.removeBatchByIds(delPlmCategoryRelatePos);
        }
        List<AddAttributeDto.SkuCategoryDto> skuCategoryList = dto.getSkuCategoryList();
        List<PlmCategoryRelatePo> plmCategoryRelatePos = ProdBuilder.buildPlmCategoryRelatePoList(attrId, skuCategoryList);
        plmCategoryRelateDao.insertBatch(plmCategoryRelatePos);
    }

    public AttributeInfoVo attrInfo(GetAttributeInfoDto dto) {
        Long attrId = dto.getAttrId();
        List<AttributeInfoVo> attributeInfoVoList = attributeRefService.listByAttrIds(Collections.singletonList(attrId));
        return attributeInfoVoList.stream().findFirst().orElse(new AttributeInfoVo());
    }

    public CommonPageResult.PageInfo<AttributePageVo> pageAttr(GetAttributePageDto dto) {
        String attrOptVal = dto.getAttributeValue();
        if (StrUtil.isNotBlank(attrOptVal)) {
            List<Long> attrIdList = attributeOptionDao.getIdsByAttrVal(attrOptVal);
            if (CollectionUtils.isEmpty(attrIdList)) {
                return new CommonPageResult.PageInfo<>();
            }
            dto.setAttrIdList(attrIdList);
        }

        Long firstAttrCategoryId = dto.getFirstAttrCategoryId();
        if (Objects.nonNull(firstAttrCategoryId)) {
            List<AttributeCategoryPo> attrCategoryPos = attributeCategoryDao.listByParentAttrCategoryId(firstAttrCategoryId);
            if (CollectionUtils.isEmpty(attrCategoryPos)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<Long> secondAttrCategoryIds = attrCategoryPos.stream()
                    .map(AttributeCategoryPo::getAttributeCategoryId)
                    .distinct()
                    .collect(Collectors.toList());
            dto.setSecondAttrCategoryIdList(secondAttrCategoryIds);
        }

        CommonPageResult.PageInfo<AttributePageVo> pageRes
                = attributeDao.getByPage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<AttributePageVo> records = pageRes.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageRes;
        }
        List<Long> attrIds = records.stream().map(AttributePageVo::getAttrId).collect(Collectors.toList());

        //属性与商品类目关联关系列表
        List<PlmCategoryRelatePo> plmCategoryRelatePos
                = plmCategoryRelateDao.listByBizIdsAndBizType(attrIds, PlmCategoryRelateBizType.ATTRIBUTE);

        //属性可选项列表
        List<AttributeOptionVo> attributeOptionList = attributeRefService.listAttrOptByAttrIds(attrIds);

        //属性类型列表
        List<Long> secondAttrCategoryIds = records.stream()
                .map(AttributePageVo::getSecondAttrCategoryId)
                .collect(Collectors.toList());
        List<AttrCategoryBo> attrCategoryBoList = attributeRefService.getAttrCatBoListBySecAttrCatId(secondAttrCategoryIds);

        records.forEach(attrDetailVo -> {
            Long attrId = attrDetailVo.getAttrId();

            //匹配属性可选项
            List<AttributeOptionVo> matchAttrOptionList = attributeOptionList.stream()
                    .filter(attrOptionVo -> Objects.equals(attrId, attrOptionVo.getAttributeId()))
                    .collect(Collectors.toList());
            attrDetailVo.setAttrOptionList(matchAttrOptionList);

            //匹配商品分类列表
            List<PlmCategoryRelatePo> matchPlmCategoryRelatePos = plmCategoryRelatePos.stream()
                    .filter(relatePo -> Objects.equals(attrId, relatePo.getBizId()))
                    .collect(Collectors.toList());
            List<SkuCategoryVo> skuCategoryVoList = ProdBuilder.buildSkuCategoryVoList(matchPlmCategoryRelatePos);
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
        return pageRes;
    }

    public List<AttributeInfoVo> listAttr(GetAttributeListDto dto) {
        AttributeStatus attrStatus = dto.getAttributeStatus();
        List<Long> attrIds = dto.getAttrIds();
        List<AttributeInputType> attributeInputTypeList = dto.getAttributeInputTypeList();

        List<Long> ids = attributeDao.listIdsByIdsAndInputType(attrIds, attributeInputTypeList, attrStatus);
        return attributeRefService.listByAttrIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void importAttr(AttributeImportationDto dto) {
        //必填校验
        String sku = ParamValidUtils.requireNotBlank(dto.getSku(), "SKU编码为空！请校验后重新导入。");
        sku = sku.trim();
        ParamValidUtils.requireNotNull(plmSkuDao.getBySku(sku), StrUtil.format("sku：{} 不存在！请校验后重新导入。", sku));

        //供应商是否存在&绑定关系是否存在
        String supplierCode = dto.getSupplierCode();
        AttributeScope attributeScope = StrUtil.isBlank(supplierCode) ? AttributeScope.GOODS : AttributeScope.SUPPLIER;
        if (Objects.equals(AttributeScope.SUPPLIER, attributeScope)) {
            ParamValidUtils.requireNotNull(supplierCode, StrUtil.format("供应商{}不存在！请校验后重新导入。", supplierCode));
            ParamValidUtils.requireEquals(true, attributeRefService.isBinding(sku, supplierCode),
                    StrUtil.format("供应商商品对照关系不存在！请校验后重新导入。"));
        }

        //属性：存在
        String attrName = ParamValidUtils.requireNotBlank(dto.getAttrName(), "属性名称为空！请校验后重新导入。");
        attrName = attrName.trim();
        List<AttributePo> attributePoList = ParamValidUtils.requireNotEmpty(attributeDao.listByAttrNameAndScope(attrName, attributeScope),
                StrUtil.format("属性名称：{} 不存在！请校验数据维度/属性名称填写正确后重新导入。", attrName));
        final AttributePo attributePo = ParamValidUtils.requireNotNull(attributePoList.stream().findFirst().orElse(null),
                StrUtil.format("属性名称：{} 不存在！请校验数据维度/属性名称填写正确后重新导入。", attrName));

        //属性sku类目: 属性关联二级类目包含sku二级类目
        Long attrId = attributePo.getAttributeId();
        Long plmSeCategoryId = getPlmSecondCategoryId(sku);
        List<PlmCategoryRelatePo> plmCategoryRelatePos
                = plmCategoryRelateDao.listByBizIdsAndBizType(Collections.singletonList(attrId), PlmCategoryRelateBizType.ATTRIBUTE);
        List<Long> attrSeCategoryIds = plmCategoryRelatePos.stream().map(PlmCategoryRelatePo::getCategoryId).collect(Collectors.toList());
        ParamValidUtils.requireContains(plmSeCategoryId, attrSeCategoryIds,
                StrUtil.format("属性名称：{} 关联的二级类目不包含sku:{}！请校验后重新导入。", attrName, sku));

        //属性状态：启用
        AttributeStatus status = attributePo.getStatus();
        ParamValidUtils.requireEquals(false, Objects.equals(AttributeStatus.DISABLE, status),
                "属性状态已禁用，不允许导入！请校验后重新导入。");

        //属性必填，属性值非空
        String attrValueStrList = dto.getAttrValueStrList();
        AttributeIsRequired isRequired = attributePo.getIsRequired();
        if (Objects.equals(AttributeIsRequired.YES, isRequired)) {
            ParamValidUtils.requireNotBlank(attrValueStrList, StrUtil.format("{} 为必填项！请校验后重新导入。", attrName));
        }
        List<AttributeOptionPo> attributeOptionPos = attributeOptionDao.listByAttrId(attrId);

        List<String> attrValueList = Lists.newArrayList();
        if (StrUtil.isNotBlank(attrValueStrList)) {
            //属性值校验：录入类型
            AttributeInputType inputType = attributePo.getInputType();

            if (Objects.equals(AttributeInputType.SINGLE_SELECT, inputType)) {
                String[] attrValues = attrValueStrList.split(",");

                //属性值非空串
                attrValueList = Arrays.stream(attrValues).map(String::trim).collect(Collectors.toList());
                ParamValidUtils.requireEquals(false, attrValueList.stream().anyMatch(String::isBlank),
                        StrUtil.format("属性{}存在空值！请校验后重新导入。", attrName));

                //属性值数量=1
                ParamValidUtils.requireLessThanOrEqual(attrValueList.size(), 1,
                        StrUtil.format("属性{}为单选下拉，属性值个数不能大于1！请校验后重新导入。", attrName));

                //属性值必须存在属性可选项中
                attrValueList.forEach(attrValue -> {
                    if (attributeOptionPos.stream().noneMatch(attrOption -> Objects.equals(attrOption.getAttributeValue(), attrValue))) {
                        throw new ParamIllegalException(StrUtil.format("属性可选值：{} 不存在！请校验后重新导入。", attrValue));
                    }
                });
            } else if (Objects.equals(AttributeInputType.MULTIPLE_SELECT, inputType)) {
                String[] attrValues = attrValueStrList.split(",");

                //属性值非空串
                attrValueList = Arrays.stream(attrValues).map(String::trim).collect(Collectors.toList());
                ParamValidUtils.requireEquals(false, attrValueList.stream().anyMatch(String::isBlank),
                        StrUtil.format("属性{}存在空值！请校验后重新导入。", attrName));

                //属性值必须存在属性可选项中
                attrValueList.forEach(attrValue -> {
                    if (attributeOptionPos.stream().noneMatch(attrOption -> Objects.equals(attrOption.getAttributeValue(), attrValue))) {
                        throw new ParamIllegalException(StrUtil.format("属性可选值：{} 不存在！请校验后重新导入。", attrValue));
                    }
                });
            } else if (Objects.equals(AttributeInputType.SINGLE_LINE_TEXT, inputType)) {
                attrValueList.add(attrValueStrList);

                ParamValidUtils.requireEquals(true, attrValueList.stream().allMatch(attrVal -> StrUtil.length(attrVal) <= 32),
                        StrUtil.format("{} 单行文本长度不能超过32个字符，请校验后重新导入。", attrName));
            } else if (Objects.equals(AttributeInputType.MULTIPLE_LINE_TEXT, inputType)) {
                attrValueList.add(attrValueStrList);

                ParamValidUtils.requireEquals(true, attrValueList.stream().allMatch(attrVal -> StrUtil.length(attrVal) <= 500),
                        StrUtil.format("{} 多行文本长度不能超过500个字符，请校验后重新导入。", attrName));
            } else if (Objects.equals(AttributeInputType.IMAGE, inputType)) {
                throw new ParamIllegalException("属性录入类型为图片，不允许导入！请校验后重新导入。");
            }
        }

        if (Objects.equals(AttributeScope.GOODS, attributeScope)) {
            List<SkuAttributePo> delSkuAttrPos = skuAttributeDao.listBySkuAndAttrIds(sku, Collections.singletonList(attrId));
            if (CollectionUtils.isNotEmpty(delSkuAttrPos)) {
                List<Long> delSkuAttrIds = delSkuAttrPos.stream().map(SkuAttributePo::getSkuAttributeId).collect(Collectors.toList());
                skuAttributeDao.removeBatchByIds(delSkuAttrIds);

                List<SkuAttributeValuePo> skuAttrValPos = skuAttributeValueDao.listBySkuAttrIds(delSkuAttrIds);
                skuAttributeValueDao.removeBatchByIds(skuAttrValPos);
            }

            SkuAttributePo skuAttrPo = ProdBuilder.buildSkuAttributePo(sku, attrId, attrName);
            skuAttributeDao.insert(skuAttrPo);
            Long skuAttrId = skuAttrPo.getSkuAttributeId();

            List<SkuAttributeValuePo> skuAttrValPos
                    = ProdBuilder.buildSkuAttributeValuePoList(skuAttrId, attrValueList, attributeOptionPos);
            skuAttributeValueDao.insertBatch(skuAttrValPos);
        } else if (Objects.equals(AttributeScope.SUPPLIER, attributeScope)) {
            List<SupplierSkuAttributePo> supSkuAttrPos
                    = supplierSkuAttributeDao.listBySupplierCodeAndSkuAndAttrIds(sku, supplierCode, Collections.singletonList(attrId));
            if (CollectionUtils.isNotEmpty(supSkuAttrPos)) {
                List<Long> delSupSkuAttrIds = supSkuAttrPos.stream().map(SupplierSkuAttributePo::getSupplierSkuAttributeId).collect(Collectors.toList());
                supplierSkuAttributeDao.removeBatchByIds(delSupSkuAttrIds);

                List<SupplierSkuAttributeValuePo> supSkuAttrValPos = supplierSkuAttributeValueDao.listBySupSkuAttrIds(delSupSkuAttrIds);
                supplierSkuAttributeValueDao.removeBatchByIds(supSkuAttrValPos);
            }

            SupplierSkuAttributePo supSkuAttrPo = ProdBuilder.buildSupplierSkuAttributePo(sku, supplierCode, attrId, attrName);
            supplierSkuAttributeDao.insert(supSkuAttrPo);
            Long supSkuAttrId = supSkuAttrPo.getSupplierSkuAttributeId();

            List<SupplierSkuAttributeValuePo> supSkuAttrValPos
                    = ProdBuilder.buildSupplierSkuAttributeValuePoList(supSkuAttrId, attrValueList, attributeOptionPos);
            supplierSkuAttributeValueDao.insertBatch(supSkuAttrValPos);
        } else {
            throw new ParamIllegalException("导入属性数据维度异常！请联系相关业务人员处理。");
        }
    }

    private Long getPlmSecondCategoryId(String sku) {
        List<PlmGoodsDetailVo> plmCategoryList = plmRemoteService.getCategoriesBySku(Collections.singletonList(sku));
        PlmGoodsDetailVo matchPlmSkuCategory
                = plmCategoryList.stream().filter(plmGoodsDetailVo -> plmGoodsDetailVo.getSkuCodeList().contains(sku)).findFirst().orElse(null);
        if (Objects.isNull(matchPlmSkuCategory)) {
            throw new ParamIllegalException("{}分类信息缺失，请维护后重新导入！");
        }
        List<PlmCategoryVo> categoryList = matchPlmSkuCategory.getCategoryList();
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new ParamIllegalException("{}分类信息缺失，请维护后重新导入！");
        }
        PlmCategoryVo matchSecondCategory
                = categoryList.stream().filter(plmCategoryVo -> Objects.equals(SECOND_ATTR_CATEGORY_LEVEL, plmCategoryVo.getLevel())).findFirst().orElse(null);
        if (Objects.isNull(matchSecondCategory)) {
            throw new ParamIllegalException("{}{}级分类信息缺失，请维护后重新导入！", sku, SECOND_ATTR_CATEGORY_LEVEL);
        }
        return matchSecondCategory.getCategoryId();
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.OPERATE_ATTR_RISK, key = "#dto.attrRiskLockKey", waitTime = 1,
            leaseTime = -1, exceptionDesc = "属性风险信息处理中，请稍后再试。")
    public void updateAttrRisk(UpdateAttributeRiskDto dto) {
        // 入参校验
        dto.validate();

        // 校验：属性存在
        List<UpdateAttributeRiskInfoDto> updateAttrRiskDtoList = dto.getUpdateAttributeRiskInfoDtoList();
        List<Long> attrIds = updateAttrRiskDtoList.stream()
                .map(UpdateAttributeRiskInfoDto::getAttrId)
                .collect(Collectors.toList());
        List<AttributePo> attributePos = ParamValidUtils.requireNotEmpty(attributeDao.listByIds(attrIds),
                "属性不存在，请校验后提交！");
        List<Long> existingAttrIds = attributePos.stream()
                .map(AttributePo::getAttributeId)
                .collect(Collectors.toList());
        List<String> unmatchedAttrNames = updateAttrRiskDtoList.stream()
                .filter(updateAttributeRiskInfoDto -> !existingAttrIds.contains(updateAttributeRiskInfoDto.getAttrId()))
                .map(UpdateAttributeRiskInfoDto::getAttrName)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(unmatchedAttrNames)) {
            throw new ParamIllegalException("更新属性风险信息失败！属性名称：" +
                    String.join(",", unmatchedAttrNames) + "不存在，请校验后提交");
        }

        // 校验：属性可选项存在
        List<Long> attrOptIds = updateAttrRiskDtoList.stream()
                .flatMap(updateAttributeRiskInfoDto -> updateAttributeRiskInfoDto.getAttributeRiskOptInfoList().stream()
                        .map(AttributeRiskOptInfoDto::getAttrOptionId)).collect(Collectors.toList());
        List<AttributeOptionPo> attrOptPoList = ParamValidUtils.requireNotEmpty(attributeOptionDao.listByIds(attrOptIds),
                "属性可选项不存在，请校验后提交！");
        List<Long> existingAttrOptIds = attrOptPoList.stream()
                .map(AttributeOptionPo::getAttributeOptionId)
                .collect(Collectors.toList());
        List<String> unmatchedAttrOptNames = updateAttrRiskDtoList.stream()
                .flatMap(updateAttributeRiskInfoDto -> updateAttributeRiskInfoDto.getAttributeRiskOptInfoList().stream())
                .filter(attributeRiskOptInfoDto -> !existingAttrOptIds.contains(attributeRiskOptInfoDto.getAttrOptionId()))
                .map(AttributeRiskOptInfoDto::getAttributeValue)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(unmatchedAttrOptNames)) {
            throw new ParamIllegalException("更新属性风险信息失败！属性可选项名称：" +
                    String.join(",", unmatchedAttrOptNames) + "不存在，请校验后提交");
        }

        //删除后新增
        List<Long> delAttrRiskIds = updateAttrRiskDtoList.stream()
                .map(UpdateAttributeRiskInfoDto::getAttrRiskId)
                .collect(Collectors.toList());
        List<AttributeRiskPo> delAttributeRiskPos = attributeRiskDao.listByIds(delAttrRiskIds);
        if (CollectionUtils.isNotEmpty(delAttributeRiskPos)) {
            attributeRiskDao.removeBatchByIds(delAttributeRiskPos);
            List<AttributeOptionRiskPo> delAttrOptRiskPos = attributeOptionRiskDao.listByAttrRiskIds(delAttrRiskIds);
            if (CollectionUtils.isNotEmpty(delAttrOptRiskPos)) {
                attributeOptionRiskDao.removeBatchByIds(delAttrOptRiskPos);
            }
        }

        for (UpdateAttributeRiskInfoDto updateAttrRiskDto : updateAttrRiskDtoList) {
            Long attrId = updateAttrRiskDto.getAttrId();

            AttributeRiskPo attributeRiskPo = ProdBuilder.buildAttributeRiskPo(updateAttrRiskDto);
            attributeRiskDao.insert(attributeRiskPo);
            Long attrRiskId = attributeRiskPo.getAttributeRiskId();

            //校验：属性配置项是否属于当前属性
            List<AttributeOptionPo> matchAttrOptList
                    = attrOptPoList.stream().filter(attrOptPo -> Objects.equals(attrOptPo.getAttributeId(), attrId)).collect(Collectors.toList());
            ParamValidUtils.requireNotEmpty(matchAttrOptList, "属性配置项不存在，请校验后提交！");

            List<Long> curAttrOptIdList = matchAttrOptList.stream().map(AttributeOptionPo::getAttributeOptionId).collect(Collectors.toList());
            List<AttributeRiskOptInfoDto> attrRiskOptList = updateAttrRiskDto.getAttributeRiskOptInfoList();
            boolean allMatchAttrOpt = attrRiskOptList.stream().allMatch(attrRiskOptDto -> curAttrOptIdList.contains(attrRiskOptDto.getAttrOptionId()));
            ParamValidUtils.requireEquals(true, allMatchAttrOpt, "属性配置项与属性风险信息不匹配，请校验后提交！");

            List<AttributeOptionRiskPo> attributeOptionRiskPoList = ProdBuilder.buildAttributeOptionRiskPoList(attrRiskId, attrRiskOptList);
            attributeOptionRiskDao.insertBatch(attributeOptionRiskPoList);
        }
    }

    public List<AttributeRiskInfoVo> attrRiskList() {
        //查询属性风险信息
        List<AttributeRiskPo> attrRiskPoList = attributeRiskDao.listAll(MAX_ATTR_RISK_TOTAL);
        if (CollectionUtils.isEmpty(attrRiskPoList)) {
            return Collections.emptyList();
        }

        //查询属性可选项风险信息
        List<Long> attrRiskIds = attrRiskPoList.stream()
                .map(AttributeRiskPo::getAttributeRiskId)
                .collect(Collectors.toList());
        List<AttributeOptionRiskPo> attrOptRiskPoList = attributeOptionRiskDao.listByAttrRiskIds(attrRiskIds);

        //查询属性信息
        List<Long> attrIds = attrRiskPoList.stream()
                .map(AttributeRiskPo::getAttributeId)
                .collect(Collectors.toList());
        List<AttributePo> attributePoList = attributeDao.listByIds(attrIds);

        //查询属性可选项信息
        List<Long> attrOptIds = attrOptRiskPoList.stream()
                .map(AttributeOptionRiskPo::getAttributeOptionId)
                .collect(Collectors.toList());
        List<AttributeOptionPo> attributeOptionPoList = attributeOptionDao.listByIds(attrOptIds);
        return ProdBuilder.buildAttributeRiskInfoVoList(attrRiskPoList, attributePoList, attrOptRiskPoList, attributeOptionPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.OPERATE_ATTR_RISK, key = "#dto.attrRiskLockKey", waitTime = 1,
            leaseTime = -1, exceptionDesc = "属性风险信息处理中，请稍后再试。")
    public void deleteAttrRisk(DeleteAttributeRiskDto dto) {
        List<Long> attrRiskIdList = dto.getAttrRiskIdList();
        List<AttributeRiskPo> delAttrRiskPoList = ParamValidUtils.requireNotEmpty(attributeRiskDao.listByIds(attrRiskIdList), "属性风险信息不存在，请校验后删除！");
        List<Long> delAttrRiskIds = delAttrRiskPoList.stream().map(AttributeRiskPo::getAttributeRiskId).collect(Collectors.toList());
        attributeRiskDao.removeBatchByIds(delAttrRiskIds);

        List<AttributeOptionRiskPo> attributeOptionRiskPos = attributeOptionRiskDao.listByAttrRiskIds(delAttrRiskIds);
        if (CollectionUtils.isNotEmpty(attributeOptionRiskPos)) {
            attributeOptionRiskDao.removeBatchByIds(attributeOptionRiskPos.stream().map(AttributeOptionRiskPo::getAttributeOptionRiskId).collect(Collectors.toList()));
        }
    }

    /**
     * @Description 查询PLM所有二级类目信息
     * @author yanjiawei
     * @Date 2024/10/18 10:20
     */
    public List<PlmCategoryInfoVo> getPlmAllSecondCateList() {
        //查询二级类目
        PlmCategoryGradeDto queryParam = new PlmCategoryGradeDto();
        queryParam.setGrade(SECOND_CATEGORY_LEVEL);
        List<com.hete.supply.plm.api.basedata.entity.vo.PlmCategoryVo> categoryTree = plmRemoteService.getCategoryTree(queryParam);
        if (CollectionUtils.isEmpty(categoryTree)) {
            return Collections.emptyList();
        }

        //封装返回值
        List<PlmCategoryInfoVo> resultList = Lists.newArrayList();
        for (com.hete.supply.plm.api.basedata.entity.vo.PlmCategoryVo plmCategoryVo : categoryTree) {
            List<com.hete.supply.plm.api.basedata.entity.vo.PlmCategoryVo> childCategoryList = plmCategoryVo.getChildCategoryList();
            if (CollectionUtils.isEmpty(childCategoryList)) {
                continue;
            }
            for (com.hete.supply.plm.api.basedata.entity.vo.PlmCategoryVo categoryVo : childCategoryList) {
                if (!Objects.equals(SECOND_CATEGORY_LEVEL, categoryVo.getGrade())) {
                    continue;
                }
                PlmCategoryInfoVo plmCategoryInfoVo = new PlmCategoryInfoVo();
                plmCategoryInfoVo.setCategoryId(categoryVo.getCategoryId());
                plmCategoryInfoVo.setCategoryName(categoryVo.getCategoryName());
                plmCategoryInfoVo.setLevel(categoryVo.getGrade());
                plmCategoryInfoVo.setPath(categoryVo.getPath());
                resultList.add(plmCategoryInfoVo);
            }
        }
        return resultList;
    }


    /**
     * @Description : 初始化属性分类
     * @author yanjiawei
     * @Date 2024/10/18 10:19
     */
    @Transactional(rollbackFor = Exception.class)
    public void initAttrCategory() {
        //获取属性分类初始化数据
        List<AttrCategoryInitBo> categories = scmAttrCategoryProp.getCategories();
        if (CollectionUtils.isEmpty(categories)) {
            log.info("属性分类初始化数据为空，不进行初始化");
            return;
        }

        //删除所有属性分类
        List<AttributeCategoryPo> attrCategoryPos = attributeCategoryDao.listAll();
        if (CollectionUtils.isNotEmpty(attrCategoryPos)) {
            attributeCategoryDao.removeBatchByIds(attrCategoryPos);
        }

        //插入属性分类
        for (AttrCategoryInitBo category : categories) {
            String categoryName = category.getCategoryName();
            AttributeCategoryPo attributeCategoryPo = ProdBuilder.buildAttributeCategoryPo(categoryName);
            attributeCategoryDao.insert(attributeCategoryPo);
            Long attributeCategoryId = attributeCategoryPo.getAttributeCategoryId();

            List<AttrCategoryInitBo.SubCategory> subCategoryNames = category.getSubCategoryNames();
            if (CollectionUtils.isEmpty(subCategoryNames)) {
                continue;
            }
            List<AttributeCategoryPo> subAttrCategoryPoList = ProdBuilder.buildAttributeCategoryPoList(attributeCategoryId, subCategoryNames);
            attributeCategoryDao.insertBatch(subAttrCategoryPoList);
        }
    }

    private CalSkuRiskPreBo initCalSkuRiskPreBo() {
        CalSkuRiskPreBo calSkuRiskPreBo = new CalSkuRiskPreBo();

        // 筛选已单选下拉/多选下拉配置属性SKU编码
        List<AttributeInputType> attrInputTypes
                = Arrays.asList(AttributeInputType.SINGLE_SELECT, AttributeInputType.MULTIPLE_SELECT);
        calSkuRiskPreBo.setAttrInputTypes(attrInputTypes);

        //属性风险信息
        List<AttributeRiskPo> attrRiskPoList = attributeRiskDao.listAll(MAX_ATTR_RISK_TOTAL);
        if (CollectionUtils.isEmpty(attrRiskPoList)) {
            log.info("【数据准备】：没有配置属性风险信息，跳过计算。");
            return calSkuRiskPreBo;
        }

        //属性风险配置列表&数据字典
        calSkuRiskPreBo.setAttributeRiskPoList(attrRiskPoList);
        List<Long> attrRiskIds = attrRiskPoList.stream()
                .map(AttributeRiskPo::getAttributeRiskId)
                .collect(Collectors.toList());

        //属性选项风险配置列表&数据字典
        List<AttributeOptionRiskPo> attributeOptionRiskPoList = attributeOptionRiskDao.listByAttrRiskIds(attrRiskIds);
        calSkuRiskPreBo.setAttributeOptionRiskPoList(attributeOptionRiskPoList);

        //属性信息
        List<Long> attrIds = attrRiskPoList.stream()
                .map(AttributeRiskPo::getAttributeId)
                .collect(Collectors.toList());
        List<AttributePo> optAttrPoList = attributeDao.listByAttrIdsAndInputTypes(attrIds, attrInputTypes);
        calSkuRiskPreBo.setAttributePoList(optAttrPoList);

        //属性配置项信息
        List<AttributeOptionPo> optOptionPoList = attributeOptionDao.listByAttrIds(attrIds);
        calSkuRiskPreBo.setAttributeOptPoList(optOptionPoList);

        //商品属性信息
        List<SkuAttributePo> skuAttributePoList = skuAttributeDao.listByAttrIds(attrIds);
        calSkuRiskPreBo.setSkuAttributePoList(skuAttributePoList);

        //商品属性值信息
        List<Long> skuAttrIds = skuAttributePoList.stream()
                .map(SkuAttributePo::getSkuAttributeId)
                .collect(Collectors.toList());
        List<SkuAttributeValuePo> skuAttributeValuePos = skuAttributeValueDao.listBySkuAttrIds(skuAttrIds);
        calSkuRiskPreBo.setSkuAttributeValuePoList(skuAttributeValuePos);

        //供应商商品属性信息
        List<SupplierSkuAttributePo> supplierSkuAttributePoList = supplierSkuAttributeDao.listByAttrIds(attrIds);
        calSkuRiskPreBo.setSupplierSkuAttributePoList(supplierSkuAttributePoList);

        //供应商商品属性值信息
        List<Long> supSkuAttrIds = supplierSkuAttributePoList.stream()
                .map(SupplierSkuAttributePo::getSupplierSkuAttributeId)
                .collect(Collectors.toList());
        List<SupplierSkuAttributeValuePo> supplierSkuAttributeValuePoList
                = supplierSkuAttributeValueDao.listBySupSkuAttrIds(supSkuAttrIds);
        calSkuRiskPreBo.setSupplierSkuAttributeValuePoList(supplierSkuAttributeValuePoList);
        return calSkuRiskPreBo;
    }

    public void doCalSkuRiskJob() {
        //清空数据
        attributeBaseService.clearSkuRisk();

        //数据准备
        CalSkuRiskPreBo calSkuRiskPreBo = initCalSkuRiskPreBo();

        //筛选sku列表
        Set<String> skuList = filterSkuList(calSkuRiskPreBo);
        if (CollectionUtils.isEmpty(skuList)) {
            log.info("【筛选SKU列表】：没有可计算的SKU列表，跳过计算。");
            return;
        }

        //计算sku总分
        calSkuScore(calSkuRiskPreBo, skuList);

        //评级
        calRiskLevel(calSkuRiskPreBo);

        //保存数据
        attributeBaseService.saveSkuRisk(calSkuRiskPreBo);
    }

    private Set<String> getAttrSkuList(CalSkuRiskPreBo calSkuRiskPreBo) {
        List<AttributeInputType> attrInputTypes = calSkuRiskPreBo.getAttrInputTypes();
        List<AttributePo> optAttrPoList = calSkuRiskPreBo.getAttributePoList();
        if (CollectionUtils.isEmpty(optAttrPoList)) {
            log.info("【筛选SKU列表】：筛选商品属性SKU结束！不存在录入类型：{}的属性。", attrInputTypes.stream()
                    .map(AttributeInputType::getRemark)
                    .collect(Collectors.joining("/")));
            return Collections.emptySet();
        }

        //条件1：已配置商品属性SKU编码
        List<SkuAttributePo> skuAttributePoList = calSkuRiskPreBo.getSkuAttributePoList();
        Set<String> goodAttrSkuList = skuAttributePoList.stream()
                .map(SkuAttributePo::getSku)
                .collect(Collectors.toSet());

        //条件2：查询已配置供应商商品属性SKU编码
        List<SupplierSkuAttributePo> supplierSkuAttributePoList = calSkuRiskPreBo.getSupplierSkuAttributePoList();
        Set<String> supAttrSkuList = supplierSkuAttributePoList
                .stream().map(SupplierSkuAttributePo::getSku)
                .collect(Collectors.toSet());

        Set<String> res = Stream.of(goodAttrSkuList, supAttrSkuList)
                .filter(CollectionUtils::isNotEmpty).flatMap(Set::stream)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(res)) {
            log.info("【筛选SKU列表】：筛选商品属性SKU结束！不存在录入类型：{}的属性SKU编码列表",
                    attrInputTypes.stream().map(AttributeInputType::getRemark).collect(Collectors.joining("/")));
            return Collections.emptySet();
        }
        log.info("【筛选SKU列表】：筛选商品属性SKU结束！筛选到SKU编码：{}", JSON.toJSONString(res));
        return res;
    }

    private Set<String> filterCategorySkuList(Set<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            log.info("【筛选SKU列表】：筛选plm商品分类SKU结束！没有可筛选的SKU列表。");
            return Collections.emptySet();
        }

        // 查询plm商品类目：通过SKU列表查询plm二级类目
        List<PlmGoodsDetailVo> goodCategoryList = plmRemoteService.getCategoriesBySku(new ArrayList<>(skuList));
        if (CollectionUtils.isEmpty(goodCategoryList)) {
            log.info("【筛选SKU列表】：筛选plm商品分类SKU结束！查询sku商品分类信息为空。");
            return Collections.emptySet();
        }

        // 过滤条件：筛选二级类目等于头套的SKU列表
        List<String> filterWiCategorySkuList = goodCategoryList.stream()
                .filter(goodsDetail -> goodsDetail.getCategoryList().stream()
                        .anyMatch(category -> Objects.equals(2, category.getLevel()) && Objects.equals("头套", category.getCategoryNameCn())))
                .flatMap(goodsDetail -> goodsDetail.getSkuCodeList().stream())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterWiCategorySkuList)) {
            log.info("【筛选SKU列表】：筛选plm商品分类SKU结束！筛选二级类目等于头套的SKU不满足，没有符合的SKU列表。");
            return Collections.emptySet();
        }
        Set<String> res = skuList.stream().filter(filterWiCategorySkuList::contains).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(res)) {
            log.info("【筛选SKU列表】：筛选plm商品分类SKU结束！筛选二级类目等于头套的SKU列表为空。");
            return Collections.emptySet();
        }
        log.info("【筛选SKU列表】：筛选plm商品分类SKU结束！筛选到SKU编码：{}", JSON.toJSONString(res));
        return res;
    }

    private Set<String> filterPurchaseOrderSkuList(Set<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            log.info("【筛选SKU列表】：筛选采购单SKU结束！没有可筛选的SKU列表。");
            return Collections.emptySet();
        }

        // 过滤条件：采购子单明细&创建=近半年&状态!=已作废
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime halfYearBefore = now.minusMonths(6);
        List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList
                = purchaseChildOrderItemDao.listBySkuListAndTimeRange(new ArrayList<>(skuList), halfYearBefore, now);
        if (CollectionUtils.isEmpty(purchaseChildOrderItemPoList)) {
            log.info("【筛选SKU列表】：筛选采购单SKU结束！通过SKU列表没有对应采购子单明细。");
            return Collections.emptySet();
        }

        Set<String> poiNoList = purchaseChildOrderItemPoList.stream()
                .map(PurchaseChildOrderItemPo::getPurchaseChildOrderNo)
                .collect(Collectors.toSet());
        List<PurchaseChildOrderPo> purchaseChildOrderPoList
                = purchaseChildOrderDao.getListByNoList(new ArrayList<>(poiNoList));
        if (CollectionUtils.isEmpty(purchaseChildOrderPoList)) {
            log.info("【筛选SKU列表】：筛选采购单SKU结束！通过采购子单明细采购子单号没有对应采购子单。");
            return Collections.emptySet();
        }

        //非作废状态
        Set<String> res = purchaseChildOrderItemPoList.stream().filter(purchaseChildOrderItemPo -> {
            String puChildOrderNo = purchaseChildOrderItemPo.getPurchaseChildOrderNo();
            String sku = purchaseChildOrderItemPo.getSku();

            PurchaseChildOrderPo isMatch = purchaseChildOrderPoList.stream().filter(purchaseChildOrderPo ->
                    Objects.equals(puChildOrderNo, purchaseChildOrderPo.getPurchaseChildOrderNo()) &&
                            !PurchaseOrderStatus.DELETE.equals(purchaseChildOrderPo.getPurchaseOrderStatus())
            ).findFirst().orElse(null);
            if (Objects.isNull(isMatch)) {
                log.info("【筛选SKU列表】：SKU=>{},采购单状态=>{}", sku, PurchaseOrderStatus.DELETE);
                return false;
            } else {
                log.info("【筛选SKU列表】：SKU=>{},采购单状态=>{}", sku, isMatch.getPurchaseOrderStatus());
                return true;
            }
        }).map(PurchaseChildOrderItemPo::getSku).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(res)) {
            log.info("【筛选SKU列表】：筛选采购单SKU结束！通过SKU无法筛选创建时间>={}&采购单状态=>{}的SKU列表", halfYearBefore, PurchaseOrderStatus.DELETE);
            return Collections.emptySet();
        }
        log.info("【筛选SKU列表】：筛选采购单SKU结束！筛选到SKU编码：{}", JSON.toJSONString(res));
        return res;
    }

    private Set<String> filterSkuList(CalSkuRiskPreBo calSkuRiskPreBo) {
        Set<String> res;

        //筛选已配置属性sku列表
        res = getAttrSkuList(calSkuRiskPreBo);

        //筛选指定类目
        res = filterCategorySkuList(res);

        //筛选采购单SKU
        res = filterPurchaseOrderSkuList(res);
        return res;
    }

    private void calRiskLevel(CalSkuRiskPreBo calSkuRiskPreBo) {
        TreeMap<String, BigDecimal> skuScoreMap = calSkuRiskPreBo.getSkuScoreMap();
        if (CollectionUtils.isEmpty(skuScoreMap)) {
            log.info("【计算SKU风险等级】：没有可计算的SKU总分列表，跳过计算。");
            return;
        }
        Map<String, SkuRisk> skuRiskMap = calSkuRiskPreBo.getSkuRiskMap();

        //特殊规则供应商列表
        List<String> specSuppliers = scmAttrRiskProp.getSuppliers();
        log.info("【计算SKU风险等级】：特殊规则供应商列表=>{}", JSON.toJSONString(specSuppliers));

        //查询商品-供应商对照关系
        SupplierBindingListQueryBo queryParam = new SupplierBindingListQueryBo();
        queryParam.setSkuList(new ArrayList<>(skuScoreMap.keySet()));
        List<SupplierProductComparePo> skuSupComparePoList
                = supProdCompareRefService.getBindingSupplierList(queryParam);
        log.info("【计算SKU风险等级】：查询sku=>{} 供应商商品对照关系结果=>{}",
                skuScoreMap.keySet(), JSON.toJSONString(skuSupComparePoList));

        //特殊供应商关联的sku列表
        List<String> specSkuList = skuSupComparePoList.stream()
                .filter(supSkuAttrPo -> specSuppliers.contains(supSkuAttrPo.getSupplierCode()))
                .map(SupplierProductComparePo::getSku).collect(Collectors.toList());
        log.info("【计算SKU风险等级】：特殊供应商关联的sku列表=>{}", JSON.toJSONString(specSkuList));

        if (CollectionUtils.isNotEmpty(specSkuList)) {
            //按分数倒序排序
            Map<String, BigDecimal> specSkuScoreMap = skuScoreMap.entrySet().stream()
                    .filter(entry -> specSkuList.contains(entry.getKey()))
                    .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            //筛选前60%进行统计（向下取整）
            int totalCount = specSkuScoreMap.size();
            int filterCount = (int) Math.floor(totalCount * 0.6);
            if (filterCount > 0) {
                //获取前60%分数值
                TreeMap<String, BigDecimal> top60SkuScoreMap = specSkuScoreMap.entrySet().stream().limit(filterCount)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, TreeMap::new));
                Collection<BigDecimal> highRiskScoreSet = top60SkuScoreMap.values();
                log.info("【计算SKU风险等级】：特殊供应商定义高风险分数列表=>{}", JSON.toJSONString(top60SkuScoreMap));

                //风险评级
                specSkuScoreMap.forEach((skuCode, score) -> {
                    if (highRiskScoreSet.contains(score)) {
                        skuRiskMap.put(skuCode, SkuRisk.HIGH);
                        log.info("【计算SKU风险等级】：特殊供应商：sku=>{},分数=>{} 风险等级=>{}。", skuCode, score, SkuRisk.HIGH);
                    }
                });
            }
        }

        Map<SkuRisk, RiskRangeBo> rule = scmAttrRiskProp.getRule();
        if (CollectionUtils.isEmpty(rule)) {
            log.info("【计算SKU风险等级】：没有配置风险等级规则，跳过计算。");
            return;
        }

        for (Map.Entry<String, BigDecimal> skuScoreEntry : skuScoreMap.entrySet()) {
            String sku = skuScoreEntry.getKey();
            BigDecimal score = skuScoreEntry.getValue();

            SkuRisk skuRisk = skuRiskMap.get(sku);
            if (Objects.nonNull(skuRisk)) {
                log.info("【计算SKU风险等级】：sku=>{}已存在风险等级，不处理。", sku);
                continue;
            }

            SkuRisk riskLevel = findRiskLevel(score, rule);
            log.info("【计算SKU风险等级】：非特殊供应商：sku=>{},分数=>{} 风险等级=>{}。", sku, score, riskLevel);
            skuRiskMap.put(sku, riskLevel);
        }
    }


    private void calSkuScore(CalSkuRiskPreBo calSkuRiskPreBo, Set<String> skuList) {
        //属性风险配置信息
        List<AttributeRiskPo> attrRiskPoList = calSkuRiskPreBo.getAttributeRiskPoList();
        List<AttributeOptionRiskPo> attrOptRiskPoList = calSkuRiskPreBo.getAttributeOptionRiskPoList();

        //商品属性&属性值
        List<SkuAttributePo> skuAttrPoList = calSkuRiskPreBo.getSkuAttributePoList();
        List<SkuAttributeValuePo> skuAttrValPoList = calSkuRiskPreBo.getSkuAttributeValuePoList();

        //供应商商品属性&属性值
        List<SupplierSkuAttributePo> supSkuAttrPoList = calSkuRiskPreBo.getSupplierSkuAttributePoList();
        List<SupplierSkuAttributeValuePo> supSkuAttrValPoList = calSkuRiskPreBo.getSupplierSkuAttributeValuePoList();

        //风险等级日志列表
        List<SkuRiskLogPo> skuRiskLogPoList = calSkuRiskPreBo.getSkuRiskLogPoList();

        //sku与供应商绑定关系
        SupplierBindingListQueryBo queryParam = new SupplierBindingListQueryBo();
        queryParam.setSkuList(new ArrayList<>(skuList));
        List<SupplierProductComparePo> bindingSupList = supProdCompareRefService.getBindingSupplierList(queryParam);

        TreeMap<String, BigDecimal> res = new TreeMap<>();
        for (String sku : skuList) {
            BigDecimal totalScore = BigDecimal.ZERO;

            //统计商品维度属性风险值
            List<SkuAttributePo> matchSkuAttrList = skuAttrPoList.stream()
                    .filter(item -> item.getSku().equals(sku))
                    .collect(Collectors.toList());
            for (SkuAttributePo skuAttributePo : matchSkuAttrList) {
                //匹配属性风险
                Long attrId = skuAttributePo.getAttributeId();
                AttributeRiskPo matchAttrRisk
                        = attrRiskPoList.stream().filter(attrRiskPo -> Objects.equals(attrRiskPo.getAttributeId(), attrId)).findFirst().orElse(null);
                if (Objects.isNull(matchAttrRisk)) {
                    log.info("【计算SKU风险评分】：商品属性没有配置风险评分，跳过计算。attrId=>{}", attrId);
                    continue;
                }
                String attrName = matchAttrRisk.getAttributeName();
                BigDecimal coefficient = matchAttrRisk.getCoefficient();

                Long skuAttrId = skuAttributePo.getSkuAttributeId();
                List<Long> attrOptIds = skuAttrValPoList.stream()
                        .filter(item -> Objects.equals(skuAttrId, item.getSkuAttributeId()))
                        .map(SkuAttributeValuePo::getValueId)
                        .collect(Collectors.toList());

                BigDecimal score = calAttrScore(sku, null, attrId, attrOptIds, coefficient, attrName, attrOptRiskPoList, skuRiskLogPoList);
                totalScore = totalScore.add(score);
            }

            //统计供应商维度属性风险值
            List<SupplierSkuAttributePo> matchSupSkuAttrList = supSkuAttrPoList.stream()
                    .filter(item -> item.getSku().equals(sku))
                    .collect(Collectors.toList());

            //sku绑定的供应商编码
            List<String> bindSupCodeList = bindingSupList.stream()
                    .filter(bindingSup -> Objects.equals(bindingSup.getSku(), sku))
                    .map(SupplierProductComparePo::getSupplierCode).collect(Collectors.toList());
            for (SupplierSkuAttributePo supSkuAttrPo : matchSupSkuAttrList) {
                //校验供应商绑定关系
                Long attrId = supSkuAttrPo.getAttributeId();
                String supplierCode = supSkuAttrPo.getSupplierCode();
                if (!bindSupCodeList.contains(supplierCode)) {
                    log.info("【计算SKU风险总分】：sku=>{} 供应商编码=>{} 不在绑定供应商列表中，跳过计算。", sku, supplierCode);
                    continue;
                }

                //匹配属性风险
                AttributeRiskPo matchAttrRisk
                        = attrRiskPoList.stream().filter(attrRiskPo -> Objects.equals(attrRiskPo.getAttributeId(), attrId)).findFirst().orElse(null);
                if (Objects.isNull(matchAttrRisk)) {
                    log.info("【计算SKU风险评分】：供应商商品属性没有配置风险评分，跳过计算。attrId=>{}", attrId);
                    continue;
                }
                String attrName = matchAttrRisk.getAttributeName();
                BigDecimal coefficient = matchAttrRisk.getCoefficient();

                Long supSkuAttrId = supSkuAttrPo.getSupplierSkuAttributeId();
                List<Long> attrOptIds = supSkuAttrValPoList.stream()
                        .filter(item -> Objects.equals(supSkuAttrId, item.getSupplierSkuAttributeId()))
                        .map(SupplierSkuAttributeValuePo::getValueId)
                        .collect(Collectors.toList());

                BigDecimal score = calAttrScore(sku, supplierCode, attrId, attrOptIds, coefficient, attrName, attrOptRiskPoList, skuRiskLogPoList);
                totalScore = totalScore.add(score);
            }
            res.put(sku, totalScore);
            log.info("【计算SKU风险总分】：sku=>{} 总分=>{}", sku, totalScore);
        }

        calSkuRiskPreBo.setSkuScoreMap(res);
    }

    private BigDecimal calAttrScore(String sku,
                                    String supplierCode,
                                    Long attrId,
                                    List<Long> attrOptIds,
                                    BigDecimal riskCoefficient,
                                    String attrName,
                                    List<AttributeOptionRiskPo> attrOptRiskPoList,
                                    List<SkuRiskLogPo> skuRiskLogPoList) {
        BigDecimal res = BigDecimal.ZERO;
        for (Long attrOptId : attrOptIds) {
            //匹配属性可选项风险
            AttributeOptionRiskPo matchAttrOptRisk = attrOptRiskPoList.stream()
                    .filter(attrOptRiskPo -> Objects.equals(attrOptId, attrOptRiskPo.getAttributeOptionId()))
                    .findFirst().orElse(null);
            if (Objects.isNull(matchAttrOptRisk)) {
                log.info("【计算SKU风险评分】：属性可选项没有配置风险评分，跳过计算。attrOptId=>{}", attrOptId);
                continue;
            }
            String attrOptName = matchAttrOptRisk.getAttributeOptionValue();
            BigDecimal riskScore = matchAttrOptRisk.getScore();

            BigDecimal score = riskCoefficient.multiply(riskScore);
            log.info("【计算SKU风险总分】：sku=>{} {}({}) * {}({}) = {}",
                    sku,
                    attrName, riskCoefficient,
                    attrOptName, riskScore,
                    score);
            res = res.add(score);

            SkuRiskLogPo skuRiskLogPo = ProdBuilder.buildSkuRiskLogPo(sku, supplierCode, attrId, attrName, attrOptId, attrOptName, score, riskCoefficient);
            skuRiskLogPoList.add(skuRiskLogPo);
        }
        return res;
    }

    private static SkuRisk findRiskLevel(BigDecimal score, Map<SkuRisk, RiskRangeBo> rule) {
        if (CollectionUtils.isEmpty(rule)) {
            log.info("【计算SKU风险等级】：属性风险规则为空，无法计算风险等级。");
            return null;
        }

        //总分>=7.5:高风险；总分>=6.5且<7.5:中风险；总分>=5且<6.5:低风险；总分<5:不处理
        for (Map.Entry<SkuRisk, RiskRangeBo> entry : rule.entrySet()) {
            SkuRisk riskLevel = entry.getKey();
            RiskRangeBo range = entry.getValue();

            BigDecimal min = range.getMin();
            BigDecimal max = range.getMax();

            if (Objects.isNull(min) || Objects.isNull(max)) {
                throw new IllegalArgumentException("属性风险范围配置异常，请检查。");
            }

            // 左闭：score >= min
            boolean isMinValid = score.compareTo(min) >= 0;
            // 右开：score < max
            boolean isMaxValid = score.compareTo(max) < 0;

            if (isMinValid && isMaxValid) {
                log.info("【计算SKU风险等级】：分数=>{} 风险等级=>{}。", score, riskLevel);
                return riskLevel;
            }
        }
        log.info("【计算SKU风险等级】：分数=>{} 无风险等级。", score);
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void importSupSkuMaterialAttr(SupSkuMaterialAttrImportationDto dto) {
        String sku = ParamValidUtils.requireNotBlank(dto.getSku(), "sku不能为空！请校验后导入。");
        ParamValidUtils.requireNotNull(plmSkuDao.getBySku(sku.trim()),
                StrUtil.format("商品{}信息不存在！请校验后导入。", sku));

        String supplierCode = ParamValidUtils.requireNotBlank(dto.getSupplierCode(),
                "供应商编码不能为空！请校验后导入。");
        ParamValidUtils.requireNotNull(supplierDao.getBySupplierCode(supplierCode),
                StrUtil.format("供应商{}信息不存在！请校验后导入。", supplierCode));

        ParamValidUtils.requireEquals(true, attributeRefService.isBinding(sku, supplierCode),
                StrUtil.format("供应商{}与商品{}对照关系不存在！请校验后导入。", supplierCode, sku));

        String crotchLengthStr = ParamValidUtils.requireNotBlank(dto.getCrotchLength(),
                "裆长尺寸不能为空！请校验后导入。");
        CrotchLength crotchLength = ParamValidUtils.requireNotNull(CrotchLength.parse(crotchLengthStr),
                StrUtil.format("裆长尺寸不存在 {} 下拉值！请校验后导入。", crotchLengthStr));

        //校验裆长部位字符长度不能超过500
        String crotchPosition = ParamValidUtils.requireNotBlank(dto.getCrotchPosition(),
                "裆长部位不能为空！请校验后导入。");
        ParamValidUtils.requireLessThanOrEqual(crotchPosition.length(), 500,
                "裆长部位字符长度不能超过500！请校验后导入。");

        //校验深色克重：大于0 最大值6位，小数最多2位
        BigDecimal darkWeight = null;
        String darkWeightStr = dto.getDarkWeight();
        if (StrUtil.isNotBlank(darkWeightStr)) {
            try {
                darkWeight = new BigDecimal(darkWeightStr);
            } catch (Exception e) {
                throw new ParamIllegalException("深色克重格式不正确！请校验后导入。");
            }
            ParamValidUtils.requireGreaterThan(darkWeight, BigDecimal.ZERO,
                    "深色克重必须大于0！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(darkWeight.precision() - darkWeight.scale(), 6,
                    "深色克重整数最多6位！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(darkWeight.scale(), 2,
                    "深色克重小数最多2位！请校验后导入。");
        }

        //校验浅色克重：大于0 最大值6位，小数最多2位
        BigDecimal lightWeight = null;
        String lightWeightStr = dto.getLightWeight();
        if (StrUtil.isNotBlank(dto.getLightWeight())) {
            try {
                lightWeight = new BigDecimal(lightWeightStr);
            } catch (Exception e) {
                throw new ParamIllegalException("浅色克重格式不正确！请校验后导入。");
            }
            ParamValidUtils.requireGreaterThan(lightWeight, BigDecimal.ZERO,
                    "浅色克重必须大于0！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(lightWeight.precision() - lightWeight.scale(), 6,
                    "浅色克重整数最多6位！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(lightWeight.scale(), 2, "浅色克重小数最多2位！请校验后导入。");
        }

        //校验裆长配比字符长度不能超过500
        String crotchLengthRatio = ParamValidUtils.requireNotBlank(dto.getCrotchLengthRatio(),
                "裆长配比不能为空！请校验后导入。");
        ParamValidUtils.requireLessThanOrEqual(crotchLengthRatio.length(), 500,
                "裆长配比字符长度不能超过500！请校验后导入。");

        //校验原料克重：大于0 最大值6位，小数最多2位
        BigDecimal weight;
        String weightStr = ParamValidUtils.requireNotBlank(dto.getWeight(), "原料克重不能为空！请校验后导入。");
        try {
            weight = new BigDecimal(weightStr);
        } catch (Exception e) {
            throw new ParamIllegalException("原料克重格式不正确！请校验后导入。");
        }
        ParamValidUtils.requireGreaterThan(weight, BigDecimal.ZERO, "原料克重必须大于0！请校验后导入。");
        ParamValidUtils.requireLessThanOrEqual(weight.precision() - weight.scale(), 6,
                "原料克重整数最多6位！请校验后导入。");
        ParamValidUtils.requireLessThanOrEqual(weight.scale(), 2, "原料克重小数最多2位！请校验后导入。");


        SupplierSkuMaterialAttributePo supSkuMaterialAttrPo
                = ProdBuilder.buildSupplierSkuMaterialAttributePo(sku, supplierCode, crotchLength, crotchPosition, darkWeight, lightWeight, crotchLengthRatio, weight);
        supSkuMaterialAttrDao.insert(supSkuMaterialAttrPo);
    }


    @Transactional(rollbackFor = Exception.class)
    public void importSupSkuCraftAttr(SupSkuCraftAttrImportationDto dto) {
        String sku = ParamValidUtils.requireNotBlank(dto.getSku(), "sku不能为空！请校验后导入。");
        ParamValidUtils.requireNotNull(plmSkuDao.getBySku(sku.trim()),
                StrUtil.format("商品{}信息不存在！请校验后导入。", sku));

        String supplierCode = ParamValidUtils.requireNotBlank(dto.getSupplierCode(), "供应商编码不能为空！请校验后导入。");
        ParamValidUtils.requireNotNull(supplierDao.getBySupplierCode(supplierCode),
                StrUtil.format("供应商{}信息不存在！请校验后导入。", supplierCode));

        ParamValidUtils.requireEquals(true, attributeRefService.isBinding(sku, supplierCode),
                StrUtil.format("供应商{}与商品{}对照关系不存在！请校验后导入。", supplierCode, sku));

        //校验缠管字符长度不能超过500
        String tubeWrapping = ParamValidUtils.requireNotBlank(dto.getTubeWrapping(), "缠管不能为空！请校验后导入。");
        ParamValidUtils.requireLessThanOrEqual(tubeWrapping.length(), 500, "缠管字符长度不能超过500！请校验后导入。");

        //根数不能为空且必须大于0
        int rootsCnt;
        String rootsCntStr = ParamValidUtils.requireNotBlank(dto.getRootsCnt(), "根数不能为空！请校验后导入。");
        try {
            rootsCnt = Integer.parseInt(rootsCntStr);
        } catch (Exception e) {
            throw new ParamIllegalException("根数格式不正确！请校验后导入。");
        }
        ParamValidUtils.requireGreaterThan(rootsCnt, 0, "根数必须大于0！请校验后导入。");

        //层数不能为空且必须大于0
        int layersCnt;
        String layersCntStr = ParamValidUtils.requireNotBlank(dto.getLayersCnt(), "层数不能为空！请校验后导入。");
        try {
            layersCnt = Integer.parseInt(layersCntStr);
        } catch (Exception e) {
            throw new ParamIllegalException("层数格式不正确！请校验后导入。");
        }
        ParamValidUtils.requireGreaterThan(layersCnt, 0, "层数必须大于0！请校验后导入。");

        //特殊处理字符长度不能超过500
        String specialHandling = ParamValidUtils.requireNotBlank(dto.getSpecialHandling(), "特殊处理不能为空！请校验后导入。");
        ParamValidUtils.requireLessThanOrEqual(specialHandling.length(), 500, "特殊处理字符长度不能超过500！请校验后导入。");

        SupplierSkuCraftAttributePo supSkuCraftAttrPo = ProdBuilder.buildSupplierSkuCraftAttributePo(sku, supplierCode, tubeWrapping, rootsCnt, layersCnt, specialHandling);
        supSkuCraftAttrDao.insert(supSkuCraftAttrPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void initAttr(InitAttributeImportationDto dto) {
        //数据维度
        String scope = ParamValidUtils.requireNotBlank(dto.getScope(), "数据维度不能为空！请校验后导入。");
        AttributeScope attrScope = AttributeScope.getByDesc(scope);
        ParamValidUtils.requireNotNull(attrScope, StrUtil.format("数据维度:{} 不存在！请校验后导入。", scope));

        //属性名称
        String attrName = ParamValidUtils.requireNotBlank(dto.getAttributeName(), "属性名称不能为空！请校验后导入。");
        boolean attrNameExist = checkIsExist(attrName, 0L, attrScope);
        ParamValidUtils.requireEquals(attrNameExist, false, StrUtil.format("属性名称：{}已存在！请校验后导入", attrName));

        //次级属性类型
        String attrCategoryName = ParamValidUtils.requireNotBlank(dto.getAttributeCategoryName(),
                "次级属性类型不能为空！请校验后导入。");
        AttributeCategoryPo attrCategoryPo = attributeCategoryDao.getByName(attrCategoryName, 0L);
        ParamValidUtils.requireNotNull(attrCategoryPo,
                StrUtil.format("次级属性类型:{} 不存在！请校验后导入。", attrCategoryName));
        Long attrCategoryId = attrCategoryPo.getAttributeCategoryId();

        //是否必填
        String isRequired = ParamValidUtils.requireNotBlank(dto.getIsRequired(), "是否必填不能为空！请校验后导入。");
        AttributeIsRequired required = AttributeIsRequired.getByDesc(dto.getIsRequired());
        ParamValidUtils.requireNotNull(required, StrUtil.format("是否必填:{} 不存在！请校验后导入。", isRequired));

        //录入类型
        String inputType = ParamValidUtils.requireNotBlank(dto.getInputType(), "录入类型不能为空！请校验后导入。");
        AttributeInputType attrInputType = AttributeInputType.getByDesc(inputType);
        ParamValidUtils.requireNotNull(attrInputType, StrUtil.format("录入类型:{} 不存在！请校验后导入。", inputType));

        AttributePo attributePo = ProdBuilder.buildAttributePo(attrName,
                attrCategoryId, attrCategoryName, attrInputType, required, attrScope);
        attributeDao.insert(attributePo);
        Long attributeId = attributePo.getAttributeId();

        //属性选项
        if (attrInputType == AttributeInputType.SINGLE_SELECT ||
                attrInputType == AttributeInputType.MULTIPLE_SELECT) {
            String attributeValue = ParamValidUtils.requireNotBlank(dto.getAttributeOptValues(),
                    "属性值不能为空！请校验后导入。");
            String[] attrValueArr = attributeValue.split(",");
            List<String> optValueList = Arrays.asList(attrValueArr);
            ParamValidUtils.requireEquals(true, optValueList.stream().allMatch(attrVal -> StrUtil.length(attrVal) <= 500),
                    StrUtil.format("{} 单行文本/多长文本长度不能超过500个字符，请校验后提交", attrName));
            List<AttributeOptionPo> attributeOptionPoList
                    = ProdBuilder.buildAddAttributeOptionPoList(attributeId, attrName, optValueList);
            attributeOptionDao.insertBatch(attributeOptionPoList);
        }

        //关联类目
        String relateCategoryNames = ParamValidUtils.requireNotBlank(dto.getRelateCategoryNames(),
                "关联类目不能为空！请校验后导入。");
        String[] relateCategoryNameList = relateCategoryNames.split(",");

        List<AddAttributeDto.SkuCategoryDto> plmCategoryList = Arrays.stream(relateCategoryNameList).map(categoryName -> {
            ObtainCategoryVo category = plmRemoteService.getCategoryByName(categoryName);
            ParamValidUtils.requireNotNull(category,
                    StrUtil.format("关联类目:{} 不存在！请校验后导入。", categoryName));
            ParamValidUtils.requireNotBlank(category.getCategoryName(),
                    StrUtil.format("关联类目:{} 不存在！请校验后导入。", categoryName));

            AddAttributeDto.SkuCategoryDto skuCategoryDto = new AddAttributeDto.SkuCategoryDto();
            skuCategoryDto.setSkuCategoryId(category.getCategoryId());
            skuCategoryDto.setSkuCategoryName(category.getCategoryName());
            return skuCategoryDto;
        }).collect(Collectors.toList());
        List<PlmCategoryRelatePo> plmCategoryRelatePos
                = ProdBuilder.buildPlmCategoryRelatePoList(attributeId, plmCategoryList);
        plmCategoryRelateDao.insertBatch(plmCategoryRelatePos);
    }
}









