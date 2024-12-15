package com.hete.supply.scm.server.supplier.service.biz;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.server.scm.entity.po.EmployeeRestTimePo;
import com.hete.supply.scm.server.scm.process.dao.EmployeeRestTimeDao;
import com.hete.supply.scm.server.scm.process.entity.bo.EmployeeRestTimeBo;
import com.hete.supply.scm.server.scm.process.entity.dto.EmployeeRestTimeDto;
import com.hete.supply.scm.server.scm.process.service.base.ProcessPlanBaseService;
import com.hete.supply.scm.server.scm.process.service.biz.EmployeeRestTimeBizService;
import com.hete.supply.scm.server.supplier.converter.EmployeeRestTimeConverter;
import com.hete.supply.scm.server.supplier.converter.SupplierEmployeeConverter;
import com.hete.supply.scm.server.supplier.dao.EmployeeGradeDao;
import com.hete.supply.scm.server.supplier.dao.EmployeeGradeProcessDao;
import com.hete.supply.scm.server.supplier.dao.EmployeeGradeRelationDao;
import com.hete.supply.scm.server.supplier.entity.dto.*;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradePo;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradeProcessPo;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradeRelationPo;
import com.hete.supply.scm.server.supplier.entity.vo.EmployeeGradeSearchVo;
import com.hete.supply.scm.server.supplier.entity.vo.EmployeeSearchVo;
import com.hete.supply.scm.server.supplier.entity.vo.ProcessSimpleVo;
import com.hete.supply.scm.server.supplier.service.base.EmployeeBaseService;
import com.hete.supply.udb.api.entity.dto.OrgUsersQueryDto;
import com.hete.supply.udb.api.entity.vo.UserCodeNameVo;
import com.hete.supply.udb.api.enums.UserState;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/7/24 23:53
 */
@Service
@RequiredArgsConstructor
public class EmployeeBizService {

    private final EmployeeBaseService employeeBaseService;
    private final EmployeeGradeDao employeeGradeDao;
    private final EmployeeGradeRelationDao employeeGradeRelationDao;
    private final EmployeeGradeProcessDao employeeGradeProcessDao;
    private final UdbRemoteService udbRemoteService;
    private final ProcessPlanBaseService processPlanBaseService;
    private final EmployeeRestTimeBizService employeeRestTimeBizService;
    private final EmployeeRestTimeDao employeeRestTimeDao;

    @Value("${spm.employee.processing.orgChain}")
    private String orgChain;

