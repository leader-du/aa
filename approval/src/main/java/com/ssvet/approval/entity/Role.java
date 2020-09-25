package com.ssvet.approval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Data
public class Role{


    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色等级 1-普通员工 2-基地总经理
3-基地区域总经理/事业部总经理 4-直营基地管理中心总经理/研发与运营中心总经理/
人力总监/财务中心总经理/社会培训部 5-总裁
     */
    private Integer roleGrade;


}
