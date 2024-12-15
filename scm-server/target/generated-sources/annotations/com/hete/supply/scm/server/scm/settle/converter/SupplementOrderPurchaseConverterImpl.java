package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderPurchaseDto;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPurchasePo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo.SupplementOrderPurchaseVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:57+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class SupplementOrderPurchaseConverterImpl implements SupplementOrderPurchaseConverter {

    @Override
    public SupplementOrderPurchaseVo convert(SupplementOrderPurchasePo po) {
        if ( po == null ) {
            return null;
        }

        SupplementOrderPurchaseVo supplementOrderPurchaseVo = new SupplementOrderPurchaseVo();

        supplementOrderPurchaseVo.setSupplementOrderPurchaseId( po.getSupplementOrderPurchaseId() );
        supplementOrderPurchaseVo.setSupplementOrderPurchaseType( po.getSupplementOrderPurchaseType() );
        supplementOrderPurchaseVo.setBusinessNo( po.getBusinessNo() );
        supplementOrderPurchaseVo.setSku( po.getSku() );
        supplementOrderPurchaseVo.setSpu( po.getSpu() );
        supplementOrderPurchaseVo.setSkuNum( po.getSkuNum() );
        supplementOrderPurchaseVo.setSupplementPrice( po.getSupplementPrice() );
        supplementOrderPurchaseVo.setSettlePrice( po.getSettlePrice() );
        supplementOrderPurchaseVo.setSupplementRemarks( po.getSupplementRemarks() );
        supplementOrderPurchaseVo.setSkuBatchCode( po.getSkuBatchCode() );
        supplementOrderPurchaseVo.setSettleUnitPrice( po.getSettleUnitPrice() );

        return supplementOrderPurchaseVo;
    }

    @Override
    public List<SupplementOrderPurchaseVo> detail(List<SupplementOrderPurchasePo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<SupplementOrderPurchaseVo> list = new ArrayList<SupplementOrderPurchaseVo>( poList.size() );
        for ( SupplementOrderPurchasePo supplementOrderPurchasePo : poList ) {
            list.add( convert( supplementOrderPurchasePo ) );
        }

        return list;
    }

    @Override
    public List<SupplementOrderPurchasePo> create(List<SupplementOrderPurchaseDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<SupplementOrderPurchasePo> list = new ArrayList<SupplementOrderPurchasePo>( dto.size() );
        for ( SupplementOrderPurchaseDto supplementOrderPurchaseDto : dto ) {
            list.add( supplementOrderPurchaseDtoToSupplementOrderPurchasePo( supplementOrderPurchaseDto ) );
        }

        return list;
    }

    @Override
    public List<SupplementOrderPurchasePo> edit(List<SupplementOrderPurchaseDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<SupplementOrderPurchasePo> list = new ArrayList<SupplementOrderPurchasePo>( dto.size() );
        for ( SupplementOrderPurchaseDto supplementOrderPurchaseDto : dto ) {
            list.add( supplementOrderPurchaseDtoToSupplementOrderPurchasePo( supplementOrderPurchaseDto ) );
        }

        return list;
    }

    protected SupplementOrderPurchasePo supplementOrderPurchaseDtoToSupplementOrderPurchasePo(SupplementOrderPurchaseDto supplementOrderPurchaseDto) {
        if ( supplementOrderPurchaseDto == null ) {
            return null;
        }

        SupplementOrderPurchasePo supplementOrderPurchasePo = new SupplementOrderPurchasePo();

        supplementOrderPurchasePo.setBusinessNo( supplementOrderPurchaseDto.getBusinessNo() );
        supplementOrderPurchasePo.setSupplementOrderPurchaseType( supplementOrderPurchaseDto.getSupplementOrderPurchaseType() );
        supplementOrderPurchasePo.setSpu( supplementOrderPurchaseDto.getSpu() );
        supplementOrderPurchasePo.setSku( supplementOrderPurchaseDto.getSku() );
        supplementOrderPurchasePo.setSkuNum( supplementOrderPurchaseDto.getSkuNum() );
        supplementOrderPurchasePo.setSupplementPrice( supplementOrderPurchaseDto.getSupplementPrice() );
        supplementOrderPurchasePo.setSettlePrice( supplementOrderPurchaseDto.getSettlePrice() );
        supplementOrderPurchasePo.setSupplementRemarks( supplementOrderPurchaseDto.getSupplementRemarks() );
        supplementOrderPurchasePo.setSkuBatchCode( supplementOrderPurchaseDto.getSkuBatchCode() );
        supplementOrderPurchasePo.setSettleUnitPrice( supplementOrderPurchaseDto.getSettleUnitPrice() );

        return supplementOrderPurchasePo;
    }
}
