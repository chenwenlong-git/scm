package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderRawPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPamphletOrderRawListVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPamphletRawDetailVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPamphletRawInfoVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DevelopPamphletOrderRawConverterImpl implements DevelopPamphletOrderRawConverter {

    @Override
    public List<DevelopPamphletRawDetailVo> convert(List<DevelopPamphletOrderRawPo> pawPoList) {
        if ( pawPoList == null ) {
            return null;
        }

        List<DevelopPamphletRawDetailVo> list = new ArrayList<DevelopPamphletRawDetailVo>( pawPoList.size() );
        for ( DevelopPamphletOrderRawPo developPamphletOrderRawPo : pawPoList ) {
            list.add( developPamphletOrderRawPoToDevelopPamphletRawDetailVo( developPamphletOrderRawPo ) );
        }

        return list;
    }

    @Override
    public List<DevelopPamphletOrderRawListVo> convertVoList(List<DevelopPamphletOrderRawPo> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DevelopPamphletOrderRawListVo> list = new ArrayList<DevelopPamphletOrderRawListVo>( dtoList.size() );
        for ( DevelopPamphletOrderRawPo developPamphletOrderRawPo : dtoList ) {
            list.add( developPamphletOrderRawPoToDevelopPamphletOrderRawListVo( developPamphletOrderRawPo ) );
        }

        return list;
    }

    @Override
    public List<DevelopPamphletRawInfoVo> convertRawInfo(List<DevelopPamphletOrderRawPo> pawPoList) {
        if ( pawPoList == null ) {
            return null;
        }

        List<DevelopPamphletRawInfoVo> list = new ArrayList<DevelopPamphletRawInfoVo>( pawPoList.size() );
        for ( DevelopPamphletOrderRawPo developPamphletOrderRawPo : pawPoList ) {
            list.add( developPamphletOrderRawPoToDevelopPamphletRawInfoVo( developPamphletOrderRawPo ) );
        }

        return list;
    }

    protected DevelopPamphletRawDetailVo developPamphletOrderRawPoToDevelopPamphletRawDetailVo(DevelopPamphletOrderRawPo developPamphletOrderRawPo) {
        if ( developPamphletOrderRawPo == null ) {
            return null;
        }

        DevelopPamphletRawDetailVo developPamphletRawDetailVo = new DevelopPamphletRawDetailVo();

        developPamphletRawDetailVo.setMaterialType( developPamphletOrderRawPo.getMaterialType() );
        developPamphletRawDetailVo.setSku( developPamphletOrderRawPo.getSku() );
        developPamphletRawDetailVo.setSkuCnt( developPamphletOrderRawPo.getSkuCnt() );
        developPamphletRawDetailVo.setSkuBatchCode( developPamphletOrderRawPo.getSkuBatchCode() );

        return developPamphletRawDetailVo;
    }

    protected DevelopPamphletOrderRawListVo developPamphletOrderRawPoToDevelopPamphletOrderRawListVo(DevelopPamphletOrderRawPo developPamphletOrderRawPo) {
        if ( developPamphletOrderRawPo == null ) {
            return null;
        }

        DevelopPamphletOrderRawListVo developPamphletOrderRawListVo = new DevelopPamphletOrderRawListVo();

        developPamphletOrderRawListVo.setDevelopPamphletOrderRawId( developPamphletOrderRawPo.getDevelopPamphletOrderRawId() );
        developPamphletOrderRawListVo.setVersion( developPamphletOrderRawPo.getVersion() );
        developPamphletOrderRawListVo.setSku( developPamphletOrderRawPo.getSku() );
        developPamphletOrderRawListVo.setSkuBatchCode( developPamphletOrderRawPo.getSkuBatchCode() );

        return developPamphletOrderRawListVo;
    }

    protected DevelopPamphletRawInfoVo developPamphletOrderRawPoToDevelopPamphletRawInfoVo(DevelopPamphletOrderRawPo developPamphletOrderRawPo) {
        if ( developPamphletOrderRawPo == null ) {
            return null;
        }

        DevelopPamphletRawInfoVo developPamphletRawInfoVo = new DevelopPamphletRawInfoVo();

        developPamphletRawInfoVo.setMaterialType( developPamphletOrderRawPo.getMaterialType() );
        developPamphletRawInfoVo.setSku( developPamphletOrderRawPo.getSku() );
        developPamphletRawInfoVo.setSkuCnt( developPamphletOrderRawPo.getSkuCnt() );
        developPamphletRawInfoVo.setSkuBatchCode( developPamphletOrderRawPo.getSkuBatchCode() );

        return developPamphletRawInfoVo;
    }
}
