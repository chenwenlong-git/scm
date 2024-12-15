package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.supply.scm.api.scm.entity.vo.PurchaseStatusModelVo;
import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/1
 */
@Getter
@AllArgsConstructor
public enum PurchaseOrderStatus implements IRemark {
    // 采购单状态:WAIT_APPROVE(待审核),WAIT_CONFIRM(计划确认),WAIT_FOLLOWER_CONFIRM(跟单确认),WAIT_RECEIVE_ORDER(待接单),WAIT_SCHEDULING(待排产),WAIT_COMMISSIONING(待投产),COMMISSION(前处理),PRETREATMENT(后处理),SEWING(三联机中),AFTER_TREATMENT(高针中),POST_QC(后整质检中),WAIT_DELIVER(待发货),WAIT_RECEIPT(待收货),RECEIPTED(已收货),WAIT_QC(待质检),WAIT_WAREHOUSING(待入库),WAREHOUSED(已入库),RETURN(已退货),SETTLE(已结算),DELETE(已作废),FINISH(已完结),
    WAIT_APPROVE("待审核", 2),
    WAIT_CONFIRM("计划确认", 3),
    WAIT_FOLLOWER_CONFIRM("跟单确认", 4),
    WAIT_RECEIVE_ORDER("待接单", 5),
    WAIT_SCHEDULING("待排产", 6),
    WAIT_COMMISSIONING("待投产", 7),
    COMMISSION("前处理", 8),
    PRETREATMENT("后处理", 9),
    SEWING("三联机中", 10),
    AFTER_TREATMENT("高针中", 11),
    POST_QC("后整质检中", 12),
    WAIT_DELIVER("待发货", 13),
    WAIT_RECEIPT("待收货", 14),
    RECEIPTED("已收货", 15),
    WAIT_QC("待质检", 16),
    WAIT_WAREHOUSING("待入库", 17),
    WAREHOUSED("已入库", 18),
    FINISH("已完结", 22),
    RETURN("已退货", 19),
    DELETE("已作废", 21),

    ;

    private final String remark;
    private final Integer sort;

    public static List<PurchaseOrderStatus> getSupplierAllStatusList() {
        final List<PurchaseOrderStatus> statusList = Arrays.asList(PurchaseOrderStatus.values());

        return statusList.stream()
                .filter(status -> status.getSort() >= WAIT_RECEIVE_ORDER.getSort())
                .collect(Collectors.toList());
    }

    public static List<PurchaseOrderStatus> getSupplierPurchaseStatusList() {
        final List<PurchaseOrderStatus> statusList = Arrays.asList(PurchaseOrderStatus.values());

        return statusList.stream()
                .filter(status -> status.getSort() > WAIT_RECEIVE_ORDER.getSort())
                .filter(status -> status != DELETE)
                .collect(Collectors.toList());
    }

    public static PurchaseOrderStatus convertDeliverStatusToPurchaseStatus(DeliverOrderStatus deliverOrderStatus) {
        if (DeliverOrderStatus.WAIT_DELIVER.equals(deliverOrderStatus)) {
            return PurchaseOrderStatus.WAIT_DELIVER;
        }

        if (DeliverOrderStatus.WAIT_RECEIVE.equals(deliverOrderStatus)) {
            return PurchaseOrderStatus.WAIT_RECEIPT;
        }
        if (DeliverOrderStatus.RECEIVED.equals(deliverOrderStatus)) {
            return PurchaseOrderStatus.RECEIPTED;
        }
        if (DeliverOrderStatus.WAIT_QC.equals(deliverOrderStatus)) {
            return PurchaseOrderStatus.WAIT_QC;
        }
        if (DeliverOrderStatus.WAIT_WAREHOUSING.equals(deliverOrderStatus)) {
            return PurchaseOrderStatus.WAIT_WAREHOUSING;
        }
        if (DeliverOrderStatus.WAREHOUSED.equals(deliverOrderStatus)) {
            return PurchaseOrderStatus.WAREHOUSED;
        }

        if (DeliverOrderStatus.DELETED.equals(deliverOrderStatus)) {
            return PurchaseOrderStatus.WAIT_DELIVER;
        }

        if (DeliverOrderStatus.RETURN.equals(deliverOrderStatus)) {
            return PurchaseOrderStatus.RETURN;
        }

        if (DeliverOrderStatus.RECEIVING.equals(deliverOrderStatus)) {
            return PurchaseOrderStatus.WAIT_RECEIPT;
        }

        throw new BizException("错误的枚举类型");

    }

