package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Region;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuzhiqi
 * @description
 * @date 2021/3/11
 */
@Mapper
public interface RegionMapper {
    List<Region> selectRegionList(Region region);

    Region selectRegionById(Long regionId);

    int insertRegion(Region region);

    int updateRegion(Region region);

    int deleteRegionByIds(Long[] regionIds);

    List<Region> selectCitys();

    List<Region> selectDistricts(Long cityId);
}
