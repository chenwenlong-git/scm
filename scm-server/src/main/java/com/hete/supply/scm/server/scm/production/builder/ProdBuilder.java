package com.hete.supply.scm.server.scm.production.builder;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataAttrImportDto;
import com.hete.supply.scm.api.scm.entity.enums.CrotchLength;
import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import com.hete.supply.scm.server.scm.entity.bo.AttrVariantCompareBo;
import com.hete.supply.scm.server.scm.entity.bo.UpdateProduceDataAttrBo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataAttrPo;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProduceDataRawProcessImportDto;
import com.hete.supply.scm.server.scm.entity.dto.ProdDataAttrImportDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemSupplierPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataAttrPo;
import com.hete.supply.scm.server.scm.entity.po.ScmImagePo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.production.entity.bo.AttrCategoryInitBo;
import com.hete.supply.scm.server.scm.production.entity.bo.VerifyAttrValueBo;
import com.hete.supply.scm.server.scm.production.entity.dto.*;
import com.hete.supply.scm.server.scm.production.entity.po.*;
import com.hete.supply.scm.server.scm.production.entity.vo.*;
import com.hete.supply.scm.server.scm.production.enums.*;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
public class ProdBuilder {
    public static List<AttributeCategoryVo> buildAttributeCategoryVos(List<AttributeCategoryPo> attributeCategoryPos) {
        if (CollectionUtils.isEmpty(attributeCategoryPos)) {
            return Collections.emptyList();
        }
        return attributeCategoryPos.stream().map(attrPo -> {
            AttributeCategoryVo attrVo = new AttributeCategoryVo();
            attrVo.setAttCategoryId(attrPo.getAttributeCategoryId());
            attrVo.setAttrCategoryName(attrPo.getAttributeCategoryName());
            return attrVo;
        }).collect(Collectors.toList());
    }

    public static AttributePo buildAttributePo(AttributeCategoryPo attrCategoryPo, AddAttributeDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        AttributePo attributePo = new AttributePo();
        attributePo.setAttributeName(dto.getAttrName().trim());
        attributePo.setAttributeCategoryId(attrCategoryPo.getAttributeCategoryId());
        attributePo.setAttributeCategoryName(attrCategoryPo.getAttributeCategoryName());
        attributePo.setInputType(dto.getAttributeInputType());
        attributePo.setIsRequired(dto.getAttributeIsRequired());
        attributePo.setScope(dto.getAttributeScope());
        attributePo.setStatus(AttributeStatus.ENABLE);
        return attributePo;
    }

    public static List<AttributeOptionPo> buildAddAttributeOptionPos(Long attributeId,
                                                                     String attributeName,
                                                                     List<AddAttributeDto.AddAttributeOptionDto> attributeOptionList) {
        if (CollectionUtils.isEmpty(attributeOptionList)) {
            return Collections.emptyList();
        }
        return attributeOptionList.stream().map(attrOptionDto -> {
            AttributeOptionPo attributeOptionPo = new AttributeOptionPo();
            attributeOptionPo.setAttributeId(attributeId);
            attributeOptionPo.setAttributeName(attributeName);
            attributeOptionPo.setAttributeValue(attrOptionDto.getAttributeValue());
            return attributeOptionPo;
        }).collect(Collectors.toList());
    }

    public static AttributeOptionPo buildAddAttributeOptionPo(Long attributeId,
                                                              String attributeName,
                                                              String attrOptionValue) {
        if (Objects.isNull(attributeId)) {
            return null;
        }
        AttributeOptionPo attributeOptionPo = new AttributeOptionPo();
        attributeOptionPo.setAttributeId(attributeId);
        attributeOptionPo.setAttributeName(attributeName);
        attributeOptionPo.setAttributeValue(attrOptionValue);
        return attributeOptionPo;
    }

    public static List<AttributeInfoVo> buildAttributeDetailVoList(List<AttributePo> attributePoList) {
        if (CollectionUtils.isEmpty(attributePoList)) {
            return Collections.emptyList();
        }
        return attributePoList.stream().map(attributePo -> {
            AttributeInfoVo attributeInfoVo = new AttributeInfoVo();
            attributeInfoVo.setAttrId(attributePo.getAttributeId());
            attributeInfoVo.setAttrName(attributePo.getAttributeName());
            attributeInfoVo.setSecondAttrCategoryId(attributePo.getAttributeCategoryId());
            attributeInfoVo.setAttributeInputType(attributePo.getInputType());
            attributeInfoVo.setAttributeIsRequired(attributePo.getIsRequired());
            attributeInfoVo.setAttributeStatus(attributePo.getStatus());
            attributeInfoVo.setAttributeScope(attributePo.getScope());
            attributeInfoVo.setVersion(attributePo.getVersion());
            return attributeInfoVo;
        }).collect(Collectors.toList());
    }

