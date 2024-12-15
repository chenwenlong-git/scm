package com.hete.supply.scm.server.scm.supplier.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.mc.api.workflow.enums.WorkflowState;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.remote.dubbo.WorkflowService;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.feishu.config.FeiShuConfig;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierPaymentAccountDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierSubjectDao;
import com.hete.supply.scm.server.scm.supplier.entity.dto.*;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierSubjectPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierPaymentAccountDropDownVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierPaymentAccountSearchVo;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountStatus;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierPaymentAccountBaseService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/12/5 17:39
 */
@Service
@RequiredArgsConstructor
public class SupplierPaymentAccountBizService {

    private final SupplierPaymentAccountBaseService supplierPaymentAccountBaseService;
    private final SupplierPaymentAccountDao supplierPaymentAccountDao;
    private final FeishuAuditOrderDao feishuAuditOrderDao;
    private final IdGenerateService idGenerateService;
    private final AuthBaseService authBaseService;
    private final FeiShuConfig feiShuConfig;
    private final UdbRemoteService udbRemoteService;
    private final WorkflowService workflowService;
    private final SupplierSubjectDao supplierSubjectDao;
    private final SupplierDao supplierDao;


    /**
     * 列表
     *
     * @param dto:
     * @return PageInfo<SupplierPaymentAccountSearchVo>
     * @author ChenWenLong
     * @date 2023/12/5 18:13
     */
    public CommonPageResult.PageInfo<SupplierPaymentAccountSearchVo> search(SupplierPaymentAccountSearchDto dto) {
        SupplierPaymentAccountSearchDto supplierPaymentAccountSearchDto = this.searchAuthSupplierWhere(dto);
        if (null == supplierPaymentAccountSearchDto) {
            return new CommonPageResult.PageInfo<>();
        }
        return supplierPaymentAccountBaseService.search(supplierPaymentAccountSearchDto);
    }

