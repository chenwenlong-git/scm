package com.hete.supply.scm.remote.dubbo;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.hete.supply.sda.api.general.entity.dto.ObtainPlatDto;
import com.hete.supply.sda.api.general.entity.dto.PlatInfoDto;
import com.hete.supply.sda.api.general.entity.vo.ObtainPlatVo;
import com.hete.supply.sda.api.general.entity.vo.PlatVo;
import com.hete.supply.sda.api.general.enums.PlatformInfoState;
import com.hete.supply.sda.api.general.facade.PlatFacade;
import com.hete.supply.sda.api.scm.bf.entity.vo.ScmPurchaseExcDataVo;
import com.hete.supply.sda.api.scm.bf.entity.vo.ScmQcExcDataVo;
import com.hete.supply.sda.api.scm.bf.entity.vo.ScmReceiveExcDataVo;
import com.hete.supply.sda.api.scm.bf.facade.ScmBfFacade;
import com.hete.supply.sda.api.scm.process.entity.vo.DailyPlatformDemandVo;
import com.hete.supply.sda.api.scm.process.entity.vo.DailyPlatformShipmentVo;
import com.hete.supply.sda.api.scm.process.facade.ScmProcessOrderFacade;
import com.hete.supply.sda.api.scm.purchase.entity.vo.ScmPurchaseCntVo;
import com.hete.supply.sda.api.scm.purchase.entity.vo.ScmPurchaseDeliverDataVo;
import com.hete.supply.sda.api.scm.purchase.facade.ScmPurchaseFacade;
import com.hete.supply.sda.api.scm.sku.entity.vo.ScmSkuOrderStockoutReasonVo;
import com.hete.supply.sda.api.scm.sku.entity.vo.ScmSkuStockoutMonthReasonVo;
import com.hete.supply.sda.api.scm.sku.entity.vo.ScmSkuStockoutReasonVo;
import com.hete.supply.sda.api.scm.sku.entity.vo.ScmSkuStockoutVo;
import com.hete.supply.sda.api.scm.sku.facade.ScmSkuStockoutFacade;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.dubbo.util.DubboResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/2/26 15:07
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class SdaRemoteService {
    @DubboReference(check = false)
    private PlatFacade platFacade;

    @DubboReference(check = false)
    private ScmPurchaseFacade scmPurchaseFacade;

    @DubboReference(check = false)
    private ScmProcessOrderFacade scmProcessOrderFacade;

    @DubboReference(check = false)
    private ScmSkuStockoutFacade scmSkuStockoutFacade;

    @DubboReference(check = false)
    private ScmBfFacade scmBfFacade;


    public List<ScmSkuStockoutMonthReasonVo> getSkuStockoutMonthReason(LocalDate createDate) {
        final CommonResult<ResultList<ScmSkuStockoutMonthReasonVo>> data = scmSkuStockoutFacade.getSkuStockoutMonthReason(createDate);
        if (null == data) {
            throw new BizException("调用dubbo接口错误");
        }

        final ResultList<ScmSkuStockoutMonthReasonVo> data1 = data.getData();
        if (null == data1) {
            throw new BizException("数据为空");
        }

        return data1.getList();
    }

    public List<ScmSkuOrderStockoutReasonVo> getSkuStockoutOrderReason(LocalDate createDate) {
        final CommonResult<ResultList<ScmSkuOrderStockoutReasonVo>> data = scmSkuStockoutFacade.getSkuOrderStockoutReason(createDate);
        if (null == data) {
            throw new BizException("调用dubbo接口错误");
        }

        final ResultList<ScmSkuOrderStockoutReasonVo> data1 = data.getData();
        if (null == data1) {
            throw new BizException("数据为空");
        }

        return data1.getList();
    }

    public List<ScmSkuStockoutReasonVo> getSkuStockoutReason(LocalDate createDate) {
        final CommonResult<ResultList<ScmSkuStockoutReasonVo>> data = scmSkuStockoutFacade.getSkuStockoutReason(createDate);
        if (null == data) {
            throw new BizException("调用dubbo接口错误");
        }

        final ResultList<ScmSkuStockoutReasonVo> data1 = data.getData();
        if (null == data1) {
            throw new BizException("数据为空");
        }

        return data1.getList();
    }

    public List<ScmSkuStockoutVo> getSkuStockout(LocalDate createDate) {
        final CommonResult<ResultList<ScmSkuStockoutVo>> data = scmSkuStockoutFacade.getSkuStockout(createDate);
        if (null == data) {
            throw new BizException("调用dubbo接口错误");
        }

        final ResultList<ScmSkuStockoutVo> data1 = data.getData();
        if (null == data1) {
            throw new BizException("数据为空");
        }

        return data1.getList();
    }


    public ScmPurchaseCntVo getPurchaseCntData(LocalDate createDate) {
        final CommonResult<ScmPurchaseCntVo> data = scmPurchaseFacade.getPurchaseCntData(createDate);
        if (null == data) {
            throw new BizException("调用dubbo接口错误");
        }
        final ScmPurchaseCntVo scmPurchaseCntVo = data.getData();
        if (ObjectUtils.isEmpty(scmPurchaseCntVo)) {
            throw new BizException("采购统计数据为空！");
        }

        return scmPurchaseCntVo;
    }

    public String getNameByCode(@NotBlank(message = "平台代码不能为空") String code) {
        final ObtainPlatDto dto = new ObtainPlatDto();
        dto.setPlatCodeList(Collections.singletonList(code));
        final CommonResult<ResultList<ObtainPlatVo>> result = platFacade.obtainPlatByCodeList(dto);
        final ResultList<ObtainPlatVo> data = result.getData();
        final List<ObtainPlatVo> list = data.getList();
        if (CollectionUtils.isEmpty(list)) {
            throw new ParamIllegalException("平台代码：{},获取不到对应的平台名称", code);
        }
        return list.get(0).getPlatName();
    }

    public Map<String, String> getNameMapByCodeList(List<String> codeList) {
        if (CollectionUtils.isEmpty(codeList)) {
            return new HashMap<>();
        }
        final ObtainPlatDto dto = new ObtainPlatDto();
        dto.setPlatCodeList(codeList);
        final CommonResult<ResultList<ObtainPlatVo>> result = platFacade.obtainPlatByCodeList(dto);
        final ResultList<ObtainPlatVo> data = result.getData();
        final List<ObtainPlatVo> list = data.getList();
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .collect(Collectors.toMap(ObtainPlatVo::getPlatCode, ObtainPlatVo::getPlatName));
    }

    public String getCodeByName(@NotBlank(message = "平台名称不能为空") String name) {
        final PlatInfoDto dto = new PlatInfoDto();
        dto.setState(PlatformInfoState.ENABLE);
        final CommonResult<ResultList<PlatVo>> resultListCommonResult = platFacade.obtainPlatList(dto);
        final ResultList<PlatVo> data = resultListCommonResult.getData();
        final List<PlatVo> list = data.getList();
        if (CollectionUtils.isEmpty(list)) {
            throw new ParamIllegalException("数据异常，获取不到启用的平台");
        }
        return list.stream()
                .filter(platVo -> platVo.getPlatName().equals(name))
                .findFirst()
                .map(PlatVo::getPlatCode)
                .orElse("");
    }

    /**
     * 查询本月每一天各平台加工需求
     *
     * @return 查询结果的通用封装
     */
    public DailyPlatformDemandVo queryDailyDemandForPlatforms(LocalDate createDate) {
        CommonResult<DailyPlatformDemandVo> result = scmProcessOrderFacade.queryDailyDemandForPlatforms(createDate);
        return DubboResponseUtil.checkCodeAndGetData(result);
    }

    /**
     * 查询本月每一天各平台出货数
     *
     * @return 查询结果的通用封装
     */
    public DailyPlatformShipmentVo queryDailyShipments(LocalDate createDate) {
        CommonResult<DailyPlatformShipmentVo> result
                = scmProcessOrderFacade.queryDailyShipments(createDate);
        return DubboResponseUtil.checkCodeAndGetData(result);
    }

    public List<ScmPurchaseDeliverDataVo> getPurchaseDeliverData(LocalDate createDate) {
        final CommonResult<ResultList<ScmPurchaseDeliverDataVo>> data = scmPurchaseFacade.getPurchaseDeliverData(createDate);
        if (null == data) {
            throw new BizException("调用dubbo接口错误");
        }

        final ResultList<ScmPurchaseDeliverDataVo> data1 = data.getData();
        if (null == data1) {
            throw new BizException("数据为空");
        }

        return data1.getList();
    }

    public List<ScmPurchaseExcDataVo> getBfPurchaseExcData(LocalDate createDate) {
        final CommonResult<ResultList<ScmPurchaseExcDataVo>> data = scmBfFacade.getBfPurchaseExcData(createDate);
        if (null == data) {
            throw new BizException("调用dubbo接口错误");
        }

        final ResultList<ScmPurchaseExcDataVo> data1 = data.getData();
        if (null == data1) {
            throw new BizException("数据为空");
        }

        return data1.getList();
    }

    public List<ScmQcExcDataVo> getBfQcExcData(LocalDate createDate) {
        final CommonResult<ResultList<ScmQcExcDataVo>> data = scmBfFacade.getBfQcExcData(createDate);
        if (null == data) {
            throw new BizException("调用dubbo接口错误");
        }

        final ResultList<ScmQcExcDataVo> data1 = data.getData();
        if (null == data1) {
            throw new BizException("数据为空");
        }

        return data1.getList();
    }

    public List<ScmReceiveExcDataVo> getBfReceiveExcData(LocalDate createDate) {
        final CommonResult<ResultList<ScmReceiveExcDataVo>> data = scmBfFacade.getBfReceiveExcData(createDate);
        if (null == data) {
            throw new BizException("调用dubbo接口错误");
        }

        final ResultList<ScmReceiveExcDataVo> data1 = data.getData();
        if (null == data1) {
            throw new BizException("数据为空");
        }

        return data1.getList();
    }


}