    public CommonPageResult.PageInfo<EmployeeGradeSearchVo> searchEmployeeGrade(EmployeeGradeSearchDto dto) {
        final CommonPageResult.PageInfo<EmployeeGradeSearchVo> employeeGradeSearchVoPageInfo = employeeGradeDao.searchEmployeeGrade(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<EmployeeGradeSearchVo> records = employeeGradeSearchVoPageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }
        final List<Long> employeeGradeIdList = records.stream()
                .map(EmployeeGradeSearchVo::getEmployeeGradeId)
                .collect(Collectors.toList());
        final List<EmployeeGradeProcessPo> employeeGradeProcessPoList = employeeGradeProcessDao.getListByEmployeeGradeIdList(employeeGradeIdList);
        final Map<Long, List<EmployeeGradeProcessPo>> employeeGradeIdPoMap = employeeGradeProcessPoList.stream()
                .collect(Collectors.groupingBy(EmployeeGradeProcessPo::getEmployeeGradeId));
        final Map<Long, List<ProcessSimpleVo>> employeeGradeIdVoMap = employeeGradeIdPoMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> SupplierEmployeeConverter.employeeGradeProcessPoToVo(entry.getValue())));

        records.forEach(record -> {
            record.setProcessSimpleList(employeeGradeIdVoMap.get(record.getEmployeeGradeId()));
        });

        return employeeGradeSearchVoPageInfo;
    }

    /**
     * 根据员工搜索条件查询员工信息，包括员工编号、姓名等，同时根据订单类型筛选员工。
     *
     * @param dto 包含员工搜索条件的数据传输对象
     * @return 包含员工搜索结果的分页信息对象
     */
    public CommonPageResult.PageInfo<EmployeeSearchVo> searchEmployee(EmployeeSearchDto dto) {
        // 获取订单类型
        final ProcessOrderType processOrderType = dto.getProcessOrderType();
        if (Objects.nonNull(processOrderType)) {
            // 获取与订单类型绑定的可用员工编号
            Set<String> processPlanEmployees = getProcessPlanEmployees(processOrderType);
            if (CollectionUtils.isEmpty(processPlanEmployees)) {
                // 若员工编号集合为空，直接返回空分页结果
                return new CommonPageResult.PageInfo<>(Collections.emptyList());
            }
            // 设置员工编号集合到搜索条件中
            dto.setEmployeeNos(processPlanEmployees);
        }

        // 执行员工搜索并获取分页结果
        CommonPageResult.PageInfo<EmployeeSearchVo> employeeSearchVoPageInfo =
                employeeGradeRelationDao.searchEmployee(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        // 补充员工休息时间信息到搜索结果中
        enrichEmployeeData(employeeSearchVoPageInfo.getRecords());
        return employeeSearchVoPageInfo;
    }


    /**
     * 根据分页信息补充员工额外信息
     *
     * @param records 包含员工搜索记录的列表
     */
    private void enrichEmployeeData(List<EmployeeSearchVo> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }

        // 提取员工编号，去除空白值，并转换为集合
        Set<String> employeeNos = records.stream().map(EmployeeSearchVo::getEmployeeNo).filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());

        // 批量获取员工休息时间信息
        List<EmployeeRestTimeBo> employeeRestTimes = employeeRestTimeBizService.getEmployeeRestTimes(employeeNos);
        if (CollectionUtils.isEmpty(employeeRestTimes)) {
            return;
        }

        // 遍历员工搜索记录，根据员工编号匹配休息时间信息并补充到记录中
        records.forEach(record -> {
            final String employeeNo = record.getEmployeeNo();
            List<EmployeeRestTimeBo> matchEmployeeRestTimes
                    = employeeRestTimes.stream().filter(restTimeBo -> Objects.equals(employeeNo, restTimeBo.getEmployeeNo())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(matchEmployeeRestTimes)) {
                record.setEmployeeRestTimeList(matchEmployeeRestTimes.stream().map(matchEmployeeRestTime -> {
                    EmployeeRestTimeDto dto = new EmployeeRestTimeDto();
                    dto.setRestStartTime(matchEmployeeRestTime.getRestStartTime());
                    dto.setRestEndTime(matchEmployeeRestTime.getRestEndTime());
                    return dto;
                }).collect(Collectors.toList()));

            }
        });
    }


    /**
     * 根据订单类型获取可用的员工编号集合。
     *
     * @param processOrderType 订单类型，用于确定员工编号集合
     * @return 可用的员工编号集合，如果订单类型为null，则返回空集合
     */
    private Set<String> getProcessPlanEmployees(ProcessOrderType processOrderType) {
        if (Objects.isNull(processOrderType)) {
            return Collections.emptySet();
        }
        return processPlanBaseService.getProcessPlanEmployees(processOrderType);
    }


    @Transactional(rollbackFor = Exception.class)
    public void createEmployeeGrade(EmployGradeCreateDto dto) {
        if (null != employeeGradeDao.getByName(dto.getGradeName())) {
            throw new ParamIllegalException("职级名称:{}已经存在，请更换职级名称后重新提交！", dto.getGradeName());
        }
        boolean duplicate = employeeBaseService.checkDuplicateProcessIds(dto.getProcessSimpleList());
        if (duplicate) {
            throw new ParamIllegalException("请勿配置重复的工序能力范围！");
        }

        final EmployeeGradePo employeeGradePo = new EmployeeGradePo();
        employeeGradePo.setGradeType(dto.getGradeType());
        employeeGradePo.setGradeName(dto.getGradeName());
        employeeGradePo.setGradeLevel(dto.getGradeLevel());
        employeeGradeDao.insert(employeeGradePo);

        final List<EmployeeGradeProcessPo> employeeGradeProcessPoList = SupplierEmployeeConverter.processSimpleDtoToPo(employeeGradePo.getEmployeeGradeId(), dto.getProcessSimpleList());
        employeeGradeProcessDao.insertBatch(employeeGradeProcessPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editEmployeeGrade(EmployGradeEditDto dto) {
        final EmployeeGradePo dbEmployeeGradePo = employeeGradeDao.getByName(dto.getGradeName());
        if (null != dbEmployeeGradePo && !dbEmployeeGradePo.getEmployeeGradeId().equals(dto.getEmployeeGradeId())) {
            throw new ParamIllegalException("职级名称:{}已经存在，请更换职级名称后重新提交！", dto.getGradeName());
        }

        final EmployeeGradePo employeeGradePo = employeeGradeDao.getByIdVersion(dto.getEmployeeGradeId(), dto.getVersion());
        if (null == employeeGradePo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        boolean duplicate = employeeBaseService.checkDuplicateProcessIds(dto.getProcessSimpleList());
        if (duplicate) {
            throw new ParamIllegalException("请勿配置重复的工序能力范围！");
        }

        employeeGradePo.setGradeName(dto.getGradeName());
        employeeGradePo.setGradeLevel(dto.getGradeLevel());
        employeeGradeDao.updateByIdVersion(employeeGradePo);

        //  能力范围，删除旧数据，再新增新数据
        final List<EmployeeGradeProcessPo> employeeGradeProcessPoList = SupplierEmployeeConverter.processSimpleDtoToPo(employeeGradePo.getEmployeeGradeId(), dto.getProcessSimpleList());
        employeeGradeProcessDao.removeByEmployeeGradeId(employeeGradePo.getEmployeeGradeId());
        employeeGradeProcessDao.insertBatch(employeeGradeProcessPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editEmployee(EmployEditDto dto) {
        final EmployeeGradeRelationPo employeeGradeRelationPo = employeeGradeRelationDao.getByIdVersion(dto.getEmployeeGradeRelationId(), dto.getVersion());
        if (null == employeeGradeRelationPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        final EmployeeGradePo employeeGradePo = employeeGradeDao.getById(dto.getEmployeeGradeId());
        if (null == employeeGradePo) {
            throw new BizException("查找不到对应的员工职级，请联系系统管理员！");
        }

        employeeGradeRelationPo.setEmployeeGradeId(employeeGradePo.getEmployeeGradeId());
        employeeGradeRelationDao.updateByIdVersion(employeeGradeRelationPo);

        final List<EmployeeRestTimeDto> employeeRestTimeList = dto.getEmployeeRestTimeList();
        employeeBaseService.validateRestTimes(employeeRestTimeList);
        boolean duplicate = employeeBaseService.checkDuplicateRestTimes(employeeRestTimeList);
        if (duplicate) {
            throw new ParamIllegalException("请勿配置重复的停工时间！");
        }

        //  休息时间，删除旧数据，再新增新数据
        final String employeeNo = employeeGradeRelationPo.getEmployeeNo();
        final String employeeName = employeeGradeRelationPo.getEmployeeName();
        final List<EmployeeRestTimePo> employeeRestTimePos = EmployeeRestTimeConverter.convertToPoList(employeeRestTimeList, employeeNo, employeeName);
        employeeRestTimeDao.removeByEmployeeNo(employeeNo);
        employeeRestTimeDao.insertBatch(employeeRestTimePos);
    }

    /**
     * 定时任务变更加工部员工
     */
    @Transactional(rollbackFor = Exception.class)
    public void employeeChangeTask() {
        // 获取scm维护的所有员工
        final List<EmployeeGradeRelationPo> employeeGradeRelationPoList = employeeGradeRelationDao.getAll();
        final List<String> employeeNoList = employeeGradeRelationPoList.stream()
                .map(EmployeeGradeRelationPo::getEmployeeNo)
                .collect(Collectors.toList());
        // 获取udb加工部在职员工
        final OrgUsersQueryDto orgUsersQueryDto = new OrgUsersQueryDto();
        orgUsersQueryDto.setOrgChain(orgChain);
        orgUsersQueryDto.setUserState(UserState.ON_JOB);
        final List<UserCodeNameVo> onJobUserVos = udbRemoteService.queryUserListByOrgCode(orgUsersQueryDto);
        if (CollectionUtils.isEmpty(onJobUserVos)) {
            throw new BizException("该组织部门:{}下没有在职的员工，定时任务执行失败！");
        }

        // 遍历过滤出db不存在的在职员工并insert
        final List<EmployeeGradeRelationPo> newEmployeeGradeRelationPoList = onJobUserVos.stream()
                .filter(userVo -> !employeeNoList.contains(userVo.getUserCode()))
                .map(userVo -> {
                    final EmployeeGradeRelationPo employeeGradeRelationPo = new EmployeeGradeRelationPo();
                    employeeGradeRelationPo.setEmployeeNo(userVo.getUserCode());
                    employeeGradeRelationPo.setEmployeeName(userVo.getUsername());
                    return employeeGradeRelationPo;
                }).collect(Collectors.toList());
        employeeGradeRelationDao.insertBatch(newEmployeeGradeRelationPoList);


        // 获取udb加工部离职员工
        orgUsersQueryDto.setOrgChain(orgChain);
        orgUsersQueryDto.setUserState(UserState.OFF_JOB);
        final List<UserCodeNameVo> offJobUserVos = udbRemoteService.queryUserListByOrgCode(orgUsersQueryDto);
        // 无离职员工，定时任务结束
        if (CollectionUtils.isEmpty(offJobUserVos)) {
            return;
        }
        // 过滤获取scm维护的员工处于在职，在udb处于离职的员工，并remove
        final List<String> offJobUserCodeList = offJobUserVos.stream()
                .map(UserCodeNameVo::getUserCode)
                .filter(employeeNoList::contains)
                .collect(Collectors.toList());
        employeeGradeRelationDao.removeByEmployeeNoList(offJobUserCodeList);
    }
}
