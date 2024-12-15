package com.hete.supply.scm.server.scm.supplier.converter;

import com.hete.supply.scm.api.scm.entity.vo.GetBySupplierCodeVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierImportationDto.ImportationDetail;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierAddDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierEditDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierDetailVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:57+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class SupplierConverterImpl implements SupplierConverter {

    @Override
    public SupplierDetailVo poToDetail(SupplierPo po) {
        if ( po == null ) {
            return null;
        }

        SupplierDetailVo supplierDetailVo = new SupplierDetailVo();

        supplierDetailVo.setVersion( po.getVersion() );
        supplierDetailVo.setSupplierCode( po.getSupplierCode() );
        supplierDetailVo.setSupplierName( po.getSupplierName() );
        supplierDetailVo.setSupplierType( po.getSupplierType() );
        supplierDetailVo.setSupplierStatus( po.getSupplierStatus() );
        supplierDetailVo.setSupplierGrade( po.getSupplierGrade() );
        supplierDetailVo.setCapacity( po.getCapacity() );
        supplierDetailVo.setDevUser( po.getDevUser() );
        supplierDetailVo.setDevUsername( po.getDevUsername() );
        supplierDetailVo.setFollowUser( po.getFollowUser() );
        supplierDetailVo.setFollowUsername( po.getFollowUsername() );
        supplierDetailVo.setShipToCountry( po.getShipToCountry() );
        supplierDetailVo.setShipToProvince( po.getShipToProvince() );
        supplierDetailVo.setShipToCity( po.getShipToCity() );
        supplierDetailVo.setShipToAddress( po.getShipToAddress() );
        supplierDetailVo.setRemarks( po.getRemarks() );
        supplierDetailVo.setJoinTime( po.getJoinTime() );
        supplierDetailVo.setLogisticsAging( po.getLogisticsAging() );
        supplierDetailVo.setReconciliationCycle( po.getReconciliationCycle() );
        supplierDetailVo.setSettleTime( po.getSettleTime() );
        supplierDetailVo.setSupplierAlias( po.getSupplierAlias() );

        return supplierDetailVo;
    }

    @Override
    public SupplierPo create(SupplierAddDto dto) {
        if ( dto == null ) {
            return null;
        }

        SupplierPo supplierPo = new SupplierPo();

        supplierPo.setSupplierCode( dto.getSupplierCode() );
        supplierPo.setSupplierName( dto.getSupplierName() );
        supplierPo.setSupplierType( dto.getSupplierType() );
        supplierPo.setSupplierGrade( dto.getSupplierGrade() );
        supplierPo.setCapacity( dto.getCapacity() );
        supplierPo.setDevUser( dto.getDevUser() );
        supplierPo.setFollowUser( dto.getFollowUser() );
        supplierPo.setJoinTime( dto.getJoinTime() );
        supplierPo.setShipToCountry( dto.getShipToCountry() );
        supplierPo.setShipToProvince( dto.getShipToProvince() );
        supplierPo.setShipToCity( dto.getShipToCity() );
        supplierPo.setShipToAddress( dto.getShipToAddress() );
        supplierPo.setRemarks( dto.getRemarks() );
        supplierPo.setLogisticsAging( dto.getLogisticsAging() );
        supplierPo.setSupplierAlias( dto.getSupplierAlias() );
        supplierPo.setReconciliationCycle( dto.getReconciliationCycle() );
        supplierPo.setSettleTime( dto.getSettleTime() );

        return supplierPo;
    }

    @Override
    public SupplierPo edit(SupplierEditDto dto) {
        if ( dto == null ) {
            return null;
        }

        SupplierPo supplierPo = new SupplierPo();

        supplierPo.setVersion( dto.getVersion() );
        supplierPo.setSupplierCode( dto.getSupplierCode() );
        supplierPo.setSupplierName( dto.getSupplierName() );
        supplierPo.setSupplierType( dto.getSupplierType() );
        supplierPo.setSupplierGrade( dto.getSupplierGrade() );
        supplierPo.setCapacity( dto.getCapacity() );
        supplierPo.setDevUser( dto.getDevUser() );
        supplierPo.setFollowUser( dto.getFollowUser() );
        supplierPo.setJoinTime( dto.getJoinTime() );
        supplierPo.setShipToCountry( dto.getShipToCountry() );
        supplierPo.setShipToProvince( dto.getShipToProvince() );
        supplierPo.setShipToCity( dto.getShipToCity() );
        supplierPo.setShipToAddress( dto.getShipToAddress() );
        supplierPo.setRemarks( dto.getRemarks() );
        supplierPo.setLogisticsAging( dto.getLogisticsAging() );
        supplierPo.setSupplierAlias( dto.getSupplierAlias() );
        supplierPo.setReconciliationCycle( dto.getReconciliationCycle() );
        supplierPo.setSettleTime( dto.getSettleTime() );

        return supplierPo;
    }

    @Override
    public List<GetBySupplierCodeVo> poListToSupplierCodeVo(List<SupplierPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<GetBySupplierCodeVo> list = new ArrayList<GetBySupplierCodeVo>( poList.size() );
        for ( SupplierPo supplierPo : poList ) {
            list.add( supplierPoToGetBySupplierCodeVo( supplierPo ) );
        }

        return list;
    }

    @Override
    public SupplierPo convert(ImportationDetail dto) {
        if ( dto == null ) {
            return null;
        }

        SupplierPo supplierPo = new SupplierPo();

        supplierPo.setSupplierCode( dto.getSupplierCode() );
        supplierPo.setSupplierName( dto.getSupplierName() );
        supplierPo.setSupplierType( dto.getSupplierType() );
        supplierPo.setSupplierGrade( dto.getSupplierGrade() );
        supplierPo.setCapacity( dto.getCapacity() );
        supplierPo.setSupplierInvoicing( dto.getSupplierInvoicing() );
        supplierPo.setTaxPoint( dto.getTaxPoint() );
        supplierPo.setCreditCode( dto.getCreditCode() );
        supplierPo.setCorporateName( dto.getCorporateName() );
        supplierPo.setLegalPerson( dto.getLegalPerson() );
        supplierPo.setBusinessTimeStart( dto.getBusinessTimeStart() );
        supplierPo.setBusinessTimeEnd( dto.getBusinessTimeEnd() );
        supplierPo.setDevUsername( dto.getDevUsername() );
        supplierPo.setFollowUsername( dto.getFollowUsername() );
        supplierPo.setSupplierExport( dto.getSupplierExport() );
        supplierPo.setJoinTime( dto.getJoinTime() );
        supplierPo.setCountry( dto.getCountry() );
        supplierPo.setProvince( dto.getProvince() );
        supplierPo.setCity( dto.getCity() );
        supplierPo.setAddress( dto.getAddress() );
        supplierPo.setShipToCountry( dto.getShipToCountry() );
        supplierPo.setShipToProvince( dto.getShipToProvince() );
        supplierPo.setShipToCity( dto.getShipToCity() );
        supplierPo.setShipToAddress( dto.getShipToAddress() );
        supplierPo.setRemarks( dto.getRemarks() );
        supplierPo.setLogisticsAging( dto.getLogisticsAging() );
        supplierPo.setSupplierAlias( dto.getSupplierAlias() );

        return supplierPo;
    }

    protected GetBySupplierCodeVo supplierPoToGetBySupplierCodeVo(SupplierPo supplierPo) {
        if ( supplierPo == null ) {
            return null;
        }

        GetBySupplierCodeVo getBySupplierCodeVo = new GetBySupplierCodeVo();

        getBySupplierCodeVo.setSupplierCode( supplierPo.getSupplierCode() );
        getBySupplierCodeVo.setSupplierName( supplierPo.getSupplierName() );
        getBySupplierCodeVo.setSupplierType( supplierPo.getSupplierType() );
        getBySupplierCodeVo.setSupplierStatus( supplierPo.getSupplierStatus() );

        return getBySupplierCodeVo;
    }
}
