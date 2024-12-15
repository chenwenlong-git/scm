package com.hete.supply.scm.remote.dubbo;

import com.hete.supply.bss.api.oss.entity.dto.FileDto;
import com.hete.supply.bss.api.oss.entity.dto.FileListDto;
import com.hete.supply.bss.api.oss.entity.vo.FileVo;
import com.hete.supply.bss.api.oss.facade.FileFacade;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.dubbo.util.DubboResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件服务客户端
 *
 * @author ChenWenLong
 * @date 2023/3/15 16:06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BssRemoteService {
    @DubboReference(check = false)
    private FileFacade fileFacade;

    /**
     * 通过文件唯一码查询文件
     *
     * @param fileCode
     * @return
     */
    public FileVo getFile(String fileCode) {
        FileDto fileDto = new FileDto();
        fileDto.setFileCode(fileCode);
        CommonResult<FileVo> result = fileFacade.getFile(fileDto);
        return DubboResponseUtil.checkCodeAndGetData(result);
    }

    /**
     * 根据文件编码批量查询
     *
     * @param fileCodeList
     * @return
     */
    public ResultList<FileVo> getFileList(List<String> fileCodeList) {
        FileListDto fileDto = new FileListDto();
        fileDto.setFileCodeList(fileCodeList);
        CommonResult<ResultList<FileVo>> result = fileFacade.getFileList(fileDto);
        return DubboResponseUtil.checkCodeAndGetData(result);
    }

}