    private SupplierPaymentAccountSearchDto searchAuthSupplierWhere(SupplierPaymentAccountSearchDto dto) {
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return null;
        }
        return dto;
    }


    /**
     * 设置默认/取消默认
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/6 11:04
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitDefault(SupplierPaymentAccountDefaultDto dto) {
        SupplierPaymentAccountPo supplierPaymentAccountPo = supplierPaymentAccountDao.getById(dto.getSupplierPaymentAccountId());
        Assert.notNull(supplierPaymentAccountPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        supplierPaymentAccountBaseService.verifyAuth(supplierPaymentAccountPo.getSupplierPaymentAccountId());
        if (BooleanType.TRUE.equals(dto.getIsDefault()) && BooleanType.TRUE.equals(supplierPaymentAccountPo.getIsDefault())) {
            throw new ParamIllegalException("当前已处于默认，请刷新页面后重试！");
        }
        if (BooleanType.FALSE.equals(dto.getIsDefault()) && BooleanType.FALSE.equals(supplierPaymentAccountPo.getIsDefault())) {
            throw new ParamIllegalException("当前已处于非默认，请刷新页面后重试！");
        }
        SupplierPaymentAccountPo updatePo = new SupplierPaymentAccountPo();
        updatePo.setSupplierPaymentAccountId(supplierPaymentAccountPo.getSupplierPaymentAccountId());
        updatePo.setVersion(supplierPaymentAccountPo.getVersion());
        updatePo.setIsDefault(dto.getIsDefault());
        supplierPaymentAccountDao.updateByIdVersion(updatePo);
        String isDefault = "取消默认";
        String scenes = "取消默认";
        if (BooleanType.TRUE.equals(dto.getIsDefault())) {
            isDefault = "增加默认";
            scenes = "设置默认";
        }
        supplierPaymentAccountBaseService.createStatusChangeLog(supplierPaymentAccountPo, scenes, isDefault);
    }

    /**
     * 提交弃用
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/6 11:07
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitDeprecated(SupplierPaymentAccountIdAndVersionDto dto) {
        SupplierPaymentAccountPo supplierPaymentAccountPo = supplierPaymentAccountDao.getByIdVersion(dto.getSupplierPaymentAccountId(), dto.getVersion());
        Assert.notNull(supplierPaymentAccountPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        supplierPaymentAccountBaseService.verifyAuth(supplierPaymentAccountPo.getSupplierPaymentAccountId());
        if (!SupplierPaymentAccountStatus.EFFECTIVE.equals(supplierPaymentAccountPo.getSupplierPaymentAccountStatus())) {
            throw new ParamIllegalException("状态处于【{}】才能进行修改操作，请刷新页面后重试！",
                    SupplierPaymentAccountStatus.EFFECTIVE.getRemark());
        }

        supplierPaymentAccountPo.setSupplierPaymentAccountId(supplierPaymentAccountPo.getSupplierPaymentAccountId());
        supplierPaymentAccountPo.setVersion(supplierPaymentAccountPo.getVersion());
        supplierPaymentAccountPo.setSupplierPaymentAccountStatus(SupplierPaymentAccountStatus.WAIT_INVALID_EXAMINE);


        // 飞书审批数据
        List<FeishuAuditOrderPo> feishuAuditOrderPoList = feishuAuditOrderDao.getByBusinessIdAndStatusList(supplierPaymentAccountPo.getSupplierPaymentAccountId(),
                List.of(WorkflowState.PRE_START, WorkflowState.START));
        if (CollectionUtils.isNotEmpty(feishuAuditOrderPoList)) {
            throw new BizException("收款账号：{}存在正在审批的单据，需要审批后才能进行操作，请刷新页面后重试！",
                    supplierPaymentAccountPo.getAccount());
        }
        String feishuAuditOrderNo = idGenerateService.getConfuseCode(ScmConstant.FEISHU_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
        FeishuAuditOrderPo feishuAuditOrderPo = new FeishuAuditOrderPo();
        feishuAuditOrderPo.setFeishuAuditOrderNo(feishuAuditOrderNo);
        feishuAuditOrderPo.setBusinessId(supplierPaymentAccountPo.getSupplierPaymentAccountId());
        feishuAuditOrderPo.setFeishuAuditOrderType(FeishuAuditOrderType.SUPPLIER_PAYMENT_ACCOUNT);

        supplierPaymentAccountPo.setFeishuAuditOrderNo(feishuAuditOrderNo);

        supplierPaymentAccountDao.updateByIdVersion(supplierPaymentAccountPo);
        supplierPaymentAccountBaseService.createStatusChangeLog(supplierPaymentAccountPo, "账号弃用", null);

        // 创建审批单数据
        feishuAuditOrderDao.insert(feishuAuditOrderPo);

        // 处理非飞书账号登录创建审批单场景使用默认账号
        String submitReviewUserKey = GlobalContext.getUserKey();
        UserVo userVo = udbRemoteService.getByUserCode(submitReviewUserKey);
        if (userVo != null && StringUtils.isBlank(userVo.getFeiShuOpenId())) {
            submitReviewUserKey = feiShuConfig.getDefaultAccount();
        }

        workflowService.createSupplierPaymentAccountInvalidInstance(feishuAuditOrderPo,
                supplierPaymentAccountPo,
                submitReviewUserKey);

    }

    /**
     * 编辑
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/6 11:45
     */
    @Transactional(rollbackFor = Exception.class)
    public void edit(SupplierPaymentAccountEditDto dto) {
        supplierPaymentAccountBaseService.editAccount(dto);
    }

    /**
     * 创建
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/6 11:51
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(SupplierPaymentAccountCreateDto dto) {
        // 验证收款账号和主体信息
        List<SupplierSubjectPo> supplierSubjectPoList = supplierSubjectDao.getListBySupplierCodeList(List.of(dto.getSupplierCode()));
        Map<String, List<String>> subjectSupplierCodeMap = supplierSubjectPoList.stream().collect(Collectors.groupingBy(SupplierSubjectPo::getSupplierCode,
                Collectors.mapping(SupplierSubjectPo::getSubject, Collectors.toList())));
        supplierPaymentAccountBaseService.checkPaymentAccountSubjectInfo(List.of(dto), subjectSupplierCodeMap);

        // 创建收款账号
        supplierPaymentAccountBaseService.createAccount(List.of(dto));
    }

    /**
     * 设置启用/禁用
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/7 16:56
     */
    @Transactional(rollbackFor = Exception.class)
    public void openClose(SupplierPaymentAccountOpenCloseDto dto) {
        SupplierPaymentAccountPo supplierPaymentAccountPo = supplierPaymentAccountDao.getByIdVersion(dto.getSupplierPaymentAccountId(), dto.getVersion());
        Assert.notNull(supplierPaymentAccountPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        SupplierPaymentAccountPo updatePo = new SupplierPaymentAccountPo();
        updatePo.setSupplierPaymentAccountId(supplierPaymentAccountPo.getSupplierPaymentAccountId());
        updatePo.setVersion(supplierPaymentAccountPo.getVersion());
        String scenes = "";
        if (BooleanType.TRUE.equals(dto.getIsOpenClose())) {
            updatePo.setSupplierPaymentAccountStatus(SupplierPaymentAccountStatus.EFFECTIVE);
            scenes = "操作启用";
        }
        if (BooleanType.FALSE.equals(dto.getIsOpenClose())) {
            updatePo.setSupplierPaymentAccountStatus(SupplierPaymentAccountStatus.INVALID);
            scenes = "操作禁用";
        }
        supplierPaymentAccountDao.updateByIdVersion(updatePo);
        supplierPaymentAccountBaseService.createStatusChangeLog(supplierPaymentAccountPo, scenes, null);
    }

    /**
     * 下拉获取供应商的生效收款账户信息
     *
     * @param dto:
     * @return List<SupplierPaymentAccountDropDownVo>
     * @author ChenWenLong
     * @date 2024/5/21 15:12
     */
    public List<SupplierPaymentAccountDropDownVo> getSupplierPaymentAccountList(SpAcPyReqDto dto) {
        List<SupplierPaymentAccountDropDownVo> voList = new ArrayList<>();

        final List<SupplierPaymentAccountPo> supplierPaymentAccountPoList = supplierPaymentAccountDao.getList(dto);
        final Map<String, String> supplierPoMap = supplierDao.getSupplierNameBySupplierCodeList(dto.getSupplierCodeList());

        // 获取生效的账号
        Map<String, List<SupplierPaymentAccountPo>> supplierPaymentAccountPoMap = supplierPaymentAccountPoList.stream()
                .filter(supplierPaymentAccountPo -> SupplierPaymentAccountStatus.EFFECTIVE.equals(supplierPaymentAccountPo.getSupplierPaymentAccountStatus()))
                .collect(Collectors.groupingBy(SupplierPaymentAccountPo::getSupplierCode));
        supplierPaymentAccountPoMap.forEach((String supplierCode, List<SupplierPaymentAccountPo> supplierPaymentAccountPos) -> {
            SupplierPaymentAccountDropDownVo supplierPaymentAccountDropDownVo = new SupplierPaymentAccountDropDownVo();
            supplierPaymentAccountDropDownVo.setSupplierCode(supplierCode);
            supplierPaymentAccountDropDownVo.setSupplierName(supplierPoMap.get(supplierCode));

            List<SupplierPaymentAccountDropDownVo.PaymentAccountDropDownItemVo> paymentAccountDropDownItemVoList = supplierPaymentAccountPos.stream().map(supplierPaymentAccountPo -> {
                SupplierPaymentAccountDropDownVo.PaymentAccountDropDownItemVo paymentAccountDropDownItemVo = new SupplierPaymentAccountDropDownVo.PaymentAccountDropDownItemVo();
                paymentAccountDropDownItemVo.setSupplierPaymentAccountId(supplierPaymentAccountPo.getSupplierPaymentAccountId());
                paymentAccountDropDownItemVo.setAccount(supplierPaymentAccountPo.getAccount());
                paymentAccountDropDownItemVo.setSubject(supplierPaymentAccountPo.getSubject());
                paymentAccountDropDownItemVo.setSupplierPaymentAccountType(supplierPaymentAccountPo.getSupplierPaymentAccountType());
                paymentAccountDropDownItemVo.setAccountUsername(supplierPaymentAccountPo.getAccountUsername());
                paymentAccountDropDownItemVo.setBankName(supplierPaymentAccountPo.getBankName());
                paymentAccountDropDownItemVo.setBankSubbranchName(supplierPaymentAccountPo.getBankSubbranchName());
                paymentAccountDropDownItemVo.setRemarks(supplierPaymentAccountPo.getRemarks());
                paymentAccountDropDownItemVo.setCurrency(supplierPaymentAccountPo.getSupplierPaymentCurrencyType().toCurrency());
                paymentAccountDropDownItemVo.setBankProvince(supplierPaymentAccountPo.getBankProvince());
                paymentAccountDropDownItemVo.setBankCity(supplierPaymentAccountPo.getBankCity());
                paymentAccountDropDownItemVo.setBankArea(supplierPaymentAccountPo.getBankArea());
                return paymentAccountDropDownItemVo;
            }).collect(Collectors.toList());

            supplierPaymentAccountDropDownVo.setPaymentAccountDropDownItemVoList(paymentAccountDropDownItemVoList);
            voList.add(supplierPaymentAccountDropDownVo);
        });

        return voList;

    }
}
