package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.RegexUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Region;
import com.ruoyi.system.domain.Well;
import com.ruoyi.system.mapper.RegionMapper;
import com.ruoyi.system.mapper.WellMapper;
import com.ruoyi.system.service.IWellService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author liuzhiqi
 * @description
 * @date 2021/3/12
 */
@Service
public class WellServiceImpl implements IWellService {

    @Autowired
    private WellMapper wellMapper;

    @Autowired
    private RegionMapper regionMapper;

    private static String TEMPLATE_PATH = "D:/coding/java/RuoYi-Vue-12/ruoyi-admin/src/main/resources/templates/info.docx";

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<Well> selectWellList(Well well) {
        return wellMapper.selectWellList(well);
    }
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<Map<String, Object>> statusList(Well well) {
        List<Map<String, Object>> map = wellMapper.selectStatusList(well);
        return map;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public Integer countByIsFill(Well well) {
        return wellMapper.countByIsFill(well);
    }

    @Override
    public String exportInfoById(Long id) {
        Well well = wellMapper.selectWellById(id);
        JSONObject json = (JSONObject) JSON.toJSON(well);
        String fileName = well.getWellCode() + "基本信息表.docx";
        formatWellForWord(json);
        try(FileOutputStream out = new FileOutputStream(RuoYiConfig.getDownloadPath() + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(out)){
            XWPFTemplate template = XWPFTemplate.compile(TEMPLATE_PATH).render(json);
            template.write(bos);
            template.close();
            bos.flush();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }


    @Override
    public String exportInfoByIds(String ids, ServletResponse response, String username) throws IOException {
        String datePath = DateUtils.parseDateToStr("yyyyMMddHHmm", new Date());
        File dir = new File(RuoYiConfig.getDownloadPath() + datePath);
        if (!dir.exists()) {
            System.out.println("=========创建下载路径===========");
            dir.mkdirs();
        } else {
            System.out.println("=========文件路径存在==========");
        }
        String[] split = ids.split(",");
        for (String s : split) {
            Well well = wellMapper.selectWellById(Long.parseLong(s));
            if (well == null) {
                continue;
            }
            JSONObject json = (JSONObject) JSON.toJSON(well);
            formatWellForWord(json);
            try (FileOutputStream out = new FileOutputStream(dir.getAbsolutePath() + "/" + well.getWellCode() + "基本信息表..docx");
                 BufferedOutputStream bos = new BufferedOutputStream(out);) {
                XWPFTemplate template = XWPFTemplate.compile(TEMPLATE_PATH).render(json);
                template.write(bos);
                template.close();
                bos.flush();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String zipFile = RuoYiConfig.getDownloadPath() + username + "基本信息表-" + datePath + ".zip";
        System.out.println("压缩文件:" + zipFile);
        zipMultiFile(dir.getAbsolutePath(), zipFile, false);
        File file = new File(zipFile);
        if (file.exists()) {
            System.out.println("压缩完成" + file.length());
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("压缩失败");
        }
        return username + "基本信息表-" + datePath + ".zip";
    }

    private void formatWellForWord(JSONObject json) {
        if (json.getDate("digTime") != null) {
            json.put("digTime", DateUtils.parseDateToStr("yyyyMM", json.getDate("digTime")));
        }
        if (json.getDate("abandonTime") != null) {
            json.put("abandonTime", DateUtils.parseDateToStr("yyyyMM", json.getDate("abandonTime")));
        }
        if (json.getDate("informTime") != null) {
            json.put("informTime", DateUtils.parseDateToStr("yyyyMM", json.getDate("informTime")));
        }
        if (json.getDate("fillStartTime") != null) {
            json.put("fillStartTime", DateUtils.parseDateToStr( "yyyyMM", json.getDate("fillStartTime")));
        } else {
            json.put("fillStartTime", "");
        }
        if (json.getDate("fillEndTime") != null) {
            json.put("fillEndTime", DateUtils.parseDateToStr( "yyyyMM", json.getDate("fillEndTime")));
        } else {
            json.put("fillEndTime", "");
        }
        if (json.getDate("wellType") != null) {
            json.put("wellType", DictUtils.getDictLabel("well_type", json.get("wellType") + ""));
        }
        if (json.getDate("purpose") != null) {
            json.put("purpose", DictUtils.getDictLabel("well_diff", json.get("purpose") + ""));
        }
        if (json.getDate("abandonReason") != null) {
            json.put("abandonReason", DictUtils.getDictLabel("abandon_reason", json.get("abandonReason") + ""));
        }
        if (json.getDate("isFill") != null) {
            json.put("isFill", DictUtils.getDictLabel("dynamic_update", json.get("isFill") + ""));
        }

    }





    @Override
    public int report(Long[] ids, Long updateBy, Integer level) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        map.put("updateBy", updateBy);
        map.put("status1", 2);
        map.put("filed1", "status" + level);
        map.put("filed1_time", "status" + level + "_time");
        if(level != 1) {
            map.put("status2", 1);
            map.put("filed2", "status" + (level-1));
            map.put("filed2_time", "status" + (level - 1) + "_time");
            return wellMapper.reportWell(map);
        } else {
            return wellMapper.returnWell(map);
        }
    }

    @Override
    public int auditReturn(Long[] ids, Long userId, Long postId, Integer level) {
        int i = 0;
        Date date = new Date();
        for (Long id : ids) {
            Well well = wellMapper.selectWellById(id);
            Integer status1 = well.getStatus1();
            Integer status2 = well.getStatus2();
            if (level == 1) {
                if (postId != null) {
                    if (status1 == 1) {
                        well.setStatus1(0);
                        well.setStatus1Time(date);
                        well.setStatus2(4);
                        well.setStatus2Time(date);
                    } else if (status1 == 2) {
                        well.setStatus1(4);
                        well.setStatus1Time(date);
                    } else if (status1 == 4) {
                        well.setStatus1(0);
                        well.setStatus1Time(date);
                        well.setStatus2(4);
                        well.setStatus2Time(date);
                    } else {
                        continue;
                    }
                } else {
                    well.setStatus1(4);
                    well.setStatus1Time(date);
                }
            }
            if (level == 2) {
                if (status2 == 1 || status2 == 4) {
                    well.setStatus2(0);
                    well.setStatus2Time(date);
                    well.setStatus3(4);
                    well.setStatus3Time(date);
                } else {
                    continue;
                }
            }
            well.setUpdateBy(userId.toString());
            well.setUpdateTime(date);
            i = wellMapper.updateWell(well);
        }
        return i;
    }

    @Override
    public List<Map<String, Object>> countProvince(Well well) {
        String department = "全部";
        if (well.getDepartment() != null) {
            department = DictUtils.getDictValue("department", well.getDepartment() + "");
        }
        List<Map<String, Object>> list = new ArrayList<>(2);
        Map<String, Object> map1 = wellMapper.countProvinceReport(well);
        Map<String, Object> map2 = wellMapper.countProvinceUpdate(well);
        map1.putAll(map2);
        map1.put("department",department);
        list.add(map1);
        return list;
    }

    @Override
    public List<Map<String, Object>> statusListByDay(Well well) {
        return wellMapper.statusListByDay(well);
    }

    @Override
    public List<Map<String, Object>> selectFilledListByDay(Well well) {
        return wellMapper.selectFilledListByDay(well);
    }




    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<Map<String, Object>> countCity(Well well, Integer level, Long deptId) {
        String department = "全部";
        if (well.getDepartment() != null) {
            department = DictUtils.getDictLabel("department", well.getDepartment() + "");
        }
        List<Map<String, Object>> list = new ArrayList<>();
        List<Region> regions = new ArrayList<>();
        if (level == 2) {
            Region region = regionMapper.selectRegionById(deptId);
            regions.add(region);
        } else if (level == 3) {
            return list;
        } else {
            regions = regionMapper.selectCitys();
        }
        List<Map<String, Object>> maps1 = wellMapper.countCityReports(well);
        List<Map<String, Object>> maps2 = wellMapper.countCityUpdates(well);
        countForZero(list, regions, maps1, maps2, department);
        return list;
    }

    @Override
    public List<Map<String, Object>> countDistricts(Well well, Integer level, Long deptId) {
        String department = "全部";
        if (well.getDepartment() != null) {
            department = DictUtils.getDictLabel("department", well.getDepartment() + "");
        }
        List<Map<String, Object>> list = new ArrayList<>();
        List<Region> regions = new ArrayList<>();
        if (level == 3) {
            Region region = regionMapper.selectRegionById(deptId);
            regions.add(region);
        } else {
            regions = regionMapper.selectDistricts(well.getCity());
        }
        List<Map<String, Object>> maps1 = wellMapper.countDistrictsReports(well);
        List<Map<String, Object>> maps2 = wellMapper.countDistrictsUpdates(well);
        countForZero(list, regions, maps1, maps2, department);
        return list;
    }


    public static void zipMultiFile(String filepath, String zippath, boolean dirFlag) {
        try {
            File file = new File(filepath);
            File zipFile = new File(zippath);
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File fileSec : files) {
                    if (dirFlag) {
                        recursionZip(zipOut, fileSec, file.getName() + File.separator);
                    } else {
                        recursionZip(zipOut, fileSec, "");
                    }
                }
            }
            zipOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void recursionZip(ZipOutputStream zipOut, File file, String baseDir) throws Exception {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileSec : files) {
                recursionZip(zipOut, fileSec, baseDir + file.getName() + File.separator);
            }
        } else {
            byte[] buf = new byte[1024];
            InputStream input = new FileInputStream(file);
            zipOut.putNextEntry(new ZipEntry(baseDir + file.getName()));
            int len;
            while ((len = input.read(buf)) != -1) {
                zipOut.write(buf, 0, len);
            }
            input.close();
        }
    }


    @Override
    public Well selectWellById(Long id) {
        return wellMapper.selectWellById(id);
    }

    @Override
    public AjaxResult addWell(Well well) {
        Date date = new Date();
        well.setCreateTime(date);
        well.setStatus3(1);
        well.setStatus3Time(date);
        String s = beforeInsert(well);
        if(StringUtils.isNotBlank(s)){
            return AjaxResult.error(s);
        }else{
            Region region = regionMapper.selectRegionById(well.getDistrict());
            well.setDistrictStr(region.getName());
            region.setMaxOrder(region.getMaxOrder() + 1);
            regionMapper.updateRegion(region);
            well.setOrder(region.getMaxOrder());
            String orderStr = String.format("%04d", region.getMaxOrder());
            String wellCode = region.getDistrictCode() + orderStr + region.getRegionCode() + "W";
            well.setWellCode(wellCode);
            String wellType = DictUtils.getDictLabel("well_type", well.getWellType() + "");
            well.setName(wellType + orderStr);
        }
        int i = wellMapper.addWell(well);
        if (i > 0) {
            return AjaxResult.success("新增废井成功");
        } else if (i == 0){
            return AjaxResult.error("废弃井编号已经存在");
        } else {
            return AjaxResult.error("新增废井失败");
        }
    }

    @Override
    public AjaxResult updateWell(Well well) {
        well.setUpdateTime(new Date());
        String s = beforeInsert(well);
        if(StringUtils.isNotBlank(s)){
            return AjaxResult.error(s);
        }
        int i = wellMapper.updateWell(well);
        if (i > 0) {
            return AjaxResult.success("更新数据成功");
        }else if (i == 0){
            return AjaxResult.error("废井编号已经存在");
        }else{
            return AjaxResult.error("更新废井失败");
        }
    }

    @Override
    public int dynamicUpdate(Well well) {
        well.setUpdateTime(new Date());
        return wellMapper.dynamicUpdate(well);
    }


    @Override
    public int deleteWellByIds(Long[] ids) {
        return wellMapper.deleteByIds(ids);
    }


    public String beforeInsert(Well well){
        StringBuilder stringBuffer = new StringBuilder();
        Date date = new Date();
        if(well.getWellType() == null || well.getPurpose() == null){
            stringBuffer.append("废弃井信息不能为空").append("<br>");
        }
        try {
            Region region = regionMapper.selectRegionById(well.getCity());
            well.setCityStr(region.getName());

            if(well.getDigTime().after(date)){
                stringBuffer.append("建井时间不能晚于当前时间").append("<br/>");
            }
            if(well.getAbandonTime().after(date)){
                stringBuffer.append("废弃时间不能晚于当前时间").append("<br/>");
            }
            if(well.getInformTime().after(date)){
                stringBuffer.append("填表时间不可晚于当前时间").append("<br/>");
            }

            if(well.getDigTime().after(well.getAbandonTime())){
                stringBuffer.append("废弃时间不可小于建井时间").append("<br/>");
            }
            if(well.getIsFill() != 4 && well.getFillEndTime() != null) {
                if (well.getFillStartTime().after(well.getFillEndTime())) {
                    stringBuffer.append("回填开始时间不可晚于回填结束时间").append("<br/>");
                }
            }
            if(well.getIsFill() == 3 && StringUtils.isBlank(well.getUpdateRemark())){
                stringBuffer.append("动态更新备注未填报").append("<br/>");
            }
            if(well.getAbandonReason() == 5 && StringUtils.isBlank(well.getAbandonRemark())){
                stringBuffer.append("废弃原因备注未填报").append("<br/>");
            }

            if(!RegexUtil.isChinese(well.getContacter())){
                stringBuffer.append("联系人填写错误").append("<br/>");
            }
            if(!RegexUtil.isChinese(well.getInformer())){
                stringBuffer.append("填表人填写错误").append("<br/>");
            }
            if(!RegexUtil.isChinese(well.getInvestigator())){
                stringBuffer.append("排查人填写错误").append("<br/>");
            }
        } catch (Exception e) {
            stringBuffer.append("消息填报有误").append("br/>");
        }
        return stringBuffer.toString();
    }

    private void countForZero(List<Map<String, Object>> list, List<Region> regions, List<Map<String, Object>>
            maps1, List<Map<String, Object>> maps2, String department) {
        for (Region region : regions) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("regionName", region.getName());
            map.put("department", department);
            map.put("regionId", region.getRegionId());
            map.put("type1", 0);
            map.put("type2", 0);
            map.put("type3", 0);
            map.put("typeTotal", 0);
            map.put("update1", 0);
            map.put("update2", 0);
            map.put("update3", 0);
            map.put("updateTotal", 0);
            Iterator<Map<String, Object>> iterator1 = maps1.iterator();
            while (iterator1.hasNext()) {
                Map<String, Object> next = iterator1.next();
                if (region.getRegionId().equals(next.get("regionId"))) {
                    map.put("type1", next.get("type1"));
                    map.put("type2", next.get("type2"));
                    map.put("type3", next.get("type3"));
                    map.put("typeTotal", next.get("typeTotal"));
                }
            }
            Iterator<Map<String, Object>> iterator2 = maps2.iterator();
            while (iterator2.hasNext()) {
                Map<String, Object> next = iterator2.next();
                if (region.getRegionId().equals(next.get("regionId"))) {
                    map.put("update1", next.get("update1"));
                    map.put("update2", next.get("update2"));
                    map.put("update3", next.get("update3"));
                    map.put("updateTotal", next.get("updateTotal"));
                }
            }
            list.add(map);
        }
    }
}
