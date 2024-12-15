package com.hete.supply.scm.server.scm.process.service.base;/**
 * 工序模板公共服务
 *
 * @author yanjiawei
 * Created on 2023/9/13.
 */

import com.hete.supply.scm.server.scm.process.dao.ProcessTemplateDao;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessTemplatePo;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * @author yanjiawei
 * @date 2023年09月13日 11:20
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class ProcessTemplateBaseService {

    private final ProcessTemplateDao processTemplateDao;

    /**
     * 检查工序模板名称的唯一性。
     *
     * @param templateName 要检查的工序模板名称
     * @param templateId   当前工艺模板的ID
     * @throws ParamIllegalException 如果存在具有相同名称但不同ID的工艺模板，则抛出异常
     */
    public void checkTemplateNameUniqueness(Long templateId, String templateName) {
        ProcessTemplatePo existingTemplate = processTemplateDao.getByName(templateName);
        if (Objects.nonNull(existingTemplate) && !templateId.equals(existingTemplate.getProcessTemplateId())) {
            throw new ParamIllegalException("{}已存在", templateName);
        }
    }
}