    public static List<AttributeOptionVo> buildAttributeOptionVoList(List<AttributeOptionPo> attributeOptionPos) {
        if (CollectionUtils.isEmpty(attributeOptionPos)) {
            return Collections.emptyList();
        }
        return attributeOptionPos.stream().map(attrOptionPo -> {
            AttributeOptionVo attributeOptionVo = new AttributeOptionVo();
            attributeOptionVo.setAttributeId(attrOptionPo.getAttributeId());
            attributeOptionVo.setAttrOptionId(attrOptionPo.getAttributeOptionId());
            attributeOptionVo.setAttributeValue(attrOptionPo.getAttributeValue());
            return attributeOptionVo;
        }).collect(Collectors.toList());
    }

    public static List<SkuAttrValueVo> buildSkuAttrValueVoList(List<SkuAttributeValuePo> matchSkuAttrValueList) {
        if (CollectionUtils.isEmpty(matchSkuAttrValueList)) {
            return Collections.emptyList();
        }
        return matchSkuAttrValueList.stream().map(skuAttrValuePo -> {
            SkuAttrValueVo skuAttrValueVo = new SkuAttrValueVo();
            skuAttrValueVo.setSkuAttrValueId(skuAttrValuePo.getSkuAttributeValueId());
            skuAttrValueVo.setValueId(skuAttrValuePo.getValueId());
            skuAttrValueVo.setValue(skuAttrValuePo.getValue());
            return skuAttrValueVo;
        }).collect(Collectors.toList());
    }

    public static SkuAttributePo buildSkuAttributePo(String sku, Long attrId, String attrName) {
        SkuAttributePo skuAttributePo = new SkuAttributePo();
        skuAttributePo.setSku(sku);
        skuAttributePo.setAttributeId(attrId);
        skuAttributePo.setAttributeName(attrName);
        return skuAttributePo;
    }

    public static List<SkuAttributeValuePo> buildSkuAttributeValuePoList(Long skuAttributeId, List<UpdateSkuAttributeDto.SkuAttrValueDto> skuAttrValDtoList) {
        if (CollectionUtils.isEmpty(skuAttrValDtoList)) {
            return Collections.emptyList();
        }
        return skuAttrValDtoList.stream().map(attrValueDto -> {
            SkuAttributeValuePo skuAttributeValuePo = new SkuAttributeValuePo();
            skuAttributeValuePo.setSkuAttributeId(skuAttributeId);
            skuAttributeValuePo.setValueId(attrValueDto.getValueId());
            skuAttributeValuePo.setValue(attrValueDto.getValue());
            return skuAttributeValuePo;
        }).collect(Collectors.toList());
    }

    public static List<SkuAttributeValuePo> buildSkuAttributeValuePoList(Long skuAttrId, List<String> attrValueList, List<AttributeOptionPo> attributeOptionPos) {
        if (CollectionUtils.isEmpty(attrValueList)) {
            return Collections.emptyList();
        }
        return attrValueList.stream().map(attrValue -> {
            SkuAttributeValuePo skuAttributeValuePo = new SkuAttributeValuePo();
            skuAttributeValuePo.setSkuAttributeId(skuAttrId);
            skuAttributeValuePo.setValue(attrValue);
            attributeOptionPos.stream().filter(attrOptionPo -> Objects.equals(attrOptionPo.getAttributeValue(), attrValue))
                    .findFirst().ifPresent(attributeOptionPo -> skuAttributeValuePo.setValueId(attributeOptionPo.getAttributeOptionId()));
            return skuAttributeValuePo;
        }).collect(Collectors.toList());
    }

    public static List<SupSkuMaterialAttrDetailVo> buildMaterialAttrDetailVoList(List<SupplierSkuMaterialAttributePo> supplierSkuMaterialAttributePos) {
        if (CollectionUtils.isEmpty(supplierSkuMaterialAttributePos)) {
            return Collections.emptyList();
        }
        return supplierSkuMaterialAttributePos.stream().map(supplierSkuMaterialAttributePo -> {
            SupSkuMaterialAttrDetailVo supSkuMaterialAttrDetailVo = new SupSkuMaterialAttrDetailVo();
            supSkuMaterialAttrDetailVo.setSupplierSkuMaterialAttrId(supplierSkuMaterialAttributePo.getSupplierSkuMaterialAttributeId());
            supSkuMaterialAttrDetailVo.setCrotchLength(supplierSkuMaterialAttributePo.getCrotchLength());
            supSkuMaterialAttrDetailVo.setCrotchPosition(supplierSkuMaterialAttributePo.getCrotchPosition());
            supSkuMaterialAttrDetailVo.setDarkWeight(supplierSkuMaterialAttributePo.getDarkWeight());
            supSkuMaterialAttrDetailVo.setLightWeight(supplierSkuMaterialAttributePo.getLightWeight());
            supSkuMaterialAttrDetailVo.setCrotchLengthRatio(supplierSkuMaterialAttributePo.getCrotchLengthRatio());
            supSkuMaterialAttrDetailVo.setWeight(supplierSkuMaterialAttributePo.getWeight());
            return supSkuMaterialAttrDetailVo;
        }).collect(Collectors.toList());
    }

