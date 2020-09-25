package com.ssvet.approval.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户角色中间表
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联用户ID
     */
    @TableId("ur_user_Id")
    private Integer urUserId;

    /**
     * 关联角色ID
     */
    private Integer urRoleId;


}
