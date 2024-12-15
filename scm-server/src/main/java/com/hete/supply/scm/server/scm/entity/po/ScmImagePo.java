package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * scm图片表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("scm_image")
@ApiModel(value = "ScmImagePo对象", description = "scm图片表")
public class ScmImagePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "scm_image_id", type = IdType.ASSIGN_ID)
    private Long scmImageId;


    @ApiModelProperty(value = "图片文件编码")
    private String fileCode;


    @ApiModelProperty(value = "业务枚举")
    private ImageBizType imageBizType;


    @ApiModelProperty(value = "业务id")
    private Long imageBizId;


}
