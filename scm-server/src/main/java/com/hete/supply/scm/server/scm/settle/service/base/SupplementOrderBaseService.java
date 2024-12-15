package com.hete.supply.scm.server.scm.settle.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderDto;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleItemType;
import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.server.scm.dao.ScmImageDao;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.settle.config.SettleConfig;
import com.hete.supply.scm.server.scm.settle.converter.SupplementOrderConverter;
import com.hete.supply.scm.server.scm.settle.converter.SupplementOrderOtherConverter;
import com.hete.supply.scm.server.scm.settle.converter.SupplementOrderProcessConverter;
import com.hete.supply.scm.server.scm.settle.converter.SupplementOrderPurchaseConverter;
import com.hete.supply.scm.server.scm.settle.dao.*;
import com.hete.supply.scm.server.scm.settle.entity.bo.SupplementOrderPurchaseExportBo;
import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderUpdateDto;
import com.hete.supply.scm.server.scm.settle.entity.po.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderVo;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderBillType;
import com.hete.supply.scm.server.scm.settle.enums.SupplementOrderExamine;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 补款单基础类
 *
 * @author ChenWenLong
 * @date 2022/11/15 10:08
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplementOrderBaseService {

    private final SupplementOrderDao supplementOrderDao;
    private final SupplementOrderPurchaseDao supplementOrderPurchaseDao;
    private final SupplementOrderProcessDao supplementOrderProcessDao;
    private final ScmImageDao scmImageDao;
    private final ScmImageBaseService scmImageBaseService;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final PurchaseSettleOrderItemBaseService purchaseSettleOrderItemBaseService;
    private final PurchaseSettleOrderItemDao purchaseSettleOrderItemDao;
    private final ProcessSettleOrderItemDao processSettleOrderItemDao;
    private final ProcessSettleOrderBillDao processSettleOrderBillDao;
    private final SupplementOrderOtherDao supplementOrderOtherDao;
    private final SettleConfig settleConfig;
    private final UdbRemoteService udbRemoteService;

    /**
     * 补款备注字数限制
     */
    public final static int MAX_SUPPLEMENT_REMARKS = 500;


    /**
     * 列表
     *
     * @author ChenWenLong
     * @date 2022/11/16 19:20
     */
    public CommonPageResult.PageInfo<SupplementOrderVo> searchSupplementOrder(SupplementOrderDto supplementOrderDto) {

        if (StringUtils.isNotBlank(supplementOrderDto.getPurchaseSettleOrderNo())) {

            List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList = purchaseSettleOrderItemDao.getByPurchaseSettleOrderNo(supplementOrderDto.getPurchaseSettleOrderNo(), PurchaseSettleItemType.REPLENISH);
            if (CollectionUtil.isEmpty(purchaseSettleOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getBySupplementOrderNoList(purchaseSettleOrderItemPoList.stream().map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(supplementOrderPoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            supplementOrderDto.setSupplementOrderIds(supplementOrderPoList.stream().map(SupplementOrderPo::getSupplementOrderId).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(supplementOrderDto.getProcessSettleOrderNo())) {

            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderNo(supplementOrderDto.getProcessSettleOrderNo());
            if (CollectionUtil.isEmpty(processSettleOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<ProcessSettleOrderBillPo> processSettleOrderBillPoList = processSettleOrderBillDao.getBatchProcessSettleOrderItemId(processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderItemId).collect(Collectors.toList()), ProcessSettleOrderBillType.REPLENISH);
            if (CollectionUtil.isEmpty(processSettleOrderBillPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getBySupplementOrderNoList(processSettleOrderBillPoList.stream().map(ProcessSettleOrderBillPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(supplementOrderPoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            supplementOrderDto.setSupplementOrderIds(supplementOrderPoList.stream().map(SupplementOrderPo::getSupplementOrderId).collect(Collectors.toList()));
        }
        return supplementOrderDao.selectSupplementOrderPage(PageDTO.of(supplementOrderDto.getPageNo(), supplementOrderDto.getPageSize()), supplementOrderDto);
    }

    /**
     * 详情
     *
     * @author ChenWenLong
     * @date 2022/11/16 19:25
     */
    public SupplementOrderDetailVo getSupplementOrderDetail(SupplementOrderDetailDto dto) {
        SupplementOrderPo supplementOrderDetail = supplementOrderDao.getBySupplementOrderNo(dto.getSupplementOrderNo());
        if (supplementOrderDetail == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        SupplementOrderDetailVo detailVo = SupplementOrderConverter.INSTANCE.convert(supplementOrderDetail);

        //获取补款单采购明细
        List<SupplementOrderPurchasePo> purchasePos = supplementOrderPurchaseDao.getBySupplementOrderId(supplementOrderDetail.getSupplementOrderId());
        detailVo.setSupplementOrderPurchaseList(SupplementOrderPurchaseConverter.INSTANCE.detail(purchasePos));

        //获取补款单加工明细
        List<SupplementOrderProcessPo> processPos = supplementOrderProcessDao.getBySupplementOrderId(supplementOrderDetail.getSupplementOrderId());
        detailVo.setSupplementOrderProcessList(SupplementOrderProcessConverter.INSTANCE.detail(processPos));

        //获取其他明细
        List<SupplementOrderOtherPo> otherPos = supplementOrderOtherDao.getBySupplementOrderId(supplementOrderDetail.getSupplementOrderId());
        detailVo.setSupplementOrderOtherList(SupplementOrderOtherConverter.INSTANCE.detail(otherPos));

        //获取凭证
        List<Long> longs = List.of(supplementOrderDetail.getSupplementOrderId());
        List<String> scmImagePos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SUPPLEMENT_ORDER, longs);
        detailVo.setFileCodeList(scmImagePos);
        List<String> scmFilePos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SUPPLEMENT_ORDER_FILE, longs);
        detailVo.setDocumentCodeList(scmFilePos);

        if (SupplementType.PROCESS.equals(supplementOrderDetail.getSupplementType())) {
            List<ProcessSettleOrderBillPo> processSettleOrderBillPoList = processSettleOrderBillDao.getByBussinessNo(supplementOrderDetail.getSupplementOrderNo());
            if (CollectionUtil.isNotEmpty(processSettleOrderBillPoList)) {
                ProcessSettleOrderItemPo processSettleOrderItemPo = processSettleOrderItemDao.getById(processSettleOrderBillPoList.get(0).getProcessSettleOrderItemId());
                if (processSettleOrderItemPo != null) {
                    detailVo.setSettleOrderNo(processSettleOrderItemPo.getProcessSettleOrderNo());
                }
            }
        } else {
            PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = purchaseSettleOrderItemBaseService.getItemByBusinessNo(supplementOrderDetail.getSupplementOrderNo());
            if (purchaseSettleOrderItemPo != null) {
                detailVo.setSettleOrderNo(purchaseSettleOrderItemPo.getPurchaseSettleOrderNo());
            }
        }

        return detailVo;
    }

    /**
     * 审核
     *
     * @author ChenWenLong
     * @date 2022/11/16 19:26
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean examine(SupplementOrderUpdateDto dto) {
        SupplementOrderPo supplementOrderPo = supplementOrderDao.getByIdVersion(dto.getSupplementOrderId(), dto.getVersion());
        if (supplementOrderPo == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        SupplementOrderExamine supplementOrderExamine = SupplementOrderExamine.converterType(supplementOrderPo.getSupplementStatus(),
                supplementOrderPo.getSupplementType(), dto.getExamine());

        String user = GlobalContext.getUserKey();
        String username = GlobalContext.getUsername();
        LocalDateTime nowTime = new DateTime().toLocalDateTime();

        supplementOrderPo.setSupplementOrderId(supplementOrderPo.getSupplementOrderId());
        supplementOrderPo.setVersion(supplementOrderPo.getVersion());

        SupplementStatus supplementStatus = supplementOrderPo.getSupplementStatus();
        switch (supplementOrderExamine) {
            case ALREADY_CONFIRM:
                SupplementStatus confirm;
                // 如果是加工补款类型，确认提交后，进入待审核状态
                if (SupplementType.PROCESS.equals(supplementOrderPo.getSupplementType())) {
                    confirm = supplementStatus.processToWaitExamine();
                } else if (SupplementType.PRICE.equals(supplementOrderPo.getSupplementType())) {
                    confirm = supplementStatus.priceToWaitExamine();
                } else {
                    confirm = supplementStatus.toConfirm();
                }

                supplementOrderPo.setSupplementStatus(confirm);
                supplementOrderPo.setSubmitUser(user);
                supplementOrderPo.setSubmitUsername(username);
                supplementOrderPo.setSubmitTime(nowTime);
                break;
            case CONFIRM_AGREE:
                SupplementStatus audited = supplementStatus.toExamine();
                supplementOrderPo.setSupplementStatus(audited);
                supplementOrderPo.setConfirmUser(user);
                supplementOrderPo.setConfirmRefuseRemarks(null);
                supplementOrderPo.setConfirmUsername(username);
                supplementOrderPo.setConfirmTime(nowTime);
                break;
            case CONFIRM_REFUSE:
                SupplementStatus notAudited = supplementStatus.toNotExamine();
                supplementOrderPo.setSupplementStatus(notAudited);
                if (StringUtils.isBlank(dto.getRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}原因", SupplementOrderExamine.CONFIRM_REFUSE.getRemark());
                }
                if (dto.getRefuseRemarks().length() > 500) {
                    throw new ParamIllegalException("拒绝原因长度不能超过 500 个字符");
                }
                supplementOrderPo.setConfirmRefuseRemarks(dto.getRefuseRemarks());
                supplementOrderPo.setConfirmUser(user);
                supplementOrderPo.setConfirmUsername(username);
                supplementOrderPo.setConfirmTime(nowTime);
                break;
            case EXAMINE_AGREE:
                SupplementStatus examine = supplementStatus.toAudited();
                supplementOrderPo.setSupplementStatus(examine);
                supplementOrderPo.setExamineUser(user);
                supplementOrderPo.setExamineRefuseRemarks(null);
                supplementOrderPo.setExamineUsername(username);
                supplementOrderPo.setExamineTime(nowTime);
                break;
            case EXAMINE_REFUSE:
                // 如果是加工补款类型，审核拒绝后，进入待提交状态
                SupplementStatus notExamine;
                if (SupplementType.PROCESS.equals(supplementOrderPo.getSupplementType())) {
                    notExamine = supplementStatus.processToNotAudited();
                } else {
                    notExamine = supplementStatus.toNotAudited();
                }
                supplementOrderPo.setSupplementStatus(notExamine);
                if (StringUtils.isBlank(dto.getRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}原因", SupplementOrderExamine.EXAMINE_REFUSE.getRemark());
                }
                if (dto.getRefuseRemarks().length() > 500) {
                    throw new ParamIllegalException("拒绝原因长度不能超过 500 个字符");
                }
                supplementOrderPo.setExamineRefuseRemarks(dto.getRefuseRemarks());
                supplementOrderPo.setExamineUser(user);
                supplementOrderPo.setExamineUsername(username);
                supplementOrderPo.setExamineTime(nowTime);
                break;
            case PRICE_CONFIRM_AGREE:
                SupplementStatus priceConfirm = supplementStatus.toWaitConfirm();
                supplementOrderPo.setSupplementStatus(priceConfirm);
                supplementOrderPo.setPriceConfirmUser(user);
                supplementOrderPo.setPriceRefuseRemarks(null);
                supplementOrderPo.setPriceConfirmUsername(username);
                supplementOrderPo.setPriceConfirmTime(nowTime);
                break;
            case PRICE_CONFIRM_REFUSE:
                SupplementStatus notPriceConfirm = supplementStatus.toNotWaitConfirm();
                supplementOrderPo.setSupplementStatus(notPriceConfirm);
                if (StringUtils.isBlank(dto.getRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}原因", SupplementOrderExamine.PRICE_CONFIRM_REFUSE.getRemark());
                }
                if (dto.getRefuseRemarks().length() > 500) {
                    throw new ParamIllegalException("拒绝原因长度不能超过 500 个字符");
                }
                supplementOrderPo.setPriceRefuseRemarks(dto.getRefuseRemarks());
                supplementOrderPo.setPriceConfirmUser(user);
                supplementOrderPo.setPriceConfirmUsername(username);
                supplementOrderPo.setPriceConfirmTime(nowTime);
                break;
            default:
                throw new ParamIllegalException("请求类型错误！");
        }

        // 更新状态更新处理人
        supplementOrderPo = this.updateHandleUser(supplementOrderPo, settleConfig);
        supplementOrderDao.updateByIdVersion(supplementOrderPo);

        this.createStatusChangeLog(supplementOrderPo, supplementOrderExamine);

        return true;
    }

    /**
     * 日志
     *
     * @param supplementOrderPo
     */
    public void createStatusChangeLog(SupplementOrderPo supplementOrderPo, SupplementOrderExamine supplementOrderExamine) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.SUPSTATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.SUPPLEMENT_ORDER_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.SUPSTATUS.name());
        bizLogCreateMqDto.setBizCode(supplementOrderPo.getSupplementOrderNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(GlobalContext.getUserKey());
        bizLogCreateMqDto.setOperateUsername(GlobalContext.getUsername());

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();
        // 待收货
        bizLogCreateMqDto.setContent(supplementOrderPo.getSupplementStatus().getName());

        if (SupplementOrderExamine.CONFIRM_REFUSE.equals(supplementOrderExamine)) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey(SupplementOrderExamine.CONFIRM_REFUSE.getName());
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(supplementOrderPo.getConfirmRefuseRemarks());
            logVersionBos.add(logVersionBo);
        }

        if (SupplementOrderExamine.EXAMINE_REFUSE.equals(supplementOrderExamine)) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey(SupplementOrderExamine.EXAMINE_REFUSE.getName());
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(supplementOrderPo.getExamineRefuseRemarks());
            logVersionBos.add(logVersionBo);
        }

        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    /**
     * 通过供应商和审核时间查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 17:50
     */
    public List<SupplementOrderPo> getBySupplierCodeAndExamineTime(List<String> supplierCodeList, LocalDateTime examineTime, LocalDateTime examineTimeStart, LocalDateTime examineTimeEnd) {
        return supplementOrderDao.getBySupplierCodeAndExamineTime(supplierCodeList, examineTime, examineTimeStart, examineTimeEnd);
    }

    /**
     * 通过用户和审核时间查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 17:50
     */
    public List<SupplementOrderPo> getBySupplementUserAndExamineTime(String supplementUser, LocalDateTime examineTime, LocalDateTime examineTimeStart, LocalDateTime examineTimeEnd) {
        return supplementOrderDao.getBySupplementUserAndExamineTime(supplementUser, examineTime, examineTimeStart, examineTimeEnd);
    }

    /**
     * 通过编号批量更新状态
     *
     * @author ChenWenLong
     * @date 2023/1/14 15:38
     */
    public void updateBatchSupplementOrderNo(List<String> supplementOrderNos, SupplementStatus supplementStatus) {
        supplementOrderDao.updateBatchSupplementOrderNo(supplementOrderNos, supplementStatus);
    }

    /**
     * 通过编号批量查询列表其他类型详情
     *
     * @author ChenWenLong
     * @date 2023/3/7 14:59
     */
    public List<SupplementOrderOtherPo> getOtherBatchSupplementOrderNo(List<String> supplementOrderNoList) {
        return supplementOrderOtherDao.getOtherBatchSupplementOrderNo(supplementOrderNoList);
    }

    /**
     * 通过编号批量查询列表采购类型详情
     *
     * @author ChenWenLong
     * @date 2023/3/7 14:59
     */
    public List<SupplementOrderPurchaseExportBo> getPurchaseBatchSupplementOrderNo(List<String> supplementOrderNoList) {
        return supplementOrderDao.getPurchaseBatchSupplementOrderNo(supplementOrderNoList);
    }

    /**
     * 补款单搜索条件
     *
     * @param dto:
     * @return SupplementOrderDto
     * @author ChenWenLong
     * @date 2023/7/6 11:30
     */
    public SupplementOrderDto getSearchSupplementOrderWhere(SupplementOrderDto dto) {
        if (StringUtils.isNotBlank(dto.getPurchaseSettleOrderNo())) {

            List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList = purchaseSettleOrderItemDao.getByPurchaseSettleOrderNo(dto.getPurchaseSettleOrderNo(), PurchaseSettleItemType.REPLENISH);
            if (CollectionUtil.isEmpty(purchaseSettleOrderItemPoList)) {
                return null;
            }
            List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getBySupplementOrderNoList(
                    purchaseSettleOrderItemPoList.stream()
                            .map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(supplementOrderPoList)) {
                return null;
            }

            dto.setSupplementOrderIds(supplementOrderPoList.stream().map(SupplementOrderPo::getSupplementOrderId).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(dto.getProcessSettleOrderNo())) {

            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderNo(dto.getProcessSettleOrderNo());
            if (CollectionUtil.isEmpty(processSettleOrderItemPoList)) {
                return null;
            }
            List<ProcessSettleOrderBillPo> processSettleOrderBillPoList = processSettleOrderBillDao.getBatchProcessSettleOrderItemId(
                    processSettleOrderItemPoList.stream()
                            .map(ProcessSettleOrderItemPo::getProcessSettleOrderItemId).collect(Collectors.toList()), ProcessSettleOrderBillType.REPLENISH);
            if (CollectionUtil.isEmpty(processSettleOrderBillPoList)) {
                return null;
            }
            List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getBySupplementOrderNoList(
                    processSettleOrderBillPoList.stream()
                            .map(ProcessSettleOrderBillPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(supplementOrderPoList)) {
                return null;
            }

            dto.setSupplementOrderIds(supplementOrderPoList.stream().map(SupplementOrderPo::getSupplementOrderId).collect(Collectors.toList()));
        }
        return dto;
    }

    /**
     * 根据状态来更新处理人
     *
     * @param supplementOrderPo:
     * @param settleConfig:
     * @return SupplementOrderPo
     * @author ChenWenLong
     * @date 2024/8/31 21:05
     */
    public SupplementOrderPo updateHandleUser(SupplementOrderPo supplementOrderPo, SettleConfig settleConfig) {
        log.info("根据状态来更新处理人的开始：po=>{}", JacksonUtil.parse2Str(supplementOrderPo));
        if (SupplementStatus.WAIT_SUBMIT.equals(supplementOrderPo.getSupplementStatus())) {
            if (StringUtils.isNotBlank(supplementOrderPo.getCreateUser())) {
                supplementOrderPo.setHandleUser(supplementOrderPo.getCreateUser());
                if (StringUtils.isNotBlank(supplementOrderPo.getCreateUsername())) {
                    supplementOrderPo.setHandleUsername(supplementOrderPo.getCreateUsername());
                } else {
                    final UserVo userVo = udbRemoteService.getByUserCode(supplementOrderPo.getCreateUser());
                    if (null == userVo) {
                        throw new BizException("创建人的数据不存在，请联系系统管理员！", supplementOrderPo.getCreateUser());
                    }
                    supplementOrderPo.setHandleUsername(userVo.getUsername());
                    supplementOrderPo.setCreateUsername(userVo.getUsername());
                }
            } else {
                supplementOrderPo.setHandleUser(GlobalContext.getUserKey());
                supplementOrderPo.setHandleUsername(GlobalContext.getUsername());
            }
            supplementOrderPo.setHandleTime(LocalDateTime.now());
        } else if (SupplementStatus.WAIT_PRICE.equals(supplementOrderPo.getSupplementStatus())) {
            supplementOrderPo.setHandleUser(settleConfig.getConfirmUser());
            final UserVo userVo = udbRemoteService.getByUserCode(settleConfig.getConfirmUser());
            if (null == userVo) {
                throw new BizException("确认人的数据不存在，请联系系统管理员进行配置！", settleConfig.getConfirmUser());
            }
            supplementOrderPo.setHandleUsername(userVo.getUsername());
            supplementOrderPo.setHandleTime(LocalDateTime.now());
        } else if (SupplementStatus.WAIT_CONFIRM.equals(supplementOrderPo.getSupplementStatus())) {
            supplementOrderPo.setHandleUser(settleConfig.getSupplierName());
            supplementOrderPo.setHandleUsername(settleConfig.getSupplierName());
            supplementOrderPo.setHandleTime(LocalDateTime.now());
        } else if (SupplementStatus.WAIT_EXAMINE.equals(supplementOrderPo.getSupplementStatus())) {
            supplementOrderPo.setHandleUser(settleConfig.getExamineUser());
            final UserVo userVo = udbRemoteService.getByUserCode(settleConfig.getExamineUser());
            if (null == userVo) {
                throw new BizException("审核人的数据不存在，请联系系统管理员进行配置！", settleConfig.getExamineUser());
            }
            supplementOrderPo.setHandleUsername(userVo.getUsername());
            supplementOrderPo.setHandleTime(LocalDateTime.now());
        } else if (SupplementStatus.AUDITED.equals(supplementOrderPo.getSupplementStatus())) {
            if (StringUtils.isNotBlank(supplementOrderPo.getCreateUser())) {
                supplementOrderPo.setHandleUser(supplementOrderPo.getCreateUser());
                if (StringUtils.isNotBlank(supplementOrderPo.getCreateUsername())) {
                    supplementOrderPo.setHandleUsername(supplementOrderPo.getCreateUsername());
                } else {
                    final UserVo userVo = udbRemoteService.getByUserCode(supplementOrderPo.getCreateUser());
                    if (null == userVo) {
                        throw new BizException("创建人的数据不存在，请联系系统管理员！", supplementOrderPo.getCreateUser());
                    }
                    supplementOrderPo.setHandleUsername(userVo.getUsername());
                    supplementOrderPo.setCreateUsername(userVo.getUsername());
                }
            } else {
                supplementOrderPo.setHandleUser(GlobalContext.getUserKey());
                supplementOrderPo.setHandleUsername(GlobalContext.getUsername());
            }
            supplementOrderPo.setHandleTime(LocalDateTime.now());
        }
        log.info("根据状态来更新处理人的结果：po=>{}", JacksonUtil.parse2Str(supplementOrderPo));
        return supplementOrderPo;
    }
}
