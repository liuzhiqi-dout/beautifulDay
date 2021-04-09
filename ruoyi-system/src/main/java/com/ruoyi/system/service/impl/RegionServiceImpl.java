package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Region;
import com.ruoyi.system.mapper.RegionMapper;
import com.ruoyi.system.service.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuzhiqi
 * @description
 * @date 2021/3/11
 */
@Service
public class RegionServiceImpl implements IRegionService {

    @Autowired
    private RegionMapper regionMapper;

    @Override
    public List<Region> selectRegionList(Region region) {
        return regionMapper.selectRegionList(region);

    }

    @Override
    public Region selectRegionById(Long regionId) {
        return regionMapper.selectRegionById(regionId);
    }

    @Override
    public int insertRegion(Region region) {
        return regionMapper.insertRegion(region);
    }

    @Override
    public int updateRegion(Region region) {
        return regionMapper.updateRegion(region);
    }

    @Override
    public int deleteRegionByIds(Long[] regionIds) {
        return regionMapper.deleteRegionByIds(regionIds);
    }

    @Override
    public List<Region> selectCitys() {
        return regionMapper.selectCitys();
    }

    @Override
    public List<Region> selectDistricts(Long cityId) {
        return regionMapper.selectDistricts(cityId);
    }


}
