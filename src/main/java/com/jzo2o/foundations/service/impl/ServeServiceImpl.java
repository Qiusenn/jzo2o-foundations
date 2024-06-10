package com.jzo2o.foundations.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.enums.FoundationHotEnum;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.mysql.utils.PageHelperUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author qiusen
 * @since 2024-06-06
 */
@Service
@RequiredArgsConstructor
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {


    private final ServeItemMapper serveItemMapper;

    private final RegionMapper regionMapper;
    /**
     * 区域服务分页查询
     * @param servePageQueryReqDTO
     * @return
     */
    @Override
    public PageResult<ServeResDTO> pageQuery(ServePageQueryReqDTO servePageQueryReqDTO) {
        return PageHelperUtils.selectPage(servePageQueryReqDTO,
                () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));
    }

    /**
     * 区域服务批量新增
     * @param serveUpsertReqDTOList
     */
    @Override
    @Transactional
    public void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        for (ServeUpsertReqDTO serveUpsertReqDTO : serveUpsertReqDTOList) {
            //1.校验服务项是否为启用状态，不是启用状态不能新增
            ServeItem serveItem = serveItemMapper.selectById(serveUpsertReqDTO.getServeItemId());
            //如果服务项信息不存在或未启用
            if(ObjectUtil.isNull(serveItem) || serveItem.getActiveStatus()!= FoundationStatusEnum.ENABLE.getStatus()){
                throw new ForbiddenOperationException("该服务未启用无法添加到区域下使用");
            }

            //2.校验是否重复新增
            Integer count = lambdaQuery()
                    .eq(Serve::getRegionId, serveUpsertReqDTO.getRegionId())
                    .eq(Serve::getServeItemId, serveUpsertReqDTO.getServeItemId())
                    .count();
            if(count>0){
                throw new ForbiddenOperationException(serveItem.getName()+"服务已存在");
            }

            //3.新增服务
            Serve serve = BeanUtil.toBean(serveUpsertReqDTO, Serve.class);
            Region region = regionMapper.selectById(serveUpsertReqDTO.getRegionId());
            serve.setCityCode(region.getCityCode());
            baseMapper.insert(serve);
        }
    }

    /**
     * 区域服务价格修改
     * @param id
     * @param price
     */
    @Override
    public void updatePrice(Long id, BigDecimal price) {
        //1.更新服务价格
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getPrice, price)
                .update();

        if(!update){
            throw new CommonException("修改服务价格失败");
        }
    }

    /**
     * 区域服务上架
     * @param id
     */
    @Override
    @Transactional
    public void onSale(Long id){
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //上架状态
        Integer saleStatus = serve.getSaleStatus();
        //草稿或下架状态方可上架
        if (!(saleStatus==FoundationStatusEnum.INIT.getStatus() || saleStatus==FoundationStatusEnum.DISABLE.getStatus())) {
            throw new ForbiddenOperationException("草稿或下架状态方可上架");
        }
        //服务项id
        Long serveItemId = serve.getServeItemId();
        ServeItem serveItem = serveItemMapper.selectById(serveItemId);
        if(ObjectUtil.isNull(serveItem)){
            throw new ForbiddenOperationException("所属服务项不存在");
        }
        //服务项的启用状态
        Integer activeStatus = serveItem.getActiveStatus();
        //服务项为启用状态方可上架
        if (!(FoundationStatusEnum.ENABLE.getStatus()==activeStatus)) {
            throw new ForbiddenOperationException("服务项为启用状态方可上架");
        }

        //更新上架状态
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus())
                .update();
        if(!update){
            throw new CommonException("启动服务失败");
        }
    }

    /**
     * 区域服务删除
     * @param id
     */
    @Override
    public void del(Long id) {
        getBaseMapper().deleteById(id);
    }

    /**
     * 区域服务下架
     * @param id
     */
    @Override
    @Transactional
    public void offSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //上架状态
        Integer saleStatus = serve.getSaleStatus();
        if (!(saleStatus==FoundationStatusEnum.ENABLE.getStatus())) {
            throw new ForbiddenOperationException("商品未上架");
        }
        //服务项id
        Long serveItemId = serve.getServeItemId();
        ServeItem serveItem = serveItemMapper.selectById(serveItemId);
        if(ObjectUtil.isNull(serveItem)){
            throw new ForbiddenOperationException("所属服务项不存在");
        }
        //服务项的启用状态
        Integer activeStatus = serveItem.getActiveStatus();
        //服务项为启用状态方可上架
        if (!(FoundationStatusEnum.ENABLE.getStatus()==activeStatus)) {
            throw new ForbiddenOperationException("服务项为启用状态方可下架");
        }

        //更新上架状态
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.DISABLE.getStatus())
                .update();
        if(!update){
            throw new CommonException("启动服务失败");
        }
    }

    /**
     * 设置区域服务为热门
     * @param id
     */
    @Override
    public void onHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //设置区域服务为热门状态
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getIsHot, FoundationHotEnum.HOT.getStatus())
                .update();
        if(!update){
            throw new CommonException("启动服务失败");
        }
    }

    /**
     * 设置区域服务取消热门
     * @param id
     */
    @Override
    public void offHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //设置区域服务为热门状态
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getIsHot, FoundationHotEnum.DISABLE_HOT.getStatus())
                .update();
        if(!update){
            throw new CommonException("启动服务失败");
        }
    }

    /**
     * 获取有区域下有多少启用的服务
     * @param id 区域id
     * @param status 2
     * @return
     */
    @Override
    public int queryServeCountByRegionIdAndSaleStatus(Long id, int status) {
        return lambdaQuery()
                .eq(Serve::getRegionId, id)
                .eq(Serve::getSaleStatus, status)
                .count();
    }

    /**
     * 统计存在关联的服务项id且状态为上架
     * @param id 服务项id
     * @param status
     * @return
     */
    @Override
    public int queryServeCountByServerItemAndSaleStatus(Long id, int status) {
        return lambdaQuery()
                .eq(Serve::getServeItemId, id)
                .eq(Serve::getSaleStatus, status)
                .count();
    }
}
