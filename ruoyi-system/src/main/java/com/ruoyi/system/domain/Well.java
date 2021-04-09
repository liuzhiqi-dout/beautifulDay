package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;


/**
 * @author liuzhiqi
 * @description
 * @date 2021/3/12
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Well extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "序号", sort = 1)
    @Id
    private Long id;

    @Excel(name = "顺序码", sort = 4)
    private Integer order;

    @Excel(name = "废弃井编号", sort = 2)
    @NotEmpty(message = "废弃井编号未填报")
    @Length(min = 15, max = 15, message = "废弃井编号长度不符要求")
    @Pattern(regexp = "^(\\d{14})W$", message = "废弃井编号不符合要求")
    private String wellCode;

    @Excel(name = "名称", sort = 3)
    @NotEmpty(message = "废弃井名称未填报")
    @Length(min = 8, message = "废弃井名称不符合要求")
    private String name;

    @Excel(name = "原始编号", sort = 4)
    private String originCode;

    //废弃井类型 1：矿井，2：钻井，3：水井
    @Excel(name = "废弃井类型", dictType = "well_type", sort = 5)
    @NotNull(message = "废弃井类型未填报")
    private Integer wellType;

    @Excel(name = "废弃井井别", dictType = "well_diff", sort = 6)
    @NotNull(message = "废气井井别未填报")
    private Integer purpose;

    @NotNull(message = "省辖市未填报")
    private Long city;

    @NotNull(message = "区/县未填报")
    private Long district;

    @Excel(name = "乡镇(街道办)", sort = 11)
    @NotEmpty(message = "乡镇未填报")
    @Length(max = 32, message = "乡镇(街道办)过长")
    private String street;

    @Excel(name = "村(街)、门牌号", sort = 12)
    @NotEmpty(message = "村(街)、门牌号未填报")
    @Length(max = 32, message = "村(街)、门牌号过长")
    private String address;

    @Excel(name = "所属单位", sort = 7)
    @NotEmpty(message = "所属单位未填报")
    @Length(max = 32, message = "所属单位名称过长")
    private String company;

    @Excel(name = "联系人", sort = 13)
    @NotEmpty(message = "联系人未填报")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{0,}$", message = "联系人填报有误")
    @Length(min = 2, max = 5, message = "联系人长度错误")
    private String contacter;

    @Excel(name = "联系电话", sort = 14)
    @NotEmpty(message = "联系电话未填报")
    @Pattern(regexp = "^((0\\d{2,3}-\\d{7,8})|(1\\d{10}))$", message = "联系电话填报有误")
    private String phone;

    @JsonFormat(pattern = "yyyy-MM")
    @Excel(name = "建井时间", width = 30, dateFormat = "yyyyMM", sort = 8)
    @NotNull(message = "建井时间未填报")
    private Date digTime;

    @Excel(name = "废弃原因", sort = 15, dictType = "abandon_reason")
    private Integer abandonReason;

    @JsonFormat(pattern = "yyyy-MM")
    @Excel(name = "废弃时间", width = 30, dateFormat = "yyyyMM", sort = 16)
    private Date abandonTime;

    @Excel(name = "纬度(度)", sort = 20)
    @NotNull(message = "废弃井纬度(度)未填报")
    @Range(message = "废弃井纬度超出河南省边界，范围在[31-36]之内", min = 31L, max = 36L)
    private Integer lat1;

    @Excel(name = "纬度(分)", sort = 21)
    @NotNull(message = "废弃井纬度(分)未填报")
    @Range(message = "废弃井纬度（分）填报有误，范围在[0-60)之内", min = 0, max = 59L)
    private Integer lat2;

    @Excel(name = "纬度(秒)", sort = 22)
    @NotNull(message = "废弃井纬度(秒)未填报")
    @Range(message = "废弃井纬度（秒）填报有误，范围在[0-60)之内", min = 0, max = 59L)
    private Integer lat3;

    //经纬度
    private String gps;

    @Excel(name = "经度(度)", sort = 17)
    @NotNull(message = "废弃井经度(分)未填报")
    @Range(message = "废弃井经度超出河南省边界，范围在[110-116]之内", min = 110L, max = 116L)
    private Integer lng1;

    @Excel(name = "经度(分)", sort = 18)
    @NotNull(message = "废弃井纬度(分)未填报")
    @Range(message = "废弃井经度（分）填报有误，范围在[0-60)之内", min = 0, max = 59L)
    private Integer lng2;

    @Excel(name = "经度(秒)", sort = 19)
    @NotNull(message = "废弃井经度(秒)未填报")
    @Range(message = "废弃井经度（秒）填报有误，范围在[0-60)之内", min = 0, max = 59L)
    private Integer lng3;

    @Excel(name = "井（管）筒基本情况", sort = 23)
    @NotNull(message = "井（管）筒基本情况未填报")
    private String info;

    @Excel(name = "填表人", sort = 28)
    @NotBlank(message = "填表人未填报")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{0,}$", message = "填表人填报有误")
    private String informer;

    @Excel(name = "排查人", sort = 29)
    @NotBlank(message = "排查人未填报")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{0,}$", message = "排查人填报有误")
    private String Investigator;

    @JsonFormat(pattern = "yyyy-MM")
    @Excel(name = "填表时间", width = 30, dateFormat = "yyyy-MM", sort = 25)
    private Date informTime;

    @Excel(name = "备注", sort = 27)
    private String remark;

    //创建人：创建时间，更新人：更新时间
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    @Excel(name = "动态更新", sort = 24, dictType = "dynamic_update")
    private Integer isFill;

    @JsonFormat(pattern = "yyyy-MM")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM", sort = 25)
    private Date fillStartTime;

    @JsonFormat(pattern = "yyyy-MM")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM", sort = 25)
    private Date fillEndTime;


    //审核时间
    private Date auditTime;

    //审核人
    private String auditBy;
    private Long department;

    @Excel(name = "省辖市", sort = 9)
    private String cityStr;

    @Excel(name = "区（县、市）", sort = 10)
    private String districtStr;

    @Excel(name = "废弃原因备注", sort = 15)
    private String abandonRemark;

    @Excel(name = "动态更新备注", sort = 24)
    private String updateRemark;

    //状态
    private Integer status;
    private Integer status1;
    private Integer status2;
    private Integer status3;
    private Date status1Time;
    private Date status2Time;
    private Date status3Time;
    private Date dynamicUpdateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getWellCode() {
        return wellCode;
    }

    public void setWellCode(String wellCode) {
        this.wellCode = wellCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public Integer getWellType() {
        return wellType;
    }

    public void setWellType(Integer wellType) {
        this.wellType = wellType;
    }

    public Integer getPurpose() {
        return purpose;
    }

    public void setPurpose(Integer purpose) {
        this.purpose = purpose;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public Long getDistrict() {
        return district;
    }

    public void setDistrict(Long district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContacter() {
        return contacter;
    }

    public void setContacter(String contacter) {
        this.contacter = contacter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDigTime() {
        return digTime;
    }

    public void setDigTime(Date digTime) {
        this.digTime = digTime;
    }

    public Integer getAbandonReason() {
        return abandonReason;
    }

    public void setAbandonReason(Integer abandonReason) {
        this.abandonReason = abandonReason;
    }

    public Date getAbandonTime() {
        return abandonTime;
    }

    public void setAbandonTime(Date abandonTime) {
        this.abandonTime = abandonTime;
    }

    public Integer getLat1() {
        return lat1;
    }

    public void setLat1(Integer lat1) {
        this.lat1 = lat1;
    }

    public Integer getLat2() {
        return lat2;
    }

    public void setLat2(Integer lat2) {
        this.lat2 = lat2;
    }

    public Integer getLat3() {
        return lat3;
    }

    public void setLat3(Integer lat3) {
        this.lat3 = lat3;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public Integer getLng1() {
        return lng1;
    }

    public void setLng1(Integer lng1) {
        this.lng1 = lng1;
    }

    public Integer getLng2() {
        return lng2;
    }

    public void setLng2(Integer lng2) {
        this.lng2 = lng2;
    }

    public Integer getLng3() {
        return lng3;
    }

    public void setLng3(Integer lng3) {
        this.lng3 = lng3;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInformer() {
        return informer;
    }

    public void setInformer(String informer) {
        this.informer = informer;
    }

    public String getInvestigator() {
        return Investigator;
    }

    public void setInvestigator(String investigator) {
        Investigator = investigator;
    }

    public Date getInformTime() {
        return informTime;
    }

    public void setInformTime(Date informTime) {
        this.informTime = informTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsFill() {
        return isFill;
    }

    public void setIsFill(Integer isFill) {
        this.isFill = isFill;
    }

    public Date getFillStartTime() {
        return fillStartTime;
    }

    public void setFillStartTime(Date fillStartTime) {
        this.fillStartTime = fillStartTime;
    }

    public Date getFillEndTime() {
        return fillEndTime;
    }

    public void setFillEndTime(Date fillEndTime) {
        this.fillEndTime = fillEndTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public String getCityStr() {
        return cityStr;
    }

    public void setCityStr(String cityStr) {
        this.cityStr = cityStr;
    }

    public String getDistrictStr() {
        return districtStr;
    }

    public void setDistrictStr(String districtStr) {
        this.districtStr = districtStr;
    }

    public String getAbandonRemark() {
        return abandonRemark;
    }

    public void setAbandonRemark(String abandonRemark) {
        this.abandonRemark = abandonRemark;
    }

    public String getUpdateRemark() {
        return updateRemark;
    }

    public void setUpdateRemark(String updateRemark) {
        this.updateRemark = updateRemark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus1() {
        return status1;
    }

    public void setStatus1(Integer status1) {
        this.status1 = status1;
    }

    public Integer getStatus2() {
        return status2;
    }

    public void setStatus2(Integer status2) {
        this.status2 = status2;
    }

    public Integer getStatus3() {
        return status3;
    }

    public void setStatus3(Integer status3) {
        this.status3 = status3;
    }

    public Date getStatus1Time() {
        return status1Time;
    }

    public void setStatus1Time(Date status1Time) {
        this.status1Time = status1Time;
    }

    public Date getStatus2Time() {
        return status2Time;
    }

    public void setStatus2Time(Date status2Time) {
        this.status2Time = status2Time;
    }

    public Date getStatus3Time() {
        return status3Time;
    }

    public void setStatus3Time(Date status3Time) {
        this.status3Time = status3Time;
    }

    public Date getDynamicUpdateTime() {
        return dynamicUpdateTime;
    }

    public void setDynamicUpdateTime(Date dynamicUpdateTime) {
        this.dynamicUpdateTime = dynamicUpdateTime;
    }
}
