package com.ssvet.approval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("area_tb")
public class Area {

    private static final long serialVersionUID = 1L;

    /**
     * 大区ID
     */
    @TableId(value = "aid", type = IdType.AUTO)
    private Integer aid;

    /**
     * 大区名称
     */

    private String aname;

    /**
     * 关联区域经理ID
     */

    private Integer aUid;
}