    public static List<SupSkuCraftAttrDetailVo> buildCraftAttrDetailVoList(List<SupplierSkuCraftAttributePo> supplierSkuCraftAttributePos) {
        if (CollectionUtils.isEmpty(supplierSkuCraftAttributePos)) {
            return Collections.emptyList();
        }
        return supplierSkuCraftAttributePos.stream().map(supplierSkuCraftAttributePo -> {
            SupSkuCraftAttrDetailVo supSkuCraftAttrDetailVo = new SupSkuCraftAttrDetailVo();
            supSkuCraftAttrDetailVo.setSupplierSkuCraftAttrId(supplierSkuCraftAttributePo.getSupplierSkuCraftAttributeId());
            supSkuCraftAttrDetailVo.setTubeWrapping(supplierSkuCraftAttributePo.getTubeWrapping());
            supSkuCraftAttrDetailVo.setRootsCnt(supplierSkuCraftAttributePo.getRootsCnt());
            supSkuCraftAttrDetailVo.setLayersCnt(supplierSkuCraftAttributePo.getLayersCnt());
            supSkuCraftAttrDetailVo.setSpecialHandling(supplierSkuCraftAttributePo.getSpecialHandling());
            return supSkuCraftAttrDetailVo;
        }).collect(Collectors.toList());
    }

    public static List<SupplierSkuMaterialAttributePo> buildSupplierSkuMaterialAttributePoList(String sku,
                                                                                               String supplierCode,
                                                                                               List<UpdateSupSkuMaterialAttrDetailDto> updateSupSkuMaterialAttrDetailDtoList) {
        if (CollectionUtils.isEmpty(updateSupSkuMaterialAttrDetailDtoList)) {
            return Collections.emptyList();
        }
        return updateSupSkuMaterialAttrDetailDtoList.stream().map(updateSupSkuMaterialAttrDetailDto -> {
            SupplierSkuMaterialAttributePo supplierSkuMaterialAttributePo = new SupplierSkuMaterialAttributePo();
            supplierSkuMaterialAttributePo.setSku(sku);
            supplierSkuMaterialAttributePo.setSupplierCode(supplierCode);
            supplierSkuMaterialAttributePo.setCrotchLength(updateSupSkuMaterialAttrDetailDto.getCrotchLength());
            supplierSkuMaterialAttributePo.setCrotchPosition(updateSupSkuMaterialAttrDetailDto.getCrotchPosition());
            supplierSkuMaterialAttributePo.setDarkWeight(updateSupSkuMaterialAttrDetailDto.getDarkWeight());
            supplierSkuMaterialAttributePo.setLightWeight(updateSupSkuMaterialAttrDetailDto.getLightWeight());
            supplierSkuMaterialAttributePo.setCrotchLengthRatio(updateSupSkuMaterialAttrDetailDto.getCrotchLengthRatio());
            supplierSkuMaterialAttributePo.setWeight(updateSupSkuMaterialAttrDetailDto.getWeight());
            return supplierSkuMaterialAttributePo;
        }).collect(Collectors.toList());
    }

    public static List<SupplierSkuCraftAttributePo> buildSupplierSkuCraftAttributePoList(String sku,
                                                                                         String supplierCode,
                                                                                         List<UpdateSupSkuCraftAttrDetailDto> updateSupSkuCraftAttrDetailDtoList) {
        if (CollectionUtils.isEmpty(updateSupSkuCraftAttrDetailDtoList)) {
            return Collections.emptyList();
        }
        return updateSupSkuCraftAttrDetailDtoList.stream().map(updateSupSkuCraftAttrDetailDto -> {
            SupplierSkuCraftAttributePo supplierSkuCraftAttributePo = new SupplierSkuCraftAttributePo();
            supplierSkuCraftAttributePo.setSku(sku);
            supplierSkuCraftAttributePo.setSupplierCode(supplierCode);
            supplierSkuCraftAttributePo.setTubeWrapping(updateSupSkuCraftAttrDetailDto.getTubeWrapping());
            supplierSkuCraftAttributePo.setRootsCnt(updateSupSkuCraftAttrDetailDto.getRootsCnt());
            supplierSkuCraftAttributePo.setLayersCnt(updateSupSkuCraftAttrDetailDto.getLayersCnt());
            supplierSkuCraftAttributePo.setSpecialHandling(updateSupSkuCraftAttrDetailDto.getSpecialHandling());
            return supplierSkuCraftAttributePo;
        }).collect(Collectors.toList());
    }

    public static List<SupSkuAttrValueVo> buildSupSkuAttrValueVoList(List<SupplierSkuAttributeValuePo> matchSupSkuAttrValueList) {
        if (CollectionUtils.isEmpty(matchSupSkuAttrValueList)) {
            return Collections.emptyList();
        }
        return matchSupSkuAttrValueList.stream().map(supplierSkuAttributeValuePo -> {
            SupSkuAttrValueVo supSkuAttrValueVo = new SupSkuAttrValueVo();
            supSkuAttrValueVo.setSupSkuAttrValueId(supplierSkuAttributeValuePo.getSupplierSkuAttributeValueId());
            supSkuAttrValueVo.setValueId(supplierSkuAttributeValuePo.getValueId());
            supSkuAttrValueVo.setValue(supplierSkuAttributeValuePo.getValue());
            return supSkuAttrValueVo;
        }).collect(Collectors.toList());
    }

