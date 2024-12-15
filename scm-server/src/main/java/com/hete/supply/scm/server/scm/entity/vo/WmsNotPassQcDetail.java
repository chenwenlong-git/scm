package com.hete.supply.scm.server.scm.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/11/2.
 */
@Data
@NoArgsConstructor
public class WmsNotPassQcDetail {

    /**
     * 质检单详情id主键
     */
    private Long qcDetailId;

    /**
     * 批次码
     */
    private String batchCode;

    /**
     * 赫特sku
     */
    private String skuCode;

    /**
     * 不通过数量
     */
    private Integer notPassAmount;


    /**
     * 质检不合格原因
     */
    private String qcNotPassedReason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 图片文件ID列表
     */
    private List<String> pictureUrlList;

}
