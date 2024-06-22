package com.jzo2o.foundations.service;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author qiusen
 * @since 2024-06-06
 */
public interface IServeService extends IService<Serve> {

    /**
     * 区域服务分页查询
     * @param servePageQueryReqDTO
     * @return
     */
    PageResult<ServeResDTO> pageQuery(ServePageQueryReqDTO servePageQueryReqDTO);

    /**
     * 区域服务批量新增
     * @param serveUpsertReqDTOList
     */
    void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList);

    /**
     * 区域服务价格修改
     * @param id
     * @param price
     */
    void updatePrice(Long id, BigDecimal price);

    /**
     * 区域服务上架
     * @param id
     */
    void onSale(Long id);

    /**
     * 区域服务删除
     * @param id
     */
    void del(Long id);

    /**
     * 区域服务下架
     * @param id
     */
    void offSale(Long id);

    /**
     * 设置区域服务为热门
     * @param id
     */
    void onHot(Long id);

    /**
     * 设置区域服务取消热门
     * @param id
     */
    void offHot(Long id);

    /**
     * 获取有区域下有多少启用的服务
     * @param id
     * @param status
     * @return
     */
    int queryServeCountByRegionIdAndSaleStatus(Long id, int status);

    /**
     * 统计存在关联的服务项id且状态为上架
     * @param id 服务项id
     * @param status
     * @return
     */
    int queryServeCountByServerItemAndSaleStatus(Long id, int status);
}
