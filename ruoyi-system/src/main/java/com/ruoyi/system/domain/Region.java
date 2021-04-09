package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

/**
 * @author liuzhiqi
 * @description
 * @date 2021/3/11
 */
@Data
@ToString
public class Region extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "区域ID")
    private Long regionId;
    
    @Excel(name = "父级ID")
    private Long parentId;
    
    @Excel(name = "区域名称")
    private String name;
    
    @Excel(name = "行政区域化代码")
    private String districtCode;
    
    @Excel(name = "二级地址化代码")
    private String regionCode;

    @Excel(name = "层级")
    private Integer levelId;

    @Excel(name = "顺序码")
    private Integer maxOrder;


}
