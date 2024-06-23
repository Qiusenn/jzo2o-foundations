package com.jzo2o.foundations.service;

import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import java.util.List;

/**
 * 首页查询相关功能
 *
 * @author itcast
 * @create 2023/8/21 10:55
 **/
public interface HomeService {
    /**
     * 根据区域id获取服务图标信息
     *
     * @param regionId 区域id
     * @return 服务图标列表
     */
    List<ServeCategoryResDTO> queryServeIconCategoryByRegionIdCache(Long regionId);

    /**
     * 服务分类列表
     * @param regionId
     * @return
     */
    List<ServeAggregationTypeSimpleResDTO> queryServeTypeList(Long regionId);

    /**
     * 首页热门服务列表
     * @param regionId
     * @return
     */
    List<ServeAggregationSimpleResDTO> queryHotServeListByRegionIdCache(Long regionId);

    /**
     * 根据id查询服务
     * @param id
     * @return
     */
    ServeAggregationSimpleResDTO queryServeDetailByServeId(Long id);
}