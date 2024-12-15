package com.hete.supply.scm.server.scm.supplier.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchasePreOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.SkuGetSuggestSupplierDto;
import com.hete.supply.scm.api.scm.entity.enums.IsCapacitySatisfy;
import com.hete.supply.scm.api.scm.entity.enums.IsMaterialStockSatisfy;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.api.scm.entity.vo.PurchasePreOrderVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuGetSuggestSupplierVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierSubjectImportationDto;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.common.util.StringUtil;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.remote.udb.SupplierAdminRemoteService;
import com.hete.supply.scm.remote.udb.UserService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.bo.CalPreShelfTimeBo;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataGetRawInventoryBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataGetCapacityDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemRawPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemSupplierPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataPo;
import com.hete.supply.scm.server.scm.entity.vo.ProduceDataGetCapacityVo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.supplier.builder.SupplierBuilder;
import com.hete.supply.scm.server.scm.supplier.builder.SupplierCapacityBuilder;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierConverter;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierPaymentAccountConverter;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierSubjectConverter;
import com.hete.supply.scm.server.scm.supplier.dao.*;
import com.hete.supply.scm.server.scm.supplier.entity.bo.*;
import com.hete.supply.scm.server.scm.supplier.entity.dto.*;
import com.hete.supply.scm.server.scm.supplier.entity.po.*;
import com.hete.supply.scm.server.scm.supplier.entity.vo.*;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountStatus;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierSubjectType;
import com.hete.supply.scm.server.scm.supplier.handler.AbstractSkuGetSuggestSupplierHandler;
import com.hete.supply.scm.server.scm.supplier.handler.SkuGetSuggestSupplierHandlerFactory;
import com.hete.supply.scm.server.scm.supplier.service.base.*;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierRefService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.udb.api.entity.dto.SupplierUpdateDto;
import com.hete.supply.udb.api.entity.dto.UserCodeDto;
import com.hete.supply.udb.api.entity.dto.UsernameListDto;
import com.hete.supply.udb.api.entity.vo.UserCodeNameVo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.supply.udb.api.enums.EnableStateEnum;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.supply.wms.api.interna.entity.dto.SkuInstockInventoryQueryDto;
import com.hete.supply.wms.api.interna.entity.vo.SkuInventoryVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.dubbo.util.DubboResponseUtil;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.hete.supply.scm.server.scm.entity.bo.CalPreShelfTimeBo.*;