    public static List<PlmCategoryRelatePo> buildPlmCategoryRelatePoList(Long attributeId,
                                                                         List<AddAttributeDto.SkuCategoryDto> skuCategoryList) {
        if (CollectionUtils.isEmpty(skuCategoryList)) {
            return Collections.emptyList();
        }
        return skuCategoryList.stream().map(skuCategoryDto -> {
            PlmCategoryRelatePo plmCategoryRelatePo = new PlmCategoryRelatePo();
            plmCategoryRelatePo.setBizId(attributeId);
            plmCategoryRelatePo.setBizType(PlmCategoryRelateBizType.ATTRIBUTE);
            plmCategoryRelatePo.setCategoryId(skuCategoryDto.getSkuCategoryId());
            plmCategoryRelatePo.setCategoryName(skuCategoryDto.getSkuCategoryName());
            return plmCategoryRelatePo;
        }).collect(Collectors.toList());
    }

    public static List<SkuCategoryVo> buildSkuCategoryVoList(List<PlmCategoryRelatePo> plmCategoryRelatePo) {
        if (CollectionUtils.isEmpty(plmCategoryRelatePo)) {
            return Collections.emptyList();
        }
        return plmCategoryRelatePo.stream().map(po -> {
            SkuCategoryVo skuCategoryVo = new SkuCategoryVo();
            skuCategoryVo.setSkuCategoryId(po.getCategoryId());
            skuCategoryVo.setSkuCategoryName(po.getCategoryName());
            return skuCategoryVo;
        }).collect(Collectors.toList());
    }


    public static SupplierSkuAttributePo buildSupplierSkuAttributePo(String sku, String supplierCode, Long attrId, String attrName) {
        SupplierSkuAttributePo supplierSkuAttributePo = new SupplierSkuAttributePo();
        supplierSkuAttributePo.setSku(sku);
        supplierSkuAttributePo.setSupplierCode(supplierCode);
        supplierSkuAttributePo.setAttributeId(attrId);
        supplierSkuAttributePo.setAttributeName(attrName);
        return supplierSkuAttributePo;
    }

    public static List<SupplierSkuAttributeValuePo> buildSupplierSkuAttributeValuePoList(Long supplierSkuAttrId, List<String> attrValueList, List<AttributeOptionPo> attributeOptionPos) {
        if (CollectionUtils.isEmpty(attrValueList)) {
            return Collections.emptyList();
        }
        return attrValueList.stream().map(attrValue -> {
            SupplierSkuAttributeValuePo supplierSkuAttributeValuePo = new SupplierSkuAttributeValuePo();
            supplierSkuAttributeValuePo.setSupplierSkuAttributeId(supplierSkuAttrId);
            supplierSkuAttributeValuePo.setValue(attrValue);
            attributeOptionPos.stream()
                    .filter(attributeOptionPo -> attrValue.equals(attributeOptionPo.getAttributeValue())).findFirst()
                    .ifPresent(attributeOptionPo -> supplierSkuAttributeValuePo.setValueId(attributeOptionPo.getAttributeOptionId()));
            return supplierSkuAttributeValuePo;
        }).collect(Collectors.toList());
    }

    public static List<SupplierSkuAttributeValuePo> buildSupplierSkuAttributeValuePoList(Long supplierSkuAttrId, List<SupSkuAttributeInfoDto.SupSkuAttrValueDto> attrValueList) {
        if (CollectionUtils.isEmpty(attrValueList)) {
            return Collections.emptyList();
        }
        return attrValueList.stream().map(attrValDto -> {
            SupplierSkuAttributeValuePo supplierSkuAttributeValuePo = new SupplierSkuAttributeValuePo();
            supplierSkuAttributeValuePo.setSupplierSkuAttributeId(supplierSkuAttrId);
            supplierSkuAttributeValuePo.setValue(attrValDto.getValue());
            supplierSkuAttributeValuePo.setValueId(attrValDto.getValueId());
            return supplierSkuAttributeValuePo;
        }).collect(Collectors.toList());
    }

    public static AttributeRiskPo buildAttributeRiskPo(UpdateAttributeRiskInfoDto updateAttributeRiskInfoDto) {
        AttributeRiskPo attributeRiskPo = new AttributeRiskPo();
        attributeRiskPo.setAttributeId(updateAttributeRiskInfoDto.getAttrId());
        attributeRiskPo.setAttributeName(updateAttributeRiskInfoDto.getAttrName());
        attributeRiskPo.setCoefficient(updateAttributeRiskInfoDto.getCoefficient());
        return attributeRiskPo;
    }

    public static List<AttributeOptionRiskPo> buildAttributeOptionRiskPoList(Long attrRiskId, List<AttributeRiskOptInfoDto> attributeRiskOptInfoList) {
        if (CollectionUtils.isEmpty(attributeRiskOptInfoList)) {
            return Collections.emptyList();
        }
        return attributeRiskOptInfoList.stream().map(attributeRiskOptInfoDto -> {
            AttributeOptionRiskPo attributeOptionRiskPo = new AttributeOptionRiskPo();
            attributeOptionRiskPo.setAttributeRiskId(attrRiskId);
            attributeOptionRiskPo.setAttributeOptionId(attributeRiskOptInfoDto.getAttrOptionId());
            attributeOptionRiskPo.setAttributeOptionValue(attributeRiskOptInfoDto.getAttributeValue());
            attributeOptionRiskPo.setScore(attributeRiskOptInfoDto.getScore());
            return attributeOptionRiskPo;
        }).collect(Collectors.toList());
    }

