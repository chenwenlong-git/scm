package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderScanBackVo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderScanPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SettleProcessOrderScanVo.SettleProcessOrderScanDetail;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProcessOrderScanConverterImpl implements ProcessOrderScanConverter {

    @Override
    public ProcessOrderScanBackVo convert(ProcessOrderScanPo processOrderScanPo) {
        if ( processOrderScanPo == null ) {
            return null;
        }

        ProcessOrderScanBackVo processOrderScanBackVo = new ProcessOrderScanBackVo();

        processOrderScanBackVo.setProcessOrderScanId( processOrderScanPo.getProcessOrderScanId() );
        processOrderScanBackVo.setProcessFirst( processOrderScanPo.getProcessFirst() );
        processOrderScanBackVo.setProcessCode( processOrderScanPo.getProcessCode() );
        processOrderScanBackVo.setProcessName( processOrderScanPo.getProcessName() );
        processOrderScanBackVo.setReceiptTime( processOrderScanPo.getReceiptTime() );
        processOrderScanBackVo.setReceiptUser( processOrderScanPo.getReceiptUser() );
        processOrderScanBackVo.setReceiptUsername( processOrderScanPo.getReceiptUsername() );
        processOrderScanBackVo.setCompleteTime( processOrderScanPo.getCompleteTime() );
        processOrderScanBackVo.setCompleteUser( processOrderScanPo.getCompleteUser() );
        processOrderScanBackVo.setCompleteUsername( processOrderScanPo.getCompleteUsername() );
        processOrderScanBackVo.setVersion( processOrderScanPo.getVersion() );

        return processOrderScanBackVo;
    }

    @Override
    public List<SettleProcessOrderScanDetail> settleProcessOrderScanList(List<ProcessSettleOrderScanPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<SettleProcessOrderScanDetail> list = new ArrayList<SettleProcessOrderScanDetail>( poList.size() );
        for ( ProcessSettleOrderScanPo processSettleOrderScanPo : poList ) {
            list.add( processSettleOrderScanPoToSettleProcessOrderScanDetail( processSettleOrderScanPo ) );
        }

        return list;
    }

    protected SettleProcessOrderScanDetail processSettleOrderScanPoToSettleProcessOrderScanDetail(ProcessSettleOrderScanPo processSettleOrderScanPo) {
        if ( processSettleOrderScanPo == null ) {
            return null;
        }

        SettleProcessOrderScanDetail settleProcessOrderScanDetail = new SettleProcessOrderScanDetail();

        settleProcessOrderScanDetail.setCompleteTime( processSettleOrderScanPo.getCompleteTime() );
        settleProcessOrderScanDetail.setProcessOrderNo( processSettleOrderScanPo.getProcessOrderNo() );
        settleProcessOrderScanDetail.setProcessName( processSettleOrderScanPo.getProcessName() );
        settleProcessOrderScanDetail.setQualityGoodsCnt( processSettleOrderScanPo.getQualityGoodsCnt() );
        settleProcessOrderScanDetail.setProcessCommission( processSettleOrderScanPo.getProcessCommission() );
        settleProcessOrderScanDetail.setTotalProcessCommission( processSettleOrderScanPo.getTotalProcessCommission() );
        settleProcessOrderScanDetail.setOrderUsername( processSettleOrderScanPo.getOrderUsername() );

        return settleProcessOrderScanDetail;
    }
}
