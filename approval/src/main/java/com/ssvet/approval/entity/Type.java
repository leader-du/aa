package com.ssvet.approval.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 流程表
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("approval_type")
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程ID
     */
    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;

    /**
     * 流程名称
     */
    private String typeName;


}
