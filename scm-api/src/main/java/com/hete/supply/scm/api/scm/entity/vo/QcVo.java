package com.hete.supply.scm.api.scm.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * 质检信息
 *
 * @author yanjiawei
 * Created on 2023/10/12.
 */
@Data
public class QcVo {

    /**
     * 质检单与质检明细列表
     */
    private List<QcOrderVo> qcOrderVos;
}