    public static List<AttributeRiskInfoVo> buildAttributeRiskInfoVoList(List<AttributeRiskPo> attrRiskPoList,
                                                                         List<AttributePo> attrPoList,
                                                                         List<AttributeOptionRiskPo> attrOptRiskPoList,
                                                                         List<AttributeOptionPo> attrOptionPoList) {
        if (CollectionUtils.isEmpty(attrRiskPoList)) {
            return Collections.emptyList();
        }
        return attrRiskPoList.stream().map(attributeRiskPo -> {
            Long attrRiskId = attributeRiskPo.getAttributeRiskId();
            Long attributeId = attributeRiskPo.getAttributeId();

            AttributeRiskInfoVo attributeRiskInfoVo = new AttributeRiskInfoVo();
            attributeRiskInfoVo.setAttrRiskId(attrRiskId);
            attributeRiskInfoVo.setAttrId(attributeId);
            attributeRiskInfoVo.setCoefficient(attributeRiskPo.getCoefficient());

            attributeRiskInfoVo.setAttrName(attributeRiskPo.getAttributeName());
            attrPoList.stream().filter(attributePo -> Objects.equals(attributeId, attributePo.getAttributeId())).findFirst()
                    .ifPresent(attributePo -> attributeRiskInfoVo.setAttrName(attributePo.getAttributeName()));

            List<AttributeRiskOptInfoVo> attributeRiskOptInfoList = attrOptRiskPoList.stream()
                    .filter(attrOptionRiskPo -> Objects.equals(attrRiskId, attrOptionRiskPo.getAttributeRiskId()))
                    .map(attrOptionRiskPo -> {
                        Long attributeOptId = attrOptionRiskPo.getAttributeOptionId();

                        AttributeRiskOptInfoVo attributeRiskOptInfoVo = new AttributeRiskOptInfoVo();
                        attributeRiskOptInfoVo.setAttrOptionId(attributeOptId);
                        attributeRiskOptInfoVo.setScore(attrOptionRiskPo.getScore());

                        attributeRiskOptInfoVo.setAttributeValue(attrOptionRiskPo.getAttributeOptionValue());
                        attrOptionPoList.stream().filter(attributeOptionPo -> Objects.equals(attributeOptId, attributeOptionPo.getAttributeOptionId())).findFirst()
                                .ifPresent(attributeOptionPo -> attributeRiskOptInfoVo.setAttributeValue(attributeOptionPo.getAttributeValue()));
                        return attributeRiskOptInfoVo;
                    }).collect(Collectors.toList());
            attributeRiskInfoVo.setAttributeRiskOptInfoList(attributeRiskOptInfoList);
            return attributeRiskInfoVo;
        }).collect(Collectors.toList());
    }

    public static AttributePo buildUpdateAttributePo(AttributeCategoryPo attrCategoryPo, AttributePo attributePo, UpdateAttributeDto dto) {
        attributePo.setAttributeName(dto.getAttrName());
        attributePo.setAttributeCategoryId(attrCategoryPo.getAttributeCategoryId());
        attributePo.setAttributeCategoryName(attrCategoryPo.getAttributeCategoryName());
        attributePo.setIsRequired(dto.getAttributeIsRequired());
        attributePo.setStatus(dto.getAttributeStatus());
        return attributePo;
    }

    public static AttributeCategoryPo buildAttributeCategoryPo(String categoryName) {
        AttributeCategoryPo attributeCategoryPo = new AttributeCategoryPo();
        attributeCategoryPo.setAttributeCategoryName(categoryName);
        return attributeCategoryPo;
    }

    public static List<AttributeCategoryPo> buildAttributeCategoryPoList(Long parentAttributeCategoryId, List<AttrCategoryInitBo.SubCategory> subCategoryNames) {
        return subCategoryNames.stream().map(subCategory -> {
            AttributeCategoryPo attributeCategoryPo = new AttributeCategoryPo();
            attributeCategoryPo.setParentAttributeCategoryId(parentAttributeCategoryId);
            attributeCategoryPo.setAttributeCategoryName(subCategory.getCategoryName());
            return attributeCategoryPo;
        }).collect(Collectors.toList());
    }

    public static List<SkuRiskPo> buildSkuRiskPoList(Map<String, SkuRisk> skuRiskMap, TreeMap<String, BigDecimal> skuScoreMap) {
        if (CollectionUtils.isEmpty(skuRiskMap)) {
            return Collections.emptyList();
        }
        return skuRiskMap.entrySet().stream().map(entry -> {
            SkuRiskPo skuRiskPo = new SkuRiskPo();
            skuRiskPo.setSku(entry.getKey());
            skuRiskPo.setScore(skuScoreMap.getOrDefault(entry.getKey(), BigDecimal.ZERO));
            skuRiskPo.setLevel(entry.getValue());
            return skuRiskPo;
        }).collect(Collectors.toList());
    }

