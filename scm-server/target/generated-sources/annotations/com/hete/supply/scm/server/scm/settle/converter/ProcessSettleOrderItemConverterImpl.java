package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.ProcessSettleOrderDetailVo.ProcessSettleOrderItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:57+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProcessSettleOrderItemConverterImpl implements ProcessSettleOrderItemConverter {

    @Override
    public List<ProcessSettleOrderItem> processSettleOrderItemList(List<ProcessSettleOrderItemPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<ProcessSettleOrderItem> list = new ArrayList<ProcessSettleOrderItem>( poList.size() );
        for ( ProcessSettleOrderItemPo processSettleOrderItemPo : poList ) {
            list.add( processSettleOrderItemPoToProcessSettleOrderItem( processSettleOrderItemPo ) );
        }

        return list;
    }

    protected ProcessSettleOrderItem processSettleOrderItemPoToProcessSettleOrderItem(ProcessSettleOrderItemPo processSettleOrderItemPo) {
        if ( processSettleOrderItemPo == null ) {
            return null;
        }

        ProcessSettleOrderItem processSettleOrderItem = new ProcessSettleOrderItem();

        processSettleOrderItem.setProcessSettleOrderItemId( processSettleOrderItemPo.getProcessSettleOrderItemId() );
        processSettleOrderItem.setCompleteUser( processSettleOrderItemPo.getCompleteUser() );
        processSettleOrderItem.setCompleteUsername( processSettleOrderItemPo.getCompleteUsername() );
        if ( processSettleOrderItemPo.getProcessNum() != null ) {
            processSettleOrderItem.setProcessNum( String.valueOf( processSettleOrderItemPo.getProcessNum() ) );
        }
        if ( processSettleOrderItemPo.getSkuNum() != null ) {
            processSettleOrderItem.setSkuNum( String.valueOf( processSettleOrderItemPo.getSkuNum() ) );
        }
        processSettleOrderItem.setSettlePrice( processSettleOrderItemPo.getSettlePrice() );

        return processSettleOrderItem;
    }
}