    public static List<PurchaseOrderStatus> fastSupplyStatus() {
        return Arrays.asList(WAIT_RECEIVE_ORDER, WAIT_SCHEDULING, WAIT_COMMISSIONING, COMMISSION, PRETREATMENT, SEWING,
                AFTER_TREATMENT, POST_QC, WAIT_DELIVER, WAIT_RECEIPT, RECEIPTED, WAIT_QC, WAIT_WAREHOUSING, WAREHOUSED,
                FINISH);
    }


    /**
     * 只有待确认状态才可以到待接单状态
     *
     * @return
     */
    public PurchaseOrderStatus toWaitReceiveOrder() {
        if (WAIT_FOLLOWER_CONFIRM != this) {
            throw new ParamIllegalException("当前采购需求单不处于【{}】，请刷新后重试！",
                    WAIT_FOLLOWER_CONFIRM.getRemark());
        }
        return WAIT_RECEIVE_ORDER;
    }


    public static PurchaseOrderStatus getEarliestStatus(List<PurchaseOrderStatus> statusList) {
        if (statusList == null || statusList.isEmpty()) {
            throw new BizException("错误的状态列表，无法获取最早状态。");
        }
        PurchaseOrderStatus purchaseOrderStatus = statusList.get(0);

        for (PurchaseOrderStatus status : statusList) {
            if (status.getSort() < purchaseOrderStatus.getSort()) {
                purchaseOrderStatus = status;
            }
        }

        return purchaseOrderStatus;
    }

    public PurchaseOrderStatus forceToFinish() {
        if (this != WAREHOUSED && this != RETURN) {
            throw new ParamIllegalException("当前采购单状态:{}，不处于【{}】或【{}】，无法终止来货，请刷新后重试！"
                    , this.remark, WAREHOUSED.getRemark(), RETURN.getRemark());
        }

        return FINISH;
    }

    public PurchaseOrderStatus toWaitCommissioning() {
        if (WAIT_SCHEDULING != this) {
            throw new ParamIllegalException("当前采购子单不处于【{}】，无法进行排产操作，请刷新后重试！",
                    WAIT_SCHEDULING.getRemark());
        }
        return WAIT_COMMISSIONING;
    }

    public PurchaseOrderStatus toCommission() {
        if (WAIT_COMMISSIONING != this) {
            throw new ParamIllegalException("当前采购子单不处于【{}】，无法进行投产操作，请刷新后重试！",
                    WAIT_COMMISSIONING.getRemark());
        }
        return COMMISSION;
    }

    public PurchaseOrderStatus toPretreatment() {
        if (COMMISSION != this) {
            throw new ParamIllegalException("当前采购子单不处于【{}】，无法进行前处理操作，请刷新后重试！",
                    COMMISSION.getRemark());
        }
        return PRETREATMENT;
    }

    public PurchaseOrderStatus toSewing() {
        if (PRETREATMENT != this) {
            throw new ParamIllegalException("当前采购子单不处于【{}】，无法进行缝制操作，请刷新后重试！",
                    PRETREATMENT.getRemark());
        }
        return SEWING;
    }

    public PurchaseOrderStatus toAfterTreatment() {
        if (SEWING != this) {
            throw new ParamIllegalException("当前采购子单不处于【{}】，无法进行后处理操作，请刷新后重试！", SEWING.getRemark());
        }
        return AFTER_TREATMENT;
    }

    public PurchaseOrderStatus toPostQc() {
        if (AFTER_TREATMENT != this) {
            throw new ParamIllegalException("当前采购子单不处于【{}】，无法进行后整质检操作", AFTER_TREATMENT.getRemark());
        }
        return POST_QC;
    }

    public PurchaseOrderStatus toWaitDeliver() {
        if (POST_QC != this) {
            throw new ParamIllegalException("当前采购子单不处于【{}】，无法进行完成质检操作", POST_QC.getRemark());
        }
        return WAIT_DELIVER;
    }

