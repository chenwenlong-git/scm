package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.InventoryShortageReportExportDto;
import com.hete.supply.scm.api.scm.entity.dto.ProcessOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.enums.MissingInformation;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderExportByItemVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderExportByMaterialVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderExportByOrderVo;
import com.hete.supply.scm.server.scm.entity.bo.OrdersToProcessPlanParametersBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderQueryBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessPlanOrderBo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 加工单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Mapper
interface ProcessOrderMapper extends BaseDataMapper<ProcessOrderPo> {


    /**
     * 查询列表
     *
     * @param page
     * @param processOrderQueryDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    IPage<ProcessOrderVo> getByPage(Page<Void> page, @Param("params") ProcessOrderQueryDto processOrderQueryDto,
                                    @Param("processOrderQueryBo") ProcessOrderQueryBo processOrderQueryBo,
                                    @Param("processOrderStatus") ProcessOrderStatus processOrderStatus);

    /**
     * 查询总数（按单查询）
     *
     * @param processOrderQueryByApiDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    Integer getExportTotalsByOrder(@Param("params") ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                   @Param("processOrderQueryBo") ProcessOrderQueryBo processOrderQueryBo,
                                   @Param("processOrderStatus") ProcessOrderStatus processOrderStatus);

    /**
     * 查询列表（按单查询）
     *
     * @param page
     * @param processOrderQueryByApiDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    IPage<ProcessOrderExportByOrderVo> getExportListByOrder(Page<Void> page, @Param("params") ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                                            @Param("processOrderQueryBo") ProcessOrderQueryBo processOrderQueryBo,
                                                            @Param("processOrderStatus") ProcessOrderStatus processOrderStatus);

    /**
     * 查询总数（按sku查询）
     *
     * @param processOrderQueryByApiDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    Integer getExportTotalsByItem(@Param("params") ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                  @Param("processOrderQueryBo") ProcessOrderQueryBo processOrderQueryBo,
                                  @Param("processOrderStatus") ProcessOrderStatus processOrderStatus);

    /**
     * 查询列表（按sku查询）
     *
     * @param page
     * @param processOrderQueryByApiDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    IPage<ProcessOrderExportByItemVo> getExportListByItem(Page<Void> page, @Param("params") ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                                          @Param("processOrderQueryBo") ProcessOrderQueryBo processOrderQueryBo,
                                                          @Param("processOrderStatus") ProcessOrderStatus processOrderStatus);

    /**
     * 查询总数（按sku查询）
     *
     * @param processOrderQueryByApiDto
     * @param processOrderQueryBo
     * @param processOrderStatus
     * @return
     */
    Integer getExportTotalsByMaterial(@Param("params") ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                      @Param("processOrderQueryBo") ProcessOrderQueryBo processOrderQueryBo,
                                      @Param("processOrderStatus") ProcessOrderStatus processOrderStatus);

    /**
     * @Description 原料导出分页查询
     * @author yanjiawei
     * @Date 2024/12/3 上午10:53
     */
    IPage<ProcessOrderExportByMaterialVo> getExportListByMaterial(Page<Void> page, @Param("params") ProcessOrderQueryByApiDto processOrderQueryByApiDto,
                                                                  @Param("processOrderQueryBo") ProcessOrderQueryBo processOrderQueryBo,
                                                                  @Param("processOrderStatus") ProcessOrderStatus processOrderStatus);

    /**
     * 获取需要排产的数据
     *
     * @param param
     * @return
     */
    List<ProcessPlanOrderBo> getOrdersToProcessPlan(@Param("param") OrdersToProcessPlanParametersBo param);

    /**
     * 查询需要排产数据条数
     *
     * @param param
     * @return
     */
    int getNeedProcessPlanCount(OrdersToProcessPlanParametersBo param);

    /**
     * 获取库存缺货报告总数。
     *
     * @param dto 包含查询条件的库存缺货报告导出数据传输对象
     * @return 库存缺货报告总数
     */
    Integer getInventoryShortageReportTotals(@Param("param") InventoryShortageReportExportDto dto);

    int countStoredNumBetweenTimes(LocalDateTime dayOfBeginTime,
                                   LocalDateTime dayOfEndTime);

    IPage<ProcessOrderPo> getDeliveryWarehouseInitData(Page<ProcessOrderPo> page,
                                                       ProcessOrderType processOrderType,
                                                       ProcessOrderStatus processOrderStatus,
                                                       List<MissingInformation> missInfoList,
                                                       String deliveryWarehouseCode,
                                                       List<String> skuList);

}
