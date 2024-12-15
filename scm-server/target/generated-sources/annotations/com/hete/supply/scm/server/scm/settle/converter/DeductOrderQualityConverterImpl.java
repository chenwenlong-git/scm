package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderQualityDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderQualityPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo.DeductOrderQualityVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DeductOrderQualityConverterImpl implements DeductOrderQualityConverter {

    @Override
    public DeductOrderQualityVo convert(DeductOrderQualityPo po) {
        if ( po == null ) {
            return null;
        }

        DeductOrderQualityVo deductOrderQualityVo = new DeductOrderQualityVo();

        deductOrderQualityVo.setDeductOrderQualityId( po.getDeductOrderQualityId() );
        deductOrderQualityVo.setDeductOrderPurchaseType( po.getDeductOrderPurchaseType() );
        deductOrderQualityVo.setBusinessNo( po.getBusinessNo() );
        deductOrderQualityVo.setSpu( po.getSpu() );
        deductOrderQualityVo.setSku( po.getSku() );
        deductOrderQualityVo.setSkuNum( po.getSkuNum() );
        deductOrderQualityVo.setDeductPrice( po.getDeductPrice() );
        deductOrderQualityVo.setSettlePrice( po.getSettlePrice() );
        deductOrderQualityVo.setDeductRemarks( po.getDeductRemarks() );

        return deductOrderQualityVo;
    }

    @Override
    public List<DeductOrderQualityVo> deductOrderQualityList(List<DeductOrderQualityPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DeductOrderQualityVo> list = new ArrayList<DeductOrderQualityVo>( poList.size() );
        for ( DeductOrderQualityPo deductOrderQualityPo : poList ) {
            list.add( convert( deductOrderQualityPo ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderQualityPo> create(List<DeductOrderQualityDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderQualityPo> list = new ArrayList<DeductOrderQualityPo>( dto.size() );
        for ( DeductOrderQualityDto deductOrderQualityDto : dto ) {
            list.add( deductOrderQualityDtoToDeductOrderQualityPo( deductOrderQualityDto ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderQualityPo> edit(List<DeductOrderQualityDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderQualityPo> list = new ArrayList<DeductOrderQualityPo>( dto.size() );
        for ( DeductOrderQualityDto deductOrderQualityDto : dto ) {
            list.add( deductOrderQualityDtoToDeductOrderQualityPo( deductOrderQualityDto ) );
        }

        return list;
    }

    protected DeductOrderQualityPo deductOrderQualityDtoToDeductOrderQualityPo(DeductOrderQualityDto deductOrderQualityDto) {
        if ( deductOrderQualityDto == null ) {
            return null;
        }

        DeductOrderQualityPo deductOrderQualityPo = new DeductOrderQualityPo();

        deductOrderQualityPo.setBusinessNo( deductOrderQualityDto.getBusinessNo() );
        deductOrderQualityPo.setDeductOrderPurchaseType( deductOrderQualityDto.getDeductOrderPurchaseType() );
        deductOrderQualityPo.setSku( deductOrderQualityDto.getSku() );
        deductOrderQualityPo.setSpu( deductOrderQualityDto.getSpu() );
        deductOrderQualityPo.setSkuNum( deductOrderQualityDto.getSkuNum() );
        deductOrderQualityPo.setDeductPrice( deductOrderQualityDto.getDeductPrice() );
        deductOrderQualityPo.setSettlePrice( deductOrderQualityDto.getSettlePrice() );
        deductOrderQualityPo.setDeductRemarks( deductOrderQualityDto.getDeductRemarks() );

        return deductOrderQualityPo;
    }
}
