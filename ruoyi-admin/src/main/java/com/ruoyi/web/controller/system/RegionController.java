package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Region;
import com.ruoyi.system.service.IRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author liuzhiqi
 * @description
 * @date 2021/3/11
 */
@Api(tags = "区域")
@RestController
@RequestMapping("/system/region")
public class RegionController extends BaseController {

    @Autowired
    IRegionService regionService;

    /**
     * 查询区域列表
     */
    @PreAuthorize("@ss.hasPermi('system:region:list')")
    @GetMapping("/list")
    public TableDataInfo list(Region region){
        startPage();
        List<Region> regionList = regionService.selectRegionList(region);
        return getDataTable(regionList);
    }

    /**
     * 导出区域列表
     */
    @PreAuthorize("@ss.hasPermi('system:region:export')")
    @Log(title = "区域", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Region region) {
        List<Region> list = regionService.selectRegionList(region);
        ExcelUtil util = new ExcelUtil<>(Region.class);
        return util.exportExcel(list,"Region");
    }
    /**
     * 获取区域详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:region:query')")
    @GetMapping(value = "/{regionId}")
    public AjaxResult getInfo(@PathVariable("regionId") Long regionId){
        return AjaxResult.success(regionService.selectRegionById(regionId));
    }
    /**
     * 新增区域
     */
    @PreAuthorize("@ss.hasPermi('system:region:add')")
    @Log(title = "区域", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Region region){
        return AjaxResult.success(regionService.insertRegion(region));
    }
    /**
     * 修改区域
     */
    @PreAuthorize("@ss.hasPermi('system:region:edit')")
    @Log(title = "区域", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult update(@RequestBody Region region){
        return toAjax(regionService.updateRegion(region));
    }
    /**
     * 删除区域
     */
    @PreAuthorize("@ss.hasPermi('system:region:remove')")
    @Log(title = "区域", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{regionIds}")
    public AjaxResult delete(@PathVariable Long[] regionIds){
        return toAjax(regionService.deleteRegionByIds(regionIds));
    }
    /**
     *查询所有城市
     */
    @ApiOperation("查询所有城市")
    @GetMapping("/citys")
    public AjaxResult citys(){
        List<Region> list = regionService.selectCitys();
        return AjaxResult.success(list);
    }
    /**
     * 查询区县
     */
    @ApiOperation("/根据城市id查询所有辖区的区县")
    @GetMapping("/{cityId}/districts")
    public AjaxResult districts(@PathVariable Long cityId){
        List<Region> list = regionService.selectDistricts(cityId);
        return AjaxResult.success(list);
    }
}
