package com.hete.supply.scm.server.scm.process.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.server.scm.process.dao.ProcessDescDao;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescCreateDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescEditDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescQueryDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescRemoveDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessDescPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessDescVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/11/8 09:41
 */
@Service
@RequiredArgsConstructor
public class ProcessDescBizService {

    private final ProcessDescDao processDescDao;

    /**
     * 加工描述查询
     *
     * @param processDescQueryDto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessDescVo> getByPage(ProcessDescQueryDto processDescQueryDto) {
        CommonPageResult.PageInfo<ProcessDescVo> pageInfo = processDescDao.getByPage(PageDTO.of(processDescQueryDto.getPageNo(), processDescQueryDto.getPageSize()),
                processDescQueryDto);

        List<ProcessDescVo> records = pageInfo.getRecords();

        if (CollectionUtils.isNotEmpty(records)) {
            List<ProcessDescVo> formatRecords = records.stream()
                    .peek((item) -> item.setDescValuesAsList(Arrays.asList(item.getDescValues().split(","))))
                    .collect(Collectors.toList());
            pageInfo.setRecords(formatRecords);
        }


        return pageInfo;
    }

    /**
     * 创建加工描述
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(ProcessDescCreateDto dto) {
        ProcessDescPo sameNameProcessDescPo = processDescDao.getByName(dto.getName(), dto.getProcessFirst());
        if (sameNameProcessDescPo != null) {
            throw new ParamIllegalException("{}已存在", dto.getName());
        }

        ProcessDescPo processDescPo = new ProcessDescPo();

        processDescPo.setName(dto.getName());
        processDescPo.setProcessFirst(dto.getProcessFirst());
        processDescPo.setDescValues(String.join(",", dto.getValues()));

        processDescDao.insert(processDescPo);
    }

    /**
     * 编辑加工描述
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void edit(ProcessDescEditDto dto) {
        ProcessDescPo sameNameProcessDescPo = processDescDao.getByName(dto.getName(), dto.getProcessFirst());
        if (sameNameProcessDescPo != null && !sameNameProcessDescPo.getProcessDescId().equals(dto.getProcessDescId())) {
            throw new ParamIllegalException("{}已存在", dto.getName());
        }

        ProcessDescPo processDescPo = new ProcessDescPo();

        processDescPo.setProcessDescId(dto.getProcessDescId());
        processDescPo.setProcessFirst(dto.getProcessFirst());
        processDescPo.setName(dto.getName());
        processDescPo.setDescValues(String.join(",", dto.getValues()));
        processDescPo.setVersion(dto.getVersion());

        processDescDao.updateByIdVersion(processDescPo);
    }

    /**
     * 删除加工描述
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void remove(ProcessDescRemoveDto dto) {
        final ProcessDescPo processDescPo = processDescDao.getByIdVersion(dto.getProcessDescId(), dto.getVersion());
        if (null == processDescPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        processDescDao.removeByIdVersion(processDescPo);
    }
}
