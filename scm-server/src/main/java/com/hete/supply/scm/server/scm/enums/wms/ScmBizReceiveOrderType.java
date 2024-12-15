package com.hete.supply.scm.server.scm.enums.wms;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rockyHuas
 * @date 2023/06/21 11:21
 */
@Getter
@AllArgsConstructor
public enum ScmBizReceiveOrderType implements IRemark {
    // 业务类型
    BULK("采购大货入库"),
    OUTSOURCING_PROCESS_RAW("委外加工原料归还"),
    SEAL_SAMPLE("封样入库"),
    SAMPLE_RAW("打样原料归还"),
    PROCESS_FINISHED_PRODUCT("加工成品入库"),
    PROCESS_RAW("加工原料归还"),
    REPAIR_RAW("返修原料归还"),
    DEFECTIVE_PROCESS("加工次品报废"),
    INSIDE_CHECK("库内抽查"),
    PROCESS_DEFECT_RECORD("加工次品记录"),
    MATERIAL_DEFECT("原料次品"),
    DEVELOP_SEAL_SAMPLE("开发需求封样入库"),
    DEVELOP_SALE("开发需求闪售入库"),
    DEVELOP_SAMPLE_RAW("开发版单原料归还"),
    ;

    private final String remark;

}