    public static SupplierSkuSamplePo buildSupplierSkuSamplePo(String sku, String supplierCode, String sourceOrderNo) {
        SupplierSkuSamplePo skuSupplierSamplePo = new SupplierSkuSamplePo();
        skuSupplierSamplePo.setSku(sku);
        skuSupplierSamplePo.setSupplierCode(supplierCode);
        skuSupplierSamplePo.setSourceOrderNo(sourceOrderNo);
        return skuSupplierSamplePo;
    }

    public static List<ScmImagePo> buildScmImagePoList(Long supplierSkuSampleId, Set<String> samplePicFileCodeList) {
        return samplePicFileCodeList.stream().map(fileCode -> {
            ScmImagePo scmImagePo = new ScmImagePo();
            scmImagePo.setFileCode(fileCode);
            scmImagePo.setImageBizType(ImageBizType.SUPPLIER_SKU_SAMPLE_PIC);
            scmImagePo.setImageBizId(supplierSkuSampleId);
            return scmImagePo;
        }).collect(Collectors.toList());
    }

    public static List<VerifyAttrValueBo> buildVerifySkuAttrValueBoList(List<UpdateSkuAttributeDto.SkuAttrValueDto> skuAttrValDtoList) {
        if (CollectionUtils.isEmpty(skuAttrValDtoList)) {
            return Collections.emptyList();
        }
        return skuAttrValDtoList.stream().map(skuAttrValDto -> {
            VerifyAttrValueBo verifyAttrValueBo = new VerifyAttrValueBo();
            verifyAttrValueBo.setAttrOptId(skuAttrValDto.getValueId());
            verifyAttrValueBo.setValue(skuAttrValDto.getValue());
            return verifyAttrValueBo;
        }).collect(Collectors.toList());
    }

    public static List<VerifyAttrValueBo> buildVerifySupSkuAttrValueBoList(List<SupSkuAttributeInfoDto.SupSkuAttrValueDto> supSkuAttrValDtoList) {
        if (CollectionUtils.isEmpty(supSkuAttrValDtoList)) {
            return Collections.emptyList();
        }
        return supSkuAttrValDtoList.stream().map(supSkuAttrValDto -> {
            VerifyAttrValueBo verifyAttrValueBo = new VerifyAttrValueBo();
            verifyAttrValueBo.setAttrOptId(supSkuAttrValDto.getValueId());
            verifyAttrValueBo.setValue(supSkuAttrValDto.getValue());
            return verifyAttrValueBo;
        }).collect(Collectors.toList());
    }

    public static SkuRiskLogPo buildSkuRiskLogPo(String sku, String supplierCode,
                                                 Long attrId, String attrName,
                                                 Long attrOptId, String attrOptName,
                                                 BigDecimal score, BigDecimal riskCoefficient) {
        SkuRiskLogPo skuRiskLogPo = new SkuRiskLogPo();
        skuRiskLogPo.setSku(sku);
        skuRiskLogPo.setSupplierCode(supplierCode);
        skuRiskLogPo.setAttributeId(attrId);
        skuRiskLogPo.setAttributeName(attrName);
        skuRiskLogPo.setAttributeOptionId(attrOptId);
        skuRiskLogPo.setAttributeOptionValue(attrOptName);
        skuRiskLogPo.setScore(score);
        skuRiskLogPo.setCoefficient(riskCoefficient);
        return skuRiskLogPo;
    }


    public static SupplierSkuMaterialAttributePo buildSupplierSkuMaterialAttributePo(String sku, String supplierCode,
                                                                                     CrotchLength crotchLength, String crotchPosition,
                                                                                     BigDecimal darkWeight, BigDecimal lightWeight,
                                                                                     String crotchLengthRatio, BigDecimal weight) {
        SupplierSkuMaterialAttributePo po = new SupplierSkuMaterialAttributePo();
        po.setSku(sku);
        po.setSupplierCode(supplierCode);
        po.setCrotchLength(crotchLength);
        po.setCrotchPosition(crotchPosition);
        if (Objects.nonNull(darkWeight)) {
            po.setDarkWeight(darkWeight);
        }
        if (Objects.nonNull(lightWeight)) {
            po.setLightWeight(lightWeight);
        }
        po.setCrotchLengthRatio(crotchLengthRatio);
        po.setWeight(weight);
        return po;
    }

    public static SupplierSkuCraftAttributePo buildSupplierSkuCraftAttributePo(String sku, String supplierCode,
                                                                               String tubeWrapping, int rootsCnt,
                                                                               int layersCnt, String specialHandling) {
        SupplierSkuCraftAttributePo po = new SupplierSkuCraftAttributePo();
        po.setSku(sku);
        po.setSupplierCode(supplierCode);
        po.setTubeWrapping(tubeWrapping);
        po.setRootsCnt(rootsCnt);
        po.setLayersCnt(layersCnt);
        po.setSpecialHandling(specialHandling);
        return po;
    }