    public PurchaseOrderStatus toWaitReceipt() {

        return WAIT_RECEIPT;
    }

    public PurchaseOrderStatus toReceipted() {
        if (WAIT_RECEIPT != this) {
            throw new ParamIllegalException("当前采购子单不处于【{}】，无法进行完成发货操作", WAIT_RECEIPT.getRemark());
        }
        return RECEIPTED;
    }

    private static final Map<Integer, PurchaseOrderStatus> SORT_PURCHASE_ORDER_STATUS_MAP = new HashMap<>();

    static {
        for (PurchaseOrderStatus value : values()) {
            SORT_PURCHASE_ORDER_STATUS_MAP.put(value.getSort(), value);
        }
    }


    public PurchaseOrderStatus afterStatus() {
        final Integer sort = this.getSort();

        return SORT_PURCHASE_ORDER_STATUS_MAP.get(sort + 1);
    }

    public PurchaseOrderStatus preStatus() {
        final Integer sort = this.getSort();

        return SORT_PURCHASE_ORDER_STATUS_MAP.get(sort - 1);
    }

    public List<PurchaseStatusModelVo> preStatusList() {
        final Integer sort = this.getSort();
        return SORT_PURCHASE_ORDER_STATUS_MAP.entrySet()
                .stream()
                .filter(entry -> entry.getKey() < sort && entry.getKey() >= COMMISSION.getSort()
                        && entry.getKey() <= AFTER_TREATMENT.getSort())
                .filter(entry -> entry.getKey() != (sort - 1))
                .map(entry -> {
                    final PurchaseOrderStatus purchaseOrderStatus = entry.getValue();
                    final PurchaseStatusModelVo purchaseStatusModel = new PurchaseStatusModelVo();
                    purchaseStatusModel.setPurchaseOrderStatus(purchaseOrderStatus);
                    return purchaseStatusModel;
                })
                .sorted(Comparator.comparing(PurchaseStatusModelVo::getPurchaseOrderStatus))
                .collect(Collectors.toList());
    }

    public List<PurchaseStatusModelVo> afterStatusList() {
        final Integer sort = this.getSort();
        return SORT_PURCHASE_ORDER_STATUS_MAP.entrySet()
                .stream()
                .filter(entry -> entry.getKey() > sort && entry.getKey() <= POST_QC.getSort()
                        && entry.getKey() >= WAIT_SCHEDULING.getSort())
                .filter(entry -> entry.getKey() != (sort + 1))
                .map(entry -> {
                    final PurchaseOrderStatus purchaseOrderStatus = entry.getValue();
                    final PurchaseStatusModelVo purchaseStatusModel = new PurchaseStatusModelVo();
                    purchaseStatusModel.setPurchaseOrderStatus(purchaseOrderStatus);
                    return purchaseStatusModel;
                })
                .sorted(Comparator.comparing(PurchaseStatusModelVo::getPurchaseOrderStatus))
                .collect(Collectors.toList());
    }

    /**
     * 获取采购工作中状态
     */
    public static List<PurchaseOrderStatus> getWorkingStatusList() {
        final List<PurchaseOrderStatus> statusList = Arrays.asList(PurchaseOrderStatus.values());

        return statusList.stream()
                .filter(status -> status.getSort() >= WAIT_CONFIRM.getSort())
                .filter(status -> status.getSort() <= WAIT_WAREHOUSING.getSort())
                .filter(status -> status != DELETE)
                .collect(Collectors.toList());
    }


    public static List<PurchaseOrderStatus> getOverdueCheckStatusList() {
        final List<PurchaseOrderStatus> statusList = Arrays.asList(PurchaseOrderStatus.values());

        return statusList.stream()
                .filter(status -> status.getSort() >= WAIT_RECEIVE_ORDER.getSort())
                .filter(status -> status.getSort() <= WAIT_WAREHOUSING.getSort())
                .filter(status -> status != DELETE)
                .collect(Collectors.toList());
    }

    /**
     * 可编辑状态
     *
     * @return
     */
    public static List<PurchaseOrderStatus> getEditStatus() {

        return Arrays.asList(WAIT_CONFIRM, WAIT_FOLLOWER_CONFIRM, WAIT_RECEIVE_ORDER, WAIT_SCHEDULING, WAIT_COMMISSIONING,
                COMMISSION, PRETREATMENT, SEWING, AFTER_TREATMENT, POST_QC, WAIT_DELIVER);
    }

