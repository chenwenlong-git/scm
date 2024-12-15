package com.hete.supply.scm.server.scm.supplier.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.mc.api.workflow.enums.WorkflowState;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.remote.dubbo.WorkflowService;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.bo.ScmImageBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.feishu.config.FeiShuConfig;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierPaymentAccountConverter;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierPaymentAccountDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierSubjectDao;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierPaymentAccountCreateDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierPaymentAccountEditDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierPaymentAccountSearchDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierSubjectPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierPaymentAccountSearchVo;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountStatus;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountType;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentCurrencyType;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/12/6 15:19
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplierPaymentAccountBaseService {

    private final SupplierDao supplierDao;
    private final SupplierPaymentAccountDao supplierPaymentAccountDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final IdGenerateService idGenerateService;
    private final ScmImageBaseService scmImageBaseService;
    private final FeishuAuditOrderDao feishuAuditOrderDao;
    private final AuthBaseService authBaseService;
    private final UdbRemoteService udbRemoteService;
    private final WorkflowService workflowService;
    private final FeiShuConfig feiShuConfig;
    private final SupplierSubjectDao supplierSubjectDao;

    /**
     * 个人账号限制上传数量
     */
    private final static Integer ACCOUNT_PERSONAL_NUM = 2;

    /**
     * 对公账号限制上传数量
     */
    private final static Integer ACCOUNT_COMPANY_NUM = 1;

    /**
     * 收款授权书上传数量
     */
    private final static Integer ACCOUNT_AUTH_NUM = 30;


    private final static String LOG_KEY = "操作";


    /**
     * 列表
     *
     * @param dto:
     * @return PageInfo<SupplierPaymentAccountSearchVo>
     * @author ChenWenLong
     * @date 2023/12/5 18:13
     */
    public CommonPageResult.PageInfo<SupplierPaymentAccountSearchVo> search(SupplierPaymentAccountSearchDto dto) {
        CommonPageResult.PageInfo<SupplierPaymentAccountSearchVo> pageResult = supplierPaymentAccountDao.selectSupplierPaymentAccountPage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<SupplierPaymentAccountSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }

        List<Long> supplierPaymentAccountIdList = records.stream()
                .map(SupplierPaymentAccountSearchVo::getSupplierPaymentAccountId)
                .collect(Collectors.toList());
        // 查询审批单信息
        Map<Long, List<FeishuAuditOrderPo>> feishuAuditOrderPoMap = feishuAuditOrderDao.getMapByBusinessIdList(supplierPaymentAccountIdList);

        // 获取图片
        Map<Long, List<String>> fileCodePersonalMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SUPPLIER_PAYMENT_PERSONAL, supplierPaymentAccountIdList);
        Map<Long, List<String>> fileCodeAuthMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SUPPLIER_PAYMENT_AUTH, supplierPaymentAccountIdList);
        Map<Long, List<String>> fileCodeCompanyMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SUPPLIER_PAYMENT_COMPANY, supplierPaymentAccountIdList);

        for (SupplierPaymentAccountSearchVo record : records) {
            Long supplierPaymentAccountId = record.getSupplierPaymentAccountId();
            List<FeishuAuditOrderPo> feishuAuditOrderPoList = feishuAuditOrderPoMap.get(supplierPaymentAccountId);
            List<SupplierPaymentAccountSearchVo.SupplierPaymentAccountAuditOrderVo> auditOrderVoList = Optional.ofNullable(feishuAuditOrderPoList)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(po -> {
                        SupplierPaymentAccountSearchVo.SupplierPaymentAccountAuditOrderVo auditOrderVo = new SupplierPaymentAccountSearchVo.SupplierPaymentAccountAuditOrderVo();
                        auditOrderVo.setWorkflowNo(po.getWorkflowNo());
                        auditOrderVo.setWorkflowTitle(po.getWorkflowTitle());
                        auditOrderVo.setWorkflowResult(po.getWorkflowResult());
                        auditOrderVo.setWorkflowState(po.getWorkflowState());
                        auditOrderVo.setProcessBusinessId(po.getProcessBusinessId());
                        return auditOrderVo;
                    }).collect(Collectors.toList());
            if (fileCodePersonalMap.containsKey(record.getSupplierPaymentAccountId())) {
                record.setPersonalFileCodeList(fileCodePersonalMap.get(record.getSupplierPaymentAccountId()));
            }
            if (fileCodeAuthMap.containsKey(record.getSupplierPaymentAccountId())) {
                record.setAuthFileCodeList(fileCodeAuthMap.get(record.getSupplierPaymentAccountId()));
            }
            if (fileCodeCompanyMap.containsKey(record.getSupplierPaymentAccountId())) {
                record.setCompanyFileCodeList(fileCodeCompanyMap.get(record.getSupplierPaymentAccountId()));
            }
            record.setSupplierPaymentAccountAuditOrderList(auditOrderVoList);
        }
        return pageResult;
    }

    /**
     * 批量创建账号
     *
     * @param dtoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/6 15:20
     */
    public void createAccount(List<SupplierPaymentAccountCreateDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }

        List<String> supplierCodeList = dtoList.stream()
                .map(SupplierPaymentAccountCreateDto::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
        Map<String, SupplierPo> supplierPoMap = supplierDao.getSupplierMapBySupplierCodeList(supplierCodeList);

        List<String> accountList = dtoList.stream()
                .map(SupplierPaymentAccountCreateDto::getAccount)
                .distinct()
                .collect(Collectors.toList());

        Assert.isTrue(accountList.size() == dtoList.size(), () -> new ParamIllegalException("账号信息中存在重复账号，请先修改后重试！"));

        //是否存在已创建的账号
        List<SupplierPaymentAccountPo> supplierPaymentAccountPoList = supplierPaymentAccountDao.getListByAccountList(accountList);
        for (SupplierPaymentAccountPo supplierPaymentAccountPo : supplierPaymentAccountPoList) {
            throw new ParamIllegalException("账号：{}已存在，请先修改后重试！", supplierPaymentAccountPo.getAccount());
        }

        //验证数据
        for (SupplierPaymentAccountCreateDto dto : dtoList) {
            SupplierPo supplierPo = supplierPoMap.get(dto.getSupplierCode());
            Assert.notNull(supplierPo, () -> new BizException("供应商代码：{}查询不到，请刷新页面后重试！", dto.getSupplierCode()));
            if (SupplierPaymentAccountType.PERSONAL_ACCOUNT.equals(dto.getSupplierPaymentAccountType())) {
                Assert.notEmpty(dto.getPersonalFileCodeList(), () -> new ParamIllegalException("身份证照片正反面不能为空！"));
                Assert.isTrue(dto.getPersonalFileCodeList().size() <= ACCOUNT_PERSONAL_NUM, () -> new ParamIllegalException("身份证照片正反面最多只能上传{}张！", ACCOUNT_PERSONAL_NUM));
            }
            if (SupplierPaymentAccountType.PERSONAL_ACCOUNT.equals(dto.getSupplierPaymentAccountType())
                    && !SupplierType.TEMPORARY.equals(supplierPo.getSupplierType())) {
                Assert.notEmpty(dto.getAuthFileCodeList(), () -> new ParamIllegalException("收款授权书不能为空！"));
                Assert.isTrue(dto.getAuthFileCodeList().size() <= ACCOUNT_AUTH_NUM, () -> new ParamIllegalException("收款授权书最多只能上传{}张！", ACCOUNT_AUTH_NUM));
            }
            if (SupplierPaymentAccountType.COMPANY_ACCOUNT.equals(dto.getSupplierPaymentAccountType())) {
                Assert.notEmpty(dto.getCompanyFileCodeList(), () -> new ParamIllegalException("企业营业执照不能为空！"));
                Assert.isTrue(dto.getCompanyFileCodeList().size() == ACCOUNT_COMPANY_NUM, () -> new ParamIllegalException("企业营业执照只能上传{}张！", ACCOUNT_COMPANY_NUM));
            }
            if (SupplierPaymentCurrencyType.USD.equals(dto.getSupplierPaymentCurrencyType())) {
                Assert.notBlank(dto.getSwiftCode(), () -> new ParamIllegalException("Swift code不能为空！"));
                Assert.isTrue(dto.getSwiftCode().length() <= 32, () -> new ParamIllegalException("Swift code字符长度不能超过 32 位！"));
            }

        }

        List<SupplierPaymentAccountPo> insertPoList = SupplierPaymentAccountConverter.createDtoToPo(dtoList, supplierPoMap);

        List<ScmImageBo> scmImageBoPersonalList = new ArrayList<>();
        List<ScmImageBo> scmImageBoAuthList = new ArrayList<>();
        List<ScmImageBo> scmImageBoCompanyList = new ArrayList<>();

        List<FeishuAuditOrderPo> insertFeishuAuditOrderList = new ArrayList<>();

        for (SupplierPaymentAccountPo supplierPaymentAccountPo : insertPoList) {
            final long snowflakeId = idGenerateService.getSnowflakeId();
            supplierPaymentAccountPo.setSupplierPaymentAccountId(snowflakeId);
            dtoList.stream()
                    .filter(dto -> dto.getAccount().equals(supplierPaymentAccountPo.getAccount()))
                    .findFirst()
                    .ifPresent(dto -> {
                        if (CollectionUtils.isNotEmpty(dto.getPersonalFileCodeList())
                                && SupplierPaymentAccountType.PERSONAL_ACCOUNT.equals(dto.getSupplierPaymentAccountType())) {
                            ScmImageBo scmImageBo = new ScmImageBo();
                            scmImageBo.setImageBizId(supplierPaymentAccountPo.getSupplierPaymentAccountId());
                            scmImageBo.setFileCodeList(dto.getPersonalFileCodeList());
                            scmImageBoPersonalList.add(scmImageBo);
                        }
                        if (CollectionUtils.isNotEmpty(dto.getAuthFileCodeList())
                                && SupplierPaymentAccountType.PERSONAL_ACCOUNT.equals(dto.getSupplierPaymentAccountType())) {
                            ScmImageBo scmImageBo = new ScmImageBo();
                            scmImageBo.setImageBizId(supplierPaymentAccountPo.getSupplierPaymentAccountId());
                            scmImageBo.setFileCodeList(dto.getAuthFileCodeList());
                            scmImageBoAuthList.add(scmImageBo);
                        }
                        if (CollectionUtils.isNotEmpty(dto.getCompanyFileCodeList())
                                && SupplierPaymentAccountType.COMPANY_ACCOUNT.equals(dto.getSupplierPaymentAccountType())) {
                            ScmImageBo scmImageBo = new ScmImageBo();
                            scmImageBo.setImageBizId(supplierPaymentAccountPo.getSupplierPaymentAccountId());
                            scmImageBo.setFileCodeList(dto.getCompanyFileCodeList());
                            scmImageBoCompanyList.add(scmImageBo);
                        }
                    });

            // 创建日志
            this.createStatusChangeLog(supplierPaymentAccountPo, "创建账户信息成功", null);

            // 飞书审批数据
            String feishuAuditOrderNo = idGenerateService.getConfuseCode(ScmConstant.FEISHU_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
            FeishuAuditOrderPo feishuAuditOrderPo = new FeishuAuditOrderPo();
            feishuAuditOrderPo.setFeishuAuditOrderNo(feishuAuditOrderNo);
            feishuAuditOrderPo.setBusinessId(supplierPaymentAccountPo.getSupplierPaymentAccountId());
            feishuAuditOrderPo.setFeishuAuditOrderType(FeishuAuditOrderType.SUPPLIER_PAYMENT_ACCOUNT);
            insertFeishuAuditOrderList.add(feishuAuditOrderPo);

            supplierPaymentAccountPo.setFeishuAuditOrderNo(feishuAuditOrderNo);

        }

        supplierPaymentAccountDao.insertBatch(insertPoList);

        // 创建图片
        scmImageBaseService.insertBatchImageBo(scmImageBoPersonalList, ImageBizType.SUPPLIER_PAYMENT_PERSONAL);
        scmImageBaseService.insertBatchImageBo(scmImageBoAuthList, ImageBizType.SUPPLIER_PAYMENT_AUTH);
        scmImageBaseService.insertBatchImageBo(scmImageBoCompanyList, ImageBizType.SUPPLIER_PAYMENT_COMPANY);

        // 创建审批单数据
        feishuAuditOrderDao.insertBatch(insertFeishuAuditOrderList);

        // 处理非飞书账号登录创建审批单场景使用默认账号
        String submitReviewUserKey = GlobalContext.getUserKey();
        UserVo userVo = udbRemoteService.getByUserCode(submitReviewUserKey);
        if (userVo != null && StringUtils.isBlank(userVo.getFeiShuOpenId())) {
            submitReviewUserKey = feiShuConfig.getDefaultAccount();
        }

        for (FeishuAuditOrderPo feishuAuditOrderPo : insertFeishuAuditOrderList) {
            String feishuAuditOrderNo = feishuAuditOrderPo.getFeishuAuditOrderNo();
            SupplierPaymentAccountPo supplierPaymentAccountPo = insertPoList.stream()
                    .filter(po -> feishuAuditOrderNo.equals(po.getFeishuAuditOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (supplierPaymentAccountPo == null) {
                throw new BizException("审批单号：{}找不到对应的供应商收款账号信息！", feishuAuditOrderNo);
            }

            ScmImageBo scmImagePersonalBo = scmImageBoPersonalList.stream()
                    .filter(bo -> bo.getImageBizId().equals(supplierPaymentAccountPo.getSupplierPaymentAccountId()))
                    .findFirst()
                    .orElse(null);

            ScmImageBo scmImageAuthBo = scmImageBoAuthList.stream()
                    .filter(bo -> bo.getImageBizId().equals(supplierPaymentAccountPo.getSupplierPaymentAccountId()))
                    .findFirst()
                    .orElse(null);

            ScmImageBo scmImageCompanyBo = scmImageBoCompanyList.stream()
                    .filter(bo -> bo.getImageBizId().equals(supplierPaymentAccountPo.getSupplierPaymentAccountId()))
                    .findFirst()
                    .orElse(null);

            List<String> scmImagePersonalList = new ArrayList<>();
            if (scmImagePersonalBo != null) {
                scmImagePersonalList = scmImagePersonalBo.getFileCodeList();
            }

            List<String> scmImageAuthList = new ArrayList<>();
            if (scmImageAuthBo != null) {
                scmImageAuthList = scmImageAuthBo.getFileCodeList();
            }

            List<String> scmImageCompanyList = new ArrayList<>();
            if (scmImageCompanyBo != null) {
                scmImageCompanyList = scmImageCompanyBo.getFileCodeList();
            }

            workflowService.createSupplierPaymentAccountInstance(feishuAuditOrderPo,
                    supplierPaymentAccountPo,
                    scmImagePersonalList,
                    scmImageAuthList,
                    scmImageCompanyList,
                    submitReviewUserKey);
        }


    }

    /**
     * 日志
     *
     * @param supplierPaymentAccountPo
     */
    public void createStatusChangeLog(SupplierPaymentAccountPo supplierPaymentAccountPo,
                                      String scenes,
                                      String isDefault) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.SUPPLIER_PAYMENT_ACCOUNT_STATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.SUPPLIER_PAYMENT_ACCOUNT_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.SUPPLIER_PAYMENT_ACCOUNT_STATUS.name());
        bizLogCreateMqDto.setBizCode(String.valueOf(supplierPaymentAccountPo.getSupplierPaymentAccountId()));
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        String userKey = StringUtils.isNotBlank(GlobalContext.getUserKey()) ? GlobalContext.getUserKey() : ScmConstant.SYSTEM_USER;
        String username = StringUtils.isNotBlank(GlobalContext.getUsername()) ? GlobalContext.getUsername() : ScmConstant.MQ_DEFAULT_USER;
        bizLogCreateMqDto.setOperateUser(userKey);
        bizLogCreateMqDto.setOperateUsername(username);

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();
        if (StringUtils.isNotBlank(isDefault)) {
            bizLogCreateMqDto.setContent(isDefault);
        } else {
            bizLogCreateMqDto.setContent(supplierPaymentAccountPo.getSupplierPaymentAccountStatus().getRemark());
        }

        LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey(LOG_KEY);
        logVersionBo.setValueType(LogVersionValueType.STRING);
        if (StringUtils.isNotBlank(scenes)) {
            logVersionBo.setValue(scenes);
        }
        logVersionBos.add(logVersionBo);

        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    /**
     * 编辑账号
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/6 15:20
     */
    public void editAccount(@NotNull SupplierPaymentAccountEditDto dto) {
        SupplierPaymentAccountPo supplierPaymentAccountPo = supplierPaymentAccountDao.getByIdVersion(dto.getSupplierPaymentAccountId(), dto.getVersion());
        if (null == supplierPaymentAccountPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        this.verifyAuth(supplierPaymentAccountPo.getSupplierPaymentAccountId());
        if (!(SupplierPaymentAccountStatus.REFUSED.equals(supplierPaymentAccountPo.getSupplierPaymentAccountStatus())
                || SupplierPaymentAccountStatus.EFFECTIVE.equals(supplierPaymentAccountPo.getSupplierPaymentAccountStatus())
                | SupplierPaymentAccountStatus.CREATE_FAIL.equals(supplierPaymentAccountPo.getSupplierPaymentAccountStatus()))) {
            throw new ParamIllegalException("状态处于【{}】【{}】【{}】才能进行修改操作，请刷新页面后重试！",
                    SupplierPaymentAccountStatus.EFFECTIVE.getRemark(),
                    SupplierPaymentAccountStatus.REFUSED.getRemark(),
                    SupplierPaymentAccountStatus.CREATE_FAIL.getRemark());
        }

        SupplierPo supplierPo = supplierDao.getBySupplierCode(dto.getSupplierCode());
        Assert.notNull(supplierPo, () -> new ParamIllegalException("供应商代码{}数据已被修改或删除，请刷新页面后重试！", dto.getSupplierCode()));

        //是否存在已创建的账号
        List<SupplierPaymentAccountPo> supplierPaymentAccountOldPoList = supplierPaymentAccountDao.getListByAccountNeId(dto.getAccount(), supplierPaymentAccountPo.getSupplierPaymentAccountId());
        if (CollectionUtils.isNotEmpty(supplierPaymentAccountOldPoList)) {
            throw new ParamIllegalException("账号：{}已存在，请先修改后重试！", dto.getAccount());
        }

        //验证数据
        if (SupplierPaymentAccountType.PERSONAL_ACCOUNT.equals(dto.getSupplierPaymentAccountType())) {
            Assert.notEmpty(dto.getPersonalFileCodeList(), () -> new ParamIllegalException("身份证照片正反面不能为空！"));
            Assert.isTrue(dto.getPersonalFileCodeList().size() <= ACCOUNT_PERSONAL_NUM, () -> new ParamIllegalException("身份证照片正反面最多只能上传{}张！", ACCOUNT_PERSONAL_NUM));
        }
        if (SupplierPaymentAccountType.PERSONAL_ACCOUNT.equals(dto.getSupplierPaymentAccountType())
                && !SupplierType.TEMPORARY.equals(supplierPo.getSupplierType())) {
            Assert.notEmpty(dto.getAuthFileCodeList(), () -> new ParamIllegalException("收款授权书不能为空！"));
            Assert.isTrue(dto.getAuthFileCodeList().size() <= ACCOUNT_AUTH_NUM, () -> new ParamIllegalException("收款授权书最多只能上传{}张！", ACCOUNT_AUTH_NUM));
        }
        if (SupplierPaymentAccountType.COMPANY_ACCOUNT.equals(dto.getSupplierPaymentAccountType())) {
            Assert.notEmpty(dto.getCompanyFileCodeList(), () -> new ParamIllegalException("企业营业执照不能为空！"));
            Assert.isTrue(dto.getCompanyFileCodeList().size() == ACCOUNT_COMPANY_NUM, () -> new ParamIllegalException("企业营业执照只能上传{}张！", ACCOUNT_COMPANY_NUM));
        }
        if (SupplierPaymentCurrencyType.USD.equals(dto.getSupplierPaymentCurrencyType())) {
            Assert.notBlank(dto.getSwiftCode(), () -> new ParamIllegalException("Swift code不能为空！"));
            Assert.isTrue(dto.getSwiftCode().length() <= 32, () -> new ParamIllegalException("Swift code字符长度不能超过 32 位！"));
        }


        SupplierPaymentAccountPo updatePo = SupplierPaymentAccountConverter.editDtoToPo(supplierPaymentAccountPo, dto, supplierPo);

        // 图片推送飞书
        List<String> personalFileCodeList = new ArrayList<>();
        List<String> authFileCodeList = new ArrayList<>();
        List<String> companyFileCodeList = new ArrayList<>();

        // 更新图片
        List<Long> supplierPaymentAccountIdList = List.of(supplierPaymentAccountPo.getSupplierPaymentAccountId());
        if (SupplierPaymentAccountType.PERSONAL_ACCOUNT.equals(dto.getSupplierPaymentAccountType())) {
            if (CollectionUtils.isNotEmpty(dto.getPersonalFileCodeList())) {
                personalFileCodeList = new ArrayList<>(dto.getPersonalFileCodeList());
            }
            if (CollectionUtils.isNotEmpty(dto.getAuthFileCodeList())) {
                authFileCodeList = new ArrayList<>(dto.getAuthFileCodeList());
            }
            List<String> scmImagePersonalPos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SUPPLIER_PAYMENT_PERSONAL, supplierPaymentAccountIdList);
            scmImageBaseService.editImage(dto.getPersonalFileCodeList(),
                    scmImagePersonalPos,
                    ImageBizType.SUPPLIER_PAYMENT_PERSONAL,
                    supplierPaymentAccountPo.getSupplierPaymentAccountId());

            List<String> scmImageAuthPos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SUPPLIER_PAYMENT_AUTH, supplierPaymentAccountIdList);
            scmImageBaseService.editImage(dto.getAuthFileCodeList(),
                    scmImageAuthPos,
                    ImageBizType.SUPPLIER_PAYMENT_AUTH,
                    supplierPaymentAccountPo.getSupplierPaymentAccountId());
        } else {
            scmImageBaseService.removeAllImageList(ImageBizType.SUPPLIER_PAYMENT_PERSONAL, supplierPaymentAccountIdList);
            scmImageBaseService.removeAllImageList(ImageBizType.SUPPLIER_PAYMENT_AUTH, supplierPaymentAccountIdList);
        }

        if (SupplierPaymentAccountType.COMPANY_ACCOUNT.equals(dto.getSupplierPaymentAccountType())) {
            if (CollectionUtils.isNotEmpty(dto.getCompanyFileCodeList())) {
                companyFileCodeList = new ArrayList<>(dto.getCompanyFileCodeList());
            }
            List<String> scmImageCompanyPos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SUPPLIER_PAYMENT_COMPANY, supplierPaymentAccountIdList);
            scmImageBaseService.editImage(dto.getCompanyFileCodeList(),
                    scmImageCompanyPos,
                    ImageBizType.SUPPLIER_PAYMENT_COMPANY,
                    supplierPaymentAccountPo.getSupplierPaymentAccountId());
        } else {
            scmImageBaseService.removeAllImageList(ImageBizType.SUPPLIER_PAYMENT_COMPANY, supplierPaymentAccountIdList);
        }


        // 创建日志
        this.createStatusChangeLog(updatePo, "编辑重提", null);


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

        updatePo.setFeishuAuditOrderNo(feishuAuditOrderNo);

        supplierPaymentAccountDao.updateByIdVersion(updatePo);

        // 创建审批单数据
        feishuAuditOrderDao.insert(feishuAuditOrderPo);

        // 处理非飞书账号登录创建审批单场景使用默认账号
        String submitReviewUserKey = GlobalContext.getUserKey();
        UserVo userVo = udbRemoteService.getByUserCode(submitReviewUserKey);
        if (userVo != null && StringUtils.isBlank(userVo.getFeiShuOpenId())) {
            submitReviewUserKey = feiShuConfig.getDefaultAccount();
        }

        workflowService.createSupplierPaymentAccountInstance(feishuAuditOrderPo,
                supplierPaymentAccountPo,
                personalFileCodeList,
                authFileCodeList,
                companyFileCodeList,
                submitReviewUserKey);
    }

    /**
     * 权限验证
     *
     * @param supplierPaymentAccountId:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/14 13:43
     */
    public void verifyAuth(Long supplierPaymentAccountId) {
        SupplierPaymentAccountPo supplierPaymentAccountPo = supplierPaymentAccountDao.getById(supplierPaymentAccountId);
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new BizException("禁止非法请求");
        }
        if (CollectionUtil.isNotEmpty(supplierCodeList) && !supplierCodeList.contains(supplierPaymentAccountPo.getSupplierCode())) {
            throw new BizException("禁止非法请求");
        }
    }

    public List<SupplierPaymentAccountPo> getListByAccount(List<String> supplierAccountList) {
        if (CollectionUtils.isEmpty(supplierAccountList)) {
            return Collections.emptyList();
        }

        return supplierPaymentAccountDao.getListByAccountList(supplierAccountList);
    }

    public SupplierPaymentAccountPo getByAccount(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }

        return supplierPaymentAccountDao.getByAccount(account);
    }

    /**
     * 验证收款账号和主体信息
     *
     * @param supplierPaymentAccountList:
     * @param subjectSupplierCodeMap:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/18 15:25
     */
    public void checkPaymentAccountSubjectInfo(List<SupplierPaymentAccountCreateDto> supplierPaymentAccountList,
                                               Map<String, List<String>> subjectSupplierCodeMap) {
        if (CollectionUtils.isEmpty(supplierPaymentAccountList)) {
            return;
        }
        for (SupplierPaymentAccountCreateDto dto : supplierPaymentAccountList) {
            // 验证主体信息存不存在
            List<String> subjectList = subjectSupplierCodeMap.get(dto.getSupplierCode());
            if (com.alibaba.nacos.common.utils.StringUtils.isNotBlank(dto.getSubject())) {
                Assert.isTrue(CollectionUtils.isNotEmpty(subjectList), () -> new ParamIllegalException("收款账户:{}的主体:{}关联不到对应的信息，请确认供应商代码:{}下存在该主体！",
                        dto.getAccount(),
                        dto.getSubject(),
                        dto.getSupplierCode()));
                Assert.isTrue(subjectList.contains(dto.getSubject()), () -> new ParamIllegalException("收款账户:{}的主体:{}关联不到对应的信息，请确认供应商代码:{}下存在该主体！",
                        dto.getAccount(),
                        dto.getSubject(),
                        dto.getSupplierCode()));

            }
        }
    }

}