    public static AttributePo buildAttributePo(String attrName,
                                               Long attributeCategoryId,
                                               String attributeCategoryName,
                                               AttributeInputType attributeInputType,
                                               AttributeIsRequired required,
                                               AttributeScope attributeScope) {
        AttributePo attributePo = new AttributePo();
        attributePo.setAttributeName(attrName);
        attributePo.setAttributeCategoryId(attributeCategoryId);
        attributePo.setAttributeCategoryName(attributeCategoryName);
        attributePo.setInputType(attributeInputType);
        attributePo.setIsRequired(required);
        attributePo.setScope(attributeScope);
        attributePo.setStatus(AttributeStatus.ENABLE);
        return attributePo;
    }

    public static List<AttributeOptionPo> buildAddAttributeOptionPoList(Long attributeId, String attrName, List<String> optValueList) {
        if (CollectionUtils.isEmpty(optValueList)) {
            return Collections.emptyList();
        }
        return optValueList.stream().map(optValue -> {
            AttributeOptionPo attributeOptionPo = new AttributeOptionPo();
            attributeOptionPo.setAttributeId(attributeId);
            attributeOptionPo.setAttributeName(attrName);
            attributeOptionPo.setAttributeValue(optValue);
            return attributeOptionPo;
        }).collect(Collectors.toList());
    }

    public static List<ProduceDataAttrPo> buildProduceDataAttrPoList(String sku, List<ProduceDataAttrPo> copyProdAttrPoList) {
        if (CollectionUtils.isEmpty(copyProdAttrPoList)) {
            return Collections.emptyList();
        }
        return copyProdAttrPoList.stream().map(copyProdAttrPo -> {
            ProduceDataAttrPo produceDataAttrPo = new ProduceDataAttrPo();
            produceDataAttrPo.setAttributeNameId(copyProdAttrPo.getAttributeNameId());
            produceDataAttrPo.setAttrName(copyProdAttrPo.getAttrName());
            produceDataAttrPo.setAttrValue(copyProdAttrPo.getAttrValue());

            produceDataAttrPo.setSku(sku);
            produceDataAttrPo.setSpu(copyProdAttrPo.getSpu());
            return produceDataAttrPo;
        }).collect(Collectors.toList());
    }

    public static List<ProduceDataAttrPo> buildProduceDataAttrPoList(String sku, String spu, List<UpdateProduceDataAttrBo> updateProduceDataAttrList) {
        if (CollectionUtils.isEmpty(updateProduceDataAttrList)) {
            return Collections.emptyList();
        }
        return updateProduceDataAttrList.stream().map(updateProduceDataAttrBo -> {
            ProduceDataAttrPo produceDataAttrPo = new ProduceDataAttrPo();

            produceDataAttrPo.setSku(sku);
            produceDataAttrPo.setSpu(spu);

            produceDataAttrPo.setAttributeNameId(updateProduceDataAttrBo.getAttributeNameId());
            produceDataAttrPo.setAttrName(updateProduceDataAttrBo.getAttributeName());
            produceDataAttrPo.setAttrValue(updateProduceDataAttrBo.getAttributeValue());
            return produceDataAttrPo;
        }).collect(Collectors.toList());
    }

    public static List<UpdateProduceDataAttrBo> buildUpdateProduceDataAttrList(String sku, List<AttrVariantCompareBo> attrVariantCompareConfig) {
        if (CollectionUtils.isEmpty(attrVariantCompareConfig)) {
            return Collections.emptyList();
        }
        return attrVariantCompareConfig.stream()
                .map(attrVariantCompareBo -> {
                    UpdateProduceDataAttrBo updateProduceDataAttrBo = new UpdateProduceDataAttrBo();
                    updateProduceDataAttrBo.setSku(sku);
                    updateProduceDataAttrBo.setAttributeNameId(attrVariantCompareBo.getAttributeNameId());
                    updateProduceDataAttrBo.setAttributeValueId(attrVariantCompareBo.getAttributeValueId());
                    updateProduceDataAttrBo.setVariantName(attrVariantCompareBo.getVariantName());
                    updateProduceDataAttrBo.setVariantValue(attrVariantCompareBo.getVariantValue());
                    return updateProduceDataAttrBo;
                })
                .collect(Collectors.toList());
    }

    public static List<ProduceDataRamImpDto> buildProduceDataRamImpDtoList(ProduceDataRawProcessImportDto dto) {
        return Stream.of(
                        new ProduceDataRamImpDto(dto.getRawSku1(), dto.getSkuCnt1()),
                        new ProduceDataRamImpDto(dto.getRawSku2(), dto.getSkuCnt2()),
                        new ProduceDataRamImpDto(dto.getRawSku3(), dto.getSkuCnt3()),
                        new ProduceDataRamImpDto(dto.getRawSku4(), dto.getSkuCnt4()),
                        new ProduceDataRamImpDto(dto.getRawSku5(), dto.getSkuCnt5()),
                        new ProduceDataRamImpDto(dto.getRawSku6(), dto.getSkuCnt6()),
                        new ProduceDataRamImpDto(dto.getRawSku7(), dto.getSkuCnt7()))
                .filter(raw -> StrUtil.isNotBlank(raw.getRawSku()) || StrUtil.isNotBlank(raw.getSkuCnt()))
                .collect(Collectors.toList());
    }

