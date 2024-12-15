package com.hete.supply.scm.server.scm.develop.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleSettleSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleSettleStatus;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettleOrderDetailDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettleOrderExamineDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettlePayAddDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettlePayDelDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleSearchVo;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopExportBizService;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopSampleSettleBaseService;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleOrderPo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettlePayPo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.settle.converter.DevelopSampleSettlePayConverter;
import com.hete.supply.scm.server.scm.settle.dao.DevelopSampleSettleItemDao;
import com.hete.supply.scm.server.scm.settle.dao.DevelopSampleSettleOrderDao;
import com.hete.supply.scm.server.scm.settle.dao.DevelopSampleSettlePayDao;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/1 14:32
 */
@Service
@RequiredArgsConstructor
public class DevelopSampleSettleBizService {

    private final DevelopSampleSettleBaseService developSampleSettleBaseService;
    private final DevelopSampleSettleOrderDao developSampleSettleOrderDao;
    private final DevelopSampleSettlePayDao developSampleSettlePayDao;
    private final ScmImageBaseService scmImageBaseService;
    private final DevelopSampleSettleItemDao developSampleSettleItemDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final DevelopExportBizService developExportBizService;

    /**
     * 分页查询
     *
     * @param dto:
     * @return PageInfo<DevelopSampleSettleSearchVo>
     * @author ChenWenLong
     * @date 2023/8/1 16:37
     */
    public CommonPageResult.PageInfo<DevelopSampleSettleSearchVo> search(DevelopSampleSettleSearchDto dto) {
        return developSampleSettleBaseService.search(dto);
    }

    /**
     * 详情
     *
     * @param dto:
     * @return String
     * @author ChenWenLong
     * @date 2023/8/1 16:42
     */
    public DevelopSampleSettleOrderDetailVo detail(DevelopSampleSettleOrderDetailDto dto) {
        return developSampleSettleBaseService.detail(dto);
    }

    /**
     * 审核
     *
     * @param dto:
     * @return Boolean
     * @author ChenWenLong
     * @date 2023/8/1 17:02
     */
    public void examine(DevelopSampleSettleOrderExamineDto dto) {
        developSampleSettleBaseService.examine(dto);
    }

    /**
     * 提交支付信息
     *
     * @param dto:
     * @return Boolean
     * @author ChenWenLong
     * @date 2023/8/1 17:10
     */
    @Transactional(rollbackFor = Exception.class)
    public void addDevelopSampleSettleOrderPay(DevelopSampleSettlePayAddDto dto) {
        DevelopSampleSettleOrderPo developSampleSettleOrderPo = developSampleSettleOrderDao.getByIdVersion(dto.getDevelopSampleSettleOrderId(), dto.getVersion());
        if (developSampleSettleOrderPo == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        if ((developSampleSettleOrderPo.getDevelopSampleSettleStatus() == DevelopSampleSettleStatus.AUDITED) || (developSampleSettleOrderPo.getDevelopSampleSettleStatus() == DevelopSampleSettleStatus.PART_SETTLE)) {
            DevelopSampleSettlePayPo developSampleSettlePayPo = DevelopSampleSettlePayConverter.INSTANCE.addPay(dto);
            developSampleSettlePayPo.setDevelopSampleSettleOrderNo(developSampleSettleOrderPo.getDevelopSampleSettleOrderNo());
            developSampleSettlePayDao.insert(developSampleSettlePayPo);

            //增加图片凭证
            scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.SAMPLE_SETTLE_PAY, developSampleSettlePayPo.getDevelopSampleSettlePayId());
            //改变状态
            this.updateDevelopSampleSettleStatus(developSampleSettleOrderPo.getDevelopSampleSettleOrderNo());
        } else {
            throw new ParamIllegalException("状态处于" + DevelopSampleSettleStatus.AUDITED.getRemark() + "或" + DevelopSampleSettleStatus.PART_SETTLE.getRemark() + "才能进行确认支付");
        }
    }