    /**
     * 业务约定交期可编辑状态
     *
     * @return
     */
    public static List<PurchaseOrderStatus> getEditDeliverDateStatus() {
        return Arrays.asList(WAIT_CONFIRM, WAIT_FOLLOWER_CONFIRM, WAIT_SCHEDULING, WAIT_COMMISSIONING, WAIT_RECEIVE_ORDER,
                COMMISSION, PRETREATMENT, SEWING, AFTER_TREATMENT, POST_QC, WAIT_DELIVER);
    }

    /**
     * 收货仓库可编辑状态
     *
     * @return
     */
    public static List<PurchaseOrderStatus> getEditWarehouseCodeStatus() {
        return Arrays.asList(WAIT_SCHEDULING, WAIT_COMMISSIONING, COMMISSION, PRETREATMENT, SEWING, AFTER_TREATMENT, POST_QC, WAIT_DELIVER, WAIT_RECEIVE_ORDER);
    }


    /**
     * 待发货数统计状态
     *
     * @return
     */
    public static List<PurchaseOrderStatus> getWaitDeliverCntStatus() {
        final List<PurchaseOrderStatus> statusList = Arrays.asList(PurchaseOrderStatus.values());

        return statusList.stream()
                .filter(status -> status.getSort() >= WAIT_APPROVE.getSort())
                .filter(status -> status.getSort() <= POST_QC.getSort())
                .filter(status -> status != DELETE)
                .collect(Collectors.toList());
    }

    /**
     * 可完成采购需求的状态
     *
     * @return
     */
    public static List<PurchaseOrderStatus> getCanFinishStatus() {
        return Arrays.asList(DELETE, FINISH);
    }

    /**
     * 可发货状态
     *
     * @return
     */
    public static List<PurchaseOrderStatus> getPurchaseDeliverStatus() {
        return Arrays.asList(WAIT_DELIVER, WAIT_RECEIPT, RECEIPTED, WAIT_QC, WAIT_WAREHOUSING, WAREHOUSED, RETURN);
    }

    public static List<PurchaseOrderStatus> getPurchaseStatusBySort(int minSort, int maxSort) {
        final List<PurchaseOrderStatus> statusList = Arrays.asList(PurchaseOrderStatus.values());

        return statusList.stream()
                .filter(status -> status.getSort() >= minSort)
                .filter(status -> status.getSort() <= maxSort)
                .collect(Collectors.toList());
    }


    public PurchaseOrderStatus toWaitScheduling() {
        if (this != WAIT_RECEIVE_ORDER) {
            throw new ParamIllegalException("当前订单不处于【{}】状态，请刷新后重试", WAIT_RECEIVE_ORDER.getRemark());
        }

        return PurchaseOrderStatus.WAIT_SCHEDULING;
    }

    public PurchaseOrderStatus toWaitFollowerConfirm() {
        if (this != WAIT_RECEIVE_ORDER) {
            throw new ParamIllegalException("当前订单不处于【{}】状态，请刷新后重试", WAIT_RECEIVE_ORDER.getRemark());
        }
        return PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM;
    }

    public PurchaseOrderStatus planConfirm() {
        if (this != WAIT_CONFIRM) {
            throw new ParamIllegalException("当前订单不处于【{}】状态，请刷新后重试", WAIT_CONFIRM.getRemark());
        }
        return PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM;
    }

    /**
     * 补扣单不可以增加的状态
     *
     * @return
     */
    public static List<PurchaseOrderStatus> getSupplementDeductOrderNotStatus() {
        return Arrays.asList(WAIT_FOLLOWER_CONFIRM, WAIT_CONFIRM, WAIT_RECEIVE_ORDER, RETURN);
    }

    /**
     * 获取商品成本获取最新采购价过滤的状态
     *
     * @return
     */
    public static List<PurchaseOrderStatus> getCostOfGoodsNotStatus() {
        return Arrays.asList(DELETE, RETURN, WAIT_CONFIRM, WAIT_FOLLOWER_CONFIRM);
    }
}
