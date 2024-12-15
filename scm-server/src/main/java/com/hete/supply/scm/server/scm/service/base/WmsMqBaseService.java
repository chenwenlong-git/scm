package com.hete.supply.scm.server.scm.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.process.entity.dto.UpdateBatchCodePriceDto;
import com.hete.supply.scm.server.scm.process.handler.BatchCodePriceUpdateHandler;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author ChenWenLong
 * @date 2024/1/29 13:50
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WmsMqBaseService {

    private final ConsistencySendMqService consistencySendMqService;

    /**
     * 指定分割发送MQ的大小
     */
    private static final Integer SPLIT_SIZE = 50;


    /**
     * 分割进行分次执行更新批次码单价的MQ
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/1/29 14:10
     */
    public void execSendUpdateBatchCodePriceMq(UpdateBatchCodePriceDto dto) {
        if (dto == null || CollectionUtils.isEmpty(dto.getBatchCodePriceList())) {
            return;
        }

        // 进行分割
        List<List<UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice>> updateBatchCodeCostPriceList = IntStream.range(0, (dto.getBatchCodePriceList().size() + SPLIT_SIZE - 1) / SPLIT_SIZE)
                .mapToObj(i -> dto.getBatchCodePriceList().subList(i * SPLIT_SIZE, Math.min((i + 1) * SPLIT_SIZE, dto.getBatchCodePriceList().size())))
                .collect(Collectors.toList());

        List<UpdateBatchCodePriceDto> updateBatchCodePriceDtoList = new ArrayList<>();
        for (List<UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice> updateBatchCodeCostPricesList : updateBatchCodeCostPriceList) {
            UpdateBatchCodePriceDto updateBatchCodePriceDto = new UpdateBatchCodePriceDto();
            updateBatchCodePriceDto.setBatchCodePriceList(updateBatchCodeCostPricesList);
            updateBatchCodePriceDtoList.add(updateBatchCodePriceDto);
        }
        log.info("批量更新批次码单价的MQ，dto={}", JacksonUtil.parse2Str(updateBatchCodePriceDtoList));

        consistencySendMqService.batchSaveMq(BatchCodePriceUpdateHandler.class, updateBatchCodePriceDtoList);

    }


}
