package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.MaterialBackDto;
import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptCreateDto;
import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.MaterialBackVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessMaterialReceiptExportVo;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.MaterialBackBo;
import com.hete.supply.scm.server.scm.process.builder.ProcessMaterialReceiptBuilder;
import com.hete.supply.scm.server.scm.process.converter.ProcessMaterialBackConverter;
import com.hete.supply.scm.server.scm.process.converter.ProcessMaterialReceiptConverter;
import com.hete.supply.scm.server.scm.process.converter.ProcessMaterialReceiptImplConverter;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptByNoDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptConfirmDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptDetailDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptDetailVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptItemVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptVo;
import com.hete.supply.scm.server.scm.process.service.base.ProcessMaterialReceiptBaseService;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.ContainerListQueryDto;
import com.hete.supply.wms.api.basic.entity.dto.ContainerUpdateStateDto;
import com.hete.supply.wms.api.basic.entity.vo.Container4ScmListVo;
import com.hete.supply.wms.api.basic.entity.vo.ContainerUpdateStateVo;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/11/16 09:42
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessMaterialReceiptBizService {
    private final ProcessMaterialReceiptDao processMaterialReceiptDao;
    private final ProcessOrderDao processOrderDao;
    private final ProcessMaterialDetailDao processMaterialDetailDao;
    private final ProcessMaterialDetailItemDao processMaterialDetailItemDao;
    private final ProcessMaterialReceiptItemDao processMaterialReceiptItemDao;
    private final ProcessOrderBaseService processOrderBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final PlmRemoteService plmRemoteService;
    private final ProcessMaterialReceiptBaseService processMaterialReceiptBaseService;
    private final RepairOrderDao repairOrderDao;
    private final SdaRemoteService sdaRemoteService;


    /**
     * 分页查询
     *
     * @param processMaterialReceiptQueryDto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessMaterialReceiptVo> getByPage(ProcessMaterialReceiptQueryDto processMaterialReceiptQueryDto) {
        List<String> skus = processMaterialReceiptQueryDto.getSkus();
        if (CollectionUtils.isNotEmpty(skus)) {
            List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                    = processMaterialReceiptItemDao.getBySkus(skus);
            if (CollectionUtils.isEmpty(processMaterialReceiptItemPos)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<Long> processMaterialReceiptIds = processMaterialReceiptItemPos.stream()
                    .map(ProcessMaterialReceiptItemPo::getProcessMaterialReceiptId)
                    .collect(Collectors.toList());
            processMaterialReceiptQueryDto.setProcessMaterialReceiptIds(processMaterialReceiptIds);
        }
        return processMaterialReceiptDao.getByPage(PageDTO.of(processMaterialReceiptQueryDto.getPageNo(), processMaterialReceiptQueryDto.getPageSize()),
                processMaterialReceiptQueryDto);
    }

    /**
     * 通过出库单号查询
     *
     * @param dto
     * @return
     */
    public List<ProcessMaterialReceiptDetailVo> getByNo(ProcessMaterialReceiptByNoDto dto) {
        String deliveryNo = dto.getNo();
        ProcessMaterialReceiptPo processMaterialReceiptPo = processMaterialReceiptDao.getByDeliveryNo(deliveryNo);
        Assert.notNull(processMaterialReceiptPo, () -> new ParamIllegalException("出库单:{}还没有签出，请联系仓库先签出,再扫码", deliveryNo));

        List<ProcessMaterialReceiptDetailVo> processMaterialReceiptDetailVoList
                = this.getByMaterialReceiptList(List.of(processMaterialReceiptPo));

        ProcessOrderPo processOrderPo
                = processOrderDao.getByProcessOrderNo(processMaterialReceiptPo.getProcessOrderNo());
        if (Objects.nonNull(processOrderPo)) {
            processMaterialReceiptDetailVoList.forEach(vo -> {
                vo.setProcessOrderStatus(processOrderPo.getProcessOrderStatus());
                vo.setContainerCode(processOrderPo.getContainerCode());
            });
        }
        return processMaterialReceiptDetailVoList;
    }

    /**
     * 通过加工单号查询
     *
     * @param dto
     * @return
     */
    public List<ProcessMaterialReceiptDetailVo> getByProcessOrderNo(ProcessMaterialReceiptByNoDto dto) {
        String no = dto.getNo();
        List<ProcessMaterialReceiptPo> processMaterialReceiptPos = processMaterialReceiptDao.getByProcessOrderNo(no);
        return this.getByMaterialReceiptList(processMaterialReceiptPos);

    }

    /**
     * 通过原料收货列表获取
     *
     * @param processMaterialReceiptPos
     * @return
     */
    public List<ProcessMaterialReceiptDetailVo> getByMaterialReceiptList(List<ProcessMaterialReceiptPo> processMaterialReceiptPos) {

        if (CollectionUtils.isEmpty(processMaterialReceiptPos)) {
            return new ArrayList<>();
        }

        // 查询关联的详情
        final List<Long> processMaterialReceiptIdList = processMaterialReceiptPos.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                = processMaterialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptIdList);
        Map<Long, List<ProcessMaterialReceiptItemPo>> processMaterialReceiptItemPoMap
                = processMaterialReceiptItemPos.stream()
                .collect(Collectors.groupingBy(ProcessMaterialReceiptItemPo::getProcessMaterialReceiptId));

        return processMaterialReceiptPos.stream()
                .map(item -> {
                    List<ProcessMaterialReceiptItemPo> needProcessMaterialReceiptItemPos
                            = processMaterialReceiptItemPoMap.get(item.getProcessMaterialReceiptId());
                    return ProcessMaterialReceiptImplConverter.poToDetailVo(item, needProcessMaterialReceiptItemPos);
                })
                .collect(Collectors.toList());

    }

    /**
     * 加工原料收货详情
     *
     * @param dto
     * @return
     */
    public ProcessMaterialReceiptDetailVo detail(ProcessMaterialReceiptDetailDto dto) {
        Long processMaterialReceiptId = dto.getProcessMaterialReceiptId();
        ProcessMaterialReceiptPo processMaterialReceiptPo = processMaterialReceiptDao.getById(processMaterialReceiptId);
        if (null == processMaterialReceiptPo) {
            throw new BizException("原料收货单不存在");
        }
        ProcessOrderPo processOrderPo
                = processOrderDao.getByProcessOrderNo(processMaterialReceiptPo.getProcessOrderNo());
        RepairOrderPo repairOrderPo
                = repairOrderDao.getByReceiveOrderNo(processMaterialReceiptPo.getRepairOrderNo());

        // 查询关联的详情
        ArrayList<Long> processMaterialReceiptIds = new ArrayList<>();
        processMaterialReceiptIds.add(processMaterialReceiptId);
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                = processMaterialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptIds);
        ProcessMaterialReceiptDetailVo processMaterialReceiptDetailVo
                = ProcessMaterialReceiptImplConverter.poToDetailVo(processMaterialReceiptPo, processMaterialReceiptItemPos);
        List<ProcessMaterialReceiptItemVo> materialReceiptItems
                = processMaterialReceiptDetailVo.getMaterialReceiptItems();
        List<String> skuList = materialReceiptItems.stream()
                .map(ProcessMaterialReceiptItemVo::getSku)
                .collect(Collectors.toList());
        List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(skuList);
        if (CollectionUtils.isEmpty(skuEncodeBySku)) {
            return processMaterialReceiptDetailVo;
        }

        Map<String, List<PlmSkuVo>> groupedPlmSkuVo = skuEncodeBySku.stream()
                .collect(Collectors.groupingBy(PlmSkuVo::getSkuCode));
        materialReceiptItems = materialReceiptItems.stream()
                .peek(item -> {
                    List<PlmSkuVo> plmSkuVos = groupedPlmSkuVo.get(item.getSku());
                    if (CollectionUtils.isNotEmpty(plmSkuVos)) {
                        Optional<PlmSkuVo> firstPlmSkuVoOptional = plmSkuVos.stream()
                                .findFirst();
                        firstPlmSkuVoOptional.ifPresent(plmSkuVo -> item.setSkuEncode(plmSkuVo.getSkuEncode()));
                    }
                })
                .collect(Collectors.toList());
        processMaterialReceiptDetailVo.setMaterialReceiptItems(materialReceiptItems);
        if (Objects.nonNull(processOrderPo)) {
            processMaterialReceiptDetailVo.setProcessOrderStatus(processOrderPo.getProcessOrderStatus());
        }
        if (Objects.nonNull(repairOrderPo)) {
            processMaterialReceiptDetailVo.setRepairOrderStatus(repairOrderPo.getRepairOrderStatus());
        }
        return processMaterialReceiptDetailVo;

    }


    /**
     * 创建原料收货单
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmConstant.PROCESS_MATERIAL_RECEIPT_LOCK_PREFIX, key = "#dto.processOrderNo", waitTime = 1, leaseTime = -1)
    public Boolean create(ProcessMaterialReceiptCreateDto dto) {
        String processOrderNo = dto.getProcessOrderNo();
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (null == processOrderPo) {
            throw new BizException("关联的加工单不存在");
        }

        // 需要判断是否已重复创建
        String deliveryNo = dto.getDeliveryNo();
        ProcessMaterialReceiptPo queryProcessMaterialReceiptPo = processMaterialReceiptDao.getByDeliveryNo(deliveryNo);
        if (null != queryProcessMaterialReceiptPo) {
            log.warn("已创建原料收货单：{}", dto);
            return true;
        }

        ProcessMaterialReceiptPo processMaterialReceiptPo = ProcessMaterialReceiptConverter.INSTANCE.convert(dto);
        processMaterialReceiptPo.setPlaceOrderTime(processOrderPo.getCreateTime());
        processMaterialReceiptPo.setPlaceOrderUser(processOrderPo.getCreateUser());
        processMaterialReceiptPo.setPlaceOrderUsername(processOrderPo.getCreateUsername());
        processMaterialReceiptPo.setPlatform(processOrderPo.getPlatform());
        processMaterialReceiptPo.setProcessMaterialReceiptStatus(ProcessMaterialReceiptStatus.WAIT_RECEIVE);
        processMaterialReceiptPo.setDeliveryNote(processOrderPo.getDeliveryNote());
        processMaterialReceiptPo.setMaterialReceiptType(MaterialReceiptType.PROCESSING_MATERIAL);
        processMaterialReceiptDao.insert(processMaterialReceiptPo);

        // 收货明细
        List<ProcessMaterialReceiptCreateDto.MaterialReceiptItem> materialReceiptItems = dto.getMaterialReceiptItems();
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos = materialReceiptItems.stream()
                .map(it -> {
                    ProcessMaterialReceiptItemPo processMaterialReceiptItemPo = new ProcessMaterialReceiptItemPo();
                    processMaterialReceiptItemPo.setProcessMaterialReceiptId(processMaterialReceiptPo.getProcessMaterialReceiptId());
                    processMaterialReceiptItemPo.setSku(it.getSku());
                    processMaterialReceiptItemPo.setSkuBatchCode(it.getSkuBatchCode());
                    processMaterialReceiptItemPo.setDeliveryNum(it.getDeliveryNum());
                    return processMaterialReceiptItemPo;
                })
                .collect(Collectors.toList());
        processMaterialReceiptItemDao.insertBatch(processMaterialReceiptItemPos);

        // 计算加工单的可加工成品数
        ProcessMaterialDetailPo processMaterialDetailPo = processMaterialDetailDao.getByDeliveryNo(deliveryNo);
        if (null != processMaterialDetailPo) {
            // 获取原料明细详情
            List<ProcessMaterialDetailItemPo> processMaterialDetailItemPoList
                    = processMaterialDetailItemDao.getByProcessMaterialDetailId(processMaterialDetailPo.getProcessMaterialDetailId());
            if (CollectionUtils.isEmpty(processMaterialDetailItemPoList)) {
                // 加工单原料明细表详情
                List<ProcessMaterialDetailItemPo> newProcessMaterialDetailItemPoList = materialReceiptItems.stream()
                        .map(item -> {
                            ProcessMaterialDetailItemPo processMaterialDetailItemPo = new ProcessMaterialDetailItemPo();
                            processMaterialDetailItemPo.setProcessMaterialDetailId(processMaterialDetailPo.getProcessMaterialDetailId());
                            processMaterialDetailItemPo.setSku(item.getSku());
                            processMaterialDetailItemPo.setSkuBatchCode(item.getSkuBatchCode());
                            processMaterialDetailItemPo.setDeliveryNum(item.getDeliveryNum());
                            return processMaterialDetailItemPo;
                        })
                        .collect(Collectors.toList());
                processMaterialDetailItemDao.insertBatch(newProcessMaterialDetailItemPoList);
                MaterialBackBo materialBackBo
                        = processOrderBaseService.getBackStatus(processMaterialDetailPo.getProcessOrderNo());
                processOrderPo.setMaterialBackStatus(materialBackBo.getMaterialBackStatus());
                processOrderDao.updateByIdVersion(processOrderPo);
            } else {
                processOrderDao.updateByIdVersion(processOrderPo);
            }
        }

        // 创建日志
        processMaterialReceiptBaseService.createStatusChangeLog(processMaterialReceiptPo);


        return true;
    }


    /**
     * 确认收货
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmReceipt(ProcessMaterialReceiptConfirmDto dto) {
        ProcessMaterialReceiptPo processMaterialReceiptPo
                = processMaterialReceiptDao.getById(dto.getProcessMaterialReceiptId());
        if (null == processMaterialReceiptPo) {
            throw new ParamIllegalException("原料收货单不存在");
        }
        if (ProcessMaterialReceiptStatus.RECEIVED.equals(processMaterialReceiptPo.getProcessMaterialReceiptStatus())) {
            throw new ParamIllegalException("原料收货单已收货");
        }

        MaterialReceiptType materialReceiptType = processMaterialReceiptPo.getMaterialReceiptType();
        if (MaterialReceiptType.PROCESSING_MATERIAL.equals(materialReceiptType)) {
            ProcessOrderPo queriedProcessOrderPo
                    = processOrderDao.getByProcessOrderNo(processMaterialReceiptPo.getProcessOrderNo());
            if (null == queriedProcessOrderPo) {
                throw new ParamIllegalException("加工单不存在");
            }
            final String processOrderNo = queriedProcessOrderPo.getProcessOrderNo();

            List<ProcessDeliveryOrderVo> processDeliveryOrderVoList
                    = wmsRemoteService.getProcessDeliveryOrder(processMaterialReceiptPo.getProcessOrderNo(), WmsEnum.DeliveryType.PROCESS);

            // 若加工单已经关联了容器编码，则不需要重新到wms获取
            if (StringUtils.isNotBlank(queriedProcessOrderPo.getContainerCode())) {
                // 若加工单已经关联了容器编码，判断是否是待投产且无需排产，如果是则释放容器
                if (ProcessOrderStatus.WAIT_PRODUCE.equals(queriedProcessOrderPo.getProcessOrderStatus())
                        && NeedProcessPlan.FALSE.equals(queriedProcessOrderPo.getNeedProcessPlan())) {
                    final ContainerUpdateStateDto containerUpdateStateDto = new ContainerUpdateStateDto();
                    containerUpdateStateDto.setContainerCode(queriedProcessOrderPo.getContainerCode());
                    containerUpdateStateDto.setWarehouseCode(ScmConstant.PROCESS_WAREHOUSE_CODE);
                    containerUpdateStateDto.setState(WmsEnum.ContainerState.IDLE);
                    wmsRemoteService.updateContainerState(containerUpdateStateDto);
                    processOrderBaseService.clearContainerCode(processOrderNo);
                    queriedProcessOrderPo
                            = processOrderDao.getByProcessOrderNo(processMaterialReceiptPo.getProcessOrderNo());
                }

                processMaterialReceiptBaseService.confirmReceiptByMaterialReceiptPo(processMaterialReceiptPo, dto,
                        queriedProcessOrderPo, processDeliveryOrderVoList, "");
                return;
            } else {
                // 若加工单没关联容器编码，判断是否是待投产且无需排产，如果是则无需绑定容器
                if (ProcessOrderStatus.WAIT_PRODUCE.equals(queriedProcessOrderPo.getProcessOrderStatus())
                        && NeedProcessPlan.FALSE.equals(queriedProcessOrderPo.getNeedProcessPlan())) {
                    processMaterialReceiptBaseService.confirmReceiptByMaterialReceiptPo(processMaterialReceiptPo, dto,
                            queriedProcessOrderPo, processDeliveryOrderVoList, "");
                    return;
                }
            }

            // 查找wms空闲容器
            final ContainerListQueryDto containerListQueryDto = new ContainerListQueryDto();
            containerListQueryDto.setWarehouseCode(ScmConstant.PROCESS_WAREHOUSE_CODE);
            containerListQueryDto.setState(WmsEnum.ContainerState.IDLE);
            final List<Container4ScmListVo> wmsContainerList
                    = wmsRemoteService.getWmsContainerList(containerListQueryDto);

            if (CollectionUtils.isEmpty(wmsContainerList)) {
                throw new ParamIllegalException("仓储不存在空闲的容器，请联系仓储人员处理");
            }

            final Container4ScmListVo container4ScmListVo = wmsContainerList.stream()
                    .min(Comparator.comparing(Container4ScmListVo::getContainerCode))
                    .orElse(new Container4ScmListVo());
            final ContainerUpdateStateDto containerUpdateStateDto = new ContainerUpdateStateDto();
            containerUpdateStateDto.setContainerCode(container4ScmListVo.getContainerCode());
            containerUpdateStateDto.setWarehouseCode(ScmConstant.PROCESS_WAREHOUSE_CODE);
            containerUpdateStateDto.setState(WmsEnum.ContainerState.OCCUPIED);
            final ContainerUpdateStateVo containerUpdateStateVo
                    = wmsRemoteService.updateContainerState(containerUpdateStateDto);
            if (null == containerUpdateStateVo) {
                throw new ParamIllegalException("仓储容器占用失败，请联系仓储人员处理");

            }

            processMaterialReceiptBaseService.confirmReceiptByMaterialReceiptPo(processMaterialReceiptPo, dto,
                    queriedProcessOrderPo, processDeliveryOrderVoList, containerUpdateStateVo.getContainerCode());
        } else if (MaterialReceiptType.REPAIR_MATERIAL.equals(materialReceiptType)) {
            String repairOrderNo = processMaterialReceiptPo.getRepairOrderNo();
            RepairOrderPo repairOrderPo = repairOrderDao.getByRepairOrderNo(repairOrderNo);
            if (Objects.isNull(repairOrderPo)) {
                throw new BizException("确认收货失败!原料收货单关联返修单信息不存在，返修单号：{}", repairOrderNo);
            }

            List<ProcessMaterialReceiptConfirmDto.ProcessMaterialReceiptItem> updateReceiptItemDtoList
                    = dto.getProcessMaterialReceiptItems();
            List<ProcessMaterialReceiptItemPo> updateMaterialReceiptItemPos
                    = ProcessMaterialReceiptBuilder.buildProcessMaterialReceiptItemPos(updateReceiptItemDtoList);
            processMaterialReceiptItemDao.updateBatchByIdVersion(updateMaterialReceiptItemPos);

            Integer processMaterialReceiptVersion = dto.getVersion();
            ProcessMaterialReceiptBuilder.buildProcessMaterialReceiptPo(processMaterialReceiptPo, processMaterialReceiptVersion);
            processMaterialReceiptDao.updateByIdVersion(processMaterialReceiptPo);

            List<ProcessDeliveryOrderVo> warehouseRelatedMaterialReceiptOrders
                    = wmsRemoteService.getProcessDeliveryOrder(processMaterialReceiptPo.getRepairOrderNo(), WmsEnum.DeliveryType.PROCESS);
            int warehouseUnshippedCount = (int) warehouseRelatedMaterialReceiptOrders.stream()
                    .filter(it -> !it.getDeliveryState()
                            .equals(WmsEnum.DeliveryState.SIGNED_OFF) && !it.getDeliveryState()
                            .equals(WmsEnum.DeliveryState.FINISHED) && !it.getDeliveryState()
                            .equals(WmsEnum.DeliveryState.CANCELING) && !it.getDeliveryState()
                            .equals(WmsEnum.DeliveryState.CANCELED))
                    .count();

            List<ProcessMaterialReceiptPo> relatedMaterialReceipts
                    = processMaterialReceiptDao.listByRepairOrderNo(repairOrderNo);
            int unreceivedCount = (int) relatedMaterialReceipts.stream()
                    .filter(it -> ProcessMaterialReceiptStatus.WAIT_RECEIVE.equals(it.getProcessMaterialReceiptStatus()))
                    .count();
            if (warehouseUnshippedCount == 0 && unreceivedCount == 0) {
                repairOrderPo.setIsReceiveMaterial(IsReceiveMaterial.TRUE);
                repairOrderDao.updateByIdVersion(repairOrderPo);
            }

            // 创建原料收货单日志
            processMaterialReceiptBaseService.createStatusChangeLog(processMaterialReceiptPo);
        } else {
            throw new BizException("确认收货失败！无原料对应类型信息，请联系相关业务人员！");
        }
    }


    /**
     * 获取需要导出的总数量
     *
     * @param dto
     * @return
     */
    public Integer getExportTotals(ProcessMaterialReceiptQueryByApiDto dto) {
        List<String> skus = dto.getSkus();
        if (CollectionUtils.isNotEmpty(skus)) {
            List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                    = processMaterialReceiptItemDao.getBySkus(skus);
            if (CollectionUtils.isEmpty(processMaterialReceiptItemPos)) {
                return 0;
            }
            List<Long> processMaterialReceiptIds = processMaterialReceiptItemPos.stream()
                    .map(ProcessMaterialReceiptItemPo::getProcessMaterialReceiptId)
                    .collect(Collectors.toList());
            dto.setProcessMaterialReceiptIds(processMaterialReceiptIds);
        }
        return processMaterialReceiptDao.getExportTotals(dto);
    }

    /**
     * 查询导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessMaterialReceiptExportVo> getExportList(ProcessMaterialReceiptQueryByApiDto dto) {
        List<String> skus = dto.getSkus();
        if (CollectionUtils.isNotEmpty(skus)) {
            List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                    = processMaterialReceiptItemDao.getBySkus(skus);
            if (CollectionUtils.isEmpty(processMaterialReceiptItemPos)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<Long> processMaterialReceiptIds = processMaterialReceiptItemPos.stream()
                    .map(ProcessMaterialReceiptItemPo::getProcessMaterialReceiptId)
                    .collect(Collectors.toList());
            dto.setProcessMaterialReceiptIds(processMaterialReceiptIds);
        }
        CommonPageResult.PageInfo<ProcessMaterialReceiptExportVo> exportList
                = processMaterialReceiptDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        List<ProcessMaterialReceiptExportVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return exportList;
        }

        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(ProcessMaterialReceiptExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        exportList.setRecords(records.stream()
                .peek(item -> item.setPlatform(platCodeNameMap.get(item.getPlatform())))
                .collect(Collectors.toList()));
        return exportList;
    }

    public MaterialBackVo getMaterialBackInfo(MaterialBackDto dto) {
        final String processOrderNo = dto.getProcessOrderNo();
        final String skuBatchCode = dto.getSkuBatchCode();

        // 获取加工单原料可归还信息
        final MaterialBackBo materialBackBo = processOrderBaseService.getBackStatus(processOrderNo);
        if (Objects.isNull(materialBackBo)) {
            return ProcessMaterialBackConverter.convertToVo(skuBatchCode, null);
        }
        final List<MaterialBackBo.MaterialBackSku> materialBackSkus = materialBackBo.getMaterialBackSkus();
        if (CollectionUtils.isEmpty(materialBackSkus)) {
            return ProcessMaterialBackConverter.convertToVo(skuBatchCode, null);
        }

        // 匹配批次码原料可归还信息
        final MaterialBackBo.MaterialBackSku matchBackInfo
                = materialBackSkus.stream()
                .filter(materialBackSku -> Objects.equals(skuBatchCode, materialBackSku.getSkuBatchCode()))
                .findFirst()
                .orElse(null);
        return ProcessMaterialBackConverter.convertToVo(skuBatchCode, matchBackInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmConstant.PROCESS_MATERIAL_RECEIPT_LOCK_PREFIX, key = "#dto.repairOrderNo", waitTime = 1, leaseTime = -1)
    public void createRepairMaterialReceipt(ProcessMaterialReceiptCreateDto dto) {
        String repairOrderNo = dto.getRepairOrderNo();
        RepairOrderPo repairOrderPo = repairOrderDao.getByRepairOrderNo(repairOrderNo);
        if (null == repairOrderPo) {
            throw new BizException("创建返修原料收货单失败，关联的返修单不存在！{}", repairOrderNo);
        }

        String deliveryNo = dto.getDeliveryNo();
        ProcessMaterialReceiptPo queryProcessMaterialReceiptPo = processMaterialReceiptDao.getByDeliveryNo(deliveryNo);
        if (null != queryProcessMaterialReceiptPo) {
            log.warn("返修原料收货单已创建！出库单号：{}", deliveryNo);
            return;
        }

        ProcessMaterialReceiptPo processMaterialReceiptPo
                = ProcessMaterialReceiptBuilder.buildProcessMaterialReceiptPo(repairOrderPo, dto);
        processMaterialReceiptDao.insert(processMaterialReceiptPo);

        Long processMaterialReceiptId = processMaterialReceiptPo.getProcessMaterialReceiptId();
        List<ProcessMaterialReceiptCreateDto.MaterialReceiptItem> createReceiptItems = dto.getMaterialReceiptItems();
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                = ProcessMaterialReceiptBuilder.buildProcessMaterialReceiptItemPos(processMaterialReceiptId, createReceiptItems);
        processMaterialReceiptItemDao.insertBatch(processMaterialReceiptItemPos);

        processMaterialReceiptBaseService.createStatusChangeLog(processMaterialReceiptPo);
    }
}
