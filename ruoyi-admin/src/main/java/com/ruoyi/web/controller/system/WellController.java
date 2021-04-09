package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;

import com.ruoyi.system.domain.Well;
import com.ruoyi.system.service.IWellService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author liuzhiqi
 * @description
 * @date 2021/3/12
 */
@RestController
@RequestMapping("/system/well")
public class WellController extends BaseController {

    @Autowired
    private IWellService wellService;
    @Autowired
    private TokenService tokenService;

    //查询废弃井列表（管理页面）&&
    @ApiOperation("查询废弃井列表")
    @PreAuthorize("@ss.hasPermi('system:well:list')")
    @GetMapping("/list")
    public TableDataInfo list(Well well) {
        startPage();
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long postId = loginUser.getUser().getPostId();
        Integer level = loginUser.getUser().getLevel();
        well.getParams().put("postId", postId);
        well.getParams().put("level" , level);
        well.getParams().put("field","status" + level);
        List<Well> wellList = wellService.selectWellList(well);
        return getDataTable(wellList);
    }
    //查询废弃井列表（状态查询）
    @ApiOperation("按状态统计数量")
    @PreAuthorize("@ss.hasPermi('system:well:list')")
    @GetMapping("/statusList")
    public AjaxResult statusList() {
        List<Map<String, Object>> list = wellService.statusList(new Well());
        Integer count = wellService.countByIsFill(new Well());
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("status","filled");
        map.put("count", count);
        list.add(map);
        return AjaxResult.success(list);
    }
    //查询废弃井列表（时间查询）（请求参数updateTime）（结果=状态+时间）
    @ApiOperation("按天统计数量")
    @PreAuthorize("@ss.hasPermi('system:well:list')")
    @GetMapping("/dayList")
    public AjaxResult dayList(Well well) {
        //统计审核状态
        List<Map<String, Object>> list = wellService.statusListByDay(well);
        //统计动态调整
        List<Map<String, Object>> list2 = wellService.selectFilledListByDay(well);
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("statusList", list);
        map.put("filledList", list2);
        return AjaxResult.success(map);

    }
    //导出废弃井列表（数据导出）
    @ApiOperation("导出废弃井列表-汇总")
    @PreAuthorize("@ss.hasPermi('system:well:export')")
    @GetMapping("/export")
    public AjaxResult export(Well well) {
        LoginUser login = tokenService.getLoginUser(ServletUtils.getRequest());
        Integer level = login.getUser().getLevel();
        Long postId = login.getUser().getPostId();
        well.getParams().put("level", level);
        well.getParams().put("postId",postId);
        well.getParams().put("filed", "status" + level);
        List<Well> list = wellService.selectWellList(well);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(i + 1L);
        }
        ExcelUtil<Well> util = new ExcelUtil<>(Well.class);
        return util.exportExcel(list, login.getUsername() + level);

    }
    //导出废弃井列表（单条导出）
    @ApiOperation("导出废弃井基本信息表")
    @PreAuthorize("@ss.hasPermi('system:well:export')")
    @GetMapping("/export/{id}")
    public AjaxResult exportInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(wellService.exportInfoById(id));
    }

    //（批量导出）
    @ApiOperation("批量导出废弃井基本信息表")
    @PreAuthorize("@ss.hasPermi('system:well:export')")
    @GetMapping("/exports/{ids}")
    public AjaxResult exportInfos(@PathVariable("ids") String ids, ServletResponse response) throws IOException {
        LoginUser login = tokenService.getLoginUser(ServletUtils.getRequest());
        return AjaxResult.success(wellService.exportInfoByIds(ids, response, login.getUsername()));
    }


    //查询废弃井详细信息
    @ApiOperation("获取废弃井详细信息")
    @PreAuthorize("@ss.hasPermi('system:well:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult list(@PathVariable("id") Long id){
        return AjaxResult.success(wellService.selectWellById(id));
    }

    //增加一条废弃井信息
    @ApiOperation("新增废弃井信息")
    @PreAuthorize("@ss.hasPermi('system:well:add')")
    @PostMapping
    public AjaxResult edit(@RequestBody Well well) {
        LoginUser login = tokenService.getLoginUser(ServletUtils.getRequest());
        well.setCreateBy(login.getUser().getCreateBy());
        well.setDepartment(login.getUser().getPostId());
        return wellService.addWell(well);
    }

    //修改废弃井信息
    @ApiOperation("修改废弃井信息")
    @PreAuthorize("@ss.hasPermi('system:well:edit')")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody Well well) {
        LoginUser login = tokenService.getLoginUser(ServletUtils.getRequest());
        well.setUpdateBy(login.getUser().getUpdateBy());
        return wellService.updateWell(well);
    }

    //删除废弃井信息
    @ApiOperation("批量删除废弃井信息")
    @PreAuthorize("@ss.hasPermi('system:well:remove')")
    @Log(title = "废弃井", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(wellService.deleteWellByIds(ids));
    }

    @ApiOperation("动态更新")
    @PreAuthorize("@ss.hasPermi('system:well:renew')")
    @PostMapping("/dynamicUpdate")
    public AjaxResult dynamicUpdate(@RequestBody Well well) {
        if(well.getIsFill() == 3 && StringUtils.isBlank(well.getUpdateRemark())){
            return AjaxResult.error("动态更新备注未填报");
        }
        LoginUser login = tokenService.getLoginUser(ServletUtils.getRequest());
        well.setUpdateBy(login.getUser().getUserId()+ "");
        return toAjax(wellService.dynamicUpdate(well));
    }

    //上报
    @ApiOperation("上报信息")
    @PreAuthorize("@ss.hasPermi('system:well:report')")
    @PostMapping("/report/{ids}")
    public AjaxResult report(@PathVariable Long[] ids) {
        LoginUser login = tokenService.getLoginUser(ServletUtils.getRequest());
        Integer level = login.getUser().getLevel();
        int count = wellService.report(ids, login.getUser().getUserId(), level);
        return count > 0 ? AjaxResult.success() : AjaxResult.error("无可添加数据");
    }

    //退回
    @ApiOperation("同意退回")
    @PreAuthorize("@ss.hasPermi('system:well:audit')")
    @PostMapping("/auditReturn/{ids}")
    public AjaxResult auditReturn(@PathVariable Long[] ids){
        LoginUser login = tokenService.getLoginUser(ServletUtils.getRequest());
        Integer level = login.getUser().getLevel();
        Long userId = login.getUser().getUserId();
        Long postId = login.getUser().getPostId();
        System.out.println(postId);
        int count = wellService.auditReturn(ids, userId, postId, level);
        return count > 0 ? AjaxResult.success() : AjaxResult.error("申请退回失败");
     }


    @ApiOperation("汇总省")
    @PreAuthorize("@ss.hasPermi('system:well:query')")
    @GetMapping("/count/province")
    public AjaxResult countProvince(Date startTime, Date endTime, Long postId) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Integer level = loginUser.getUser().getLevel();
        if (level != 1) {
            return AjaxResult.error("无权限查询数据");
        }
        Long userPostId = loginUser.getUser().getPostId();
        Well well = new Well();

        well.getParams().put("startTime", startTime);
        well.getParams().put("endTime", endTime);
        well.getParams().put("postId", userPostId);
        if (userPostId != null && postId == null) {
            well.setDepartment(userPostId);
        }
        if (postId != null) {
            well.setDepartment(postId);
        }
        return AjaxResult.success(wellService.countProvince(well));
    }


    @ApiOperation("汇总城市")
    @PreAuthorize("@ss.hasPermi('system:well:query')")
    @GetMapping("/count/citys")
    public AjaxResult countCity(Date startTime, Date endTime, Long postId) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Integer level = loginUser.getUser().getLevel();
        Long userPostId = loginUser.getUser().getPostId();
        Long deptId = loginUser.getUser().getDeptId();
        Well well = new Well();
        if (userPostId != null && postId == null) {
            well.setDepartment(userPostId);
        }
        if (postId != null) {
            well.setDepartment(postId);
        }
        well.getParams().put("startTime", startTime);
        well.getParams().put("endTime", endTime);
        well.getParams().put("level", level);
        return AjaxResult.success(wellService.countCity(well, level, deptId));
    }

    @ApiOperation("汇总区县")
    @PreAuthorize("@ss.hasPermi('system:well:query')")
    @GetMapping("/count/districts")
    public AjaxResult countDistricts(Date startTime, Date endTime, Long postId, Long cityId) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Integer level = loginUser.getUser().getLevel();
        Long deptId = loginUser.getUser().getDeptId();
        Long userPostId = loginUser.getUser().getPostId();
        Well well = new Well();
        well.getParams().put("startTime", startTime);
        well.getParams().put("endTime", endTime);
        well.getParams().put("level", level);
        if (userPostId != null && postId == null) {
            well.setDepartment(userPostId);
        }
        if (postId != null) {
            well.setDepartment(postId);
        }
        if (level == 2) {
            well.setCity(cityId);
        } else if (level == 3) {
            well.setDistrict(deptId);
        } else {
            well.setCity(cityId);
        }
        return AjaxResult.success(wellService.countDistricts(well, level, deptId));
    }

}
