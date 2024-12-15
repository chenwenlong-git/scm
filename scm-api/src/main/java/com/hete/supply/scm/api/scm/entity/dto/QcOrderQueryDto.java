package com.hete.supply.scm.api.scm.entity.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * 质检单查询参数
 *
 * @author yanjiawei
 * Created on 2023/10/12.
 */
@Data
public class QcOrderQueryDto {

    /**
     * 质检单号查询列表
     */
    @Size(max = 100, message = "质检单号查询列表最大100条")
    private Collection<String> qcOrderNos;

    /**
     * 收货单号查询列表
     */
    @Size(max = 100, message = "收货单号查询列表最大100条")
    private Collection<String> receiveNos;
}
