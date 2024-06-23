package com.jzo2o.foundations.controller.consumer;

import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */

@Validated
@RestController("consumerServeController")
@RequestMapping("/customer/serve")
@Api(tags = "用户端 - 首页服务查询接口")
public class FirstPageServeController {

    @Resource
    private HomeService homeService;

    @GetMapping("/firstPageServeList")
    @ApiOperation("首页服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeCategoryResDTO> serveCategory(@RequestParam("regionId") Long regionId) {
        List<ServeCategoryResDTO> serveCategoryResDTOS = homeService.queryServeIconCategoryByRegionIdCache(regionId);
        return serveCategoryResDTOS;
    }

    @GetMapping("/serveTypeList")
    @ApiOperation("服务分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeAggregationTypeSimpleResDTO> serveTypeList(@RequestParam("regionId") Long regionId) {
        List<ServeAggregationTypeSimpleResDTO> dtos = homeService.queryServeTypeList(regionId);
        return dtos;
    }

    @GetMapping("/hotServeList")
    @ApiOperation("首页热门服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeAggregationSimpleResDTO> hotServeList(@RequestParam("regionId") Long regionId) {
        List<ServeAggregationSimpleResDTO> dtos = homeService.queryHotServeListByRegionIdCache(regionId);
        return dtos;
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public ServeAggregationSimpleResDTO queryServeDetailByServeId(@PathVariable("id") Long id) {
        ServeAggregationSimpleResDTO dtos = homeService.queryServeDetailByServeId(id);
        return dtos;
    }
}