/**
 * @author ChenWenLong
 * @date 2022/11/1 13:44
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierBizService {

    private final SupplierDao supplierDao;
    private final SupplierAccountDao supplierAccountDao;
    private final SupplierContactDao supplierContactDao;
    private final SupplierProductDao supplierProductDao;
    private final ScmImageBaseService scmImageBaseService;
    private final SupplierAdminRemoteService supplierAdminRemoteService;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final UserService userService;
    private final SampleBaseService sampleBaseService;
    private final AuthBaseService authBaseService;
    private final SupplierPaymentAccountBaseService supplierPaymentAccountBaseService;
    private final SupplierPaymentAccountDao supplierPaymentAccountDao;
    private final SupplierWarehouseBaseService supplierWarehouseBaseService;
    private final SupplierSubjectDao supplierSubjectDao;
    private final SupplierSubjectBaseService supplierSubjectBaseService;
    private final SkuGetSuggestSupplierHandlerFactory skuGetSuggestSupplierHandlerFactory;
    private final SupplierRefService supplyRefService;
    private final ProduceDataBaseService produceDataBaseService;
    private final SupplierInventoryBaseService supplierInventoryBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final SkuInfoDao skuInfoDao;
    private final PlmSkuDao plmSkuDao;
    private final SupplierCapacityRuleDao supplierCapacityRuleDao;
    private final SupplierCapacityBaseService supplierCapacityBaseService;

    public CommonPageResult.PageInfo<SupplierVo> searchSupplier(SupplierDto dto) {
        CommonPageResult.PageInfo<SupplierVo> pageInfo = supplierDao.selectSupplierPage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<SupplierVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageInfo;
        }
        final List<String> supplierCodeList = records.stream()
                .map(SupplierVo::getSupplierCode)
                .collect(Collectors.toList());

        // 查询生效的收款账号
        final List<SupplierPaymentAccountPo> supplierPaymentAccountPoList = supplierPaymentAccountDao.getListBySupplierCodeList(supplierCodeList);
        final Map<String, List<SupplierPaymentAccountPo>> supplierPaymentAccountPoMap = supplierPaymentAccountPoList.stream()
                .filter(supplierPaymentAccountPo -> SupplierPaymentAccountStatus.EFFECTIVE.equals(supplierPaymentAccountPo.getSupplierPaymentAccountStatus()))
                .collect(Collectors.groupingBy(SupplierPaymentAccountPo::getSupplierCode));

        // 查询主体信息
        List<SupplierSubjectPo> supplierSubjectPoList = supplierSubjectDao.getListBySupplierCodeList(supplierCodeList);
        final Map<String, List<SupplierSubjectPo>> supplierSubjectPoMap = supplierSubjectPoList.stream()
                .collect(Collectors.groupingBy(SupplierSubjectPo::getSupplierCode));

        records.forEach(record -> {
            if (supplierPaymentAccountPoMap.containsKey(record.getSupplierCode())) {
                record.setSupplierPayAccountBinding(BooleanType.TRUE);
            } else {
                record.setSupplierPayAccountBinding(BooleanType.FALSE);
            }
            List<SupplierSubjectPo> supplierSubjectPos = supplierSubjectPoMap.get(record.getSupplierCode());
            if (CollectionUtils.isNotEmpty(supplierSubjectPos)) {
                record.setSupplierSubjectMaintain(BooleanType.TRUE);
                List<SupplierVo.SupplierSearchSubjectVo> supplierSearchSubjectVoList = supplierSubjectPos.stream().map(supplierSubjectPo -> {
                    SupplierVo.SupplierSearchSubjectVo supplierSearchSubjectVo = new SupplierVo.SupplierSearchSubjectVo();
                    supplierSearchSubjectVo.setSubject(supplierSubjectPo.getSubject());
                    return supplierSearchSubjectVo;
                }).collect(Collectors.toList());
                record.setSupplierSearchSubjectList(supplierSearchSubjectVoList);
            } else {
                record.setSupplierSubjectMaintain(BooleanType.FALSE);
            }
        });
        return pageInfo;
    }

    public SupplierDetailVo getSupplierDetail(SupplierDetailDto dto) {
        SupplierPo supplierDetail = supplierDao.getBySupplierCode(dto.getSupplierCode());
        if (supplierDetail == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        SupplierDetailVo detailVo = SupplierConverter.INSTANCE.poToDetail(supplierDetail);

        //收款账号信息列表
        List<SupplierPaymentAccountPo> supplierPaymentAccountPoList = supplierPaymentAccountDao.getListBySupplierCode(supplierDetail.getSupplierCode());

        List<Long> supplierPaymentAccountIdList = supplierPaymentAccountPoList.stream()
                .map(SupplierPaymentAccountPo::getSupplierPaymentAccountId)
                .collect(Collectors.toList());
        // 获取收款账号的图片
        Map<Long, List<String>> fileCodePersonalMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SUPPLIER_PAYMENT_PERSONAL, supplierPaymentAccountIdList);
        Map<Long, List<String>> fileCodeAuthMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SUPPLIER_PAYMENT_AUTH, supplierPaymentAccountIdList);
        Map<Long, List<String>> fileCodeCompanyMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SUPPLIER_PAYMENT_COMPANY, supplierPaymentAccountIdList);
        detailVo.setSupplierPaymentAccountList(SupplierPaymentAccountConverter.poListToVoList(supplierPaymentAccountPoList, fileCodePersonalMap, fileCodeAuthMap, fileCodeCompanyMap));

        // 主体信息列表
        List<SupplierSubjectPo> supplierSubjectPoList = supplierSubjectDao.getListBySupplierCode(supplierDetail.getSupplierCode());
        List<Long> supplierSubjectIdList = supplierSubjectPoList.stream()
                .map(SupplierSubjectPo::getSupplierSubjectId)
                .collect(Collectors.toList());
        // 获取主体的图片
        Map<Long, List<String>> businessFileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SUPPLIER_SUBJECT_LICENSE, supplierSubjectIdList);
        detailVo.setSupplierSubjectList(SupplierSubjectConverter.poListToVoList(supplierSubjectPoList, businessFileCodeMap));

        return detailVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public SupplierAddVo addSupplier(SupplierAddDto dto) {
        final List<SupplierPo> supplierPoList = supplierDao.getSupplierByName(dto.getSupplierName());
        if (CollectionUtils.isNotEmpty(supplierPoList)) {
            throw new ParamIllegalException("供应商名称已被其他供应商使用，请重新输入");
        }

        // 校验入驻时间
        ScmTimeUtil.checkDateAfterNow(Collections.singletonList(dto.getJoinTime()));

        SupplierPo supplierPo = supplierDao.getBySupplierCode(dto.getSupplierCode());
        if (supplierPo != null) {
            throw new ParamIllegalException("供应商代码已被其他供应商使用，请重新输入");
        }

        SupplierPo supplierAliasPo = supplierDao.getBySupplierAlias(dto.getSupplierAlias());
        if (supplierAliasPo != null) {
            throw new ParamIllegalException("供应商别称已被其他供应商使用，请重新输入");
        }

        SupplierPo insertSupplierPo = SupplierConverter.INSTANCE.create(dto);

        UserCodeDto userCodeDto = new UserCodeDto();
        userCodeDto.setUserCode(dto.getDevUser());
        CommonResult<UserVo> byUserCode = userService.getByUserCode(userCodeDto);
        if (byUserCode != null && byUserCode.getData() != null) {
            insertSupplierPo.setDevUsername(byUserCode.getData().getUsername());
        }


        UserCodeDto userFollowCodeDto = new UserCodeDto();
        userFollowCodeDto.setUserCode(dto.getFollowUser());
        CommonResult<UserVo> userFollow = userService.getByUserCode(userFollowCodeDto);
        if (userFollow != null && userFollow.getData() != null) {
            insertSupplierPo.setFollowUsername(userFollow.getData().getUsername());
        }
        insertSupplierPo.setSupplierStatus(SupplierStatus.ENABLED);
        supplierDao.insert(insertSupplierPo);

        //创建收款账号
        if (CollectionUtils.isNotEmpty(dto.getSupplierPaymentAccountList())) {
            // 验证收款账号与主体关系是否存在
            List<String> subjectList = Optional.ofNullable(dto.getSupplierSubjectList())
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(SupplierSubjectCreateDto::getSubject)
                    .distinct()
                    .collect(Collectors.toList());
            Map<String, List<String>> subjectSupplierCodeMap = new HashMap<>();
            subjectSupplierCodeMap.put(dto.getSupplierCode(), subjectList);
            supplierPaymentAccountBaseService.checkPaymentAccountSubjectInfo(dto.getSupplierPaymentAccountList(), subjectSupplierCodeMap);

            supplierPaymentAccountBaseService.createAccount(dto.getSupplierPaymentAccountList());
        }

        //创建主体信息
        supplierSubjectBaseService.createSubject(dto.getSupplierSubjectList());


        //调用udb创建供应商
        SupplierUpdateDto supplierUpdateDto = new SupplierUpdateDto();
        supplierUpdateDto.setSupplierCode(dto.getSupplierCode());
        supplierUpdateDto.setSupplierName(dto.getSupplierName());
        supplierUpdateDto.setState(EnableStateEnum.ENABLED);
        supplierAdminRemoteService.insertUpdateSupplier(supplierUpdateDto);
        SupplierAddVo supplierAddVo = new SupplierAddVo();
        supplierAddVo.setSupplierCode(supplierUpdateDto.getSupplierCode());

        supplierWarehouseBaseService.createWarehouse(insertSupplierPo.getSupplierCode(), insertSupplierPo.getSupplierName());

        return supplierAddVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean editSupplier(SupplierEditDto dto) {
        SupplierPo supplierPo = supplierDao.getBySupplierCodeAndVersion(dto.getSupplierCode(), dto.getVersion());
        if (null == supplierPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        final List<SupplierPo> supplierPoList = supplierDao.getSupplierByName(dto.getSupplierName(), dto.getSupplierCode());
        if (CollectionUtils.isNotEmpty(supplierPoList)) {
            throw new ParamIllegalException("供应商名字已被其他供应商使用，请修改供应商名称再操作");
        }
        final List<SupplierPo> supplierPoAliasList = supplierDao.getSupplierByAliasNotCode(dto.getSupplierAlias(), dto.getSupplierCode());
        if (CollectionUtils.isNotEmpty(supplierPoAliasList)) {
            throw new ParamIllegalException("供应商别称已被其他供应商使用，请修改供应商别称再操作");
        }
        SupplierPo updateSupplierPo = SupplierConverter.INSTANCE.edit(dto);

        UserCodeDto userCodeDto = new UserCodeDto();
        userCodeDto.setUserCode(dto.getDevUser());
        CommonResult<UserVo> byUserCode = userService.getByUserCode(userCodeDto);
        if (byUserCode != null && byUserCode.getData() != null) {
            updateSupplierPo.setDevUsername(byUserCode.getData().getUsername());
        }


        UserCodeDto userFollowCodeDto = new UserCodeDto();
        userFollowCodeDto.setUserCode(dto.getFollowUser());
        CommonResult<UserVo> userFollow = userService.getByUserCode(userFollowCodeDto);
        if (userFollow != null && userFollow.getData() != null) {
            updateSupplierPo.setFollowUsername(userFollow.getData().getUsername());
        }


        updateSupplierPo.setSupplierId(supplierPo.getSupplierId());
        supplierDao.updateByIdVersion(updateSupplierPo);


        //创建收款账号
        if (CollectionUtils.isNotEmpty(dto.getSupplierPaymentAccountList())) {
            // 检验主体信息
            List<String> subjectList = Optional.ofNullable(dto.getSupplierSubjectList())
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(SupplierSubjectEditDto::getSubject)
                    .distinct()
                    .collect(Collectors.toList());
            Map<String, List<String>> subjectSupplierCodeMap = new HashMap<>();
            subjectSupplierCodeMap.put(dto.getSupplierCode(), subjectList);
            supplierPaymentAccountBaseService.checkPaymentAccountSubjectInfo(dto.getSupplierPaymentAccountList(), subjectSupplierCodeMap);

            // 创建收款账号
            List<SupplierPaymentAccountCreateDto> filterPaymentAccountList = dto.getSupplierPaymentAccountList().stream()
                    .filter(accountDto -> null == accountDto.getSupplierPaymentAccountId())
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(filterPaymentAccountList)) {
                supplierPaymentAccountBaseService.createAccount(filterPaymentAccountList);
            }

        }

        //编辑主体信息
        supplierSubjectBaseService.editSubject(dto.getSupplierSubjectList(), dto.getSupplierCode());

        //调用udb更新供应商
        SupplierUpdateDto supplierUpdateDto = new SupplierUpdateDto();
        supplierUpdateDto.setSupplierCode(dto.getSupplierCode());
        supplierUpdateDto.setSupplierName(dto.getSupplierName());
        supplierUpdateDto.setState(supplyRefService.getEnableStateEnumByStatus(supplierPo.getSupplierStatus()));
        supplierAdminRemoteService.insertUpdateSupplier(supplierUpdateDto);

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean openClose(SupplierVersionDto dto) {
        SupplierPo supplierPo = supplierDao.getBySupplierCodeAndVersion(dto.getSupplierCode(), dto.getVersion());
        if (null == supplierPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        SupplierPo updateSupplierPo = new SupplierPo();
        updateSupplierPo.setSupplierId(supplierPo.getSupplierId());
        updateSupplierPo.setVersion(supplierPo.getVersion());

        SupplierStatus supplierStatus = supplierPo.getSupplierStatus();
        switch (dto.getSupplierStatus()) {
            case ENABLED:
                SupplierStatus open = supplierStatus.toOpen();
                updateSupplierPo.setSupplierStatus(open);
                break;
            case DISABLED:
                SupplierStatus close = supplierStatus.toClose();
                updateSupplierPo.setSupplierStatus(close);
                break;
        }
        supplierDao.updateByIdVersion(updateSupplierPo);


        //调用udb创建供应商
        SupplierUpdateDto supplierUpdateDto = new SupplierUpdateDto();
        supplierUpdateDto.setSupplierCode(supplierPo.getSupplierCode());
        supplierUpdateDto.setSupplierName(supplierPo.getSupplierName());
        supplierUpdateDto.setState(supplyRefService.getEnableStateEnumByStatus(dto.getSupplierStatus()));
        supplierAdminRemoteService.insertUpdateSupplier(supplierUpdateDto);

        //日志
        createStatusChangeLog(updateSupplierPo, supplierPo.getSupplierCode());

        return true;
    }

    public List<SupplierSimpleVo> getSimpleSupplierMsg() {
        final List<SupplierPo> supplierPoList = supplierDao.getAllSupplierList();

        return supplierPoList.stream()
                .map(po -> {
                    final SupplierSimpleVo supplierSimplerVo = new SupplierSimpleVo();
                    supplierSimplerVo.setSupplierCode(po.getSupplierCode());
                    supplierSimplerVo.setSupplierName(po.getSupplierName());
                    supplierSimplerVo.setCapacity(po.getCapacity());
                    supplierSimplerVo.setJoinTime(po.getJoinTime());
                    return supplierSimplerVo;
                }).collect(Collectors.toList());
    }

    /**
     * 导入供应商（禁止使用,开启时需要调整创建的逻辑）
     *
     * @author ChenWenLong
     * @date 2022/12/5 16:33
     */
    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public void importData(@Valid SupplierImportationDto.ImportationDetail dto) {
        if (StringUtils.isNotBlank(dto.getSupplierCode())) {
            throw new BizException("导入供应商失败，系统已关闭供应商导入功能，请联系系统管理员");
        }
        List<SupplierPo> supplierPoList = supplierDao.getSupplierByCodeOrName(dto.getSupplierCode(), dto.getSupplierName());

        if (CollectionUtils.isNotEmpty(supplierPoList)) {
            throw new ParamIllegalException("创建供应商失败，失败原因：供应商代码或供应商名称已经存在");
        }

        SupplierPo supplierAliasPo = supplierDao.getBySupplierAlias(dto.getSupplierAlias());

        if (null != supplierAliasPo) {
            throw new ParamIllegalException("创建供应商失败，失败原因：供应商别名已经存在");
        }

        SupplierPo supplierPoNew = SupplierConverter.INSTANCE.convert(dto);

        if (StringUtils.isNotBlank(supplierPoNew.getFollowUsername())) {
            UsernameListDto usernameListDto = new UsernameListDto();
            List<String> usernameList = new ArrayList<>();
            usernameList.add(supplierPoNew.getFollowUsername());
            usernameListDto.setUsernameList(usernameList);
            CommonResult<ResultList<UserCodeNameVo>> resultListCommonResult = userService.listDingTalkUserByUsername(usernameListDto);
            ResultList<UserCodeNameVo> data = DubboResponseUtil.checkCodeAndGetData(resultListCommonResult);
            Optional.ofNullable(data)
                    .map(ResultList::getList)
                    .orElse(Collections.emptyList())
                    .forEach(item -> supplierPoNew.setFollowUser(item.getUserCode()));

            if (StringUtils.isBlank(supplierPoNew.getFollowUser())) {
                throw new ParamIllegalException("创建供应商失败，失败原因：用户名称 {} 查询不到对应系统用户", dto.getFollowUsername());
            }
        }

        if (StringUtils.isNotBlank(supplierPoNew.getDevUsername())) {
            UsernameListDto usernameListDto = new UsernameListDto();
            List<String> usernameList = new ArrayList<>();
            usernameList.add(supplierPoNew.getDevUsername());
            usernameListDto.setUsernameList(usernameList);
            CommonResult<ResultList<UserCodeNameVo>> resultListCommonResult = userService.listDingTalkUserByUsername(usernameListDto);
            ResultList<UserCodeNameVo> data = DubboResponseUtil.checkCodeAndGetData(resultListCommonResult);
            Optional.ofNullable(data)
                    .map(ResultList::getList)
                    .orElse(Collections.emptyList())
                    .forEach(item -> supplierPoNew.setDevUser(item.getUserCode()));
            if (StringUtils.isBlank(supplierPoNew.getDevUser())) {
                throw new ParamIllegalException("创建供应商失败，失败原因：用户名称 {} 查询不到对应系统用户", dto.getDevUsername());
            }
        }

        supplierPoNew.setSupplierStatus(SupplierStatus.ENABLED);
        supplierDao.insert(supplierPoNew);

        //创建账号信息
        if (StringUtils.isNotBlank(dto.getAccount())) {
            SupplierAccountPo supplierAccountPo = new SupplierAccountPo();
            supplierAccountPo.setNetworkAddress(dto.getNetworkAddress());
            supplierAccountPo.setRegistrationPeople(dto.getRegistrationPeople());
            supplierAccountPo.setAccount(dto.getAccount());
            supplierAccountPo.setAccountBank(dto.getAccountBank());
            supplierAccountPo.setSupplierCode(dto.getSupplierCode());
            supplierAccountPo.setSupplierName(dto.getSupplierName());
            supplierAccountDao.insert(supplierAccountPo);
        }

        //创建联系人信息
        if (StringUtils.isNotBlank(dto.getName())) {
            SupplierContactPo supplierContactPo = new SupplierContactPo();
            supplierContactPo.setName(dto.getName());
            supplierContactPo.setPhone(dto.getPhone());
            supplierContactPo.setPosition(dto.getPosition());
            supplierContactPo.setRemarks(dto.getSupplierContactRemarks());
            supplierContactPo.setSupplierCode(dto.getSupplierCode());
            supplierContactPo.setSupplierName(dto.getSupplierName());
            supplierContactDao.insert(supplierContactPo);
        }

        //调用udb创建供应商
        SupplierUpdateDto supplierUpdateDto = new SupplierUpdateDto();
        supplierUpdateDto.setSupplierCode(dto.getSupplierCode());
        supplierUpdateDto.setSupplierName(dto.getSupplierName());
        supplierUpdateDto.setState(EnableStateEnum.ENABLED);
        supplierAdminRemoteService.insertUpdateSupplier(supplierUpdateDto);


    }

    /**
     * 日志
     *
     * @param supplierPo
     * @param supplierCode
     */
    public void createStatusChangeLog(SupplierPo supplierPo, String supplierCode) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.SUPPLIER_STATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.SUPPLIER_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.SUPPLIER_STATUS.name());
        bizLogCreateMqDto.setBizCode(supplierCode);
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(GlobalContext.getUserKey());
        bizLogCreateMqDto.setOperateUsername(GlobalContext.getUsername());

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();

        bizLogCreateMqDto.setContent(supplierPo.getSupplierStatus().getName());

        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    /**
     * 根据当前登录用户下拉获取供应商
     *
     * @author ChenWenLong
     * @date 2023/2/14 13:48
     */
    public CommonPageResult.PageInfo<SupplierQuickSearchVo> getSupplierQuickSearch(SupplierQuickSearchDto dto) {
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtil.isNotEmpty(supplierCodeList)) {
            dto.setSupplierCodeList(supplierCodeList);
        } else {
            return new CommonPageResult.PageInfo<>();
        }
        return supplierDao.getSupplierQuickSearch(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
    }

    /**
     * PLM获取供应商列表
     *
     * @param dto:
     * @return List<SupplierSearchVo>
     * @author ChenWenLong
     * @date 2023/9/8 14:35
     */
    public List<SupplierSearchVo> getSupplierSearch(SupplierSearchDto dto) {
        final List<SupplierPo> supplierPoList = supplierDao.getByTypeList(dto.getSupplierTypeList());

        return supplierPoList.stream()
                .map(po -> {
                    final SupplierSearchVo supplierSearchVo = new SupplierSearchVo();
                    supplierSearchVo.setSupplierCode(po.getSupplierCode());
                    supplierSearchVo.setSupplierName(po.getSupplierName());
                    return supplierSearchVo;
                }).collect(Collectors.toList());
    }

    /**
     * 批量导入供应商主体
     *
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void importSupplierSubject(SupplierSubjectImportationDto dto) {
        String supplierCode = dto.getSupplierCode();
        String supplierSubjectTypeStr = dto.getSupplierSubjectType();
        String subject = dto.getSubject();
        String legalPerson = dto.getLegalPerson();
        String contactsName = dto.getContactsName();
        String contactsPhone = dto.getContactsPhone();
        String registerMoney = dto.getRegisterMoney();
        String businessScope = dto.getBusinessScope();
        String businessAddress = dto.getBusinessAddress();
        String creditCode = dto.getCreditCode();
        String supplierExportStr = dto.getSupplierExport();
        String supplierInvoicingStr = dto.getSupplierInvoicing();
        String taxPointStr = dto.getTaxPoint();

        // 验证不能为空的情况
        if (StringUtils.isBlank(supplierCode)) {
            throw new ParamIllegalException("供应商代码不能为空");
        }
        if (StringUtils.isBlank(supplierSubjectTypeStr)) {
            throw new ParamIllegalException("主体类型不能为空");
        }
        if (StringUtils.isBlank(subject)) {
            throw new ParamIllegalException("公司名称不能为空");
        }
        if (StringUtils.isBlank(legalPerson)) {
            throw new ParamIllegalException("法人不能为空");
        }
        if (StringUtils.isBlank(contactsName)) {
            throw new ParamIllegalException("联系人不能为空");
        }
        if (StringUtils.isBlank(contactsPhone)) {
            throw new ParamIllegalException("联系电话不能为空");
        }
        if (StringUtils.isBlank(registerMoney)) {
            throw new ParamIllegalException("注册资金不能为空");
        }
        if (StringUtils.isBlank(businessScope)) {
            throw new ParamIllegalException("经营范围不能为空");
        }
        if (StringUtils.isBlank(businessAddress)) {
            throw new ParamIllegalException("经营地址不能为空");
        }

        supplierCode = supplierCode.trim();
        supplierSubjectTypeStr = supplierSubjectTypeStr.trim();
        subject = subject.trim();
        legalPerson = legalPerson.trim();
        contactsName = contactsName.trim();
        contactsPhone = contactsPhone.trim();
        registerMoney = registerMoney.trim();
        businessScope = businessScope.trim();
        businessAddress = businessAddress.trim();

        SupplierPo supplierPo = supplierDao.getBySupplierCode(supplierCode);
        if (null == supplierPo) {
            throw new ParamIllegalException("供应商代码:{}，查询不到对应供应商的信息", supplierCode);
        }
        if (!SupplierStatus.ENABLED.equals(supplierPo.getSupplierStatus())) {
            throw new ParamIllegalException("供应商代码:{}，状态需要{}才能进行导入", supplierCode, SupplierStatus.ENABLED.getRemark());
        }
        SupplierSubjectType supplierSubjectType = SupplierSubjectType.stringConvert(supplierSubjectTypeStr);
        if (supplierSubjectType == null) {
            throw new ParamIllegalException("主体类型填写错误，请填写个人或企业");
        }

        if (subject.length() > 255) {
            throw new ParamIllegalException("公司名称字符长度不能超过 255 位");
        }
        SupplierSubjectPo supplierSubjectPo = supplierSubjectDao.getBySubject(subject);
        if (null != supplierSubjectPo) {
            throw new ParamIllegalException("创建失败，失败原因：公司名称不能重复");
        }

        if (legalPerson.length() > 255) {
            throw new ParamIllegalException("法人字符长度不能超过 255 位");
        }
        if (contactsName.length() > 255) {
            throw new ParamIllegalException("联系人字符长度不能超过 255 位");
        }
        if (!StringUtil.isValidPhoneNumber(contactsPhone)) {
            throw new ParamIllegalException("联系电话为数值，固定长度11位，请调整后再导入");
        }

        if (registerMoney.length() > 255) {
            throw new ParamIllegalException("注册资金字符长度不能超过 255 位");
        }
        if (businessScope.length() > 255) {
            throw new ParamIllegalException("经营范围字符长度不能超过 255 位");
        }
        if (businessAddress.length() > 255) {
            throw new ParamIllegalException("经营地址字符长度不能超过 255 位");
        }

        if (StringUtils.isNotBlank(creditCode)) {
            creditCode = creditCode.trim();
            if (!StringUtil.isValidNumberEnglishString(creditCode)) {
                throw new ParamIllegalException("社会信用代码限制阿拉伯数字或大写英文字母，请调整后再导入");
            }
            if (creditCode.length() > 18) {
                throw new ParamIllegalException("社会信用代码字符长度不能超过 18 位");
            }
        }
        BooleanType supplierExport = null;
        if (StringUtils.isNotBlank(supplierExportStr)) {
            supplierExportStr = supplierExportStr.trim();
            if (!(supplierExportStr.equals(BooleanType.TRUE.getValue())
                    || supplierExportStr.equals(BooleanType.FALSE.getValue()))) {
                throw new ParamIllegalException("进出口资质填写错误，请填写是或否");
            }
            supplierExport = BooleanType.getTypeByValue(supplierExportStr);
        }
        BooleanType supplierInvoicing = null;
        if (StringUtils.isNotBlank(supplierInvoicingStr)) {
            supplierInvoicingStr = supplierInvoicingStr.trim();
            if (!(supplierInvoicingStr.equals(BooleanType.TRUE.getValue())
                    || supplierInvoicingStr.equals(BooleanType.FALSE.getValue()))) {
                throw new ParamIllegalException("开票资质填写错误，请填写是或否");
            }
            supplierInvoicing = BooleanType.getTypeByValue(supplierInvoicingStr);
        }

        BigDecimal taxPoint = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(taxPointStr)) {
            taxPointStr = taxPointStr.trim();
            taxPoint = ScmFormatUtil.bigDecimalFormat(taxPointStr);
            if (null == taxPoint) {
                throw new ParamIllegalException("税点必须填写正确数值，请调整后再导入");
            }
            int integerPartLength = taxPoint.precision() - taxPoint.scale();
            if (integerPartLength > 6) {
                throw new ParamIllegalException("税点限制数值，长度6位，请调整后再导入");
            }
        }

        SupplierSubjectPo insertSupplierSubjectPo = new SupplierSubjectPo();
        insertSupplierSubjectPo.setSupplierCode(supplierCode);
        insertSupplierSubjectPo.setSupplierSubjectType(supplierSubjectType);
        insertSupplierSubjectPo.setSubject(subject);
        insertSupplierSubjectPo.setLegalPerson(legalPerson);
        insertSupplierSubjectPo.setContactsName(contactsName);
        insertSupplierSubjectPo.setContactsPhone(contactsPhone);
        insertSupplierSubjectPo.setRegisterMoney(registerMoney);
        insertSupplierSubjectPo.setBusinessScope(businessScope);
        insertSupplierSubjectPo.setBusinessAddress(businessAddress);
        insertSupplierSubjectPo.setCreditCode(creditCode);
        insertSupplierSubjectPo.setSupplierExport(supplierExport);
        insertSupplierSubjectPo.setSupplierInvoicing(supplierInvoicing);
        insertSupplierSubjectPo.setTaxPoint(taxPoint);
        supplierSubjectDao.insert(insertSupplierSubjectPo);

    }

    /**
     * 通过SKU获取供应商推荐列表
     *
     * @param requestDto:
     * @return List<SkuGetSuggestSupplierVo>
     * @author ChenWenLong
     * @date 2024/8/6 14:42
     */
    public List<SkuGetSuggestSupplierVo> getDefaultSupplierList(SkuGetSuggestSupplierDto requestDto) {
        List<SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto> dtoList = Optional.ofNullable(requestDto.getSkuAndBusinessIdBatchList())
                .orElse(new ArrayList<>());
        AbstractSkuGetSuggestSupplierHandler handlerChain = skuGetSuggestSupplierHandlerFactory.createHandlerChain();

        // 默认初始化数据
        SkuGetSuggestSupplierBo defaultSupplier = new SkuGetSuggestSupplierBo();
        List<SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo> skuGetSuggestSupplierBoList = dtoList.stream().map(dto -> {
            SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo skuGetSuggestSupplierListBo = new SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo();
            skuGetSuggestSupplierListBo.setBusinessId(dto.getBusinessId());
            skuGetSuggestSupplierListBo.setSkuGetSuggestSupplierItemBoList(new ArrayList<>());
            return skuGetSuggestSupplierListBo;
        }).collect(Collectors.toList());
        defaultSupplier.setSkuGetSuggestSupplierBoList(skuGetSuggestSupplierBoList);

        // 通过责任链模式，依次处理数据返回结果
        SkuGetSuggestSupplierBo resultBo = handlerChain.handle(dtoList, defaultSupplier);
        log.info("推荐供应商计算最终结果Bo={}", JSON.toJSONString(resultBo));
        // 组装返回结果VO
        List<SkuGetSuggestSupplierVo> resultVoList = new ArrayList<>();
        Optional.ofNullable(resultBo.getSkuGetSuggestSupplierBoList())
                .orElse(new ArrayList<>())
                .forEach(result -> {
                    SkuGetSuggestSupplierVo vo = new SkuGetSuggestSupplierVo();
                    vo.setBusinessId(result.getBusinessId());

                    List<SkuGetSuggestSupplierVo.SkuGetSuggestSupplierItemVo> skuGetSuggestSupplierItemList = Optional.ofNullable(result.getSkuGetSuggestSupplierItemAllBoList())
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(bo -> {
                                SkuGetSuggestSupplierVo.SkuGetSuggestSupplierItemVo itemVo = new SkuGetSuggestSupplierVo.SkuGetSuggestSupplierItemVo();
                                // 初始化默认值
                                itemVo.setIsDefault(BooleanType.FALSE);
                                // 查询是否存在是默认推荐
                                result.getSkuGetSuggestSupplierItemBoList()
                                        .stream()
                                        .filter(itemBo -> itemBo.getSupplierCode().equals(bo.getSupplierCode()))
                                        .findFirst()
                                        .ifPresent(skuGetSuggestSupplierItemBo -> itemVo.setIsDefault(skuGetSuggestSupplierItemBo.getIsDefault()));
                                itemVo.setSupplierCode(bo.getSupplierCode());
                                itemVo.setSupplierAlias(bo.getSupplierAlias());
                                return itemVo;
                            }).collect(Collectors.toList());
                    vo.setSkuGetSuggestSupplierItemList(skuGetSuggestSupplierItemList);

                    resultVoList.add(vo);
                });
        log.info("推荐供应商返回WMS的Vo={}", JSON.toJSONString(resultVoList));
        return resultVoList;
    }

    public PurchasePreOrderVo queryPurchasePreOrderList(PurchasePreOrderDto dto) {
        //基础校验：校验业务ID是否重复&校验预计上架日期需大于等于今天
        List<PurchasePreOrderDto.PreOrderInfoDto> preOrderInfoDtoList = dto.getPreOrderInfoDtoList();
        if (CollectionUtils.isNotEmpty(preOrderInfoDtoList)) {
            List<Long> businessIdList = preOrderInfoDtoList.stream().map(PurchasePreOrderDto.PreOrderInfoDto::getBusinessId).collect(Collectors.toList());
            if (businessIdList.size() != businessIdList.stream().distinct().count()) {
                throw new ParamIllegalException("业务ID重复！请调整后再查询。");
            }
        }
        preOrderInfoDtoList.forEach(preOrderInfoDto -> {
            LocalDate preShelfDate = preOrderInfoDto.getLatestOnShelfTime();
            if (preShelfDate.isBefore(LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId())))) {
                throw new ParamIllegalException("预计上架日期需大于等于今天！请调整后再查询。");
            }
        });

        // 初始化返回的数据
        PurchasePreOrderVo resultVo = SupplierBuilder.buildPurchasePreOrderVo(dto);
        List<PurchasePreOrderVo.PurchasePreOrderInfoVo> resultInfoVoList = resultVo.getPreOrderInfoVoList();

        // 计算预计上架日期&判断产能原料是否满足
        CalPreShelfTimeBo calPreShelfTimeBo = this.doCalPreShelfTime(preOrderInfoDtoList);
        Queue<ResBo> resQueue = calPreShelfTimeBo.getResQueue();

        //根据计算结果填充数据
        fillResFromCalculationQueue(resQueue, resultInfoVoList);

        // 获取原料库存是否满足的信息
        List<ProduceDataGetCapacityVo> getRawInventoryVoList = this.getRawInventoryData(dto);
        log.info("获取原料库存是否满足的信息Vo=>{}", JSON.toJSONString(getRawInventoryVoList));

        //根据计算结果填充数据
        fillResFromRawInventory(getRawInventoryVoList, resultInfoVoList);

        //填充最终查询结果
        fillQueryResult(resultVo);
        return resultVo;
    }

    private PurchasePreOrderVo fillQueryResult(PurchasePreOrderVo resVo) {
        List<PurchasePreOrderVo.PurchasePreOrderInfoVo> preOrderInfoVoList = resVo.getPreOrderInfoVoList();
        for (PurchasePreOrderVo.PurchasePreOrderInfoVo preOrderInfoVo : preOrderInfoVoList) {
            String capacityFailReason = preOrderInfoVo.getCapacityFailReason();
            String materialFailReason = preOrderInfoVo.getMaterialFailReason();
            if (StrUtil.isNotBlank(capacityFailReason) || StrUtil.isNotBlank(materialFailReason)) {
                preOrderInfoVo.setQueryResult(BooleanType.FALSE);
            } else {
                preOrderInfoVo.setQueryResult(BooleanType.TRUE);
            }
        }
        return resVo;
    }

    private void fillResFromRawInventory(List<ProduceDataGetCapacityVo> getRawInventoryVoList, List<PurchasePreOrderVo.PurchasePreOrderInfoVo> voList) {
        Optional.ofNullable(voList)
                .orElse(new ArrayList<>())
                .forEach(preOrderInfoVo -> Optional.ofNullable(getRawInventoryVoList)
                        .orElse(new ArrayList<>())
                        .stream()
                        .filter(getRawInventoryVo -> getRawInventoryVo.getBusinessId().equals(preOrderInfoVo.getBusinessId()))
                        .findFirst()
                        .ifPresent(getRawInventoryVo -> {
                            preOrderInfoVo.setIsMaterialStockSatisfy(IsMaterialStockSatisfy.UNSATISFIED);
                            if (BooleanType.TRUE.equals(getRawInventoryVo.getIsContentment())) {
                                preOrderInfoVo.setIsMaterialStockSatisfy(IsMaterialStockSatisfy.SATISFIED);
                            }
                            // 以下赋值结果和失败原因逻辑
                            // 供应商产能处理结果：成功，原料库存处理结果：成功  最终结果：成功
                            // 供应商产能处理结果：成功，原料库存处理结果：失败  最终结果：失败，失败原因：原料库存失败原因
                            // 供应商产能处理结果：失败，原料库存处理结果：成功  最终结果：失败，失败原因：供应商产能失败原因
                            // 供应商产能处理结果：失败，原料库存处理结果：失败  最终结果：失败，失败原因：供应商产能失败原因+原料库存失败原因
                            if (BooleanType.FALSE.equals(getRawInventoryVo.getResult())) {
                                preOrderInfoVo.setMaterialFailReason(getRawInventoryVo.getFailReason());
                            }

                        }));
    }

    private void fillResFromCalculationQueue(Queue<ResBo> resQueue, List<PurchasePreOrderVo.PurchasePreOrderInfoVo> voList) {
        voList.forEach(vo -> resQueue.stream().filter(resBo -> resBo.getBusinessId().equals(vo.getBusinessId()))
                .findFirst().ifPresent(resBo -> {
                    vo.setPreShelfTime(resBo.getPreShelfTime());
                    vo.setFailReason(resBo.getFailReason());
                    vo.setIsCapacitySatisfy(resBo.getIsCapacitySatisfy());
                    vo.setCapacityFailReason(resBo.getFailReason());
                }));
    }

    private CalPreShelfTimeBo doCalPreShelfTime(List<PurchasePreOrderDto.PreOrderInfoDto> preOrderInfoDtoList) {
        CalPreShelfTimeBo calPreShelfTimeBo = SupplierCapacityBuilder.buildCalPreShelfTimeBo(preOrderInfoDtoList);
        // 计算预处理
        log.info("步骤一：开始数据预处理与筛选=>");
        filterAndDoPrepare(calPreShelfTimeBo);

        // 计算预计上架时间
        log.info("步骤二：开始计算预计上架时间以及判断产能是否满足=>");
        calPreShelfTime(calPreShelfTimeBo);
        return calPreShelfTimeBo;
    }

    private void calPreShelfTime(CalPreShelfTimeBo calPreShelfTimeBo) {
        if (calPreShelfTimeBo.isCompletedCal()) {
            log.info("计算结果已全部返回，无需继续计算！");
            return;
        }

        Queue<CalPreShelfTimeBo.ClaBo> calQueue = calPreShelfTimeBo.getCalQueue();
        Queue<CalPreShelfTimeBo.ResBo> resQueue = calPreShelfTimeBo.getResQueue();
        Map<String, BigDecimal> supplierCapacityMap = calPreShelfTimeBo.getSupplierCapacityMap();

        int size = calQueue.size();
        for (int i = 0; i < size; i++) {
            //定义原子序号
            AtomicInteger index = new AtomicInteger(1);
            CalPreShelfTimeBo.ClaBo calBo = calQueue.poll();
            Long businessId = calBo.getBusinessId();
            BigDecimal needTotalCapacity = calBo.getNeedTotalCapacity();
            LocalDate maxProduceDate = calBo.getMaxProduceDate();
            long skuProduceCycleDays = calBo.getSkuProduceCycleDays();
            long logisticsDays = calBo.getLogisticsDays();
            String supplierCode = calBo.getSupplierCode();

            //求区间内的总产能
            LocalDate l1 = maxProduceDate.minusDays(CAPACITY_CYCLE_DAY);
            log.info("businessId=>{},【数据统计】no.{}: 最晚生产完成时间=>{} 减去 =>{}天 结果=>{}", businessId, index.getAndIncrement(), maxProduceDate, CAPACITY_CYCLE_DAY, l1);

            LocalDate l2 = LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId())).plusDays(skuProduceCycleDays);
            log.info("businessId=>{},【数据统计】no.{}: 当前时间=>{} 加上生产周期 =>{}天 结果=>{}", businessId, index.getAndIncrement(), LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId())), skuProduceCycleDays, l2);

            LocalDate searchBeginDate = ScmTimeUtil.max(l1, l2);
            log.info("businessId=>{},【数据统计】no.{}: 求[{}~{}]最大值作为筛选区间内的总产能开始时间=>{}", businessId, index.getAndIncrement(), l1, l2, searchBeginDate);

            LocalDate searchEndDate = maxProduceDate;
            log.info("businessId=>{},【数据统计】no.{}: 求最晚生产完成时间作为筛选区间内总产能结束时间=>{}", businessId, index.getAndIncrement(), searchEndDate);

            BigDecimal totalCapacity = calTotalCapacity(supplierCode, searchBeginDate, searchEndDate, supplierCapacityMap);
            log.info("businessId=>{},【数据统计】no.{}: 求区间内的总产能结果(供应商代码=>{}，开始时间=>{}，结束时间=>{})=>{}",
                    businessId,
                    index.getAndIncrement(),
                    supplierCode, searchBeginDate, searchEndDate, totalCapacity);

            LocalDate deliveryDate;
            if (totalCapacity.compareTo(needTotalCapacity) >= 0 && !searchBeginDate.isAfter(searchEndDate)) {
                log.info("businessId=>{},【数据统计】no.{}: 初始匹配时间区间[{}~{}]内总产能满足", businessId, index.getAndIncrement(), searchBeginDate, searchEndDate);

                Deque<LocalDate> searchQueue = SupplierCapacityBuilder.buildDequeueDate(searchBeginDate, searchEndDate);
                deliveryDate = calDeliveryDate(supplierCode, searchQueue, supplierCapacityMap);
                //扣减产能
                deductionCapacity(supplierCode, deliveryDate, needTotalCapacity, supplierCapacityMap, index, businessId);
                LocalDate preShelfDate = calPreShelfDate(deliveryDate, logisticsDays, WARE_HOUSE_DAY, index, businessId);

                resQueue.add(new CalPreShelfTimeBo.ResBo(calBo.getBusinessId(), IsCapacitySatisfy.SATISFIED, preShelfDate));
            } else {
                log.info("businessId=>{},【数据统计】no.{}: 初始匹配时间区间[{}~{}]内总产能不满足", businessId, index.getAndIncrement(), searchBeginDate, searchEndDate);

                BigDecimal beforeTotalCapacity = calTotalCapacity(supplierCode, searchBeginDate, searchEndDate, supplierCapacityMap);
                log.info("businessId=>{},【数据统计】no.{}: 求和初次匹配[{}~{}]内总产能=>{}", businessId, index.getAndIncrement(), searchBeginDate, searchEndDate, beforeTotalCapacity);

                //求和时间区间内总产能
                searchBeginDate = maxProduceDate.plusDays(ONE_DAY);
                log.info("businessId=>{},【数据统计】no.{}: 筛选区间内总产能开始时间+{}天 变更成=>{}", businessId, index.getAndIncrement(), ONE_DAY, searchBeginDate);

                searchEndDate = maxProduceDate.plusDays(CAPACITY_COMPENSATION_DAY);
                log.info("businessId=>{},【数据统计】no.{}: 筛选区间内总产能结束时间+{}天变更成=>{}", businessId, index.getAndIncrement(), CAPACITY_COMPENSATION_DAY, searchEndDate);

                Deque<LocalDate> searchQueue = SupplierCapacityBuilder.buildDequeueDate(searchBeginDate, searchEndDate);
                deliveryDate = calDeliveryDate(supplierCode, beforeTotalCapacity, searchQueue, needTotalCapacity, supplierCapacityMap);
                if (Objects.nonNull(deliveryDate)) {
                    log.info("businessId=>{},【数据统计】no.{}: 在初次匹配区间基础上，叠加二次匹配时间区间[{}~{}]内总产能不满足但能扣减产能",
                            businessId, index.getAndIncrement(), searchBeginDate, searchEndDate);

                    //扣减产能
                    deductionCapacity(supplierCode, deliveryDate, needTotalCapacity, supplierCapacityMap, index, businessId);
                    LocalDate preShelfDate = calPreShelfDate(deliveryDate, logisticsDays, WARE_HOUSE_DAY, index, businessId);

                    resQueue.add(new CalPreShelfTimeBo.ResBo(calBo.getBusinessId(), IsCapacitySatisfy.UNSATISFIED, preShelfDate));
                } else {
                    log.info("businessId=>{},【数据统计】no.{}: 在初次匹配区间基础上，叠加二次匹配时间区间[{}~{}]内总产能不满足",
                            businessId, index.getAndIncrement(), searchBeginDate, searchEndDate);
                    resQueue.add(new CalPreShelfTimeBo.ResBo(calBo.getBusinessId(), IsCapacitySatisfy.UNSATISFIED));
                }
            }
        }
    }


    private void deductionCapacity(String supplierCode,
                                   LocalDate deliveryDate,
                                   BigDecimal deductionCapacity,
                                   Map<String, BigDecimal> supplierCapacityMap,
                                   AtomicInteger index,
                                   Long businessId) {
        supplierCapacityMap.put(genSupCapacityKey(supplierCode, deliveryDate),
                supplierCapacityMap.getOrDefault(genSupCapacityKey(supplierCode, deliveryDate), BigDecimal.ZERO).subtract(deductionCapacity));
        log.info("businessId=>{},【数据统计】no.{}: 扣减供应商日产能：供应商编码=>{}，产能日期=>{}，产能数{}",
                businessId, index.getAndIncrement(), supplierCode, deliveryDate, deductionCapacity);
    }


    private void filterAndDoPrepare(CalPreShelfTimeBo calPreShelfTimeBo) {
        List<PurchasePreOrderDto.PreOrderInfoDto> preOrderInfoDtoList = calPreShelfTimeBo.getPreOrderInfoDtoList();
        Queue<CalPreShelfTimeBo.ClaBo> calQueue = calPreShelfTimeBo.getCalQueue();
        Queue<CalPreShelfTimeBo.ResBo> resQueue = calPreShelfTimeBo.getResQueue();
        Map<String, BigDecimal> supplierCapacityMap = calPreShelfTimeBo.getSupplierCapacityMap();

        //查询本次需要参与计算所有SKU单件产能 & 查询本次需要参与计算所有SKU生产周期
        Set<String> skuSet = preOrderInfoDtoList.stream().map(PurchasePreOrderDto.PreOrderInfoDto::getSku).collect(Collectors.toSet());
        Map<String, BigDecimal> skuCapMap = this.getSkuCapMap(skuSet);
        Map<String, Long> skuProduceCycleMap = this.getProduceCycleMap(skuSet);

        //查询本次需要参与计算所有供应商物流时效&供应商产能规则
        Set<String> supplierCodeSet = preOrderInfoDtoList.stream().map(PurchasePreOrderDto.PreOrderInfoDto::getSupplierCode).collect(Collectors.toSet());
        Map<String, Long> supLogisticsMap = this.getSupLogisticsMap(supplierCodeSet);
        Map<String, BigDecimal> supCapRuleMap = supplierCapacityBaseService.getSupCapRuleMap(supplierCodeSet);

        for (PurchasePreOrderDto.PreOrderInfoDto preOrderInfoDto : preOrderInfoDtoList) {
            //定义原子序号
            AtomicInteger index = new AtomicInteger(1);

            //1. 数据校验
            //SKU尚未维护单件产能
            Long businessId = preOrderInfoDto.getBusinessId();
            log.info("businessId=>{},【数据预处理与筛选】no.{}:开始", businessId, index.getAndIncrement());

            String sku = preOrderInfoDto.getSku();
            BigDecimal skuSingleCapacity = skuCapMap.get(sku);
            if (null == skuSingleCapacity) {
                resQueue.offer(new ResBo(businessId, SKU_NOT_MAINTAIN_CAPACITY_FAIL_REASON));
                log.info("businessId=>{} 【数据预处理与筛选】no.{}:SKU=>{}尚未维护单件产能，请维护！", businessId, index.getAndIncrement(), sku);
                continue;
            }
            log.info("businessId=>{},【数据预处理与筛选】no.{}:SKU=>{},单件产能=>{}", businessId, index.getAndIncrement(), sku, skuSingleCapacity);

            //SKU信息缺失
            Long skuProduceCycleDays = skuProduceCycleMap.get(sku);
            if (null == skuProduceCycleDays) {
                resQueue.offer(new ResBo(businessId, SKU_INFO_MISSING_FAIL_REASON));
                log.info("businessId=>{},【数据预处理与筛选】no.{}:SKU=>{}信息缺失，请维护！", businessId, index.getAndIncrement(), sku);
                continue;
            }
            log.info("businessId=>{},【数据预处理与筛选】no.{}:SKU=>{},生产周期天数=>{}", businessId, index.getAndIncrement(), sku, skuProduceCycleDays);

            //供应商产能规则
            String supplierCode = preOrderInfoDto.getSupplierCode();
            if (null == supCapRuleMap.get(supplierCode)) {
                resQueue.offer(new ResBo(businessId, IsCapacitySatisfy.SATISFIED));
                log.info("businessId=>{},【数据预处理与筛选】no.{}:供应商=>{}尚未维护产能规则，返回满足！", businessId, index.getAndIncrement(), preOrderInfoDto.getSupplierCode());
                continue;
            }

            //计算SKU所需总产能
            Integer placeOrderCnt = preOrderInfoDto.getPlaceOrderCnt();
            BigDecimal needTotalCapacity = skuSingleCapacity.multiply(BigDecimal.valueOf(placeOrderCnt));
            log.info("businessId=>{},【数据预处理与筛选】no.{}: SKU=>{},下单数量=>{},所需总产能=>{}", businessId, index.getAndIncrement(), sku, placeOrderCnt, needTotalCapacity);

            //供应商物流时效
            long logisticsDays = supLogisticsMap.getOrDefault(supplierCode, 0L);
            log.info("businessId=>{},【数据预处理与筛选】no.{}:供应商=>{} 物流时效=>{}", businessId, index.getAndIncrement(), supplierCode, logisticsDays);

            //计算最晚生产完成时间
            LocalDate latestOnShelfTime = preOrderInfoDto.getLatestOnShelfTime();
            LocalDate maxProduceDate = calMaxProduceDate(latestOnShelfTime, logisticsDays, WARE_HOUSE_DAY);
            log.info("businessId=>{},【数据预处理与筛选】no.{}:最晚生产完成时间=>{}", businessId, index.getAndIncrement(), maxProduceDate);

            //填充供应商产能信息
            LocalDate loadingSupCapBeginDate = getLoadingSupCapacityBeginDate(maxProduceDate, skuProduceCycleDays, CAPACITY_CYCLE_DAY);
            LocalDate loadingSupCapEndDate = getLoadingSupCapacityEndDate(maxProduceDate, CAPACITY_COMPENSATION_DAY);
            List<SupplierCapacityBo> supplierCapacityBos
                    = supplierCapacityBaseService.listBySupCapWithDateRange(supplierCode, loadingSupCapBeginDate, loadingSupCapEndDate);
            for (LocalDate nowDate = loadingSupCapBeginDate; !nowDate.isAfter(loadingSupCapEndDate); nowDate = nowDate.plusDays(1)) {
                LocalDate finalNowDate = nowDate;
                BigDecimal normalAvailableCapacity = supplierCapacityBos.stream()
                        .filter(supplierCapacityBo -> supplierCapacityBo.getCapacityDate().equals(finalNowDate))
                        .map(SupplierCapacityBo::getNormalAvailableCapacity)
                        .findFirst()
                        .orElse(supCapRuleMap.get(supplierCode));
                supplierCapacityMap.put(genSupCapacityKey(supplierCode, nowDate), normalAvailableCapacity);
            }
            log.info("businessId=>{},【数据预处理与筛选】no.{}:预加载供应商=>{} [{}~{}]区间内每日产能=>{}",
                    businessId, index.getAndIncrement(),
                    supplierCode, loadingSupCapBeginDate, loadingSupCapEndDate, JSON.toJSONString(supplierCapacityMap));

            //放入需计算队列
            ClaBo claBo = new ClaBo(businessId, sku, skuProduceCycleDays, supplierCode, needTotalCapacity, logisticsDays, maxProduceDate);
            calQueue.offer(claBo);
            log.info("businessId=>{},【数据预处理与筛选】no.{}:放入需计算队列：sku=>{},sku生产周期天数=>{},供应商=>{},需要总产能=>{},供应商物流天数=>{},最晚生产完成时间=>{}",
                    businessId,
                    index.getAndIncrement(),
                    sku, skuProduceCycleDays, supplierCode, needTotalCapacity, logisticsDays, maxProduceDate);
        }
    }

    /**
     * 获取原料库存是否满足的信息
     *
     * @param dto:
     * @return List<ProduceDataGetCapacityVo>
     * @author ChenWenLong
     * @date 2024/8/12 15:01
     */
    public List<ProduceDataGetCapacityVo> getRawInventoryData(PurchasePreOrderDto dto) {
        // 判断是否为空
        List<PurchasePreOrderDto.PreOrderInfoDto> dtoList = Optional.ofNullable(dto.getPreOrderInfoDtoList())
                .orElse(new ArrayList<>());
        if (CollectionUtils.isEmpty(dtoList)) {
            return new ArrayList<>();
        }
        // dto转成produceDataGetCapacityDto(入参接口进行解耦，防止接口入参修改影响下面代码)
        ProduceDataGetCapacityDto produceDataGetCapacityDto = new ProduceDataGetCapacityDto();
        produceDataGetCapacityDto.setProduceDataGetCapacityItemList(dtoList.stream()
                .map(preOrderInfoDto -> {
                    ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto produceDataGetCapacityItemDto = new ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto();
                    produceDataGetCapacityItemDto.setBusinessId(preOrderInfoDto.getBusinessId());
                    produceDataGetCapacityItemDto.setPlaceOrderCnt(preOrderInfoDto.getPlaceOrderCnt());
                    produceDataGetCapacityItemDto.setSku(preOrderInfoDto.getSku());
                    produceDataGetCapacityItemDto.setSupplierCode(preOrderInfoDto.getSupplierCode());
                    return produceDataGetCapacityItemDto;
                }).collect(Collectors.toList()));

        // 获取SKU列表
        List<String> skuList = produceDataGetCapacityDto.getProduceDataGetCapacityItemList().stream()
                .map(ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        // 获取生产资料信息
        ProduceDataGetRawInventoryBo produceDataGetRawInventoryBo = produceDataBaseService.getRawInventoryBomInfo(skuList);

        // 获取原料的SKU列表
        List<String> rawSkuList = Optional.ofNullable(produceDataGetRawInventoryBo.getProduceDataItemRawPoList())
                .orElse(new ArrayList<>())
                .stream()
                .map(ProduceDataItemRawPo::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());


        // 获取供应商库存信息 （备货库存+自备库存）
        Map<String, Integer> supplierInventoryMap = supplierInventoryBaseService.getSupplierInventoryMap(produceDataGetCapacityDto.getProduceDataGetCapacityItemList(),
                produceDataGetRawInventoryBo);

        // 获取国内自营仓库库存信息
        Map<String, Integer> skuInventoryMap = this.getInventoryMap(rawSkuList);


        // 计算是否满足原料库存逻辑处理算法
        return this.getRawInventoryDataHandle(produceDataGetCapacityDto.getProduceDataGetCapacityItemList(),
                produceDataGetRawInventoryBo.getProduceDataPoMap(),
                produceDataGetRawInventoryBo.getProduceDataItemPoList(),
                produceDataGetRawInventoryBo.getProduceDataItemSupplierPoList(),
                produceDataGetRawInventoryBo.getProduceDataItemRawPoList(),
                skuInventoryMap,
                supplierInventoryMap);

    }

    /**
     * 获取国内自营仓库的库存信息
     *
     * @param skuList:
     * @return Map<String, Integer>
     * @author ChenWenLong
     * @date 2024/8/12 16:33
     */
    private Map<String, Integer> getInventoryMap(List<String> skuList) {
        // 原料的库存
        Map<String, Integer> skuInventoryMap = new HashMap<>();

        if (CollectionUtils.isEmpty(skuList)) {
            return skuInventoryMap;
        }

        // 获取国内自营仓库信息
        final List<WarehouseVo> warehouseVoList = wmsRemoteService.getWarehouseVoByType(List.of(WmsEnum.WarehouseType.DOMESTIC_SELF_RUN));
        // 获取国内自营仓库编码
        List<String> warehouseCodeList = warehouseVoList.stream()
                .map(WarehouseVo::getWarehouseCode)
                .filter(com.baomidou.mybatisplus.core.toolkit.StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());


        // 获取国内自营仓库的库存信息
        SkuInstockInventoryQueryDto skuInstockInventoryQueryDto = new SkuInstockInventoryQueryDto();
        skuInstockInventoryQueryDto.setWarehouseCodeList(warehouseCodeList);
        skuInstockInventoryQueryDto.setSkuCodes(skuList);
        skuInstockInventoryQueryDto.setProductQuality(WmsEnum.ProductQuality.GOOD);
        skuInstockInventoryQueryDto.setDeliveryType(WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);
        List<SkuInventoryVo> skuInventoryList = wmsRemoteService.getSkuInventoryList(skuInstockInventoryQueryDto);
        log.info("查询WMS国内自营仓库的库存信息Vo=>{}", JSON.toJSONString(skuInventoryList));
        if (CollectionUtils.isEmpty(skuInventoryList)) {
            return skuInventoryMap;
        }

        for (SkuInventoryVo skuInventoryVo : skuInventoryList) {
            // 判断 skuCode 是否已经在 skuInventoryMap 中
            if (skuInventoryMap.containsKey(skuInventoryVo.getSkuCode())) {
                // 如果存在，则累加库存数量
                skuInventoryMap.put(skuInventoryVo.getSkuCode(), skuInventoryMap.get(skuInventoryVo.getSkuCode()) + skuInventoryVo.getInStockAmount());
            } else {
                // 如果不存在，则新增 key 和库存数量
                skuInventoryMap.put(skuInventoryVo.getSkuCode(), skuInventoryVo.getInStockAmount());
            }
        }

        return skuInventoryMap;

    }

    /**
     * 计算是否满足原料库存逻辑处理算法
     *
     * @param dtoList:
     * @param produceDataPoMap:
     * @param produceDataItemPoList:
     * @param produceDataItemSupplierPoList:
     * @param produceDataItemRawPoList:
     * @param skuInventoryMap:
     * @param supplierInventoryMap:
     * @return List<ProduceDataGetCapacityVo>
     * @author ChenWenLong
     * @date 2024/8/12 16:42
     */
    private List<ProduceDataGetCapacityVo> getRawInventoryDataHandle(List<ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto> dtoList,
                                                                     Map<String, ProduceDataPo> produceDataPoMap,
                                                                     List<ProduceDataItemPo> produceDataItemPoList,
                                                                     List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList,
                                                                     List<ProduceDataItemRawPo> produceDataItemRawPoList,
                                                                     @NotNull Map<String, Integer> skuInventoryMap,
                                                                     @NotNull Map<String, Integer> supplierInventoryMap) {
        // 返回初始化默认数据
        List<ProduceDataGetCapacityVo> voList = Optional.ofNullable(dtoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(preOrderInfoDto -> {
                    ProduceDataGetCapacityVo vo = new ProduceDataGetCapacityVo();
                    vo.setBusinessId(preOrderInfoDto.getBusinessId());
                    return vo;
                }).collect(Collectors.toList());

        // 获取组装好返回数据处理
        for (ProduceDataGetCapacityVo produceDataGetCapacityVo : voList) {
            ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto produceDataGetCapacityItemDto = dtoList.stream()
                    .filter(infoDto -> infoDto.getBusinessId().equals(produceDataGetCapacityVo.getBusinessId()))
                    .findFirst()
                    .orElse(null);
            if (produceDataGetCapacityItemDto == null) {
                throw new BizException("业务ID{}对应的信息不存在，请联系系统管理员！", produceDataGetCapacityVo.getBusinessId());
            }

            String sku = produceDataGetCapacityItemDto.getSku();
            String supplierCode = produceDataGetCapacityItemDto.getSupplierCode();
            log.info("当前处理业务ID:{}，供应商:{}，sku:{}", produceDataGetCapacityVo.getBusinessId(), supplierCode, sku);

            // 生产资料信息
            ProduceDataPo produceDataPo = Optional.ofNullable(produceDataPoMap)
                    .orElse(new HashMap<>())
                    .get(sku);
            log.info("当前处理业务ID:{}，供应商:{}，sku:{}，生产资料:{}", produceDataGetCapacityVo.getBusinessId(),
                    supplierCode,
                    sku,
                    JSON.toJSONString(produceDataPo));
            if (produceDataPo == null) {
                // 返回结果数据：失败，失败原因：SKU未维护BOM信息，请在SCM-商品管理-商品列表中维护！
                produceDataGetCapacityVo.setResult(BooleanType.FALSE);
                produceDataGetCapacityVo.setIsContentment(BooleanType.FALSE);
                produceDataGetCapacityVo.setFailReason("SKU:" + sku + "未维护BOM信息，请在SCM-商品管理-商品列表中维护！");
                continue;
            }

            if (BooleanType.FALSE.equals(produceDataPo.getRawManage())) {
                // 返回结果数据：成功，原料是否满足：是
                produceDataGetCapacityVo.setResult(BooleanType.TRUE);
                produceDataGetCapacityVo.setIsContentment(BooleanType.TRUE);
                continue;
            }

            // 查询当前sku全部Bom信息
            List<ProduceDataItemPo> produceDataItemPoListBySku = Optional.ofNullable(produceDataItemPoList)
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(produceDataItemPo -> produceDataItemPo.getSku().equals(sku))
                    .collect(Collectors.toList());
            log.info("当前处理业务ID:{}，Bom信息PoList=>{}", produceDataGetCapacityVo.getBusinessId(), JSON.toJSONString(produceDataItemPoListBySku));
            if (CollectionUtils.isEmpty(produceDataItemPoListBySku)) {
                // 返回结果数据：失败，失败原因：SKU未维护BOM信息，请在SCM-商品管理-商品列表中维护！
                produceDataGetCapacityVo.setResult(BooleanType.FALSE);
                produceDataGetCapacityVo.setIsContentment(BooleanType.FALSE);
                produceDataGetCapacityVo.setFailReason("SKU:" + sku + "未维护BOM信息，请在SCM-商品管理-商品列表中维护！");
                continue;
            }

            // 获取匹配到供应商而且优先级最高的BOM
            ProduceDataItemPo produceDataItemPoMax = produceDataItemPoListBySku.stream()
                    .filter(po -> Optional.ofNullable(produceDataItemSupplierPoList)
                            .orElse(new ArrayList<>()).stream()
                            .anyMatch(produceDataItemSupplierPo -> produceDataItemSupplierPo.getProduceDataItemId().equals(po.getProduceDataItemId())
                                    && produceDataItemSupplierPo.getSupplierCode().equals(supplierCode)))
                    .findFirst()
                    .orElse(null);
            if (produceDataItemPoMax == null) {
                produceDataItemPoMax = produceDataItemPoListBySku.get(0);
            }
            log.info("当前处理业务ID:{}，优先级最高BOM=>{}", produceDataGetCapacityVo.getBusinessId(), JSON.toJSONString(produceDataItemPoMax));

            // 获取优先级最高BOM的原料信息
            ProduceDataItemPo finalProduceDataItemPoMax = produceDataItemPoMax;
            List<ProduceDataItemRawPo> produceDataItemRawPoByItemIdList = Optional.ofNullable(produceDataItemRawPoList)
                    .orElse(new ArrayList<>()).stream()
                    .filter(produceDataItemRawPo -> finalProduceDataItemPoMax.getProduceDataItemId().equals(produceDataItemRawPo.getProduceDataItemId()))
                    .collect(Collectors.toList());
            log.info("当前处理业务ID:{}，获取最高BOM的原料信息=>{}", produceDataGetCapacityVo.getBusinessId(), JSON.toJSONString(produceDataItemRawPoByItemIdList));
            if (CollectionUtils.isEmpty(produceDataItemRawPoByItemIdList)) {
                // 返回结果数据：失败，失败原因：SKU未维护BOM信息，请在SCM-商品管理-商品列表中维护！
                produceDataGetCapacityVo.setResult(BooleanType.FALSE);
                produceDataGetCapacityVo.setIsContentment(BooleanType.FALSE);
                produceDataGetCapacityVo.setFailReason("SKU:" + sku + "未维护BOM信息，请在SCM-商品管理-商品列表中维护！");
                log.info("当前处理业务ID:{}，获取最高BOM的没有原料信息，最终结果=>{}", produceDataGetCapacityVo.getBusinessId(), JSON.toJSONString(produceDataGetCapacityVo));
                continue;
            }

            // 获取供应商或我司的原料库存是否满足
            BooleanType rawIsContentment = this.getRawIsContentment(skuInventoryMap,
                    supplierInventoryMap,
                    produceDataGetCapacityVo,
                    produceDataItemRawPoByItemIdList,
                    produceDataGetCapacityItemDto,
                    supplierCode);
            produceDataGetCapacityVo.setResult(BooleanType.TRUE);
            produceDataGetCapacityVo.setIsContentment(rawIsContentment);
            log.info("当前处理业务ID:{}，最终结果=>{}", produceDataGetCapacityVo.getBusinessId(), JSON.toJSONString(produceDataGetCapacityVo));
        }
        return voList;
    }

    /**
     * 获取供应商或我司的原料库存是否满足
     *
     * @param skuInventoryMap:
     * @param supplierInventoryMap:
     * @param produceDataGetCapacityVo:
     * @param produceDataItemRawPoByItemIdList:
     * @param produceDataGetCapacityItemDto:
     * @param supplierCode:
     * @return BooleanType
     * @author ChenWenLong
     * @date 2024/8/13 11:01
     */
    private BooleanType getRawIsContentment(Map<String, Integer> skuInventoryMap,
                                            Map<String, Integer> supplierInventoryMap,
                                            ProduceDataGetCapacityVo produceDataGetCapacityVo,
                                            List<ProduceDataItemRawPo> produceDataItemRawPoByItemIdList,
                                            ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto produceDataGetCapacityItemDto,
                                            String supplierCode) {
        BooleanType rawIsContentment = BooleanType.TRUE;
        for (ProduceDataItemRawPo produceDataItemRawPo : produceDataItemRawPoByItemIdList) {
            // 预计下单数量乘单件用量
            Integer placeOrderCntTotal = produceDataGetCapacityItemDto.getPlaceOrderCnt() * produceDataItemRawPo.getSkuCnt();

            // 所有原料SKU备货库存+自备库存≥预计下单数量乘单件用量
            log.info("当前处理业务ID:{}，原料SKU:{}备货库存+自备库存=>{}，下单需要数量=>{}",
                    produceDataGetCapacityVo.getBusinessId(),
                    produceDataItemRawPo.getSku(),
                    JSON.toJSONString(supplierInventoryMap.get(supplierCode + produceDataItemRawPo.getSku())),
                    placeOrderCntTotal);
            if (supplierInventoryMap.get(supplierCode + produceDataItemRawPo.getSku()) != null
                    && supplierInventoryMap.get(supplierCode + produceDataItemRawPo.getSku()) >= placeOrderCntTotal) {
                // 满足条件就无需进行下面我司原料判断，进行减去库存更新到supplierCodeSkuPoMap中
                supplierInventoryMap.put(supplierCode + produceDataItemRawPo.getSku(),
                        supplierInventoryMap.get(supplierCode + produceDataItemRawPo.getSku()) - placeOrderCntTotal);
                continue;
            }

            // 下面进行我司原料库存判断
            log.info("当前处理业务ID:{}，原料SKU:{}我司库存=>{}，下单需要数量=>{}",
                    produceDataGetCapacityVo.getBusinessId(),
                    produceDataItemRawPo.getSku(),
                    JSON.toJSONString(skuInventoryMap.get(produceDataItemRawPo.getSku())),
                    placeOrderCntTotal);
            if (skuInventoryMap.get(produceDataItemRawPo.getSku()) != null
                    && skuInventoryMap.get(produceDataItemRawPo.getSku()) >= placeOrderCntTotal) {
                // 满足条件我司原料库存就无需进行下面判断，进行减去我司原料更新到skuInventoryMap中
                skuInventoryMap.put(produceDataItemRawPo.getSku(),
                        skuInventoryMap.get(produceDataItemRawPo.getSku()) - placeOrderCntTotal);
                continue;
            }

            // 当原料库存不足时，返回结果数据：成功，原料库存是否满足：否
            rawIsContentment = BooleanType.FALSE;

        }
        return rawIsContentment;
    }

    //批量获取生产单件SKU消耗天数
    public Map<String, BigDecimal> getSkuCapMap(Collection<String> skuList) {
        List<SkuCapBo> skuCapBos = skuInfoDao.listByCapBo(skuList);
        return skuCapBos.stream().collect(Collectors.toMap(SkuCapBo::getSku, SkuCapBo::getCapacityDays));
    }

    //批量获取供应商物流时效
    public Map<String, Long> getSupLogisticsMap(Collection<String> supplierCodeList) {
        List<SupplierLogisticsBo> supplierLogisticsBos = supplierDao.listByLogisticsBo(supplierCodeList);
        return supplierLogisticsBos.stream().collect(Collectors.toMap(SupplierLogisticsBo::getSupplierCode, SupplierLogisticsBo::toLogisticsDays));
    }

    //计算最晚生产完成时间：入参：最晚上架时间、物流时效，2天仓内作业
    public LocalDate calMaxProduceDate(LocalDate maxShelfDate, long logisticsDays, long warehouseDays) {
        if (Objects.isNull(maxShelfDate)) {
            throw new ParamIllegalException("计算最晚生产完成时间失败！最晚上架时间为空。");
        }

        LocalDate res = maxShelfDate.minusDays(logisticsDays + warehouseDays);
        log.info("计算最晚生产完成时间：最晚上架时间=>{} 减去 物流时效=>{} 减去 仓内作业=>{}，计算结果=>{}", maxShelfDate, logisticsDays, warehouseDays, res);
        return res;
    }

    //批量获取SKU生产周期
    public Map<String, Long> getProduceCycleMap(Collection<String> skuList) {
        List<SkuCycleBo> skuCycleBos = plmSkuDao.listByProduceCycleBo(skuList);
        return skuCycleBos.stream().collect(Collectors.toMap(SkuCycleBo::getSku, SkuCycleBo::roundUp));
    }

    //获取供应商产能筛选开始时间
    public LocalDate getLoadingSupCapacityBeginDate(LocalDate maxProduceDate, long skuCapCycle, long cycleDays) {
        if (Objects.isNull(maxProduceDate)) {
            throw new ParamIllegalException("预加载供应商日产能开始时间失败！最晚生产完成时间为空。");
        }

        LocalDate l1 = maxProduceDate.minusDays(cycleDays);
        LocalDate l2 = LocalDate.now(ZoneId.of(TimeZoneId.CN.getTimeZoneId())).plusDays(skuCapCycle);

        //计算l1,l2最小值
        LocalDate min = ScmTimeUtil.min(l1, l2);
        log.info("预加载供应商日产能开始时间：最晚生产完成时间=>{} 减去 默认生产周期=>{}，计算结果=>【{}】 当前时间=>{} 加上 sku生产周期=>{}，计算结果=>【{}】 求上述两者最小值=>【{}】",
                maxProduceDate, cycleDays, l1, LocalDateTime.now(), skuCapCycle, l2, min);
        return min;
    }

    //获取供应商产能筛选结束时间
    public LocalDate getLoadingSupCapacityEndDate(LocalDate maxProduceDate, long backUpDays) {
        LocalDate res = maxProduceDate.plusDays(backUpDays);
        log.info("预加载供应商日产能结束时间：最晚生产完成时间=>{} 加上 后备天数=>{}，计算结果=>{}", maxProduceDate, backUpDays, res);
        return res;
    }

    public LocalDate calDeliveryDate(String supplierCode, Deque<LocalDate> searchQueue, Map<String, BigDecimal> supplierCapacityMap) {
        int size = searchQueue.size();
        for (int i = 0; i < size; i++) {
            LocalDate date = searchQueue.pollLast();
            if (null != date) {
                BigDecimal capacity = supplierCapacityMap.get(genSupCapacityKey(supplierCode, date));
                if (null != capacity && capacity.compareTo(BigDecimal.ZERO) > 0) {
                    log.info("计算交货日期：供应商=>{}，日期=>{}，当前供应商剩余产能=>{}，满足条件，返回结果=>{}", supplierCode, date, capacity, date);
                    return date;
                }
            }
        }
        throw new ParamIllegalException("计算交货日期失败！没有满足条件的日期。");
    }

    public LocalDate calDeliveryDate(String supplierCode, BigDecimal accumulatedCapacity, Deque<LocalDate> searchQueue, BigDecimal needCapacity, Map<String, BigDecimal> supplierCapacityMap) {
        int size = searchQueue.size();
        for (int i = 0; i < size; i++) {
            LocalDate date = searchQueue.pollFirst();

            // 累加当天的产能
            if (date != null) {
                BigDecimal capacity = supplierCapacityMap.getOrDefault(genSupCapacityKey(supplierCode, date), BigDecimal.ZERO);
                accumulatedCapacity = accumulatedCapacity.add(capacity);
            }
            // 检查累计产能是否满足需求
            if (accumulatedCapacity.compareTo(needCapacity) >= 0) {
                // 记录并返回满足需求的日期
                log.info("计算预计上架时间：当前日期=>{}，累计产能=>{}，需求产能=>{}", date, accumulatedCapacity, needCapacity);
                return date;
            }
        }
        return null;
    }

    //计算预计上架时间
    public LocalDate calPreShelfDate(LocalDate deliveryDate, long logisticsDays, long warehouseDays,
                                     AtomicInteger index, Long businessId) {
        if (Objects.isNull(deliveryDate)) {
            throw new ParamIllegalException("计算预计上架时间失败！交货日期为空。");
        }
        LocalDate res = deliveryDate.plusDays(logisticsDays + warehouseDays);
        log.info("businessId=>{},【数据统计】no.{}：计算预计上架日期：交货日期=>{} 加上 物流时效=>{} 加上 2天仓内作业=>{}，计算结果=>{}",
                businessId, index.getAndIncrement(),
                deliveryDate, logisticsDays, warehouseDays, res);
        return res;
    }

    //生成产能key
    private String genSupCapacityKey(String supplierCode, LocalDate capacityDate) {
        return supplierCode + capacityDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private BigDecimal calTotalCapacity(String supplierCode, LocalDate filterBeginDate, LocalDate filterEndDate, Map<String, BigDecimal> supplierCapacityMap) {
        BigDecimal totalCapacity = BigDecimal.ZERO;
        for (LocalDate date = filterBeginDate; date.isBefore(filterEndDate.plusDays(1)); date = date.plusDays(1)) {
            BigDecimal capacity = supplierCapacityMap.get(genSupCapacityKey(supplierCode, date));
            if (Objects.nonNull(capacity)) {
                totalCapacity = totalCapacity.add(capacity);
            }
        }
        return totalCapacity;
    }

    /**
     * 下拉框获取供应商
     *
     * @param dto:
     * @return List<SupplierDropDownSearchVo>
     * @author ChenWenLong
     * @date 2024/9/24 17:27
     */
    public List<SupplierDropDownSearchVo> getSupplierOpenSearch(SupplierDropDownSearchDto dto) {
        final List<SupplierPo> supplierPoList = supplierDao.getListByNameAndStatus(dto.getSearchContent(),
                dto.getSupplierStatus());

        return supplierPoList.stream()
                .map(po -> {
                    final SupplierDropDownSearchVo supplierDropDownSearchVo = new SupplierDropDownSearchVo();
                    supplierDropDownSearchVo.setSearchFieldName(po.getSupplierName());
                    supplierDropDownSearchVo.setConditionFieldName(po.getSupplierCode());
                    return supplierDropDownSearchVo;
                }).collect(Collectors.toList());
    }
}
