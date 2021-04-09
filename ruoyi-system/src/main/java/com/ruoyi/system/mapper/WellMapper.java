package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Well;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuzhiqi
 * @description
 * @date 2021/3/12
 */
@Mapper
public interface WellMapper {
    List<Well> selectWellList(Well well);

    Well selectWellById(Long id);

    int addWell(Well well);

    int deleteById(Long ids);

    int deleteByIds(Long[] ids);

    int updateWell(Well well);

    int dynamicUpdate(Well well);

    List<Map<String, Object>> selectStatusList(Well well);

    Integer countByIsFill(Well well);


    int reportWell(HashMap<String, Object> map);

    int returnWell(HashMap<String, Object> map);

    Map<String, Object> countProvinceReport(Well well);

    Map<String, Object> countProvinceUpdate(Well well);

    List<Map<String, Object>> statusListByDay(Well well);

    List<Map<String, Object>> selectFilledListByDay(Well well);

    List<Map<String, Object>> countCityReports(Well well);

    List<Map<String, Object>> countCityUpdates(Well well);

    List<Map<String, Object>> countDistrictsReports(Well well);

    List<Map<String, Object>> countDistrictsUpdates(Well well);
}
