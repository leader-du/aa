package com.ssvet.approval.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 审批记录表
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("approval_record")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审批记录ID
     */
    @TableId(value = "approval_record_id", type = IdType.AUTO)
    private Integer approvalRecordId;

    /**
     * 审批事件ID
     */
    private Integer eventId;

    /**
     * 审批意见
     */
    private String approvalUserRemark;

    /**
     * 审批状态1通过0不通过
     */
    private Integer status;

    /**
     * 审批时间
     */
    private String approvalDate;

    /**
     * 审批人id
     */

    private Integer approvalLeaderId;

    /**
     * 审批人
     */

    @TableField(exist = false)
    private User user;

    /**
     * 审批记录对应的事件
     */

    @TableField(exist = false)
    private Event event;
}
