package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Well;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author liuzhiqi
 * @description
 * @date 2021/3/12
 */
public interface IWellService {
    List<Well> selectWellList(Well well);

    Well selectWellById(Long id);

    AjaxResult addWell(Well well);

    int deleteWellByIds(Long[] ids);

    AjaxResult updateWell(Well well);

    int dynamicUpdate(Well well);

    List<Map<String, Object>> statusList(Well well);

    Integer countByIsFill(Well well);

    String exportInfoById(Long id);

    String exportInfoByIds(String ids, ServletResponse response, String username) throws IOException;

    int report(Long[] ids, Long updateBy, Integer level);

    int auditReturn(Long[] ids, Long userId, Long postId, Integer level);

    List<Map<String, Object>> countProvince(Well well);

    List<Map<String, Object>> statusListByDay(Well well);

    List<Map<String, Object>> selectFilledListByDay(Well well);

    List<Map<String, Object>> countCity(Well well, Integer level, Long deptId);

    List<Map<String, Object>> countDistricts(Well well, Integer level, Long deptId);
}
