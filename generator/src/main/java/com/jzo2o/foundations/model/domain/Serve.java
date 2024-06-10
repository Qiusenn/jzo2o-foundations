package com.jzo2o.foundations.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务表
 * </p>
 *
 * @author qiusen
 * @since 2024-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serve")
public class Serve implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 服务项id
     */
    @TableField("serve_item_id")
    private Long serveItemId;

    /**
     * 区域id
     */
    @TableField("region_id")
    private Long regionId;

    /**
     * 城市编码
     */
    @TableField("city_code")
    private String cityCode;

    /**
     * 售卖状态，0：草稿，1下架，2上架
     */
    @TableField("sale_status")
    private Integer saleStatus;

    /**
     * 价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 是否为热门，0非热门，1热门
     */
    @TableField("is_hot")
    private Integer isHot;

    /**
     * 更新为热门的时间戳
     */
    @TableField("hot_time_stamp")
    private Long hotTimeStamp;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新者
     */
    @TableField("update_by")
    private Long updateBy;


}