    /**
     * 删除支付信息
     *
     * @param dto:
     * @return Boolean
     * @author ChenWenLong
     * @date 2023/8/1 17:12
     */
    @Transactional(rollbackFor = Exception.class)
    public void delDevelopSampleSettleOrderPay(DevelopSampleSettlePayDelDto dto) {
        DevelopSampleSettlePayPo developSampleSettlePayPo = developSampleSettlePayDao.getByIdVersion(dto.getDevelopSampleSettlePayId(), dto.getVersion());
        if (developSampleSettlePayPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        DevelopSampleSettleOrderPo developSampleSettleOrderPo = developSampleSettleOrderDao.getByDevelopSampleSettleOrderNo(developSampleSettlePayPo.getDevelopSampleSettleOrderNo());
        if (developSampleSettleOrderPo == null) {
            throw new BizException("查询不到结算单信息，请刷新页面后重试！");
        }
        if (developSampleSettleOrderPo.getDevelopSampleSettleStatus() == DevelopSampleSettleStatus.SETTLE) {
            throw new ParamIllegalException("结算单状态{}，禁止删除操作", DevelopSampleSettleStatus.SETTLE.getRemark());
        }
        developSampleSettlePayDao.removeByIdVersion(dto.getDevelopSampleSettlePayId(), dto.getVersion());
        //改变状态
        this.updateDevelopSampleSettleStatus(developSampleSettleOrderPo.getDevelopSampleSettleOrderNo());
    }

    /**
     * 列表导出
     *
     * @param dto:
     * @return Boolean
     * @author ChenWenLong
     * @date 2023/8/1 17:13
     */
    @Transactional(rollbackFor = Exception.class)
    public void export(DevelopSampleSettleSearchDto dto) {
        //条件过滤
        if (null == developSampleSettleBaseService.getSearchWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }

        Integer exportTotals = developExportBizService.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));

        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_SAMPLE_SETTLE_ORDER_EXPORT.getCode(), dto));
    }

    /**
     * 更新状态逻辑
     *
     * @param developSampleSettleOrderNo:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/5 10:09
     */
    public void updateDevelopSampleSettleStatus(String developSampleSettleOrderNo) {
        //改变状态
        DevelopSampleSettleOrderPo developSampleSettleOrderPo = developSampleSettleOrderDao.getByDevelopSampleSettleOrderNo(developSampleSettleOrderNo);
        List<DevelopSampleSettlePayPo> developSampleSettlePayPoList = developSampleSettlePayDao.getListByDevelopSampleSettleOrderNo(developSampleSettleOrderNo);
        if (CollectionUtil.isNotEmpty(developSampleSettlePayPoList)) {
            BigDecimal totalPayPrice = developSampleSettlePayPoList.stream().map(DevelopSampleSettlePayPo::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalPayPrice.compareTo(developSampleSettleOrderPo.getPayPrice()) > 0) {
                throw new ParamIllegalException("支付金额总和不能应付金额");
            }
            if (totalPayPrice.compareTo(developSampleSettleOrderPo.getPayPrice()) == 0) {
                developSampleSettleOrderPo.setDevelopSampleSettleStatus(DevelopSampleSettleStatus.SETTLE);
                developSampleSettleOrderPo.setPayTime(LocalDateTime.now());
                developSampleSettleOrderPo.setPayUser(GlobalContext.getUserKey());
                developSampleSettleOrderPo.setPayUsername(GlobalContext.getUsername());

            }
            if (totalPayPrice.compareTo(developSampleSettleOrderPo.getPayPrice()) < 0) {
                developSampleSettleOrderPo.setDevelopSampleSettleStatus(DevelopSampleSettleStatus.PART_SETTLE);
            }

        } else {
            developSampleSettleOrderPo.setDevelopSampleSettleStatus(DevelopSampleSettleStatus.AUDITED);
        }
        developSampleSettleOrderDao.updateByIdVersion(developSampleSettleOrderPo);
        //写日志
        developSampleSettleBaseService.createStatusChangeLog(developSampleSettleOrderPo, null);
    }

}
