package com.jzo2o.foundations.service;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Slf4j
class IServeServiceTest {
    @Resource
    private IServeService serveService;

    //分页测试
    @Test
    public void test_page(){
        ServePageQueryReqDTO servePageQueryReqDTO = new ServePageQueryReqDTO();
        servePageQueryReqDTO.setRegionId(1686303222843662337L);
        servePageQueryReqDTO.setPageNo(1L);
        servePageQueryReqDTO.setPageSize(3L);
        PageResult<ServeResDTO> page = serveService.page(servePageQueryReqDTO);
        log.info("page : {}", page);
        Assert.notEmpty(page.getList(),"列表为空");
    }

    //区域服务查询
    @Test
    public void test_queryServeByIdCache(){
        Serve serve = serveService.queryServeByIdCache(1693815623867506689L);
        Assert.notNull(serve,"服务为空");
    }

    //服务上架测试
    @Test
    public void test_onSale(){
        //从serve表找一条下架的服务（sale_status  '售卖状态，0：草稿，1下架，2上架',）
        Serve serve = serveService.onSale(1715263395009191938L);
        Assert.notNull(serve,"服务为空");
    }

    //服务下架测试
    @Test
    public void test_offSale(){
        //从serve表找一条上架的服务（sale_status  '售卖状态，0：草稿，1下架，2上架',）
        Serve serve = serveService.offSale(1715263395009191938L);
        Assert.notNull(serve,"服务为空");
    }

    //区域服务查询
    @Test
    public void test_queryServeByIdCache2(){
        //指定一个不存在serve表的id
        Serve serve = serveService.queryServeByIdCache(123L);
        Assert.notNull(serve,"服务为空");
    }

//    @Test
//    void listServeItemByCityCode() {
//        List<Long> longs = serveService.queryServeItemIdListByCityCode("010");
//        System.out.println(longs);
//    }

//    @Test
//    void findHotServeListByCityCode() {
//        List<ServeAggregationSimpleResDTO> list = serveService.findHotServeListByCityCode("010");
//        System.out.println(list);
//    }
//
//    @Test
//    void findServeTypeListByCityCode() {
//        List<ServeAggregationTypeSimpleResDTO> list = serveService.findServeTypeListByCityCode("010");
//        System.out.println(list);
//    }
//
//    @Test
//    void findServeIconCategoryByCityCode() {
//        List<ServeCategoryResDTO> list = serveService.findServeIconCategoryByCityCode("010");
//        System.out.println(list.toString());
//    }

//    @Test
//    void findDetailById() {
//        ServeAggregationSimpleResDTO detail = serveService.findDetailById(1693815624114970626L);
//        System.out.println(detail);
//    }
//
//
//    @Test
//    void update() {
//        serveService.update(1693815624114970626L, BigDecimal.valueOf(38.3));
//    }
//
//    @Test
//    void changeHotStatus() {
//        serveService.changeHotStatus(1693815624114970626L, 1);
//    }



}