    public static List<ProduceDataProcessImpDto> buildProduceDataProcessImpDtoList(ProduceDataRawProcessImportDto dto) {
        return Stream.of(
                        new ProduceDataProcessImpDto(dto.getProcess1(), dto.getProcessSecond1()),
                        new ProduceDataProcessImpDto(dto.getProcess2(), dto.getProcessSecond2()),
                        new ProduceDataProcessImpDto(dto.getProcess3(), dto.getProcessSecond3()),
                        new ProduceDataProcessImpDto(dto.getProcess4(), dto.getProcessSecond4()),
                        new ProduceDataProcessImpDto(dto.getProcess5(), dto.getProcessSecond5())
                ).filter(process -> StrUtil.isNotBlank(process.getProcess()) || StrUtil.isNotBlank(process.getProcessSecond()))
                .collect(Collectors.toList());
    }

    public static ProduceDataItemPo buildProduceDataItemPo(String bomName, String sku, String spu, Integer sort) {
        ProduceDataItemPo produceDataItemPo = new ProduceDataItemPo();
        produceDataItemPo.setBomName(bomName);
        produceDataItemPo.setSku(sku);
        produceDataItemPo.setSpu(spu);
        produceDataItemPo.setSort(sort);
        return produceDataItemPo;
    }

    public static ProduceDataItemSupplierPo buildProduceDataItemSupplierPo(Long produceDataItemId, String bomSku, String bomSpu, SupplierPo supplierPo) {
        ProduceDataItemSupplierPo produceDataItemSupplierPo = new ProduceDataItemSupplierPo();
        produceDataItemSupplierPo.setProduceDataItemId(produceDataItemId);
        produceDataItemSupplierPo.setSku(bomSku);
        produceDataItemSupplierPo.setSpu(bomSpu);
        produceDataItemSupplierPo.setSupplierCode(supplierPo.getSupplierCode());
        produceDataItemSupplierPo.setSupplierName(supplierPo.getSupplierName());
        return produceDataItemSupplierPo;
    }

    public static List<ProdDataAttrImportDto> buildProdDataAttrImportDtoList(ProduceDataAttrImportDto dto) {
        return Stream.of(
                        new ProdDataAttrImportDto(dto.getAttrName1(), dto.getAttrValue1()),
                        new ProdDataAttrImportDto(dto.getAttrName2(), dto.getAttrValue2()),
                        new ProdDataAttrImportDto(dto.getAttrName3(), dto.getAttrValue3()),
                        new ProdDataAttrImportDto(dto.getAttrName4(), dto.getAttrValue4()),
                        new ProdDataAttrImportDto(dto.getAttrName5(), dto.getAttrValue5()),
                        new ProdDataAttrImportDto(dto.getAttrName6(), dto.getAttrValue6()),
                        new ProdDataAttrImportDto(dto.getAttrName7(), dto.getAttrValue7()),
                        new ProdDataAttrImportDto(dto.getAttrName8(), dto.getAttrValue8()),
                        new ProdDataAttrImportDto(dto.getAttrName9(), dto.getAttrValue9()),
                        new ProdDataAttrImportDto(dto.getAttrName10(), dto.getAttrValue10()),
                        new ProdDataAttrImportDto(dto.getAttrName11(), dto.getAttrValue11()),
                        new ProdDataAttrImportDto(dto.getAttrName12(), dto.getAttrValue12()),
                        new ProdDataAttrImportDto(dto.getAttrName13(), dto.getAttrValue13()),
                        new ProdDataAttrImportDto(dto.getAttrName14(), dto.getAttrValue14()),
                        new ProdDataAttrImportDto(dto.getAttrName15(), dto.getAttrValue15()),
                        new ProdDataAttrImportDto(dto.getAttrName16(), dto.getAttrValue16()),
                        new ProdDataAttrImportDto(dto.getAttrName17(), dto.getAttrValue17()),
                        new ProdDataAttrImportDto(dto.getAttrName18(), dto.getAttrValue18()),
                        new ProdDataAttrImportDto(dto.getAttrName19(), dto.getAttrValue19()),
                        new ProdDataAttrImportDto(dto.getAttrName20(), dto.getAttrValue20())
                ).filter(attrImportDto -> StrUtil.isNotBlank(attrImportDto.getAttrName()) || StrUtil.isNotBlank(attrImportDto.getAttrValue()))
                .collect(Collectors.toList());
    }

    public static List<ProduceDataAttrPo> buildProduceDataAttrList(String sku, String spu, Long attrSysNameId, String attrSysName, Set<String> attrValueList) {
        if (CollectionUtils.isEmpty(attrValueList)) {
            return Collections.emptyList();
        }
        return attrValueList.stream().map(attrValue -> {
            ProduceDataAttrPo produceDataAttrPo = new ProduceDataAttrPo();
            produceDataAttrPo.setSku(sku);
            produceDataAttrPo.setSpu(spu);

            produceDataAttrPo.setAttributeNameId(attrSysNameId);
            produceDataAttrPo.setAttrName(attrSysName);

            produceDataAttrPo.setAttrValue(attrValue);
            return produceDataAttrPo;
        }).collect(Collectors.toList());
    }
}
