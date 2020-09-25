package com.ssvet.approval.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 审批事件表
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("approval_event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审批事件ID
     */
    @TableId(value = "approval_event_id", type = IdType.AUTO)
    private Integer approvalEventId;

    /**
     * 审批类型ID
     */
    private Integer approvalTypeId;

    /**
     * 申请人ID
     */
    private Integer approvalUserId;

    /**
     * 申请事由
     */

    private String approvalReason;



    /**
     * 审批金额
     */
    private Double approvalMoney;

    /**
     * 申请备注
     */
    private String approvalRemark;

    /**
     * 当前审批人ID
     */
    private Integer nowApprovalId;

    /**
     * 审批人索引
     */
    private Integer approvalUserIndex;

    /**
     * 审批人 1-张三
     */
    private String approvalPersons;

    /**
     * 审批状态 1未审批2审批中3审批通过4审批驳回5撤销6审核完成
     */
    private Integer status;

    /**
     * 审批创建时间
     */
    private LocalDate createDate;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;
    /**
     * 图片列表
     */
    @TableField(exist = false)
    private List<Note> notes;

    @TableField(exist = false)
    private List<User> users;

    @TableField(exist = false)
    private String chName;

    @TableField(exist = false)
    private List<Record> records;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Integer days;
}
